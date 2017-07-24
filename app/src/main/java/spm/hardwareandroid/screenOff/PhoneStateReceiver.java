package spm.hardwareandroid.screenOff;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import spm.hardwareandroid.MainActivity;

/**
 * Created by root on 5/24/17.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateReceiver";

	public static boolean isScreenOff = true;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Log.i(TAG, "onReceive: " + incomingNumber);



           /* if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
            }*/

            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);

                    Toast.makeText(context, "My Number "+incomingNumber, Toast.LENGTH_SHORT).show();

                    switch (state) {

                        case TelephonyManager.CALL_STATE_RINGING:

                            if (!TextUtils.isEmpty(incomingNumber)) {

	                            if(incomingNumber.contains( "8249432914" )){
		                            MainActivity.isScreenBlockOn = true;
		                            isScreenOff = false;
		                            System.out.println("incomingNumber : " + incomingNumber);
		                            context.stopService( new Intent( context, ScreenBlockService.class ) );
	                              }

                                Intent intentService = new Intent(context, CallTrackerIntentService.class);
                                intentService.putExtra( CallTrackerIntentService.KEY_PHONE_NUMBER, incomingNumber);
                                intentService.putExtra( CallTrackerIntentService.KEY_IS_APICALLED, false);
                                context.startService(intentService);


                            }
                            break;
                    }


                }
            }, PhoneStateListener.LISTEN_CALL_STATE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}