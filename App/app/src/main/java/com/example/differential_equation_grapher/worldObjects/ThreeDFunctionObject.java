package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.CaseMap;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.Complex;
import com.example.differential_equation_grapher.function.ComplexFunction;
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.function.StandardFunctionMultiParam;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.function.Variable;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.helper.LinearNumberToColorConverter;
import com.example.differential_equation_grapher.helper.NumberToColorConverter;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.PinchReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ThreeDFunctionObject extends WorldObject implements DrawableWorldObject, OneFingerReactWorldObject, PinchReactWorldObject {
    private static final float LOW_DEF_SAMPLES_PER_INCH = 20;
    private static final float HIGH_DEF_SAMPLES_PER_INCH = 50;
    private static float samplesPerInch;

    private StandardFunctionMultiParam functionMultiParam;
    private StandardFunctionMultiParam partialDerivativeX;
    private StandardFunctionMultiParam partialDerivativeY;

    private NumberToColorConverter numberToColorConverter;

    public ThreeDFunctionObject(World world, StandardFunctionMultiParam functionMultiParam) {
        super(world);
        setFunction(functionMultiParam);
        numberToColorConverter = new LinearNumberToColorConverter(0,10);
        this.samplesPerInch = HIGH_DEF_SAMPLES_PER_INCH;
    }

    public StandardFunctionMultiParam getFunction(){
        return functionMultiParam;
    }
    public StandardFunctionMultiParam getPartialDerivativeX() {
        return partialDerivativeX;
    }

    public StandardFunctionMultiParam getPartialDerivativeY() {
        return partialDerivativeY;
    }

    public float evaluate(float x,float y){
        return (float) functionMultiParam.evaluate(x,y);
    }
    public float getGradientX(float x,float y){
        return (float) partialDerivativeX.evaluate(x,y);
    }
    public float getGradientY(float x,float y){
        return (float) partialDerivativeY.evaluate(x,y);
    }

    //make a copy, simplify it, then use it to get the derivative
    public void setFunction(StandardFunctionMultiParam functionMultiParam) {
        this.functionMultiParam = new StandardFunctionMultiParam(functionMultiParam.getFunction().copy(),'x','y');
        this.functionMultiParam.getFunction().simplify();
        this.partialDerivativeX = this.functionMultiParam.getDerivative('x');
        this.partialDerivativeY = this.functionMultiParam.getDerivative('y');
    }

    public void setMin(double min){
        numberToColorConverter.setMin((float) min);
    }
    public void setMax(double max){
        numberToColorConverter.setMax((float) max);
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        if(functionMultiParam.getFunction().getExpression() instanceof Undefined){
            return;
        }
        float left = camera.getLowestX();
        float right = camera.getHighestX();
        float top = camera.getHighestY();
        float bottom = camera.getLowestY();

        float stepX = camera.getUnitsPerInchX()/samplesPerInch;
        float stepY = camera.getUnitsPerInchY()/samplesPerInch;
        float stepXOver2 = stepX/2;
        float stepYOver2 = stepY/2;
        float lastX = right+stepXOver2;
        float lastY = top+stepYOver2;
        float unitsPerPixelX = camera.getUnitsPerPixelX();//draw a pixel bigger to prevent white lines
        float unitsPerPixelY = camera.getUnitsPerPixelY();
        for(float x=left+stepXOver2;x<=lastX;x+=stepX){
            for(float y=bottom+stepYOver2;y<=lastY;y+=stepY){
                double answer = getFunction().evaluate(x,y);
                paint.setColor(numberToColorConverter.getColor((float) answer));
                canvas.drawRect(x-stepXOver2,y+stepYOver2+unitsPerPixelY,x+stepXOver2+unitsPerPixelX,y-stepYOver2,paint);
            }
        }
    }


    @Override
    public void reactToOneFingerDown(InputStatus inputStatus) {
        //do nothing
    }

    //lower the resolution when it guesses it is going to need to redraw multiple times really quickly
    @Override
    public void reactToOneFingerMove(InputStatus inputStatus) {
        samplesPerInch = LOW_DEF_SAMPLES_PER_INCH;
    }
    @Override
    public void reactToPinch(InputStatus inputStatus) {
        samplesPerInch = LOW_DEF_SAMPLES_PER_INCH;
    }

    @Override
    public void reactToOneFingerUp(InputStatus inputStatus) {
        samplesPerInch = HIGH_DEF_SAMPLES_PER_INCH;
        getWorld().getWorldView().redraw();
    }


}
