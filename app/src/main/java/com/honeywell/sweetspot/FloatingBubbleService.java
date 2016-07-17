package com.honeywell.sweetspot;

/**
 * Created by pablo on 17/07/16.
 */

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingBubble;
//    boolean bolEstado = false;            // used to toggle the scanner state (On/Off)

    public void onCreate() {
        super.onCreate();
        floatingBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingBubble.setImageResource(R.drawable.floating_button);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //here is all the science of params
        final LayoutParams myParams = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 100;
        myParams.alpha = 90;

        // add a floatingfacebubble icon in window
        windowManager.addView(floatingBubble, myParams);

        try {
            //for moving the picture on touch and slide
            floatingBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                private long touchPressedTime =0;
                private long touchLastTime = 0;
                private int touchNumber = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            touchLastTime = System.currentTimeMillis() - touchStartTime;
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();

                            //show preferences on double tap
                            /*if ((touchLastTime > 50) && (touchLastTime < 300) && (initialTouchX == event.getX() + initialX)) {
                                touchNumber++;
                                Log.e("*****SWEET-SPOT******", "Double Touch " + touchNumber);
                                if (touchNumber >= 10) {
                                    windowManager.removeView(floatingBubble);
                                    stopSelf();
                                    Log.e("*****SWEET-SPOT******", "Service Stopped ");
                                    Toast myToast= Toast.makeText(getApplicationContext(), "Leaving SweetSpot...", Toast.LENGTH_LONG);
                                    myToast.show();
                                    Log.e("*****SWEET-SPOT******", "Leaving SweetSpot...");
                                    SimulateScanKey(false);

                                    return false;
                                }

                            } else {
                                touchNumber = 0;
                            }*/

                            //bolEstado= !bolEstado;
                            SimulateScanKey(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            //remove face bubble on long press.
                            touchPressedTime = System.currentTimeMillis() - touchStartTime;
                            Log.e("*****SWEET-SPOT******", "InitialTouchX: "+initialTouchX);
                            Log.e("*****SWEET-SPOT******", "event.getX() + initial: "+(event.getX() + initialX));
                            Log.e("*****SWEET-SPOT******", "Time Pressed: "+(touchPressedTime));

                            if ( touchPressedTime > 10000 && (round2Tenth(initialTouchX) == round2Tenth(event.getX() + initialX))) {
                                Log.e("*****SWEET-SPOT******", "Long Touch ");
                                windowManager.removeView(floatingBubble);
                                stopSelf();
                                Log.e("*****SWEET-SPOT******", "Service Stopped ");
                                Toast myToast= Toast.makeText(getApplicationContext(), "Leaving SweetSpot...", Toast.LENGTH_LONG);
                                myToast.show();
                                Log.e("*****SWEET-SPOT******", "Leaving SweetSpot...");
                                SimulateScanKey(false);
                                return false;
                            }

                            SimulateScanKey(false);
                            break;
                        case MotionEvent.ACTION_MOVE:

                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;

                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void SimulateScanKey(boolean KeyDown) {
        KeyEvent SendKeyEvent;
        Intent sendIntentDown = new Intent("com.honeywell.intent.action.SCAN_BUTTON");
        if (KeyDown) {
            SendKeyEvent = new KeyEvent(0, 0);

        } else {
            SendKeyEvent = new KeyEvent(1, 0);
        }
        sendIntentDown.putExtra("android.intent.extra.KEY_EVENT", SendKeyEvent);
        this.sendBroadcast(sendIntentDown);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private int round2Tenth(float value)
    {
        if (0 > value) {
            return (int) Math.ceil(value / 10) * 10;
        }
        return (int) Math.floor(value / 10) * 10;
    }

}
