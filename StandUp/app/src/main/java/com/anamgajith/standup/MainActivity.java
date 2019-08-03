package com.anamgajith.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch aSwitch;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 22;
    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        aSwitch = findViewById(R.id.switch1);
        intent = new Intent(MainActivity.this,AlarmBroadcast.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_NO_CREATE) != null);
        aSwitch.setChecked(alarmUp);
        setSwitchInitialText();
        pendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        controlSwitch();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmManager != null){
                    long next = alarmManager.getNextAlarmClock().getTriggerTime();
                    String nextA = "Next Alarm in "+(next/1000)/3600+" hours";
                    Toast.makeText(MainActivity.this,nextA,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setSwitchInitialText() {


        if(aSwitch.isChecked()){
            aSwitch.setText(getText(R.string.alarm_on));
        }else{
            aSwitch.setText(getText(R.string.alarm_off));
        }
    }

    private void controlSwitch() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitch.setText(getText(R.string.alarm_on));
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rootview),getText(R.string.alarm_on),Snackbar.LENGTH_SHORT);
                    alarmConfig();
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            aSwitch.setChecked(false);
                        }
                    });
                    snackbar.show();
                }else{
                    aSwitch.setText(getText(R.string.alarm_off));
                    cancelAlarm();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rootview),getText(R.string.alarm_off),Snackbar.LENGTH_SHORT);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            aSwitch.setChecked(true);
                        }
                    });
                    snackbar.show();
                    notificationManager.cancel(NOTIFICATION_ID);
                }
            }
        });
    }

    private void cancelAlarm() {
        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
        notificationManager.cancelAll();
    }

    public void createNotificationChannel(){

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"Stand Up Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Notification from My app");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void alarmConfig(){


        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        if(alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,repeatInterval,pendingIntent);
        }
    }

}
