package com.example.differential_equation_grapher.function;


import com.example.differential_equation_grapher.myAlgs.MyMath;

public class ComplexMT extends Complex{
    public static final ComplexMT additiveIdentity = new ComplexMT(0.0,0.0);
    public static final ComplexMT multiplictiveIdentity = new ComplexMT(1.0,0.0);
    public static final ComplexMT nan = new ComplexMT(Double.NaN,Double.NaN);
    
    private double magnitude;
    private double theta;
    
    public ComplexMT(double magnitude, double theta){
        this.magnitude = magnitude;
        this.theta = standardizeThetaNegPIToPI(theta);/////////////needed for  log ^of complex number principle value (between pi and -pi)
    }
    
    public double getReal(){
        return magnitude*Math.cos(theta);
    }
    
    public double getImaginary(){
        return magnitude*Math.sin(theta);
    }
    
    public double getMagnitude(){
        return magnitude;
    }
    
    public double getTheta(){
        return theta;
    }



    @Override
    public String toDisplayString() {
        if(Double.isNaN(magnitude) || Double.isNaN(theta)){
            return "NaN";
        }

        return MyMath.toNiceString(magnitude,5) + "*e^(" + MyMath.toNiceString(theta,5) + "i)";
    }

    @Override
    public Expression simplify() {
        if(theta==0.0){
            return new RealNumber(magnitude);
        }else if(theta == Math.PI){
            return new RealNumber(-magnitude);
        }else{
            return this;
        }
    }
    
    @Override
    public Complex getConjugate() {
        return new ComplexMT(magnitude,-theta);
    }

    @Override
    public Constant getAdditiveIdentity() {
        return additiveIdentity;
    }

    @Override
    public Constant getMultiplictiveIdentity() {
        return multiplictiveIdentity;
    }

    @Override
    public Constant getNaN() {
        return nan;
    }
}
