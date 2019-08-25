package com.example.differential_equation_grapher.worldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.differential_equation_grapher.Views.WorldView;
import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.myAlgs.MyMath;
import com.example.differential_equation_grapher.world.DrawableWorldObject;
import com.example.differential_equation_grapher.world.World;
import com.example.differential_equation_grapher.world.WorldObject;

public class PolarGrid extends WorldObject implements DrawableWorldObject, Grid {
    private static final String CLASS_NAME = "Polar CartesianGrid";
    private static final float LINE_THICKNESS = 0.01f;//in inches
    private final float TEXT_SIZE = 50;
    private static final float MIN_DISTANCE_BETWEEN_LINES = 0.1f;//in inches

    private enum CameraQuad{ORIGIN,XAXIS,QUAD1,YAXIS,QUAD2,NEGXAXIS,QUAD3,NEGYAXIS,QUAD4}

    public PolarGrid(World world) {
        super(world);
        setName("CartesianGrid");//there should only be one grid in the world at a time
    }


    @Override
    public float getClosestSignificantXTo(float rawX) {
        return rawX;
    }

    @Override
    public float getClosestSignificantYTo(float rawY) {
        return rawY;
    }

    @Override
    public float getClosestSignificantMagnitudeFromOrigin(float rawMagnitude) {
        return (float) MyMath.roundToNearestMultipleOf(rawMagnitude,getMagnitudeStep(null));
    }

    @Override
    public float getClosestSignificantTheta(float rawTheta) {
        return (float) MyMath.roundToNearestMultipleOf(rawTheta,getAngleStep(null));
    }


    @Override
    public void draw(WorldView view, Paint paint, Canvas canvas, Camera camera) {
        CameraQuad cameraQuad = getQuad(camera);
        //find magnitudes
        float distanceToCorner1 = (float) Math.hypot(camera.getHighestX(),camera.getHighestY());
        float distanceToCorner2 = (float) Math.hypot(camera.getLowestX(),camera.getHighestY());
        float distanceToCorner3 = (float) Math.hypot(camera.getLowestX(),camera.getLowestY());
        float distanceToCorner4 = (float) Math.hypot(camera.getHighestX(),camera.getLowestY());
        float minDistance;
        if(cameraQuad==CameraQuad.ORIGIN){
            minDistance = 0;
        }else if(cameraQuad==CameraQuad.XAXIS){
            minDistance = camera.getLowestX();
        }else if(cameraQuad==CameraQuad.YAXIS){
            minDistance = camera.getLowestY();
        }else if(cameraQuad==CameraQuad.NEGXAXIS){
            minDistance = Math.abs(camera.getHighestX());
        }else if(cameraQuad==CameraQuad.NEGYAXIS){
            minDistance = Math.abs(camera.getHighestY());
        }else{
            minDistance = Math.min(Math.min(Math.min(distanceToCorner1,distanceToCorner2),distanceToCorner3),distanceToCorner4);
        }
        float maxDistance = Math.max(Math.max(Math.max(distanceToCorner1,distanceToCorner2),distanceToCorner3),distanceToCorner4);
        float distanceStep = getMagnitudeStep(camera);


        //find angles
        double minAngle;
        double maxAngle;
        if(cameraQuad==CameraQuad.ORIGIN){
            minAngle = 0;
            maxAngle = Math.PI*2;
        }else{
            if(cameraQuad==CameraQuad.XAXIS){//use range -pi to pi to avoid the case the angle wraps around
                maxAngle = MyMath.getPrincipleDirectionFromOrigin(camera.getLowestX(),camera.getHighestY());
                minAngle = MyMath.getPrincipleDirectionFromOrigin(camera.getLowestX(),camera.getLowestY());
            }else{
                double angleToCorner1 = MyMath.getDirectionFromOrigin(camera.getHighestX(),camera.getHighestY());
                double angleToCorner2 = MyMath.getDirectionFromOrigin(camera.getLowestX(),camera.getHighestY());
                double angleToCorner3 = MyMath.getDirectionFromOrigin(camera.getLowestX(),camera.getLowestY());
                double angleToCorner4 = MyMath.getDirectionFromOrigin(camera.getHighestX(),camera.getLowestY());
                minAngle = Math.min(Math.min(Math.min(angleToCorner1,angleToCorner2),angleToCorner3),angleToCorner4);
                maxAngle = Math.max(Math.max(Math.max(angleToCorner1,angleToCorner2),angleToCorner3),angleToCorner4);
            }
        }
        double angleStep = getAngleStep(camera);
        float angleDifference = (float) (maxAngle-minAngle);

        //draw
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);

        //draw circles
        paint.setStyle(Paint.Style.STROKE);//draws an outline instead of filling in the circles

        for(float m=(float) MyMath.roundDownToNearestMultipleOf(minDistance,distanceStep);m<maxDistance;m+=distanceStep){
            canvas.drawCircle(0,0,m,paint);
        }
        //draw major circles
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS*2);
        paint.setStyle(Paint.Style.STROKE);//draws an outline instead of filling in the circles
        float majorDistanceStep = distanceStep*10;
        for(float m=(float) MyMath.roundDownToNearestMultipleOf(minDistance,majorDistanceStep);m<maxDistance;m+=majorDistanceStep){
            canvas.drawCircle(0,0,m,paint);
        }
        paint.setStyle(Paint.Style.FILL);//set back


        //draw angles
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS);
        for(double angle=MyMath.roundDownToNearestMultipleOf(minAngle,angleStep);angle<maxAngle;angle+=angleStep){
            canvas.drawLine(0,0,(float) (maxDistance*Math.cos(angle)),(float) (maxDistance*Math.sin(angle)),paint);
        }

        //draw axis
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(camera.getUnitsPerInchX()*LINE_THICKNESS*2);
        canvas.drawLine(camera.getLeft(),0f,camera.getRight(),0f,paint);
        canvas.drawLine(0f,camera.getBottom(),0f,camera.getTop(),paint);

        //draw text
        paint.setTextSize(TEXT_SIZE);//multiply by units per pixel to keep the same size at all zoom levels
        for(float m=(float) MyMath.roundDownToNearestMultipleOf(minDistance,distanceStep);m<maxDistance;m+=distanceStep){
            drawText(getTickMarkString(m),m+5*camera.getUnitsPerPixelX(), 0+5*camera.getUnitsPerPixelX(),camera.getUnitsPerPixelX(),camera.getUnitsPerPixelY(),paint,canvas);
        }
    }



    private float getMagnitudeStep(Camera camera) {
        return (float) Math.pow(10, Math.ceil(Math.log10(MIN_DISTANCE_BETWEEN_LINES*camera.getUnitsPerInchX())));/////just using units per inch x, not y
    }

    private double getAngleStep(Camera camera) {
        return Math.PI/12;
    }

    private CameraQuad getQuad(Camera camera){
        if(camera.getHighestY()>0 && camera.getLowestY()<0 && camera.getLowestX()<0 && camera.getHighestX()>0){
            return CameraQuad.ORIGIN;
        }
        if(camera.getLowestX()>0 && camera.getLowestY()>0){
            return CameraQuad.QUAD1;
        }
        if(camera.getHighestX()<0 && camera.getLowestY()>0){
            return CameraQuad.QUAD2;
        }
        if(camera.getHighestX()<0 && camera.getHighestY()<0){
            return CameraQuad.QUAD3;
        }
        if(camera.getLowestX()>0 && camera.getHighestY()<0){
            return CameraQuad.QUAD4;
        }
        if(camera.getHighestY()>0 && camera.getLowestY()<0 && camera.getLowestX()>0){
            return CameraQuad.XAXIS;
        }
        if(camera.getHighestY()>0 && camera.getLowestY()<0 && camera.getHighestX()<0){
            return CameraQuad.NEGXAXIS;
        }
        if(camera.getHighestX()>0 && camera.getLowestX()<0 && camera.getLowestY()>0){
            return CameraQuad.YAXIS;
        }
        if(camera.getHighestX()>0 && camera.getLowestX()<0 && camera.getHighestY()<0){
            return CameraQuad.NEGYAXIS;
        }
        Log.d(CLASS_NAME,"Can't figure out camera quad!");
        throw new Error();
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
