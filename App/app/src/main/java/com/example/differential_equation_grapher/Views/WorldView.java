package com.example.differential_equation_grapher.Views;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.camera.DefaultCamera;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.worldObjects.ScreenText;

//this is a View that you should put in the xml file like buttons or text fields
public class WorldView extends View {
    private static final String CLASS_NAME = "WorldView";

    private Paint paint;



    //activities should set the desired camera
    //is never null. a default camera is made in the constructor
    //The screen width and height are not known in the constructor, so they are set in the draw method
    private Camera camera;
    //world view makes its own world and input status
    private World world;
    private InputStatus inputStatus;
    private Activity activity;//activity the view is in

    //inputStatus.getmScaleGestureDetector().getScaleFactor();returns the zoom multiplier of the last change in movement
    //pinching consists of many rapid inputs, each with a scale factor (usually) near 1.0
    private ScaleGestureDetector mScaleGestureDetector;
    private boolean scaleGestureWasInProgress;
    private GestureDetector gestureDetector;


    public WorldView(Context context) {
        super(context);

        init(context,null);
    }

    public WorldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context,attrs);
    }

    public WorldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    public WorldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context,attrs);
    }

    //initiate method
    //width and height are not known here
    private void init(Context context,@Nullable AttributeSet set){
        setWorld(new World());
        this.inputStatus = new InputStatus(this);
        this.mScaleGestureDetector = new ScaleGestureDetector(context,new ScaleListener());
        this.gestureDetector = new GestureDetector(new MyGestureListener());

        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setCameraToDefault();



//        from stackoverflow: start game is called after the width and height are known
//        post(new Runnable() {
//            @Override
//            public void run() {
//                initSizeKnown(); //width and height is ready
//            }
//        });

    }

//    private void initSizeKnown() {
//        setCamera(new CenterWidthHeightCamera(getWidth()/2f,getWidth()/2f,getWidth(),getHeight(),getWidth(),getHeight()));//default values same as view size
//        Log.d(CLASS_NAME,"width in init size known method: " + getWidth());
//    }

    public void setWorld(World world) {
        this.world = world;
        world.setView(this);
    }

    public World getWorld() {
        return world;
    }

    //does not let the camera be null
    public void setCamera(Camera camera){
        if(camera==null){
            setCameraToDefault();
        }else{
            this.camera = camera;
        }
    }

    public Camera getCamera(){
        return camera;
    }

    public InputStatus getInputStatus() {
        return inputStatus;
    }

    public int getScreenWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        Log.d("CLASS_NAME", "width: " + size.x);
        return size.x;
    }

    public int getScreenHeight() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        Log.d("CLASS_NAME", "height: " + size.y);
        return size.y;
    }

    private void setCameraToDefault(){
        setCamera(new DefaultCamera());
    }

    //returns the activity or null if it can't find it
    ////////call this on constructor?
    public Activity getActivity() {
        if(activity!=null){
            return activity;
        }

        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity) context;
                return activity;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }


    public void redraw() {
        Log.d(CLASS_NAME,"manual redraw (called redraw in WorldView)");
//        invalidate();//redraw: calls onDraw(canvas)
        postInvalidate();//redraw: calls onDraw(canvas)
    }

    //knows width and height by now
    @Override
    protected void onDraw(Canvas canvas){
        camera.setScreenWidth(getWidth());//width and height only set at the last minute because they were 0 in init method
        camera.setScreenHeight(getHeight());//they are set on every draw, but that shouldn't take much time, (it might help with changing the screen orientation)
        camera.setPixelsPerInchX(getResources().getDisplayMetrics().xdpi);
        camera.setPixelsPerInchY(getResources().getDisplayMetrics().ydpi);

        canvas.save();

        //draw the world objects using the world coordinate system
        camera.transformCanvas(canvas);
        world.draw(this,paint,canvas,camera);

        //draw the world's screen objects after restoring the canvas coordinate system back to normal
        canvas.restore();
        world.drawOnScreen(this,paint,canvas,camera);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mScaleGestureDetector.onTouchEvent(event);//keeps the sgd informed on what events are happening so it can detect scaling
        if(mScaleGestureDetector.isInProgress()){
            return true;/////////return false?     stop any other events firing during scaling
        }
        gestureDetector.onTouchEvent(event);

        if(event.getPointerCount()>=2){
            scaleGestureWasInProgress = true;
        }

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(CLASS_NAME,"action down");

                if(!scaleGestureWasInProgress){
                    inputStatus.fingerDown(event.getX(),event.getY(),camera);
                    world.reactToOneFingerDown(inputStatus);
                    return true;
                }

            case MotionEvent.ACTION_UP:
                Log.d(CLASS_NAME,"action up");
                if(event.getPointerCount()==1){
                    //if the last finger got up
                    scaleGestureWasInProgress = false;
                    world.reactToOneFingerUp(inputStatus);
                    return true;
                }



            case MotionEvent.ACTION_MOVE:
                Log.d(CLASS_NAME,"action move");
                if(!scaleGestureWasInProgress){
                    inputStatus.fingerMove(event.getX(),event.getY(),camera);
                    world.reactToOneFingerMove(inputStatus);
                    return true;
                }
                if(inputStatus.isFingerDown()){

                }else{

                }
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(CLASS_NAME,"Action was CANCEL");
                return true;

            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(CLASS_NAME,"Movement occurred outside bounds of current screen element");
                return true;

            default :
                Log.d(CLASS_NAME,"unrecognized motion event");
                return super.onTouchEvent(event);
        }
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            inputStatus.setScaleFactor(scaleFactor);
            Log.d(CLASS_NAME, "scale factor: " + scaleFactor);
            world.reactToPinch(inputStatus);
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("", "Longpress detected");
            world.reactToLongPress(inputStatus);
        }
    }



}