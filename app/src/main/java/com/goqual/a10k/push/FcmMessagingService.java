package com.goqual.a10k.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.goqual.a10k.R;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityMain;

/**
 * Created by hanwool on 2017. 3. 13..
 */

public class FcmMessagingService extends FirebaseMessagingService {
    public static final String TAG = FcmMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtil.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null && remoteMessage.getData() != null) {
            LogUtil.d(TAG, "DATA? " + remoteMessage.getData().get("id").toString());
            int notiId = Integer.parseInt(remoteMessage.getData().get("id").toString());
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            android.support.v4.app.NotificationCompat.Builder notiBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(largeIcon)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody());

            Intent resultIntent = new Intent(this, ActivityMain.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(ActivityMain.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            notiBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notiManager.notify(notiId, notiBuilder.build());
        }
    }
}
