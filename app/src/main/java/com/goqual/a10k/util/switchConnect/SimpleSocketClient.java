package com.goqual.a10k.util.switchConnect;

import android.net.Network;
import android.os.Build;

import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.interfaces.IRawSocketCommunicationListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class SimpleSocketClient extends Thread{
    private final static String TAG = SimpleSocketClient.class.getSimpleName()+" wifi";
    private Socket mSocket;
    private static SimpleSocketClient instance;
    private Network mNetwork;

    private BufferedReader buffRecv;
    private BufferedWriter buffSend;
    private OutputStream os;
    private InputStream is;
    private static Thread sendingThread;

    private ArrayList<Character> receiveBuffer;
    private boolean mIsPacketStart;
    private boolean mIsPacketEnd;

    private static String mAddr;
    private static int mPort;

    private boolean isConnected = false;
    private boolean mCommBlock = false;
    private static boolean mIsRunning = false;

    private static String aLine = "";
    private LinkedBlockingQueue<byte[]> commQueue = new LinkedBlockingQueue<>();

    private IRawSocketCommunicationListener mListener;

    public static SimpleSocketClient getInstance(String addr, int port, IRawSocketCommunicationListener listener) {
        LogUtil.e(TAG, addr + "/" + port);
        mAddr = addr;
        mPort = port;
        if(instance==null) {
            LogUtil.e(TAG, "INIT INSTANCE");
            instance = new SimpleSocketClient(addr, port, listener);
        }
        return instance;
    }

    public SimpleSocketClient(String addr, int port, IRawSocketCommunicationListener listener) {
        mAddr = addr;
        mPort = port;
        mListener = listener;
    }

    public boolean connect() {
        try {
            LogUtil.e(TAG, mAddr + "/" + mPort);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSocket =
                        mNetwork.getSocketFactory().createSocket(InetAddress.getByName(mAddr), mPort);
            }
            else {
                InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(mAddr), mPort);
                mSocket = new Socket();
                mSocket.connect(socketAddress, 5000);
            }

            mListener.onConnected();
            mIsRunning = true;
        } catch (IOException e) {
            mListener.onError(e);
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            buffRecv.close();
            buffSend.close();
            is.close();
            os.close();
            sendingThread.interrupt();
            mSocket.close();
            interrupt();
            mListener.onDisconnected();
            receiveBuffer.clear();
        }
        catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
        finally {
            buffRecv = null;
            buffSend = null;
            is = null;
            os = null;
            sendingThread = null;
            mSocket = null;
            receiveBuffer = null;
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        mIsRunning = false;
        instance = null;
    }

    @Override
    public void run() {
        loop();
    }

    private void loop() {
        if(mSocket == null)         return;
        try {
            is = mSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            buffRecv = new BufferedReader(isr);
            os = mSocket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            buffSend = new BufferedWriter(osw);
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage(), e);
            mListener.onError(e);
            return;
        }
        isConnected = true;
        try{
            sendingThread = new Thread() {
                @Override
                public void run() {
                    while (!interrupted()) {
                        if(isConnected) {
                            if(!mCommBlock && commQueue.size()>0) {
                                byte[] pa = commQueue.poll();
                                LogUtil.d(TAG, "sendString :: SENDING\nCOMM_BLOCK: " + mCommBlock +
                                        "\nQUEUE_SIZE: " + commQueue.size() +
                                        "\nPACKET_SIZE: " + pa.length);
                                send(pa);
                                try {
                                    Thread.sleep(100);
                                }
                                catch (InterruptedException e) {
                                    LogUtil.e("asd", e.getMessage(), e);
                                }
                            }
                        }
                    }
                }
            };
            sendingThread.start();
        }catch (Exception e) {
            mListener.onError(e);
            LogUtil.e(TAG, e.getMessage(), e);
        }
        LogUtil.d(TAG, "socket_thread loop started");
        try {
            while (!isInterrupted()) {
                if(isConnected) {
                    try {
                        if(receiveBuffer == null) {
                            receiveBuffer = new ArrayList<>();
                        }
                        int aByte = buffRecv.read();
                        if(aByte != -1) {
                            LogUtil.d(TAG, "RECV : " + aByte + " CHAR? " + (char)aByte + " BYTE? " + (byte)aByte);
                            if(aByte == 0x02) {
                                mIsPacketStart = true;
                            }
                            if(aByte == 0x03) {
                                mIsPacketEnd = true;
                            }
                            if(mIsPacketStart) {
                                receiveBuffer.add((char)aByte);
                            }
                            if(mIsPacketEnd) {
                                Character[] line = new Character[receiveBuffer.size()];
                                receiveBuffer.toArray(line);
                                LogUtil.e(TAG, "RECEIVE: " + Arrays.toString(line) + "\nCOMM_BLOCK: " + mCommBlock);
                                mListener.onReceivePacket(line);
                                receiveBuffer = null;
                                mIsPacketStart = false;
                                mIsPacketEnd = false;
                            }
                        }
                        mCommBlock = false;
                    } catch (IOException e) {
                        LogUtil.e(TAG, e.getMessage(), e);
                        mListener.onError(e);
                    }
                }
            }
        }
        catch (Exception e) {
            LogUtil.d(TAG, "socket_thread loop terminated");
            mListener.onError(e);
            disconnect();
            isConnected = false;
        }
    }

    synchronized public boolean isConnected() {
        return isConnected;
    }

    public void sendPacket(byte[] msg) {
        LogUtil.d(TAG, "sendString :: QUEUEING\nCONN: " + isConnected() + "\nCOMM_BLOCK: " + mCommBlock + "\nMSG_LEN: " + msg.length);
        LogUtil.d(TAG, "L: " + msg.length + "\nB" + Arrays.toString(msg));
        commQueue.add(msg);
    }

    private void send(byte[] msg) {
        try {
            LogUtil.e(TAG, "SEND: " + new String(msg) + "\nLEN: " + msg.length);
            os.write(msg);
        }
        catch (SocketException e) {
            LogUtil.e(TAG, e.getMessage(), e);
            mListener.onError(e);
        }
        catch (Exception e) {
            mCommBlock = false;
            LogUtil.e("asda", e.getMessage(), e);
            mListener.onError(e);
        }
    }

    public void setNetwork(Network network) {
        mNetwork = network;
    }

    public boolean isRunning() {
        return mIsRunning;
    }
}
