package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.LongPressReactWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.TickWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class FieldFunctionFollower extends WorldObject implements DrawableWorldObject , TickWorldObject , LongPressReactWorldObject {
    private static final String CLASS_NAME = "FieldFunctionFollower";
    private static final float LINE_THICKNESS = 0.02f;//in inches

    private float x;//current world position
    private float y;//current world position
    private float radius;//in pixels
    private FieldFunctionObject field;//field it is affected by
    private float speed;//constant to adjust speed

    private PointQueue pointQueue;
    private LineDrawer lineDrawer;


    public FieldFunctionFollower(World world, FieldFunctionObject field, float x, float y) {
        super(world);
        this.field = field;
        this.pointQueue = new PointQueue(100);
        moveTo(x,y);
        this.radius = 0.04f;//in inches;
        this.speed = 1f;
        this.lineDrawer = new LineDrawer();
    }


    public void moveTo(float newX, float newY){
        this.x = newX;
        this.y = newY;

        pointQueue.addPoint(x,y);
    }


    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        //draw tail
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.line_boundary));
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        lineDrawer.getReadyToDraw();
        int size = pointQueue.getSize();
        for(int i=0;i<size;i++){
            lineDrawer.drawTo(pointQueue.getXAt(i),pointQueue.getYAt(i),canvas,paint);
        }
        //draw circle
        paint.setColor(getWorld().getWorldView().getActivity().getResources().getColor(R.color.point));
        canvas.drawCircle(x,y,radius*camera.getUnitsPerInchX(),paint);
    }

    @Override
    public void doOnTick() {
        float arrowX = (float) field.getFieldFunction().evaluateXComponent(x,y);
        float arrowY = (float) field.getFieldFunction().evaluateYComponent(x,y);
        moveTo(x+arrowX*getWorld().getTickPeriodSecs()*speed,y+arrowY*getWorld().getTickPeriodSecs()*speed);
    }

    @Override
    public void reactToLongPress(InputStatus inputStatus) {
        Log.d(CLASS_NAME,"screenX: " + inputStatus.getCurrentFingerScreenX() + " screenY: " + inputStatus.getCurrentFingerScreenY() + "worldX: " + inputStatus.getCurrentFingerWorldX() + " worldY: " + inputStatus.getCurrentFingerWorldY());

        pointQueue.clear();
        moveTo(inputStatus.getCurrentFingerWorldX(),inputStatus.getCurrentFingerWorldY());
    }


    //if limit is reached, old points are dropped out
    private class PointQueue{

        //concurrent arrays.
        private float[] xArray;
        private float[] yArray;

        //is -1 if empty
        private int firstElementIndex;
        private int lastElementIndex;

        public PointQueue(int maxPoints){
            this.xArray = new float[maxPoints];
            this.yArray = new float[maxPoints];
            this.firstElementIndex = -1;
            this.lastElementIndex = -1;
        }

        public void addPoint(float x, float y) {
            if(isEmpty()){
                xArray[0] = x;
                yArray[0] = y;
                firstElementIndex = 0;
                lastElementIndex = 0;
            }else if(isFull()){
                xArray[firstElementIndex] = x;
                yArray[firstElementIndex] = y;
                lastElementIndex = firstElementIndex;
                firstElementIndex = (firstElementIndex+1)%xArray.length;//move one to the right, wrap around if needed

            }else{
                //not full yet
                lastElementIndex = (lastElementIndex+1)%xArray.length;//move one to the right, wrap around if needed
                xArray[lastElementIndex] = x;
                yArray[lastElementIndex] = y;
            }
        }

        private boolean isFull() {
            return (lastElementIndex+1)%xArray.length==firstElementIndex;
        }

        public boolean isEmpty(){
            return firstElementIndex==-1;
        }

        public int getSize(){
            if(isEmpty()){
                return 0;
            }else if(lastElementIndex>=firstElementIndex){
                return lastElementIndex-firstElementIndex+1;
            }else{
                return xArray.length-(firstElementIndex-lastElementIndex)+1;
            }
        }

        public float getXAt(int index){
            if(index>=xArray.length || index <0){
                throw new ArrayIndexOutOfBoundsException();
            }
            return xArray[(firstElementIndex+index)%xArray.length];
        }

        public float getYAt(int index){
            if(index>=yArray.length || index <0){
                throw new ArrayIndexOutOfBoundsException();
            }
            return yArray[(firstElementIndex+index)%yArray.length];
        }

        public void clear() {
            this.firstElementIndex = -1;
            this.lastElementIndex = -1;
        }
    }
}
