package com.goqual.a10k.util;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class FcmMessagingService extends FirebaseMessagingService {
    public static final String TAG = FcmMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtil.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            LogUtil.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData() != null) {
            LogUtil.d(TAG, "DATA? " + remoteMessage.getData().toString());
        }
    }
}
