package com.anamgajith.notificationscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private JobScheduler mScheduler;
    private SeekBar mSeekbar;
    private static final int JOB_ID = 0;
    private Switch mDeviceIdle , mDeviceCharging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grabElements();
        final TextView seekBarProgress = findViewById(R.id.seekBarProgress);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > 0){
                    seekBarProgress.setText(progress+" s");
                }else {
                    seekBarProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void grabElements() {
        mDeviceIdle = findViewById(R.id.idleSwitch);
        mDeviceCharging = findViewById(R.id.chargingSwitch);
        mSeekbar = findViewById(R.id.seekbar);
    }

    public void schedulejob(View view) {
        RadioGroup networkoptions = findViewById(R.id.networkoption);

        int selectedNetworkId = networkoptions.getCheckedRadioButtonId();

        int selectedNetworkType = JobInfo.NETWORK_TYPE_NONE;


        switch (selectedNetworkId){
            case R.id.nonetwork:
                selectedNetworkType = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anynetwork:
                selectedNetworkType = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifinetwork:
                selectedNetworkType = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName serviceName = new ComponentName(getPackageName(),NotificationJobService.class.getName());

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,serviceName)
                .setRequiredNetworkType(selectedNetworkType)
                .setRequiresDeviceIdle(mDeviceIdle.isChecked())
                .setRequiresCharging(mDeviceCharging.isChecked());

        int seekBarInteger = mSeekbar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;

        if(seekBarSet){
            builder.setOverrideDeadline(seekBarInteger*1000);
        }

        boolean constraintSet = selectedNetworkType != JobInfo.NETWORK_TYPE_NONE || mDeviceCharging.isChecked() || mDeviceIdle.isChecked() || seekBarSet;


        if (constraintSet) {
            JobInfo jobInfo = builder.build();

            mScheduler.schedule(jobInfo);

            Toast.makeText(this, "Job Scheduled, job will run when " + "the constraints are met.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_SHORT).show();
        }
    }

    public void canceljob(View view) {
        if(mScheduler != null){
            mScheduler.cancelAll();
            mScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
