package spm.hardwareandroid;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class AwesomeGestureListener extends SimpleOnGestureListener {
	
	public enum GestureType {UNKNOWN, SINGLE_TAP, DOUBLE_TAP, FLING, SCROLL};
	
	private GestureType lastGestureType;
	
	public AwesomeGestureListener() {
		super();
		lastGestureType = GestureType.UNKNOWN;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		lastGestureType = GestureType.DOUBLE_TAP;		
		return super.onDoubleTap(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		//System.out.println("Down");
		lastGestureType = GestureType.UNKNOWN;		
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		lastGestureType = GestureType.FLING;
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		//lastGestureType = GestureType.LONG_PRESS;
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		lastGestureType = GestureType.SCROLL;
		return super.onScroll(e1, e2, distanceX, distanceY);
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		lastGestureType = GestureType.SINGLE_TAP;
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {	
		lastGestureType = GestureType.SINGLE_TAP;
		return super.onSingleTapConfirmed(e);
	}

	public GestureType getLastGestureType() {
		return lastGestureType;
	}

	
}
