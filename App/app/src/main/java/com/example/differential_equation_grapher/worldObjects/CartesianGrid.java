package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.differential_equation_grapher.R;
import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class CartesianGrid extends WorldObject implements DrawableWorldObject, Grid {
    private static final String CLASS_NAME = "CartesianGrid";
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private final float TEXT_SIZE = 50;
    private static final float MIN_DISTANCE_BETWEEN_LINES = 0.1f;//in inches

    public CartesianGrid(World world) {
        super(world);
        setName("CartesianGrid");
    }

    @Override
    public float getClosestSignificantXTo(float rawX){
        float spacing = getMinorSpacingX(getWorld().getWorldView().getCamera());
        return (float) MyMath.roundToNearestMultipleOf(rawX,spacing);
    }
    @Override
    public float getClosestSignificantYTo(float rawY){
        float spacing = getMinorSpacingY(getWorld().getWorldView().getCamera());
        return (float) MyMath.roundToNearestMultipleOf(rawY,spacing);
    }
    @Override
    public float getClosestSignificantMagnitudeFromOrigin(float rawMagnitude){
        return rawMagnitude;
    }
    @Override
    public float getClosestSignificantTheta(float rawTheta){
        return rawTheta;
    }

    //distance between minor grey lines
    public float getMinorSpacingX(Camera camera){
        float density = camera.getPixelsPerInchX();
        float zoom = camera.getUnitsPerPixelX();
        return (float) Math.pow(10, Math.ceil(Math.log10(zoom*MIN_DISTANCE_BETWEEN_LINES*density)));
    }
    public float getMinorSpacingY(Camera camera){
        float density = camera.getPixelsPerInchY();
        float zoom = camera.getUnitsPerPixelY();
        return (float) Math.pow(10, Math.ceil(Math.log10(zoom*MIN_DISTANCE_BETWEEN_LINES*density)));
    }

    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
//        /////////
//        ScreenText screenText = (ScreenText) getWorld().getWorldObject("Screen Text");
//        ///////
////        screenText.setString("pix/inchX: " + camera.getPixelsPerInchX());
//        screenText.setString(": " + camera.getUnitsPerPixelX()*MIN_DISTANCE_BETWEEN_LINES*camera.getPixelsPerInchX());
//        /////////

        float left = camera.getLowestX();
        float right = camera.getHighestX();
        float top = camera.getHighestY();
        float bottom = camera.getLowestY();

        float unitsPerPixelX = camera.getUnitsPerPixelX();

        float spacing = getMinorSpacingX(camera);

        float xStart = (float) Math.ceil(left/spacing)*spacing;
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        for(float i=xStart;i<right;i+=spacing){
            canvas.drawLine(i, top, i, bottom,paint);
        }

        float yStart = (float) Math.ceil(bottom/spacing)*spacing;
        for(float i=yStart;i<top;i+=spacing){
            canvas.drawLine(left, i, right, i,paint);
        }

        //thicker lines
        spacing*=10;

        //set text properties
        paint.setTextSize(TEXT_SIZE);//multiply by units per pixel to keep the same size at all zoom levels

        xStart = (float) Math.ceil(left/spacing)*spacing;

        int majorColor = getWorld().getWorldView().getActivity().getResources().getColor(R.color.majorGridLines);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS*2);
        for(float i=xStart;i<right;i+=spacing){
            paint.setColor(majorColor);
            canvas.drawLine(i, top, i, bottom,paint);
            paint.setColor(Color.BLACK);
            drawText(getTickMarkString(i),i+5*unitsPerPixelX, bottom+5*unitsPerPixelX,camera.getUnitsPerPixelX(),camera.getUnitsPerPixelY(),paint,canvas);
        }

        yStart = (float) Math.ceil(bottom/spacing)*spacing;
        for(float i=yStart;i<top;i+=spacing){
            paint.setColor(majorColor);
            canvas.drawLine(left, i, right, i,paint);
            paint.setColor(Color.BLACK);
            drawText(getTickMarkString(i),left+5*unitsPerPixelX, i+20*unitsPerPixelX,camera.getUnitsPerPixelX(),camera.getUnitsPerPixelY(),paint,canvas);
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS*2.5f);
        canvas.drawLine(left,0f,right,0f,paint);
        canvas.drawLine(0f,bottom,0f,top,paint);

    }

    //canvas is mutated temprarily so text won't glitch with weird size values
    private void drawText(String text, float x, float y,float scaleX,float scaleY, Paint paint,Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scaleX,scaleY);
        canvas.drawText(text,0,0,paint);
        canvas.restore();

    }

    private String getTickMarkString(double value){
        if(value==0){
            return "0";
        }

        if (Math.abs(value)<0.001){
            return String.format("%e",value);
        }else if(Math.abs(value)>=100000){
            return String.format("%.0e",value);
        }else{
            return MyMath.toNiceString(value,5);
        }
    }
}
