package com.goqual.a10k.util.switchConnect;

import com.goqual.a10k.util.LogUtil;

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
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class SimpleSocketClient extends Thread{
    private final static String TAG = "SimpleSocket";
    private Socket mSocket;
    private static SimpleSocketClient instance;
    private BufferedReader buffRecv;
    private BufferedWriter buffSend;
    private OutputStreamWriter osw;
    private OutputStream os;
    private InputStreamReader isr;
    private InputStream is;
    private static Thread sendingThread;

    private static String mAddr;
    private static int mPort;

    private boolean mConnected = false;
    private boolean mCommBlock = false;

    private static String aLine = "";
    private LinkedBlockingQueue<byte[]> commQueue = new LinkedBlockingQueue<>();

    public static SimpleSocketClient getInstance(String addr, int port) {
        LogUtil.e(TAG, addr + "/" + port);
        mAddr = addr;
        mPort = port;
        if(instance==null) {
            LogUtil.e(TAG, "INIT INSTANCE");
            instance = new SimpleSocketClient(addr, port);
        }
        try{
            instance.start();
        }
        catch (IllegalThreadStateException e) {
            LogUtil.e(TAG, "INSTANCE ALREADY RUN");
        }
        return instance;
    }

    public static void startConn() {
        try {
            instance.start();
        }
        catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }
    public SimpleSocketClient(String addr, int port) {
        mAddr = addr;
        mPort = port;
    }

    private boolean connect (String addr, int port) {
        try {
            LogUtil.e(TAG, addr + "/" + port);
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(addr), port);
            LogUtil.e(TAG, socketAddress.toString());
            mSocket = new Socket();
            mSocket.connect(socketAddress, 5000);
        } catch (IOException e) {
            try {
                Thread.sleep(2000);
                connect(addr, port);
            }
            catch (InterruptedException ex) {}
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            buffRecv.close();
            buffSend.close();
            sendingThread.interrupt();
            interrupt();
        }
        catch (Exception e) {
            LogUtil.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        loop();
    }

    private void loop() {
        if(! connect(mAddr, mPort)) return; // connect failed
        if(mSocket == null)         return;
        try {
            is = mSocket.getInputStream();
            isr = new InputStreamReader(is);
            buffRecv = new BufferedReader(isr);
            os = mSocket.getOutputStream();
            osw = new OutputStreamWriter(os);
            buffSend = new BufferedWriter(osw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LogUtil.e(TAG, e.getMessage(), e);
        }
        mConnected = true;
        try{
            sendingThread = new Thread() {
                @Override
                public void run() {
                    while (!interrupted()) {
                        if(mConnected) {
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
            LogUtil.e(TAG, e.getMessage(), e);
        }
        LogUtil.d(TAG, "socket_thread loop started");
        try {
            while (!isInterrupted()) {
                try {
                    aLine = buffRecv.readLine();
                    LogUtil.e(TAG, "RECEIVE: " + aLine + "\nCOMM_BLOCK: " + mCommBlock);
                    mCommBlock = false;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    LogUtil.e(TAG, e.getMessage(), e);
                }
            }
        }
        catch (Exception e) {
            LogUtil.d(TAG, "socket_thread loop terminated");
            disconnect();
            mConnected = false;
        }
    }

    synchronized public boolean isConnected() {
        return mConnected;
    }

    public void sendString(byte[] msg) {
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
        }
        catch (Exception e) {
            mCommBlock = false;
            LogUtil.e("asda", e.getMessage(), e);
        }
    }
}
