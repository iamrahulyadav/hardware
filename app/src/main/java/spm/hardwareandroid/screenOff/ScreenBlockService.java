package spm.hardwareandroid.screenOff;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import spm.hardwareandroid.AwesomeGestureListener;
import spm.hardwareandroid.MainActivity;
import spm.hardwareandroid.R;

/**
 * Created by webwerks on 5/2/16.
 */
public class ScreenBlockService extends IntentService{
    View mView;
    Context mContext;
    ImageView imageviewCross;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams paramsImageCross;
    WindowManager wm;
    private int _xDelta;
    private int _yDelta;

    private int xImageviewCross;
    private int yImageviewCross;

    private LayoutInflater inflater;

    PopupWindow popupWindow;

	private final int REQ_CODE_SPEECH_INPUT = 100;

	public ScreenBlockService() {
		super("ScreenBlockService");
	}

	@Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        mView = new ImageView(this);
        mView.setBackgroundResource( R.drawable.shadow);

        imageviewCross = new ImageView(this);
        imageviewCross.setBackgroundResource(R.drawable.cross);

        mView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

         params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.width=ViewGroup.LayoutParams.MATCH_PARENT;
        params.height=ViewGroup.LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
        params.setTitle("Load Average");
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);


        paramsImageCross = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        paramsImageCross.width=100;
        paramsImageCross.height=100;
        paramsImageCross.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        wm.addView(imageviewCross, paramsImageCross);
        imageviewCross.setVisibility(View.INVISIBLE);


        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final Point size = new Point();
        xImageviewCross =  metrics.heightPixels;
        yImageviewCross =  metrics.heightPixels;



        final GestureDetector gestureDetector = new GestureDetector(this, new AwesomeGestureListener());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            public boolean onTouch(View view, MotionEvent event) {

                Log.i("spm","touuched  ");

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();



                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                       inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                      imageviewCross.setVisibility(View.INVISIBLE);

                        break;
                    case MotionEvent.ACTION_UP:
                        imageviewCross.setVisibility(View.INVISIBLE);
                        Log.i("POS ", "my image pos " + params.x + " " + params.y+" IMmage x "+xImageviewCross+" y "+xImageviewCross);

                        if(xImageviewCross==params.y && yImageviewCross == params.y)
                            mView.setVisibility(View.INVISIBLE);

	                    if(xImageviewCross>= 1184 ){
		                    stopSelf();
	                    }

                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:


                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        wm.updateViewLayout(mView, params);
                        return true;

                }

                return gestureDetector.onTouchEvent(event);
            }
        };

        mView.setOnTouchListener(gestureListener);


        /*mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        Log.i("val   ","  "+_xDelta+"  "+_yDelta);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = X - _xDelta;
                        layoutParams.topMargin = Y - _yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                return true;
            }
        });*/



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

	@Override
	protected void onHandleIntent( @Nullable Intent intent ) {

	}


	public static void blockTheScreenNow(Context context){
	    context.startService( new Intent(context,ScreenBlockService.class) );
	   // (( MainActivity )context).finish();
    }

}
