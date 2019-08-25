package com.example.differential_equation_grapher.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class World {
    private static final String CLASS_NAME = "World";

    private WorldView worldView;

    //two separate timers because to change the tick period, the tick timer needs to cancel everything it had scheduled and get created again
    private Timer scheduleTimer;//is null until it is needed
    private Timer tickTimer;//is null at first. set when period is set
    private int tickPeriodMills;//tickTimer is automaticlly started and stopped when this is changed
//    private float tickPeriodSecs;//tickPeriodMills/1000   maintained field for speed

    private List<WorldObject> worldObjects;
    private List<DrawableWorldObject> drawableWorldObjects;
    private List<DrawScreenWorldObject> drawScreenWorldObjects;
    private List<TickWorldObject> tickWorldObjects;
    private List<OneFingerReactWorldObject> oneFingerReactWorldObjects;
    private List<PinchReactWorldObject> pinchReactWorldObjects;
    private List<LongPressReactWorldObject> longPressReactWorldObjects;

    public World(){
        this.worldObjects = new ArrayList<>();
        this.drawableWorldObjects = new ArrayList<>();
        this.drawScreenWorldObjects = new ArrayList<>();
        this.tickWorldObjects = new ArrayList<>();
        this.oneFingerReactWorldObjects = new ArrayList<>();
        this.pinchReactWorldObjects = new ArrayList<>();
        this.longPressReactWorldObjects = new ArrayList<>();
    }

    public WorldView getWorldView() {
        return worldView;
    }
    public void setView(WorldView worldView) {
        this.worldView = worldView;
    }

    public void add(WorldObject object) {
        worldObjects.add(object);
        if(object instanceof DrawableWorldObject){
            drawableWorldObjects.add((DrawableWorldObject) object);
        }
        if(object instanceof DrawScreenWorldObject){
            drawScreenWorldObjects.add((DrawScreenWorldObject) object);
        }
        if(object instanceof TickWorldObject){
            tickWorldObjects.add((TickWorldObject) object);
        }
        if(object instanceof OneFingerReactWorldObject){
            oneFingerReactWorldObjects.add((OneFingerReactWorldObject) object);
        }
        if(object instanceof PinchReactWorldObject){
            pinchReactWorldObjects.add((PinchReactWorldObject) object);
        }
        if(object instanceof LongPressReactWorldObject){
            longPressReactWorldObjects.add((LongPressReactWorldObject) object);
        }
    }

    public void delete(WorldObject object) {
        worldObjects.remove(object);
        if(object instanceof DrawableWorldObject){
            drawableWorldObjects.remove(object);
        }
        if(object instanceof DrawScreenWorldObject){
            drawScreenWorldObjects.remove(object);
        }
        if(object instanceof TickWorldObject){
            tickWorldObjects.remove(object);
        }
        if(object instanceof OneFingerReactWorldObject){
            oneFingerReactWorldObjects.remove(object);
        }
        if(object instanceof PinchReactWorldObject){
            pinchReactWorldObjects.remove(object);
        }
        if(object instanceof LongPressReactWorldObject){
            longPressReactWorldObjects.remove(object);
        }
    }

    public boolean containsWorldObject(WorldObject worldObject) {
        return worldObjects.contains(worldObject);
    }

    //returns null if can't find
    //if two names are the same, it just returns the first one if finds
    public WorldObject getWorldObject(String name){
        for(int i=0;i<worldObjects.size();i++){
            WorldObject worldObject = worldObjects.get(i);
            String thisName = worldObject.getName();
            if(thisName!=null && thisName.equals(name)){
                return worldObject;
            }
        }

        return null;
    }

    public void draw(WorldView worldView, Paint paint, Canvas canvas, Camera camera) {
        for(int i=0;i<drawableWorldObjects.size();i++){
            drawableWorldObjects.get(i).draw(worldView,paint,canvas,camera);
        }
    }

    //canvas is put back when this is called
    public void drawOnScreen(WorldView worldView, Paint paint, Canvas canvas, Camera camera) {
        for(int i=0;i<drawScreenWorldObjects.size();i++){
            drawScreenWorldObjects.get(i).drawOnScreen(worldView,paint,canvas,camera);
        }
    }

    public void sendToFrontDrawableWrold(DrawableWorldObject drawableWorldObject){
        if(drawableWorldObjects.remove(drawableWorldObject)){
            drawableWorldObjects.add(drawableWorldObject);
        }
    }
    public void sendToBackDrawableWrold(DrawableWorldObject drawableWorldObject){
        if(drawableWorldObjects.remove(drawableWorldObject)){
            drawableWorldObjects.add(0,drawableWorldObject);
        }
    }

    public void sendToFrontDrawableScreen(DrawScreenWorldObject drawableScreenWorldObject){
        if(drawScreenWorldObjects.remove(drawableScreenWorldObject)){
            drawScreenWorldObjects.add(drawableScreenWorldObject);
        }
    }
    public void sendToBackDrawableScreen(DrawScreenWorldObject drawableScreenWorldObject){
        if(drawScreenWorldObjects.remove(drawableScreenWorldObject)){
            drawScreenWorldObjects.add(0,drawableScreenWorldObject);
        }
    }

    public void scheduleWorldAction(final WorldAction action,long delayInMills){
        if(scheduleTimer==null){
            scheduleTimer = new Timer(true);
        }
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                action.doAction();
            }
        };
        tickTimer.schedule(timerTask,delayInMills);
    }

    //if period <= 0 it takes it as making the ticks stop
    //timer can't easily change the period the old timer must be canceled and a new one with the new period must take its place
    public void setTickPeriodMills(int milisecondsPerTick){
        if(milisecondsPerTick<=0){
            this.tickPeriodMills = 0;
            if(tickTimer !=null){
                tickTimer.cancel();
                tickTimer = null;
            }

        }else{
            this.tickPeriodMills = milisecondsPerTick;//remember the period because the timer will not
            if(tickTimer !=null){
                tickTimer.cancel();
            }

            tickTimer = new Timer(true);///////////////creating a new tickTimer every time, but changing the period often is not anticipated
            TimerTask timerTask = new TimerTask(){
                @Override
                public void run() {
                    Log.d(CLASS_NAME, "New Tick");
                    doOnTick();
                    Log.d(CLASS_NAME, "New Draw");//////////////make a way to decouple tick from draw?
                    worldView.postInvalidate();//redraw: calls onDraw(canvas)
                }
            };
            tickTimer.scheduleAtFixedRate(timerTask,0,milisecondsPerTick);///////////use something better than a delay?

        }

    }

    public int getTickPeriodMills(){
        return tickPeriodMills;
    }

    public float getTickPeriodSecs() {
        return tickPeriodMills/1000f;
    }

    private void doOnTick(){
        for(int i=0;i<tickWorldObjects.size();i++){
            tickWorldObjects.get(i).doOnTick();
        }
    }

    public InputStatus getInputStatus(){
        return worldView.getInputStatus();
    }




    public void reactToOneFingerDown(InputStatus inputStatus){
        for(int i=0;i<oneFingerReactWorldObjects.size();i++){
            oneFingerReactWorldObjects.get(i).reactToOneFingerDown(inputStatus);
        }
    }
    public void reactToOneFingerMove(InputStatus inputStatus){
        for(int i=0;i<oneFingerReactWorldObjects.size();i++){
            oneFingerReactWorldObjects.get(i).reactToOneFingerMove(inputStatus);
        }
    }
    public void reactToOneFingerUp(InputStatus inputStatus){
        for(int i=0;i<oneFingerReactWorldObjects.size();i++){
            oneFingerReactWorldObjects.get(i).reactToOneFingerUp(inputStatus);
        }
    }

    public void reactToPinch(InputStatus inputStatus){
        for(int i=0;i<pinchReactWorldObjects.size();i++){
            pinchReactWorldObjects.get(i).reactToPinch(inputStatus);
        }
    }

    public void reactToLongPress(InputStatus inputStatus){
        for(int i=0;i<longPressReactWorldObjects.size();i++){
            longPressReactWorldObjects.get(i).reactToLongPress(inputStatus);
        }
    }

    //delete stuff like timers
    public void unload() {
        if(scheduleTimer!=null){
            scheduleTimer.cancel();
            scheduleTimer = null;
        }
        if(tickTimer!=null){
            tickTimer.cancel();
            tickTimer = null;
        }
    }


}
