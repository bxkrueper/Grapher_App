//package com.example.differential_equation_grapher.helper;
//
//import android.graphics.Color;
//
//public class WrapAroundNumberToColorConverter implements NumberToColorConverter{
//
//    private double start;
//    private double end;
//
//    private float[] floatStorage;//this array is reused so making a new one every time is not needed
//
//    private final int TRANSPARANT = Color.HSVToColor(0,new float[]{0,0,0});
//
//    public WrapAroundNumberToColorConverter(double start, double end){
//        this.start = start;
//        this.end = end;
//    }
//
//    @Override
//    public int getColor(double number) {
//        float hue = 128;
//        float saturation = 255;
//        float lightness =  128;
//        floatStorage[0] = hue;
//        floatStorage[1] = saturation;
//        floatStorage[2] = lightness;
//        return Color.HSVToColor(floatStorage);
//    }
//
//    public double getStart() {
//        return start;
//    }
//
//    public double getEnd() {
//        return end;
//    }
//
//
//}