package spm.hardwareandroid.bound;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import spm.hardwareandroid.R;

/**
 * Created by webwerks on 10/2/16.
 */
public class ActivityBound extends AppCompatActivity{

    TextView tvTime;
    Button btnStart;

    MyBoundService myBoundService;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_bound);

        tvTime = (TextView)findViewById(R.id.tvTime);
        btnStart = (Button)findViewById(R.id.btnService);




        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mServiceBound) {
                    tvTime.setText(myBoundService.getTime());
                }
            }
        });

        if (mServiceBound) {
            tvTime.setText(myBoundService.getTime());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBoundService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(serviceConnection);
            mServiceBound = false;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBoundService.MyBinder myBinder = (MyBoundService.MyBinder) service;
            myBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };
}
