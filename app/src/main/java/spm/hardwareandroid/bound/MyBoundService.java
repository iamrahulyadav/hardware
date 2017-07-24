package spm.hardwareandroid.bound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by webwerks on 10/2/16.
 */
public class MyBoundService extends Service {

    private IBinder myBinder = new MyBinder();
    private Chronometer mChronometer;
    @Override
    public void onCreate() {
        super.onCreate();
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {

        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
         return true;
    }

    public class MyBinder extends Binder{
        MyBoundService getService(){
            return MyBoundService.this;
        }
    }
    public String getTime(){
        long elapsedMillis = SystemClock.elapsedRealtime()
                - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + ":" + millis;

    }
}
