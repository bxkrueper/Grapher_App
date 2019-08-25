package com.example.differential_equation_grapher.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LineDrawer {
    private float prevX;
    private float prevY;

    public LineDrawer(){
        this.prevX = Float.NaN;
        this.prevY = Float.NaN;
    }


    //call this first before using. clears the previous coordinates so it doesn't draw from them
    public void getReadyToDraw(){
        this.prevX = Float.NaN;
        this.prevY = Float.NaN;

    }

    public void drawTo(float x, float y, Canvas canvas, Paint paint) {
        if(allNumbersAreValid(x,y,prevX,prevY)){

            canvas.drawLine(prevX, prevY,x,y,paint);
        }
        prevX = x;
        prevY = y;
    }

    //////////what about infinity?
    private boolean allNumbersAreValid(float x, float y, float prevX, float prevY) {
        return x!=Float.NaN && y!=Float.NaN && prevX!=Float.NaN && prevY!=Float.NaN;
    }

}
