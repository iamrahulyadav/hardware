package spm.hardwareandroid.screenOff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootLoaderReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
			context.startService( new Intent(context,CallTrackerIntentService.class) );
	 // context.startService( new Intent(context,ScreenBlockService.class) );
    }
}