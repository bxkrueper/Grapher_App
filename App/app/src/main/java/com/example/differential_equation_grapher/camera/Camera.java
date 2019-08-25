package com.example.differential_equation_grapher.camera;

import android.graphics.Canvas;

public interface Camera {

    //world coordinates
    float getCenterX();
    float getCenterY();
    void setCenterX(float centerX);
    void setCenterY(float centerY);

    //world coordinates
    float getWidth();
    float getHeight();
    void setWidth(float width);
    void setHeight(float height);

    //in pixels
    void setScreenWidth(int width);
    void setScreenHeight(int height);
    float getScreenWidth();
    float getScreenHeight();

    //should always be positive
    void setPixelsPerInchX(float pixelsPerInchX);
    void setPixelsPerInchY(float pixelsPerInchY);
    float getPixelsPerInchX();
    float getPixelsPerInchY();

    //world coordinates
    //the most extreme values for the sides of the camera. may be different than getRight, getTop... if a scale is negative
    float getHighestX();
    float getLowestX();
    float getHighestY();
    float getLowestY();

    //world coordinate of indicated side of the screen
    float getRight();
    float getLeft();
    float getTop();
    float getBottom();

    //units are world coordinate units
    //CONVERSION BETWEEN PIXELS AND UNITS CAN BE NEGATIVE TO ALLOW AXIS FLIPPING
    float getPixelsPerUnitX();
    float getUnitsPerPixelX();
    float getPixelsPerUnitY();
    float getUnitsPerPixelY();
    //can return negative if screen is flipped
    float getPixelsPerUnitXNegPossible();
    float getUnitsPerPixelXNegPossible();
    float getPixelsPerUnitYNegPossible();
    float getUnitsPerPixelYNegPossible();
    void setPixelsPerUnitX(float newScale);
    void setUnitsPerPixelX(float newScale);
    void setPixelsPerUnitY(float newScale);
    void setUnitsPerPixelY(float newScale);


    //shortcut
    float getUnitsPerInchX();
    float getUnitsPerInchY();
    float getInchesPerUnitX();
    float getInchesPerUnitY();

    //convert between world coordinates and pixel locations measured from the top left
    float worldXToScreenX(float worldX);
    float worldYToScreenY(float worldY);
    float screenXToWorldX(float screenX);
    float screenYToWorldY(float screenY);

    void transformCanvas(Canvas canvas);



}
