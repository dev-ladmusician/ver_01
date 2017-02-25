package com.goqual.a10k.util.switchConnect;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class ConnectSocketData {
    private static final byte STX = 0x02;
    private static final byte ETX = 0x03;
    private static final byte VERSION = (byte)0xFF;
    private static final short MIN_LENGTH = 6;
    private char cmd;
    private short length;
    private String data;

    public ConnectSocketData() {
    }

    public ConnectSocketData(char cmd, String data) {
        this.cmd = cmd;
        this.length = (short)data.length();
        this.data = data;
    }

    public ConnectSocketData(char cmd, byte[] data) {
        this.cmd = cmd;
        this.data = new String(data);
    }

    public byte[] makePacket() {
        short totalLength = (short)(MIN_LENGTH + length);
        byte[] packet = new byte[totalLength];
        packet[0] = STX;
        packet[1] = VERSION;
        packet[2] = (byte)cmd;
        byte[] len = String.format(Locale.KOREA, "%02d", length).getBytes();
        packet[3] = len[0];
        packet[4] = len[1];
        byte[] tempData = data.getBytes();
        for(int i = 0; i<length; i++) {
            packet[i+5] = tempData[i];
        }
        packet[totalLength-1] = ETX;
        return packet;
    }

    public static ConnectSocketData parsePacket(Character[] packet) {
        ConnectSocketData data = new ConnectSocketData();
        data.setCmd((char)packet[2]);
        String len = new String(new char[]{packet[3], packet[4]});
        data.setLength(Short.parseShort(len));
        Character[] tempData = Arrays.copyOfRange(packet, 5, packet.length-1);
        char[] temp = new char[tempData.length];
        for(int i = 0; i<temp.length; i++) {
            temp[i] = tempData[i];
        }
        data.setData(new String(temp));
        return data;
    }

    public char getCmd() {
        return cmd;
    }

    public void setCmd(char cmd) {
        this.cmd = cmd;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "<%s>\nCMD : %c\nLENGTH : %d\nDATA : %s",
                ConnectSocketData.class.getSimpleName(),
                getCmd(),
                getLength(),
                getData());
    }
}
