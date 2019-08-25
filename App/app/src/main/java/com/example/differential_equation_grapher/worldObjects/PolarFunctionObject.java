package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.PolarFunction;
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class PolarFunctionObject extends WorldObject implements DrawableWorldObject {
    private static final float LINE_THICKNESS = 0.02f;//in inches

    private PolarFunction polarFunction;

    private float thetaStart;
    private float thetaEnd;
    private int numberOfSteps;

    private LineDrawer lineDrawer;
    private int color;

    public PolarFunctionObject(World world, PolarFunction polarFunction) {
        super(world);
        setFunction(polarFunction);

        //default values
        this.thetaStart = 0;
        this.thetaEnd = (float) (Math.PI*2);
        this.numberOfSteps = 50;

        this.lineDrawer = new LineDrawer();
        this.color = Color.BLACK;
    }

    public PolarFunction getFunction(){
        return polarFunction;
    }

    //make a copy, simplify it
    public void setFunction(PolarFunction polarFunction) {
        this.polarFunction = new PolarFunction(polarFunction.getFunction().copy());
        this.polarFunction.getFunction().simplify();
    }

    public float getThetaStart() {
        return thetaStart;
    }

    public void setThetaStart(float thetaStart) {
        this.thetaStart = thetaStart;
    }

    public float getThetaEnd() {
        return thetaEnd;
    }

    public void setThetaEnd(float thetaEnd) {
        this.thetaEnd = thetaEnd;
    }

    public float getThetaStep() {
        return (thetaEnd-thetaStart)/numberOfSteps;
    }

    public int getNumberOfSteps(){
        return numberOfSteps;
    }

//    public void setThetaStep(float thetaStep) {
//        this.thetaStep = thetaStep;
//    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float thetaStep = Math.abs(getThetaStep());
        if(thetaStep==0){
            return;
        }
        lineDrawer.getReadyToDraw();
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        paint.setColor(color);
        float thetaLow = Math.min(thetaStart,thetaEnd);
        float thetaHigh = Math.max(thetaStart,thetaEnd);
        for(float theta=thetaLow;theta<=thetaHigh;theta+=thetaStep){
            float radius = (float) polarFunction.evaluate(theta);
            float x = (float) polarFunction.polarToX(radius,theta);////////converting between floats and doubles a lot
            float y = (float) polarFunction.polarToY(radius,theta);
            lineDrawer.drawTo(x,y,canvas,paint);
        }

        //theteEnd probably wasn't reached due to rounding errors on adding
        float radius = (float) polarFunction.evaluate(thetaHigh);
        float x = (float) polarFunction.polarToX(radius,thetaHigh);////////converting between floats and doubles a lot
        float y = (float) polarFunction.polarToY(radius,thetaHigh);
        lineDrawer.drawTo(x,y,canvas,paint);


    }

    public int getQuadrantNumber(float x, float y) {
        if(x>=0&&y>=0){
            return 1;
        }else if(x<0&&y>=0){
            return 2;
        }else if(x<0&&y<0){
            return 3;
        }else if(x>=0&&y<0){
            return 4;
        }else{
            return 0;
        }
    }


}
