package com.example.differential_equation_grapher.helper;

import android.graphics.Color;

import com.example.differential_equation_grapher.camera.Camera;
import com.example.differential_equation_grapher.function.Complex;

public class LinearNumberToColorConverter implements NumberToColorConverter {
    private static final float minHue = 0;
    private static final float maxHue = 320;//hue is measured in degrees. set less than 360 so max is purple, not red

    private int minColor = Color.HSVToColor(new float[]{minHue,1f,.55f});
    private int maxColor = Color.HSVToColor(new float[]{maxHue,1f,.55f});
    private int colorForNaN = Color.HSVToColor(0,new float[]{0,1f,.8f});//transparent

    private float min;
    private float max;

    private static final float[] hueArray = new float[]{0,0,0};//recycled object for HSV method

    public LinearNumberToColorConverter(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public float getMin() {
        return min;
    }
    @Override
    public void setMin(float min) {
        this.min = min;
    }
    @Override
    public float getMax() {
        return max;
    }
    @Override
    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public int getColor(float number) {
        if(Float.isNaN(number)){
            return colorForNaN;
        }
        float hue;
        if(number<min){
            return minColor;
        }else if(number>max){
            return maxColor;
        }else{
            float numberRatio = (number-min)/(max-min);
            hue = minHue + numberRatio*maxHue;
        }
        hueArray[0] = hue;
        hueArray[1] = 1f;//saturation
        hueArray[2] = .8f;//lightness
        return Color.HSVToColor(hueArray);
//        return Color.HSVToColor(new float[]{hue,1f,.8f});//saturation set to max, lightness set to half
    }

}
