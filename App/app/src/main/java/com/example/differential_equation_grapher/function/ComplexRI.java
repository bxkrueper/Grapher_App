package com.example.differential_equation_grapher.function;


import com.example.differential_equation_grapher.myAlgs.MyMath;

public class ComplexRI extends Complex{
    public static final ComplexRI additiveIdentity = new ComplexRI(0.0,0.0);
    public static final ComplexRI multiplictiveIdentity = new ComplexRI(1.0,0.0);
    public static final ComplexRI nan = new ComplexRI(Double.NaN,Double.NaN);
    
    private double real;
    private double imaginary;
    
    public ComplexRI(double real, double imaginary){
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public double getReal(){
        return real;
    }
    
    public double getImaginary(){
        return imaginary;
    }
    
    public double getMagnitude(){
        return Math.hypot(real, imaginary);
    }
    
    public double getTheta(){
        double theta = MyMath.getDirectionFromOrigin(real, imaginary);
        if(theta>Math.PI){
            theta-=Math.PI*2;/////////////needed for  log ^of complex number principle value (between pi and -pi)
        }
        return theta;
    }

    @Override
    public String toDisplayString() {
        if(Double.isNaN(real) || Double.isNaN(imaginary)){
            return "NaN";
        }

        if(imaginary==0.0){
            return MyMath.toNiceString(real,5);
        }else if(real==0.0){
            if(imaginary==1.0){
                return "i";
            }else{
                return MyMath.toNiceString(imaginary,5) + "i";
            }
        }else{
            if(imaginary==1.0){
                return "(" + MyMath.toNiceString(real,5) + "+" + "i" + ")";
            }else if(imaginary==-1.0){
                return "(" + MyMath.toNiceString(real,5) + "-" + "i" + ")";
            }else{
                if(imaginary>0){
                    return "(" + MyMath.toNiceString(real,5) + "+" + MyMath.toNiceString(imaginary,5) + "i" + ")";
                }else{//imaginary<0
                    return "(" + MyMath.toNiceString(real,5) + MyMath.toNiceString(imaginary,5) + "i" + ")";
                }
                
            }
        }
    }

    @Override
    public Expression simplify() {
        if(imaginary==0.0){
            return new RealNumber(real);
        }else{
            return this;
        }
    }

    @Override
    public Complex getConjugate() {
        return new ComplexRI(real,-imaginary);
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
