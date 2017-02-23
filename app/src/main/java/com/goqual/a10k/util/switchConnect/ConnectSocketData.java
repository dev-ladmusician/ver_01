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
    private char[] data;

    public ConnectSocketData() {
    }

    public ConnectSocketData(char cmd, char[] data) {
        this.cmd = cmd;
        this.length = (short)data.length;
        this.data = data;
    }

    public ConnectSocketData(char cmd, byte[] data) {
        char[] chars = new char[data.length];
        for(int i = 0; i<data.length; i++) {
            chars[i] = (char)data[i];
        }
        this.cmd = cmd;
        this.length = (short)data.length;
        this.data = chars;
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
        for(int i = 0; i<length; i++) {
            packet[i+5] = (byte)data[i];
        }
        packet[totalLength-1] = ETX;
        return packet;
    }

    public static ConnectSocketData parsePacket(byte[] packet) {
        ConnectSocketData data = new ConnectSocketData();
        data.setCmd((char)packet[2]);
        String len = new String(new byte[]{packet[3], packet[4]});
        data.setLength(Short.parseShort(len));
        byte[] tempData = Arrays.copyOfRange(packet, 5, packet.length-1);
        char[] chars = new char[tempData.length];
        for(int i = 0; i<data.getLength(); i++) {
            chars[i] = (char)tempData[i];
        }
        data.setData(chars);
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

    public char[] getData() {
        return data;
    }

    public void setData(char[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "<%s>\nCMD : %c\nLENGTH : %d\nDATA : %s",
                ConnectSocketData.class.getSimpleName(),
                getCmd(),
                getLength(),
                Arrays.toString(getData()));
    }
}
