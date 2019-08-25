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

public class ParametricFunctionObject extends WorldObject implements DrawableWorldObject {
    private static final float LINE_THICKNESS = 0.02f;//in inches

    private StandardFunction xFunction;
    private StandardFunction yFunction;

    private float tStart;
    private float tEnd;
    private int numberOfSteps;

    private LineDrawer lineDrawer;
    private int color;

    public ParametricFunctionObject(World world, StandardFunction xFunction,StandardFunction yFunction) {
        super(world);
        setFunctionX(xFunction);
        setFunctionY(yFunction);

        //default values
        this.tStart = 0;
        this.tEnd = 10f;
        this.numberOfSteps = 50;

        this.lineDrawer = new LineDrawer();
        this.color = Color.BLACK;
    }

    public StandardFunction getFunctionX(){
        return xFunction;
    }
    public StandardFunction getFunctionY(){
        return yFunction;
    }

    //make a copy, simplify it
    public void setFunctionX(StandardFunction standardFunction) {
        this.xFunction = new StandardFunction(standardFunction.getFunction().copy(),'t');
        this.xFunction.getFunction().simplify();
    }
    //make a copy, simplify it
    public void setFunctionY(StandardFunction standardFunction) {
        this.yFunction = new StandardFunction(standardFunction.getFunction().copy(),'t');
        this.yFunction.getFunction().simplify();
    }

    public float getTStart() {
        return tStart;
    }

    public void setTStart(float thetaStart) {
        this.tStart = thetaStart;
    }

    public float getTEnd() {
        return tEnd;
    }

    public void setTEnd(float thetaEnd) {
        this.tEnd = thetaEnd;
    }

    public float getTStep() {
        return (tEnd-tStart)/numberOfSteps;
    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    public int getNumberOfSteps(){
        return numberOfSteps;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }



    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        float tStep = Math.abs(getTStep());
        if(tStep==0){
            return;
        }
        lineDrawer.getReadyToDraw();
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        paint.setColor(color);
        float tLow = Math.min(tStart,tEnd);
        float tHigh = Math.max(tStart,tEnd);
        for(float t=tLow;t<=tHigh;t+=tStep){
            float x = (float) xFunction.evaluate(t);
            float y = (float) yFunction.evaluate(t);
            lineDrawer.drawTo(x,y,canvas,paint);
        }

        //tEnd probably wasn't reached due to rounding errors on adding
        float x = (float) xFunction.evaluate(tHigh);
        float y = (float) yFunction.evaluate(tHigh);
        lineDrawer.drawTo(x,y,canvas,paint);


    }

    //draws a red line representing a t range between the min and max t values
    //for draw on Screen, not world
    public void drawTRange(float tLow,float tHigh,Paint paint, Canvas canvas,Camera camera){
        final float PROGRESS_BAR_THICKNESS = 0.05f;//in inches
        paint.setColor(Color.RED);
        float thicknessInPixels = PROGRESS_BAR_THICKNESS*camera.getPixelsPerInchY();
        paint.setStrokeWidth(thicknessInPixels);

        float lowPercent = (tLow-tStart)/(tEnd-tStart);
        float highPercent = (tHigh-tStart)/(tEnd-tStart);
        float lowPixel = lowPercent*camera.getScreenWidth();
        float highPixel = highPercent*camera.getScreenWidth();
        canvas.drawRect(lowPixel,camera.getScreenHeight()-thicknessInPixels,highPixel,camera.getScreenHeight(),paint);
    }

    public float getTFromXPositionInCamera(float x,Camera camera){
        float percent = (x-camera.getLeft())/(camera.getRight()-camera.getLeft());
        return getTStart()+ (getTEnd()-getTStart())*percent;
    }


    @Override
    public String toString() {
        return "x(t)= " + xFunction.getFunction().getExpression() + ", y(t)= " + yFunction.getFunction().getExpression();
    }
}
