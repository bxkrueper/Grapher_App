package com.example.differential_equation_grapher.camera;

import android.graphics.Canvas;

public class CenterZoomCamera implements Camera{
    private float xCenter;
    private float yCenter;
    private float pixelsPerUnitX;//make negative to flip axis negative used internally for conversions, but getters return abs to avoid confusion
    private float pixelsPerUnitY;//make negative to flip axis negative used internally for conversions, but getters return abs to avoid confusion
    private int screenWidth;
    private int screenHeight;
    private float pixelsPerInchX;//set by WorldView at start of draw method. should always be positive
    private float pixelsPerInchY;//set by WorldView at start of draw method. should always be positive

    //never negative
    private float minAbsForPixelsPerUnitX;
    private float minAbsForPixelsPerUnitY;
    private float maxAbsForPixelsPerUnitX;
    private float maxAbsForPixelsPerUnitY;

    //pixels per unit: use screenwidth (pixels) / camera width (world units)
    public CenterZoomCamera(float xCenter, float yCenter, float pixelsPerUnitX,float pixelsPerUnitY) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.pixelsPerUnitX = pixelsPerUnitX;
        this.pixelsPerUnitY = pixelsPerUnitY;

        this.minAbsForPixelsPerUnitX = 0f;
        this.minAbsForPixelsPerUnitY = 0f;
        this.maxAbsForPixelsPerUnitX = Float.MAX_VALUE;
        this.maxAbsForPixelsPerUnitY = Float.MAX_VALUE;
    }
    //make camera width or height negative to flip that axis
    public CenterZoomCamera(float xCenter, float yCenter, int screenWidth,float cameraWidth,int screenHeight,float cameraHeight) {
        this(xCenter,yCenter,screenWidth/cameraWidth,screenHeight/cameraHeight);
    }


    @Override
    public float getCenterX() {
        return xCenter;
    }

    @Override
    public float getCenterY() {
        return yCenter;
    }

    @Override
    public void setCenterX(float centerX) {
        this.xCenter = centerX;
    }

    @Override
    public void setCenterY(float centerY) {
        this.yCenter = centerY;
    }


    @Override
    public float getWidth() {
        return screenWidth/Math.abs(pixelsPerUnitX);
    }


    @Override
    public float getHeight() {
        return screenHeight/Math.abs(pixelsPerUnitY);
    }

    @Override
    public void setWidth(float width) {
        setPixelsPerUnitX(screenWidth/width);
    }

    @Override
    public void setHeight(float height) {
        setPixelsPerUnitY(screenHeight/height);
    }

    @Override
    public float getScreenWidth() {
        return screenWidth;
    }

    @Override
    public float getScreenHeight() {
        return screenHeight;
    }

    //set by world view at start of on draw method
    @Override
    public void setScreenWidth(int width) {
        this.screenWidth = width;
    }
    @Override
    public void setScreenHeight(int height) {
        this.screenHeight = height;
    }
    @Override
    public void setPixelsPerInchX(float pixelsPerInchX) {
        this.pixelsPerInchX = pixelsPerInchX;
    }
    @Override
    public void setPixelsPerInchY(float pixelsPerInchY) {
        this.pixelsPerInchY = pixelsPerInchY;
    }

    //should always be positive
    @Override
    public float getPixelsPerInchX() {
        return pixelsPerInchX;
    }
    @Override
    public float getPixelsPerInchY() {
        return pixelsPerInchY;
    }

    @Override
    public float getHighestX() {
        return xCenter+Math.abs(getWidth())/2;
    }

    @Override
    public float getLowestX() {
        return xCenter-Math.abs(getWidth())/2;
    }

    @Override
    public float getHighestY() {
        return yCenter+Math.abs(getHeight())/2;
    }

    @Override
    public float getLowestY() {
        return yCenter-Math.abs(getHeight())/2;
    }

    @Override
    public float getRight() {
        return xCenter+getWidth()/2;
    }

    @Override
    public float getLeft() {
        return xCenter-getWidth()/2;
    }

    @Override
    public float getTop() {
        return yCenter-getHeight()/2;
    }

    @Override
    public float getBottom() {
        return yCenter+getHeight()/2;
    }

    //always returns positive
    @Override
    public float getPixelsPerUnitX() {
        return Math.abs(pixelsPerUnitX);
    }
    @Override
    public float getUnitsPerPixelX() {
        return 1/Math.abs(pixelsPerUnitX);
    }
    @Override
    public float getPixelsPerUnitY() {
        return Math.abs(pixelsPerUnitY);
    }
    @Override
    public float getUnitsPerPixelY() {
        return 1/Math.abs(pixelsPerUnitY);
    }

    //can return negative if screen is flipped
    public float getPixelsPerUnitXNegPossible() {
        return pixelsPerUnitX;
    }
    public float getUnitsPerPixelXNegPossible() {
        return 1/pixelsPerUnitX;
    }
    public float getPixelsPerUnitYNegPossible() {
        return pixelsPerUnitY;
    }
    public float getUnitsPerPixelYNegPossible() {
        return 1/pixelsPerUnitY;
    }

    @Override
    public void setPixelsPerUnitX(float newScale) {
        if(Math.abs(newScale)<minAbsForPixelsPerUnitX){
            this.pixelsPerUnitX = newScale>=0?minAbsForPixelsPerUnitX:-minAbsForPixelsPerUnitX;
        }else if(Math.abs(newScale)>maxAbsForPixelsPerUnitX){
            this.pixelsPerUnitX = newScale>=0?maxAbsForPixelsPerUnitX:-maxAbsForPixelsPerUnitX;
        }else{
            this.pixelsPerUnitX = newScale;
        }
    }

    @Override
    public void setUnitsPerPixelX(float newScale) {
        setPixelsPerUnitX(1/newScale);
    }

    @Override
    public void setPixelsPerUnitY(float newScale) {
        if(Math.abs(newScale)<minAbsForPixelsPerUnitY){
            this.pixelsPerUnitY = newScale>=0?minAbsForPixelsPerUnitY:-minAbsForPixelsPerUnitY;
        }else if(Math.abs(newScale)>maxAbsForPixelsPerUnitY){
            this.pixelsPerUnitY = newScale>=0?maxAbsForPixelsPerUnitY:-maxAbsForPixelsPerUnitY;
        }else{
            this.pixelsPerUnitY = newScale;
        }
    }

    @Override
    public void setUnitsPerPixelY(float newScale) {
        setPixelsPerUnitY(1/newScale);
    }

    //always positive. use other method if the negative may be needed
    @Override
    public float getUnitsPerInchX() {
        return Math.abs(pixelsPerInchX)*getUnitsPerPixelX();
    }
    @Override
    public float getUnitsPerInchY() {
        return Math.abs(pixelsPerInchY)*getUnitsPerPixelY();
    }
    @Override
    public float getInchesPerUnitX() {
        return 1/getUnitsPerInchX();
    }
    @Override
    public float getInchesPerUnitY() {
        return 1/getUnitsPerInchY();
    }


    @Override
    public float worldXToScreenX(float worldX) {
        return (worldX-xCenter)*getPixelsPerUnitXNegPossible() + (screenWidth/2);
    }

    @Override
    public float worldYToScreenY(float worldY) {
        return ((worldY-yCenter)*getPixelsPerUnitYNegPossible() + (screenHeight/2));
    }

    /////////need to test conversions
    @Override
    public float screenXToWorldX(float screenX) {
        return (screenX - (screenWidth/2))/getPixelsPerUnitXNegPossible()+xCenter;
    }

    @Override
    public float screenYToWorldY(float screenY) {
        return(screenY - (screenHeight/2))/getPixelsPerUnitYNegPossible()+yCenter;
    }

    @Override
    public void transformCanvas(Canvas canvas) {
        canvas.translate(screenWidth/2,screenHeight/2);
        canvas.scale(getPixelsPerUnitXNegPossible(),getPixelsPerUnitYNegPossible());
        canvas.translate(-xCenter,-yCenter);
    }














    public float getMinAbsForPixelsPerUnitX() {
        return minAbsForPixelsPerUnitX;
    }

    public void setMinAbsForPixelsPerUnitX(float minAbsForPixelsPerUnitX) {
        this.minAbsForPixelsPerUnitX = Math.abs(minAbsForPixelsPerUnitX);
    }

    public float getMinAbsForPixelsPerUnitY() {
        return minAbsForPixelsPerUnitY;
    }

    public void setMinAbsForPixelsPerUnitY(float minAbsForPixelsPerUnitY) {
        this.minAbsForPixelsPerUnitY = Math.abs(minAbsForPixelsPerUnitY);
    }

    public float getMaxAbsForPixelsPerUnitX() {
        return maxAbsForPixelsPerUnitX;
    }

    public void setMaxAbsForPixelsPerUnitX(float maxAbsForPixelsPerUnitX) {
        this.maxAbsForPixelsPerUnitX = Math.abs(maxAbsForPixelsPerUnitX);
    }

    public float getMaxAbsForPixelsPerUnitY() {
        return maxAbsForPixelsPerUnitY;
    }

    public void setMaxAbsForPixelsPerUnitY(float maxAbsForPixelsPerUnitY) {
        this.maxAbsForPixelsPerUnitY = Math.abs(maxAbsForPixelsPerUnitY);
    }
}