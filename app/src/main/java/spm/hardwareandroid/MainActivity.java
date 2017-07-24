package spm.hardwareandroid;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import spm.hardwareandroid.screenOff.CallTrackerIntentService;
import spm.hardwareandroid.screenOff.ScreenBlockService;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	public static final  int     REQUEST_ID_MULTIPLE_PERMISSIONS      = 1;
	public final static  int     REQUEST_CODE_SYSTEM_WINDOW           = 1122;
	private static final int     REQUEST_PERMISSION_SETTING           = 101;
	private static final int     READ_PHONE_STATE_PERMISSION_CONSTANT = 123;
	public static        boolean isScreenBlockOn                      = false;
	Button buttonCamera;
	View   mView;
	String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE };
	private SharedPreferences preferenceUserData;
	private boolean sentToSettings             = false;
	private boolean isOverLayPermissionGranted = false;

	public static boolean isForeground( Context ctx, String myPackage ) {
		ActivityManager                         manager         = ( ActivityManager ) ctx.getSystemService( ACTIVITY_SERVICE );
		List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks( 1 );

		ComponentName componentInfo = runningTaskInfo.get( 0 ).topActivity;
		if ( componentInfo.getPackageName().equals( myPackage ) ) {
			return true;
		}
		return false;
	}

	public static boolean hasPermissions( Context context, String... permissions ) {
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null ) {
			for ( String permission : permissions ) {
				if ( ActivityCompat.checkSelfPermission( context, permission ) != PackageManager.PERMISSION_GRANTED ) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		buttonCamera = ( Button ) findViewById( R.id.btnCamera );
		buttonCamera.setOnClickListener( this );

		MainActivityPermissionsDispatcher.startMyServiceWithCheck( this );

		stopService( new Intent( MainActivity.this, ScreenBlockService.class ) );

		checkDrawOverlayPermission();


		isOverLayPermissionGranted = false;

//		if( PhoneStateReceiver.isScreenOff)
//			startService( new Intent( getApplicationContext(), ScreenBlockService.class ) );

//		finish();

	}

	@Override
	public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
		// NOTE: delegate the permission handling to generated method
		MainActivityPermissionsDispatcher.onRequestPermissionsResult( this, requestCode, grantResults );
	}

	@Override
	public void onClick( View view ) {
		switch ( view.getId() ) {
			case R.id.btnCamera:
				startService( new Intent( MainActivity.this, ScreenBlockService.class ) );
			   /* try {
	                Camera cam = Camera.open();
                }
                catch(RuntimeException exception) {
                    Toast.makeText(this, "The camera and flashlight are in use by another app.", Toast.LENGTH_LONG).show();
                    // Exit gracefully
                }*/
				break;
		}
	}

	public void getAppLunch() {
		try {
			Process        mLogcatProc = null;
			BufferedReader reader      = null;
			mLogcatProc = Runtime.getRuntime().exec( new String[]{ "logcat", "-d" } );

			reader = new BufferedReader( new InputStreamReader( mLogcatProc.getInputStream() ) );

			String              line;
			final StringBuilder log       = new StringBuilder();
			String              separator = System.getProperty( "line.separator" );

			while ( ( line = reader.readLine() ) != null ) {
				log.append( line );
				log.append( separator );
			}
			String w = log.toString();
			Toast.makeText( getApplicationContext(), w, Toast.LENGTH_LONG ).show();
		}
		catch ( Exception e ) {
			Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
		}
	}

	public void getListofApps() {
		ActivityManager                               am                    = ( ActivityManager ) getSystemService( Context.ACTIVITY_SERVICE );
		List< ActivityManager.RunningAppProcessInfo > runningAppProcessInfo = am.getRunningAppProcesses();

		for ( int i = 0; i < runningAppProcessInfo.size(); i++ ) {
			if ( runningAppProcessInfo.get( i ).processName.equals( "com.the.app.you.are.looking.for" ) ) {
				// Do you stuff
			}
		}
	}

	private void showSettingsOKCancel( String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListner ) {
		new AlertDialog.Builder( this )
				.setMessage( message )
				.setPositiveButton( "Settings", okListener )
				.setNegativeButton( "Cancel", cancelListner )
				.create()
				.show();
	}

	private void getalert( final String s ) {

		showSettingsOKCancel( s,
		                      new DialogInterface.OnClickListener() {
			                      @Override
			                      public void onClick( DialogInterface dialog, int which ) {

				                      Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
				                      Uri    uri    = Uri.fromParts( "package", getPackageName(), null );
				                      intent.setData( uri );
				                      startActivityForResult( intent, 100 );
			                      }
		                      }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {

						getalert( s );
					}
				} );

	}


	public void requestReadPhoneStatePermission() {
		if ( ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED ) {

		}
		else {
			if ( ActivityCompat.shouldShowRequestPermissionRationale( MainActivity.this, Manifest.permission.READ_PHONE_STATE ) ) {
				Toast.makeText( MainActivity.this, "READ_PHONE_STATE Explanation", Toast.LENGTH_SHORT ).show();
				ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.READ_PHONE_STATE }, 1 );
			}
			else {
				ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.READ_PHONE_STATE }, 1 );
			}
		}
	}


	private void checkRuntimePermission() {
		if ( ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED ) {
			if ( ActivityCompat.shouldShowRequestPermissionRationale( MainActivity.this, Manifest.permission.READ_PHONE_STATE ) ) {
				//Show Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
				builder.setTitle( "Need Storage Permission" );
				builder.setMessage( "This app needs storage permission." );
				builder.setPositiveButton( "Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						dialog.cancel();
						ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.READ_PHONE_STATE }, READ_PHONE_STATE_PERMISSION_CONSTANT );
					}
				} );
				builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						dialog.cancel();
					}
				} );
				builder.show();
			}
			else if ( preferenceUserData.getBoolean( Manifest.permission.READ_PHONE_STATE, false ) ) {
				//Previously Permission Request was cancelled with 'Dont Ask Again',
				// Redirect to Settings after showing Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
				builder.setTitle( "Need Storage Permission" );
				builder.setMessage( "This app needs storage permission." );
				builder.setPositiveButton( "Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						dialog.cancel();
						sentToSettings = true;
						Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
						Uri    uri    = Uri.fromParts( "package", getPackageName(), null );
						intent.setData( uri );
						startActivityForResult( intent, REQUEST_PERMISSION_SETTING );
						Toast.makeText( getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG ).show();
					}
				} );
				builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which ) {
						dialog.cancel();
					}
				} );
				builder.show();
			}
			else {
				//just request the permission
				ActivityCompat.requestPermissions( MainActivity.this, new String[]{ Manifest.permission.READ_PHONE_STATE }, READ_PHONE_STATE_PERMISSION_CONSTANT );
			}

			SharedPreferences.Editor editor = preferenceUserData.edit();
			editor.putBoolean( Manifest.permission.READ_PHONE_STATE, true );
			editor.commit();


		}
		else {
			//You already have the permission, just go ahead.
			proceedAfterPermission();
		}
	}

	private void proceedAfterPermission() {
		//We've got the permission, now we can proceed further
		//  Toast.makeText(getBaseContext(), "We got the Phone State Permission", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult( requestCode, resultCode, data );
		switch ( requestCode ) {
			case REQUEST_PERMISSION_SETTING:

				if ( ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
					//Got Permission
					proceedAfterPermission();
				}
				break;
			case REQUEST_CODE_SYSTEM_WINDOW:
				if ( Settings.canDrawOverlays( this ) ) {
					// continue here - permission was granted
					isOverLayPermissionGranted = true;
				}
				break;
		}
	}

	@NeedsPermission( { Manifest.permission.READ_PHONE_STATE/*, Manifest.permission.SYSTEM_ALERT_WINDOW*/ } )
	public void startMyService() {

	}


	@Override
	protected void onPostResume() {
		super.onPostResume();
		if ( sentToSettings ) {
			if ( ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
				//Got Permission
				proceedAfterPermission();
			}
		}
	}

	public void checkDrawOverlayPermission() {
		/** check if we already  have permission to draw over other apps */
		if ( !Settings.canDrawOverlays( MainActivity.this ) ) {
			/** if not construct intent to request permission */
			Intent intent = new Intent( Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
			                            Uri.parse( "package:" + getPackageName() ) );
			/** request permission via start activity for result */
			startActivityForResult( intent, REQUEST_CODE_SYSTEM_WINDOW );
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		startService( new Intent( this, CallTrackerIntentService.class ) );
		if ( !isScreenBlockOn ) {
			startService( new Intent( getApplicationContext(), ScreenBlockService.class ) );
		}
	}
}
