package com.example.differential_equation_grapher.camera;

import android.graphics.Canvas;

//perfectly matches screen coordinates, so world and screen coordinates are the same
public class DefaultCamera implements Camera{

    private int screenWidth;
    private int screenHeight;
    private float pixelsPerInchX;
    private float pixelsPerInchY;

    @Override
    public float getCenterX() {
        return screenWidth/2f;
    }

    @Override
    public float getCenterY() {
        return screenHeight/2f;
    }

    @Override
    public void setCenterX(float centerX) {
        //do nothing
    }

    @Override
    public void setCenterY(float centerY) {
        //do nothing
    }

    @Override
    public float getWidth() {
        return screenWidth;
    }

    @Override
    public float getHeight() {
        return screenHeight;
    }

    @Override
    public void setWidth(float width) {
        //do nothing
    }

    @Override
    public void setHeight(float height) {
        //do nothing
    }

    @Override
    public void setScreenWidth(int width) {
        this.screenWidth = width;
    }

    @Override
    public void setScreenHeight(int height) {
        this.screenHeight = height;
    }

    @Override
    public float getScreenWidth() {
        return screenWidth;
    }

    @Override
    public float getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void setPixelsPerInchX(float pixelsPerInchX) {
        this.pixelsPerInchX = pixelsPerInchX;
    }

    @Override
    public void setPixelsPerInchY(float pixelsPerInchY) {
        this.pixelsPerInchY = pixelsPerInchY;
    }

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
        return screenWidth;
    }

    @Override
    public float getLowestX() {
        return 0;
    }

    @Override
    public float getHighestY() {
        return screenHeight;
    }

    @Override
    public float getLowestY() {
        return 0;
    }

    @Override
    public float getRight() {
        return screenWidth;
    }

    @Override
    public float getLeft() {
        return 0;
    }

    @Override
    public float getTop() {
        return 0;
    }

    @Override
    public float getBottom() {
        return screenHeight;
    }

    @Override
    public float getPixelsPerUnitX() {
        return 1f;
    }

    @Override
    public float getUnitsPerPixelX() {
        return 1f;
    }

    @Override
    public float getPixelsPerUnitY() {
        return 1f;
    }

    @Override
    public float getUnitsPerPixelY() {
        return 1f;
    }

    @Override
    public float getPixelsPerUnitXNegPossible() {
        return 1f;
    }

    @Override
    public float getUnitsPerPixelXNegPossible() {
        return 1f;
    }

    @Override
    public float getPixelsPerUnitYNegPossible() {
        return 1f;
    }

    @Override
    public float getUnitsPerPixelYNegPossible() {
        return 1f;
    }

    @Override
    public void setPixelsPerUnitX(float newScale) {
        //do nothing
    }

    @Override
    public void setUnitsPerPixelX(float newScale) {
        //do nothing
    }

    @Override
    public void setPixelsPerUnitY(float newScale) {
        //do nothing
    }

    @Override
    public void setUnitsPerPixelY(float newScale) {
        //do nothing
    }

    @Override
    public float getUnitsPerInchX() {
        return pixelsPerInchX*getUnitsPerPixelX();
    }

    @Override
    public float getUnitsPerInchY() {
        return pixelsPerInchY*getUnitsPerPixelY();
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
        return (int) worldX;
    }

    @Override
    public float worldYToScreenY(float worldY) {
        return (int) worldY;
    }

    @Override
    public float screenXToWorldX(float screenX) {
        return screenX;
    }

    @Override
    public float screenYToWorldY(float screenY) {
        return screenY;
    }

    @Override
    public void transformCanvas(Canvas canvas) {
        //do nothing
    }
}
