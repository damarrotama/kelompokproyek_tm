package com.ghifa.mobile.kelompokproyek2.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ghifa.mobile.kelompokproyek2.PengumumanActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ghifa.mobile.kelompokproyek2.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    //MobilePaymentDB dbhelper;
    private int notification_id = 14335;

    FragmentManager managerFragment;
    FragmentTransaction transaksiFragment;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


            sendNotification(
                remoteMessage.getNotification().getBody(),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getClickAction()
            );


            Log.d(TAG, "Notification Message Proses: " + remoteMessage.getData().get("proses"));
            Log.d(TAG, "Notification Message Title: " + remoteMessage.getData().get("judul"));
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));

            /*String proses = remoteMessage.getData().get("proses");
            String judul = remoteMessage.getData().get("judul");
            String body = remoteMessage.getData().get("body");*/

    }

    private void sendNotification(
            String messageBody,
            String messageTitle,
            String ke_intent
    ) {



        Intent intent;

        intent = new Intent(this, PengumumanActivity.class);
        //intent.putExtra("id_pengumuman", id_pengumuman);



        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setCategory(Notification.CATEGORY_PROMO)
                .setContentTitle(messageTitle)
                .setTicker(messageBody)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.notificon)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(contentIntent)
                .setSound(notificationSound)
                //.setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        Notification notification = mBuilder.build();
        //notification.flags |= Notification.FLAG_NO_CLEAR;
        //notification.flags |= Notification.FLAG_ONGOING_EVENT;

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notification_id, notification);

    }

}
