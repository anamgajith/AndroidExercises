package com.anamgajith.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 22;
    private static final String ACTION_UPDATE_NOTIFICATION = "com.anamgajith.notification.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_DELETE_NOTIFICATION = "com.anamgajith.notification.ACTION_DELETE_NOTIFICATION";
    private NotificationReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNotificationButtonState(true, false, false);
        findViewById(R.id.notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();

                createNotificationChannel();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        notificationReceiver = new NotificationReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DELETE_NOTIFICATION);
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);

        registerReceiver(notificationReceiver,intentFilter);
    }

    private void sendNotification() {

        Intent updatedIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        Intent deletedIntent = new Intent(ACTION_DELETE_NOTIFICATION);

        PendingIntent deletedPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,deletedIntent,PendingIntent.FLAG_ONE_SHOT);
        PendingIntent updatedPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,updatedIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifiyer = getNotificationBuilder();

        notifiyer.addAction(R.drawable.ic_action_name,"Update Notification",updatedPendingIntent);
        notifiyer.setDeleteIntent(deletedPendingIntent);
        if (notificationManager == null)
            notificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notifiyer.build());
        setNotificationButtonState(false, true, true);
    }

    private NotificationCompat.Builder getNotificationBuilder(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Time to drive")
                .setContentText("Let's go for a ride")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    public void createNotificationChannel(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"My notifications",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from My app");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void updateNotification() {

        Bitmap myImage = BitmapFactory.decodeResource(getResources(),R.drawable.mynotification_1);

        NotificationCompat.Builder notifiyer = getNotificationBuilder();

        notifiyer.setStyle(new NotificationCompat.InboxStyle().setBigContentTitle("Today's Destinations").addLine("Pathiyarakkara")
                .addLine("Vadakara").addLine("Payyoli").setSummaryText("+2 more"));

        notificationManager.notify(NOTIFICATION_ID,notifiyer.build());

        setNotificationButtonState(false, false, true);

    }

    public void cancelNotification() {

        notificationManager.cancel(NOTIFICATION_ID);

        setNotificationButtonState(true, false, false);

    }

    void setNotificationButtonState(Boolean isNotifyEnabled, Boolean isUpdateEnabled, Boolean isCancelEnabled){
        findViewById(R.id.notify).setEnabled(isNotifyEnabled);
        findViewById(R.id.update).setEnabled(isUpdateEnabled);
        findViewById(R.id.cancel).setEnabled(isCancelEnabled);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
                case ACTION_DELETE_NOTIFICATION:
                    setNotificationButtonState(true, false, false);
                    break;

            }
        }

        public NotificationReceiver() {
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(notificationReceiver);
        super.onDestroy();
    }
}


