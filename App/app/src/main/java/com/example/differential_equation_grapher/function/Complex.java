package com.example.differential_equation_grapher.function;

public abstract class Complex extends Number{
    
    public abstract Complex getConjugate();


    public static final double PI2 = Math.PI*2;

    //keep theta between 0 and 2pi
    public static double standardizeTheta0to2PI(double theta){
        if(theta>PI2){
            return (theta%PI2);
        }else if(theta<0){
            return (-((-theta)%PI2))+PI2;
        }else{
            return theta;
        }
    }

    //keep theta between -pi and pi
    public static double standardizeThetaNegPIToPI(double theta){
        if(theta>Math.PI){
            return ((theta+Math.PI)%PI2)-Math.PI;
        }else if(theta<=-Math.PI){
            return -(((-theta+Math.PI)%PI2)-Math.PI);
        }else{
            return theta;
        }
    }
    
}
