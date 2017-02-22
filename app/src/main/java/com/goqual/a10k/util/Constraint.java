package com.goqual.a10k.util;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public class Constraint {
    public static String MQTT_BROKER_IP = "52.79.137.129";
    //public static String MQTT_BROKER_IP = "52.53.244.248";

    public static final String SOCKET_SERVER_IP = "http://52.78.206.170:1987";
    //public static final String SOCKER_SERVER_IP = "http://54.153.40.3:1987";
    public static final String BASE_URL = "http://52.78.206.170:3443/";
    //public static final String BASE_URL = "http://54.153.40.3:3443/";
    public static final String AUTH_BASE_URL = "http://52.78.91.10/BS/";
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNDg0NjI4MDExLCJleHAiOjE0ODUxNTM2MTF9.tZYlQLFvxMiWo_CC-MHdrQszOJaMejWfWyL5ztmsvxs";

    public static final int arg_result_item = 11;
    public static final int arg_result_repeat_item = 12;
    public static final int arg_result_ringtone = 13;

    public static final String AP_NAME = "10K_SWITCH";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String WPA2 = "WPA2";
    public static final String OPEN = "Open";

    public static final String CUSTOM_HEADER = "access_token";

    public static final String BS_SERVER_IP = "192.168.4.1"; // AP default IP

    public static final int SERVER_PORT = 5050;
    public static final int SOCKET_CONNECT_TIMEOUT = 5000;
    public static final int MAX_CONNECT_TO_BS_TRY = 5;
    public static final int MAX_RETRY_COUNT = 3;
    public final static byte PROTOCOL_VER = (byte)0xff;
}