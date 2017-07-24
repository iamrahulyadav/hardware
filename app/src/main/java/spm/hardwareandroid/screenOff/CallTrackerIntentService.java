package spm.hardwareandroid.screenOff;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

/**
 * Created by root on 5/24/17.
 */

public class CallTrackerIntentService extends IntentService {

    private static final String TAG = "IntentService";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_IS_APICALLED = "isAPicalled";

    String url = "http://api.androidhive.info/volley/person_object.json";

    String phoneNumber;

    boolean isAPiCalled   ;

    public CallTrackerIntentService() {
        super("Name for Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



	    if ( intent.hasExtra( KEY_PHONE_NUMBER ) ) {

		    phoneNumber = intent.getStringExtra( KEY_PHONE_NUMBER );
		    //    isAPiCalled = intent.getBooleanExtra(KEY_IS_APICALLED,false);
		    Log.i( TAG, "onHandleIntent: " + phoneNumber );
	    }
	    if ( !TextUtils.isEmpty( phoneNumber ) ) {
		    SharedPreferences prefs    = getSharedPreferences( "MyData", Context.MODE_PRIVATE );
		    String            userName = prefs.getString( "userName", "" );

		    Toast.makeText( this, "Number " + phoneNumber, Toast.LENGTH_SHORT ).show();
	    }
	    //  callApi();
	    // TODO: 5/24/17 call web service
    }
}