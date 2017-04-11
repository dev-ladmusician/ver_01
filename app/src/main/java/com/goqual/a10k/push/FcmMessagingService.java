package com.goqual.a10k.push;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.goqual.a10k.R;
import com.goqual.a10k.util.LogUtil;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class FcmMessagingService extends FirebaseMessagingService {
    public static final String TAG = FcmMessagingService.class.getSimpleName();
    private final int NOTI_ID = 1123;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtil.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            LogUtil.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData() != null) {
            LogUtil.d(TAG, "DATA? " + remoteMessage.getData().toString());

            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_noti)
                            .setContentTitle("10K Switch")
                            .setContentText(remoteMessage.getData().toString());

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(NOTI_ID, mBuilder.build());
        }
    }
}
