package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.differential_equation_grapher.Views.InputStatus;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.Complex;
import com.example.differential_equation_grapher.function.ComplexFunction;
import com.example.differential_equation_grapher.function.StandardFunction;
import com.example.differential_equation_grapher.function.Undefined;
import com.example.differential_equation_grapher.helper.LineDrawer;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.OneFingerReactWorldObject;
import com.example.differential_equation_grapher.world.PinchReactWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class ComplexFunctionObject extends WorldObject implements DrawableWorldObject, OneFingerReactWorldObject, PinchReactWorldObject {
    private static final float LOW_DEF_SAMPLES_PER_INCH = 20;
    private static final float HIGH_DEF_SAMPLES_PER_INCH = 50;
    private static float samplesPerInch;

    private ComplexFunction complexFunction;
    private ComplexFunction derivative;

    public ComplexFunctionObject(World world, ComplexFunction complexFunction) {
        super(world);
        setFunction(complexFunction);
        this.samplesPerInch = HIGH_DEF_SAMPLES_PER_INCH;
    }

    public ComplexFunction getFunction(){
        return complexFunction;
    }

    //make a copy, simplify it, then use it to get the derivative
    public void setFunction(ComplexFunction complexFunction) {
        this.complexFunction = new ComplexFunction(complexFunction.getFunction().copy());
        this.complexFunction.getFunction().simplify();
        this.derivative = complexFunction.getDerivative();
    }

    public ComplexFunction getDerivative() {
        return derivative;
    }

    public Complex evaluate(Complex input){
        return complexFunction.evaluate(input);
    }
    public Complex evaluateDerivative(Complex input){
        return derivative.evaluate(input);
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        if(complexFunction.getFunction().getExpression() instanceof Undefined){
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
        for(float real=left+stepXOver2;real<=lastX;real+=stepX){
            for(float imaginary=bottom+stepYOver2;imaginary<=lastY;imaginary+=stepY){
                Complex answer = getFunction().evaluate(real,imaginary);
                paint.setColor(getColor(answer,camera));
                canvas.drawRect(real-stepXOver2,imaginary+stepYOver2+unitsPerPixelY,real+stepXOver2+unitsPerPixelX,imaginary-stepYOver2,paint);
            }
        }
    }

    private int getColor(Complex answer,Camera camera) {
        float hue = (float) (Complex.standardizeTheta0to2PI(answer.getTheta())/(Math.PI*2)*360);//360: hue measured in degrees
        //todo: max value fix
        float maxCoordinateValue = Math.max(Math.max(Math.max(Math.abs(camera.getBottom()),Math.abs(camera.getTop())),Math.abs(camera.getRight())),Math.abs(camera.getLeft()));
        float lightness = (float) (answer.getMagnitude()/maxCoordinateValue);
        return Color.HSVToColor(new float[]{hue,1f,lightness});//saturation not used. set to max
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
