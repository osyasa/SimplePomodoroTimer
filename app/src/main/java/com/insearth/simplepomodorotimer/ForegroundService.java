package com.insearth.simplepomodorotimer;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


public class ForegroundService extends Service {

    NotificationCompat.Builder builder;
    String dkStr = "";
    String snStr = "";

    public void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "channel1";
            String channelDesc = " ";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("1", channelName, importance);
            channel.setDescription(channelDesc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            if(Integer.parseInt(snStr) >= 10) {
                builder = new NotificationCompat.Builder(this, "1")
                        .setContentTitle("Keep On!")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOnlyAlertOnce(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(dkStr + ":" + snStr)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setShowWhen(false);
            } else {
                builder = new NotificationCompat.Builder(this, "1")
                        .setContentTitle("Keep On!")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOnlyAlertOnce(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(dkStr + ":0" + snStr)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setShowWhen(false);;
            }

//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//            notificationManagerCompat.notify(1, builder.build());

            startForeground(101, builder.build());

        }

    }


    @Override
    public int onStartCommand (Intent intent,int flags, int startId) {

    dkStr = intent.getStringExtra("dkStr");
    snStr = intent.getStringExtra("snStr");

    createNotificationChannel();


    return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
