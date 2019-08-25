package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ConstantMediator {
    
    //order: RealNumber,Complex,Vector,Undefined(just return Undefined)
    
    
    
//    public static Constant evaluate(Arguments arguments, Operation operation, Substitution substitution) {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
    
    
    public static Constant add(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return add((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return add((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return add((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(add(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant add(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return add(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return add(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return add(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(add(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant add(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return add(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return add(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return add(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(add(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant add(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return add(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return add(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return add(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(add(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    
    
    public static Constant subtract(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return subtract((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return subtract((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return subtract((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(subtract(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant subtract(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return subtract(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return subtract(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return subtract(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(subtract(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant subtract(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return subtract(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return subtract(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return subtract(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(subtract(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant subtract(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return subtract(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return subtract(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return subtract(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(subtract(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    
    public static Constant multiply(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return multiply((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return multiply((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return multiply((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(multiply(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant multiply(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return multiply(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return multiply(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return multiply(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(multiply(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant multiply(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return multiply(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return multiply(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return multiply(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(multiply(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant multiply(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return multiply(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return multiply(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return multiply(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(multiply(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    
    public static Constant divide(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return divide((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return divide((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return divide((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(divide(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant divide(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return divide(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return divide(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return divide(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(divide(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant divide(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return divide(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return divide(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return divide(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(divide(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant divide(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return divide(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return divide(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return divide(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(divide(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    
    public static Constant power(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return power((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return power((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return power((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(power(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant power(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return power(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return power(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return power(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(power(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant power(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return power(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return power(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return power(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(power(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant power(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return power(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return power(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return power(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(power(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }

    public static Constant floor(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return floor((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return floor((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return floor((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(floor(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }

    private static Constant floor(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return floor(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return floor(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return floor(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(floor(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant floor(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return floor(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return floor(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return floor(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(floor(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant floor(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return floor(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return floor(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return floor(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(floor(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }

    public static Constant ceil(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return ceil((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return ceil((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return ceil((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c1;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(ceil(c,c2), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }

    private static Constant ceil(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return ceil(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return ceil(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return ceil(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(ceil(r,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant ceil(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return ceil(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return ceil(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return ceil(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(ceil(complex,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant ceil(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return ceil(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return ceil(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return ceil(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c2;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant c:set){
                ConstantGroup.addConstantToCollection(ceil(v,c), answerSet);
            }
            return new ConstantSet(answerSet);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    public static Constant convertToInstanceOf(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return convertToInstanceOf((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return convertToInstanceOf((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return convertToInstanceOf((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            System.out.println("convertToInstance of not implemented yet for constant set! " + c1.toDisplayString());
            return Undefined.getInstance();
        }else if(c1 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return null;
        }
    }
    
    private static Constant convertToInstanceOf(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return convertToInstanceOf(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return convertToInstanceOf(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return convertToInstanceOf(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            return new ConstantSet(r);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant convertToInstanceOf(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return convertToInstanceOf(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return convertToInstanceOf(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return convertToInstanceOf(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            return new ConstantSet(complex);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    private static Constant convertToInstanceOf(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return convertToInstanceOf(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return convertToInstanceOf(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return convertToInstanceOf(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            return new ConstantSet(v);
        }else if(c2 instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return null;
        }
    }
    
    
    
    public static int compareTo(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return compareTo((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return compareTo((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return compareTo((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            System.out.println("compareTo of not implemented yet for constant set! " + c1.toDisplayString());
            return 0;
        }else if(c1 instanceof Undefined){
            return 1;////////////
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return 0;/////////////return undefined?
        }
    }
    
    private static int compareTo(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return compareTo(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return compareTo(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return compareTo(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            System.out.println("compareTo of not implemented yet for constant set! " + c2.toDisplayString());
            return 0;
        }else if(c2 instanceof Undefined){
            return -1;
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return 0;//////////////////////
        }
    }
    private static int compareTo(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return compareTo(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return compareTo(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return compareTo(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            System.out.println("compareTo of not implemented yet for constant set! " + c2.toDisplayString());
            return 0;
        }else if(c2 instanceof Undefined){
            return -1;///////////
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return 0;
        }
    }
    private static int compareTo(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return compareTo(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return compareTo(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return compareTo(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            System.out.println("compareTo of not implemented yet for constant set! " + c2.toDisplayString());
            return 0;
        }else if(c2 instanceof Undefined){
            return -1;///////////
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return 0;
        }
    }
    
    
    
    
    
    public static boolean equals(Constant c1,Constant c2){
        if(c1 instanceof RealNumber){
            return equals((RealNumber) c1,c2);
        }else if(c1 instanceof Complex){
            return equals((Complex) c1,c2);
        }else if(c1 instanceof Vector){
            return equals((Vector) c1,c2);
        }else if(c1 instanceof ConstantSet){
            ConstantSet cs1 = (ConstantSet) c1;
            if(c2 instanceof ConstantSet){
                ConstantSet cs2 = (ConstantSet) c2;
                return cs1.getCollection().equals(cs2.getCollection());
            }else{
                return cs1.contains(c2) && cs1.size()==1;
            }
        }else if(c1 instanceof Undefined){
            return (c2 instanceof Undefined);
        }else{
            System.out.println("unrecognized constant! " + c1.toDisplayString());
            return false;
        }
    }
    
    private static boolean equals(RealNumber r,Constant c2){
        if(c2 instanceof RealNumber){
            return equals(r,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return equals(r,(Complex)c2);
        }else if(c2 instanceof Vector){
            return equals(r,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet cs2 = (ConstantSet) c2;
            return cs2.contains(r) && cs2.size()==1;
        }else if(c2 instanceof Undefined){
            return false;
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return false;
        }
    }
    private static boolean equals(Complex complex,Constant c2){
        if(c2 instanceof RealNumber){
            return equals(complex,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return equals(complex,(Complex)c2);
        }else if(c2 instanceof Vector){
            return equals(complex,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet cs2 = (ConstantSet) c2;
            return cs2.contains(complex) && cs2.size()==1;
        }else if(c2 instanceof Undefined){
            return false;
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return false;
        }
    }
    private static boolean equals(Vector v,Constant c2){
        if(c2 instanceof RealNumber){
            return equals(v,(RealNumber)c2);
        }else if(c2 instanceof Complex){
            return equals(v,(Complex)c2);
        }else if(c2 instanceof Vector){
            return equals(v,(Vector)c2);
        }else if(c2 instanceof ConstantSet){
            ConstantSet cs2 = (ConstantSet) c2;
            return cs2.contains(v) && cs2.size()==1;
        }else if(c2 instanceof Undefined){
            return false;
        }else{
            System.out.println("unrecognized constant! " + c2.toDisplayString());
            return false;
        }
    }
    
    
    
    
    public static Constant gamma(Constant c){
        if(c instanceof RealNumber){
            return gamma((RealNumber) c);
        }else if(c instanceof Complex){
            return gamma((Complex) c);
        }else if(c instanceof Vector){
            return gamma((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(gamma(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    
    
    
    public static Constant negate(Constant c){
        if(c instanceof RealNumber){
            return negate((RealNumber) c);
        }else if(c instanceof Complex){
            return negate((Complex) c);
        }else if(c instanceof Vector){
            return negate((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(negate(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }






    public static Constant round(Constant c, double roundTo) {
        if(c instanceof RealNumber){
            return round((RealNumber) c,roundTo);
        }else if(c instanceof Complex){
            return round((Complex) c,roundTo);
        }else if(c instanceof Vector){
            return round((Vector) c,roundTo);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(round(conInSet,roundTo));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    
    
    
    public static Constant abs(Constant c){
        if(c instanceof RealNumber){
            return abs((RealNumber) c);
        }else if(c instanceof Complex){
            return abs((Complex) c);
        }else if(c instanceof Vector){
            return abs((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(abs(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    
    
    
    public static Constant sin(Constant c){
        if(c instanceof RealNumber){
            return sin((RealNumber) c);
        }else if(c instanceof Complex){
            return sin((Complex) c);
        }else if(c instanceof Vector){
            return sin((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(sin(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    
    
    public static Constant cos(Constant c){
        if(c instanceof RealNumber){
            return cos((RealNumber) c);
        }else if(c instanceof Complex){
            return cos((Complex) c);
        }else if(c instanceof Vector){
            return cos((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(cos(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    
    public static Constant tan(Constant c){
        if(c instanceof RealNumber){
            return tan((RealNumber) c);
        }else if(c instanceof Complex){
            return tan((Complex) c);
        }else if(c instanceof Vector){
            return tan((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(tan(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }


    public static Constant arcsin(Constant c){
        if(c instanceof RealNumber){
            return arcsin((RealNumber) c);
        }else if(c instanceof Complex){
            return arcsin((Complex) c);
        }else if(c instanceof Vector){
            return arcsin((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(arcsin(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }

    public static Constant arccos(Constant c){
        if(c instanceof RealNumber){
            return arccos((RealNumber) c);
        }else if(c instanceof Complex){
            return arccos((Complex) c);
        }else if(c instanceof Vector){
            return arccos((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(arccos(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }

    public static Constant arctan(Constant c){
        if(c instanceof RealNumber){
            return arctan((RealNumber) c);
        }else if(c instanceof Complex){
            return arctan((Complex) c);
        }else if(c instanceof Vector){
            return arctan((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(arctan(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }

    public static Constant sinh(Constant c) {
        if(c instanceof RealNumber){
            return sinh((RealNumber) c);
        }else if(c instanceof Complex){
            return sinh((Complex) c);
        }else if(c instanceof Vector){
            return sinh((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(sinh(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }

    public static Constant cosh(Constant c) {
        if(c instanceof RealNumber){
            return cosh((RealNumber) c);
        }else if(c instanceof Complex){
            return cosh((Complex) c);
        }else if(c instanceof Vector){
            return cosh((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(cosh(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }

    public static Constant tanh(Constant c) {
        if(c instanceof RealNumber){
            return tanh((RealNumber) c);
        }else if(c instanceof Complex){
            return tanh((Complex) c);
        }else if(c instanceof Vector){
            return tanh((Vector) c);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(tanh(conInSet));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    
    
    
    public static Constant log(Constant c, double base) {
        if(c instanceof RealNumber){
            return log((RealNumber) c, base);
        }else if(c instanceof Complex){
            return log((Complex) c, base);
        }else if(c instanceof Vector){
            return log((Vector) c, base);
        }else if(c instanceof ConstantSet){
            ConstantSet set = (ConstantSet) c;
            Set<Constant> answerSet = new TreeSet<>();
            for(Constant conInSet:set){
                answerSet.add(log(conInSet,base));
            }
            return new ConstantSet(answerSet);
        }else if(c instanceof Undefined){
            return Undefined.getInstance();
        }else{
            System.out.println("unrecognized constant! " + c.toDisplayString());
            return null;
        }
    }
    public static Constant ln(Constant c) {
        return log(c,Math.E);
    }
    
    
    
    
    
    
    
    
    
    
    
    private static Constant add(RealNumber r1,RealNumber r2){
        return new RealNumber(r1.getValue()+r2.getValue());
    }
    private static Constant add(RealNumber r,Complex complex){
        return new ComplexRI(r.getValue()+complex.getReal(),complex.getImaginary());
    }
    private static Constant add(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    private static Constant add(Complex complex,RealNumber r){
        return add(r,complex);
    }
    private static Constant add(Complex complex,Complex complex2){
        return new ComplexRI(complex.getReal()+complex2.getReal(),complex.getImaginary()+complex2.getImaginary());
    }
    private static Constant add(Complex complex,Vector v){
        return Undefined.getInstance();
    }
    private static Constant add(Vector v,RealNumber r){
        return add(r,v);
    }
    private static Constant add(Vector v,Complex complex){
        return add(complex,v);
    }
    private static Constant add(Vector v1,Vector v2){
        if(v1.length()!=v2.length()){
            return Undefined.getInstance();
        }
        
        List<Constant> sumList = new ArrayList<>(v1.length());
        for(int i=0;i<v1.length();i++){
            sumList.add(add(v1.get(i),v2.get(i)));
        }
        return new Vector(sumList);
    }
    
    
    private static Constant subtract(RealNumber r1,RealNumber r2){
        return new RealNumber(r1.getValue()-r2.getValue());
    }
    private static Constant subtract(RealNumber r,Complex complex){
        return new ComplexRI(r.getValue()-complex.getReal(),-complex.getImaginary());
    }
    private static Constant subtract(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    private static Constant subtract(Complex complex,RealNumber r){
        return new ComplexRI(complex.getReal()-r.getValue(),complex.getImaginary());
    }
    private static Constant subtract(Complex complex,Complex complex2){
        return new ComplexRI(complex.getReal()-complex2.getReal(),complex.getImaginary()-complex2.getImaginary());
    }
    private static Constant subtract(Complex complex,Vector v){
        return Undefined.getInstance();
    }
    private static Constant subtract(Vector v,RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant subtract(Vector v,Complex complex){
        return Undefined.getInstance();
    }
    private static Constant subtract(Vector v1,Vector v2){
        if(v1.length()!=v2.length()){
            return Undefined.getInstance();
        }
        
        List<Constant> sumList = new ArrayList<>(v1.length());
        for(int i=0;i<v1.length();i++){
            sumList.add(subtract(v1.get(i),v2.get(i)));
        }
        return new Vector(sumList);
    }
    
    
    
    
    private static Constant multiply(RealNumber r1,RealNumber r2){
        return new RealNumber(r1.getValue()*r2.getValue());
    }
    private static Constant multiply(RealNumber r,Complex complex){
        return new ComplexRI(r.getValue()*complex.getReal(),r.getValue()*complex.getImaginary());
    }
    private static Constant multiply(RealNumber r,Vector v){
        List<Constant> productList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            productList.add(multiply(r,v.get(i)));
        }
        return new Vector(productList);
    }
    private static Constant multiply(Complex complex,RealNumber r){
        return multiply(r,complex);
    }
    private static Constant multiply(Complex complex1,Complex complex2){
        return new ComplexRI(complex1.getReal()*complex2.getReal()-complex1.getImaginary()*complex2.getImaginary(),complex1.getReal()*complex2.getImaginary()+complex1.getImaginary()*complex2.getReal());
    }
    private static Constant multiply(Complex complex,Vector v){
        System.out.println("complex*vector not implemented yet");
        return Undefined.getInstance();
    }
    private static Constant multiply(Vector v,RealNumber r){
        return multiply(r,v);
    }
    private static Constant multiply(Vector v,Complex complex){
        return multiply(complex,v);
    }
    private static Constant multiply(Vector v1,Vector v2){
        if(v1.length()!=v2.length()){
            return Undefined.getInstance();
        }
        
        List<Constant> productList = new ArrayList<>(v1.length());
        for(int i=0;i<v1.length();i++){
            productList.add(multiply(v1.get(i),v2.get(i)));
        }
        return new Vector(productList);
    }
    
    
    
    
    private static Constant divide(RealNumber r1,RealNumber r2){
        return new RealNumber(r1.getValue()/r2.getValue());
    }
    private static Constant divide(RealNumber r,Complex complex){
        double a = r.getValue();
        double c = complex.getReal();
        double d = complex.getImaginary();
        double bottom = c*c+d*d;
        return new ComplexRI((a*c)/bottom,(-a*d)/bottom);
    }
    private static Constant divide(RealNumber r,Vector v){
        List<Constant> productList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            productList.add(divide(r,v.get(i)));
        }
        return new Vector(productList);
    }
    private static Constant divide(Complex complex, RealNumber r){
        return new ComplexRI(complex.getReal()/r.getValue(),complex.getImaginary()/r.getValue());
    }
    private static Constant divide(Complex complex1,Complex complex2){
        double a = complex1.getReal();
        double b = complex1.getImaginary();
        double c = complex2.getReal();
        double d = complex2.getImaginary();
        double bottom = c*c+d*d;
        return new ComplexRI((a*c+b*d)/bottom,(b*c-a*d)/bottom);
    }
    private static Constant divide(Complex complex, Vector v){
        return Undefined.getInstance();
    }
    private static Constant divide(Vector v,RealNumber r){
        return multiply(v,new RealNumber(1/r.getValue()));
    }
    private static Constant divide(Vector v, Complex complex){
        System.out.println("vector/complex not implemented yet");
        return Undefined.getInstance();
    }
    private static Constant divide(Vector v1,Vector v2){
        return Undefined.getInstance();
    }
    
    
    
    
    private static Constant power(RealNumber r1,RealNumber r2){
        //////////////////for testing fixedConstantGroup and proxy constant
//        if(r2.getValue()==0.5){
//            RealNumber rn = new RealNumber(Math.pow(r1.getValue(), r2.getValue()));
//            return new ConstantSet(rn,negate(rn));
//        }
        /////////////
        return new RealNumber(Math.pow(r1.getValue(), r2.getValue()));
    }
    /*if a>0:
    a^Z2 = e^(Z2*ln(a))
         = e^((c+di)*ln(a))
         = e^(cln(a)) * e^(dln(a)i)
         = e^(cln(a)) * cos(dln(a))  +  e^(cln(a)) * sin(dln(a))i
    
    if a<0:
    
    */
    private static Constant power(RealNumber r,Complex complex){
        if(r.getValue()>0){
            double lna = Math.log(r.getValue());
            double d = complex.getImaginary();
            double eclna = Math.exp(complex.getReal()*lna);
            return new ComplexRI(eclna*Math.cos(d*lna),eclna*Math.sin(d*lna));
        }else if(r.getValue()==0){
            if(complex.getReal()>0){
                return new RealNumber(0.0);
            }else{
                return Undefined.getInstance();
            }
        }else{//r.getValue()<0
            return power(new ComplexRI(r.getValue(),0.0),complex);
            //test when sin and cos for complex are done?
//            Constant lna = ln(r);
//System.out.println(lna);
//            Expression dlnaEx = new Product(new RealNumber(complex.getImaginary()),lna);
//            Constant dlna = (Constant) dlnaEx.simplify();
//            System.out.println(dlna);
//            Expression eclnaEx = new Power(new E(),new Product(new RealNumber(complex.getReal()),lna));
//            System.out.println(new Product(new RealNumber(complex.getReal()),lna).simplify());
//            Constant eclna = (Constant) eclnaEx.simplify();
//            System.out.println("e^(clna)= " + eclna);
//            
//            Expression realPart = new Product(eclna,new Cos(dlna));
//            System.out.println("Cos(dlna)= " + (new Cos(dlna)).simplify());
//            Expression imaginaryPart = new Product(eclna,new Sin(dlna));
//            return (Constant) new Sum(realPart,imaginaryPart).simplify();//real part and imaginary part might actually be complex numbers
        }
        
    }
    private static Constant power(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    // (a+bi)^x == m^x (cos(ox)+isin(ox))   m == distance from (0+0i)  o == angle from real axis
    private static Constant power(Complex complex, RealNumber r){
        double hyp = complex.getMagnitude();
        double theta = complex.getTheta();
        double mPowX = Math.pow(hyp, r.getValue());
        return new ComplexMT(mPowX,theta*r.getValue());
//        return new ComplexRI(Math.cos(theta*r.getValue())*mPowX,Math.sin(theta*r.getValue())*mPowX);
    }
    // (a+bi)^(c+di) == Z1^Z2 == e^(Z2*ln(Z1))
    //                           e^(Z2*(.5ln(a^2+b^2)+io1))   o1 is theta1
    //                           e^(Z2*Z3)
    //                           e^(Z4)
    //                           e^(g+hi)
    //                           e^g * e^(hi)
    //                           e^g * (cos(h)+isin(h))
    //                           e^g * cos(h) + e^g*sin(h)i
    private static Constant power(Complex complex1,Complex complex2){
//        double a = complex1.getReal();
//        double b = complex1.getImaginary();
        Complex z3 = (Complex) ln(complex1);
//        Complex z3 = new ComplexRI(0.5*Math.log(a*a+b*b),complex1.getTheta());
        Complex z4 = (Complex) multiply(complex2,z3);
        double g = z4.getReal();
        double h = z4.getImaginary();
        double eToTheG = Math.exp(g);
        return new ComplexRI(eToTheG*Math.cos(h),eToTheG*Math.sin(h));
    }
    private static Constant power(Complex complex, Vector v){
        return Undefined.getInstance();
    }
    private static Constant power(Vector v,RealNumber r){
        List<Constant> powerList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            powerList.add(power(r,v.get(i)));
        }
        return new Vector(powerList);
    }
    private static Constant power(Vector v, Complex complex){
        return Undefined.getInstance();
    }
    private static Constant power(Vector v1,Vector v2){
        return Undefined.getInstance();
    }


    private static Constant floor(RealNumber r1,RealNumber r2){
        return new RealNumber(MyMath.roundDownToNearestMultipleOf(r1.getValue(),r2.getValue()));
    }
    private static Constant floor(RealNumber r,Complex complex){
        return Undefined.getInstance();
    }
    private static Constant floor(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    private static Constant floor(Complex complex, RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant floor(Complex complex1,Complex complex2){
        return Undefined.getInstance();
    }
    private static Constant floor(Complex complex, Vector v){
        return Undefined.getInstance();
    }
    private static Constant floor(Vector v,RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant floor(Vector v, Complex complex){
        return Undefined.getInstance();
    }
    private static Constant floor(Vector v1,Vector v2){
        return Undefined.getInstance();
    }

    private static Constant ceil(RealNumber r1,RealNumber r2){
        return new RealNumber(MyMath.roundUpToNearestMultipleOf(r1.getValue(),r2.getValue()));
    }
    private static Constant ceil(RealNumber r,Complex complex){
        return Undefined.getInstance();
    }
    private static Constant ceil(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    private static Constant ceil(Complex complex, RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant ceil(Complex complex1,Complex complex2){
        return Undefined.getInstance();
    }
    private static Constant ceil(Complex complex, Vector v){
        return Undefined.getInstance();
    }
    private static Constant ceil(Vector v,RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant ceil(Vector v, Complex complex){
        return Undefined.getInstance();
    }
    private static Constant ceil(Vector v1,Vector v2){
        return Undefined.getInstance();
    }
    
    
    private static Constant convertToInstanceOf(RealNumber r1,RealNumber r2){
        return (RealNumber) r1.copy();
    }
    private static Constant convertToInstanceOf(RealNumber r,Complex complex){
        return new ComplexRI(r.getValue(),0);
    }
    private static Constant convertToInstanceOf(RealNumber r,Vector v){
        return Undefined.getInstance();
    }
    private static Constant convertToInstanceOf(Complex complex, RealNumber r){
        if(complex.getImaginary()==0.0){
            return new RealNumber(complex.getReal());
        }else{
            return Undefined.getInstance();
        }
    }
    private static Constant convertToInstanceOf(Complex complex1,Complex complex2){
        if(complex1 instanceof ComplexRI){
            if(complex2 instanceof ComplexRI){
                return complex1;
            }else{//complex 2 instanceof ComplexMT
                return new ComplexMT(complex1.getMagnitude(),complex1.getTheta());
            }
        }else{//complex 1 instanceof ComplexMT
            if(complex2 instanceof ComplexRI){
                return new ComplexRI(complex1.getReal(),complex1.getImaginary());
            }else{//complex 2 instanceof ComplexMT
                return complex1;
            }
        }
    }
    private static Constant convertToInstanceOf(Complex complex, Vector v){
        return Undefined.getInstance();
    }
    private static Constant convertToInstanceOf(Vector v,RealNumber r){
        return Undefined.getInstance();
    }
    private static Constant convertToInstanceOf(Vector v, Complex complex){
        return Undefined.getInstance();
    }
    private static Constant convertToInstanceOf(Vector v1,Vector v2){
        return (Vector) v1.copy();
    }
    
    
    
    
    private static int compareTo(RealNumber r1,RealNumber r2){
        if(r1.getValue()==r2.getValue()){
            return 0;
        }else if(r1.getValue()<r2.getValue()){
            return -1;
        }else{
            return 1;
        }
    }
    private static int compareTo(RealNumber r,Complex complex){
        return compareTo(r,abs(complex));
    }
    private static int compareTo(RealNumber r,Vector v){
        return 0;////////////////add boolean constant?
    }
    private static int compareTo(Complex complex, RealNumber r){
        return compareTo(abs(complex),r);
    }
    private static int compareTo(Complex complex1,Complex complex2){
        return compareTo(abs(complex1),abs(complex2));
    }
    private static int compareTo(Complex complex, Vector v){
        return 0;////////////////
    }
    private static int compareTo(Vector v,RealNumber r){
        return 0;////////////////
    }
    private static int compareTo(Vector v, Complex complex){
        return 0;////////////////
    }
    private static int compareTo(Vector v1,Vector v2){
        return 0;////////////////
    }
    
    
    
    private static boolean equals(RealNumber r1,RealNumber r2){
        return r1.getValue()==r2.getValue();
    }
    private static boolean equals(RealNumber r,Complex complex){
        return r.getValue()==complex.getReal() && complex.getImaginary()==0.0;
    }
    private static boolean equals(RealNumber r,Vector v){
        return false;
    }
    private static boolean equals(Complex complex, RealNumber r){
        return equals(r,complex);
    }
    private static boolean equals(Complex complex1,Complex complex2){
        if(complex1 instanceof ComplexMT && complex2 instanceof ComplexMT){
            return complex1.getTheta()==complex2.getTheta() && complex1.getMagnitude()==complex2.getMagnitude();
        }else{
            return complex1.getReal()==complex2.getReal() && complex1.getImaginary()==complex2.getImaginary();
        }
    }
    private static boolean equals(Complex complex, Vector v){
        return false;
    }
    private static boolean equals(Vector v,RealNumber r){
        return false;
    }
    private static boolean equals(Vector v, Complex complex){
        return false;
    }
    private static boolean equals(Vector v1,Vector v2){
        if(v1.length()!=v2.length()){
            return false;
        }
        for(int i=0;i<v1.length();i++){
            if(!v1.get(i).equals(v2.get(i))){
                return false;
            }
        }
        return true;
    }
    
    
    
    
    private static Constant gamma(RealNumber r){
        double x =r.getValue();
        x++;
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                         + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                         +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return new RealNumber(Math.exp(tmp + Math.log(ser * Math.sqrt(2 * Math.PI))));
    }
    private static Constant gamma(Complex complex){
        return Undefined.getInstance();
    }
    private static Constant gamma(Vector v){
        return Undefined.getInstance();
    }
    /*
     * http://introcs.cs.princeton.edu/java/91float/Gamma.java.html
     */
//    private static double gamma(double x) {
//        x++;
//        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
//        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
//                         + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
//                         +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
//        return Math.exp(tmp + Math.log(ser * Math.sqrt(2 * Math.PI)));
//     }
    
    
    
    private static Constant round(RealNumber r,double roundTo){
        return new RealNumber(MyMath.roundToNearestMultipleOf(r.getValue(),roundTo));
    }
    private static Constant round(Complex complex,double roundTo){
        if(complex instanceof ComplexRI){
            return new ComplexRI(MyMath.roundToNearestMultipleOf(complex.getReal(),roundTo),MyMath.roundToNearestMultipleOf(complex.getImaginary(),roundTo));
        }else if(complex instanceof ComplexMT){
            return new ComplexMT(MyMath.roundToNearestMultipleOf(complex.getMagnitude(),roundTo),MyMath.roundToNearestMultipleOf(complex.getTheta(),roundTo));
        }else{
            //unrecognized complex number
            return Undefined.getInstance();
        }
    }
    private static Constant round(Vector v,double roundTo){
        List<Constant> roundList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            roundList.add(round(v.get(i),roundTo));
        }
        return new Vector(roundList);
    }




    private static Constant negate(RealNumber r){
        return new RealNumber(-r.getValue());
    }
    private static Constant negate(Complex complex){
        return new ComplexRI(-complex.getReal(),-complex.getImaginary());
    }
    private static Constant negate(Vector v){
        List<Constant> negateList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            negateList.add(negate(v.get(i)));
        }
        return new Vector(negateList);
    }
    
    
    
    
    
    private static Constant abs(RealNumber r){
        return new RealNumber(Math.abs(r.getValue()));
    }
    private static Constant abs(Complex complex){
        return new RealNumber(Math.sqrt(Math.pow(complex.getReal(), 2)+Math.pow(complex.getImaginary(), 2)));
    }
    private static Constant abs(Vector v){
        Expression exp = new RealNumber(0);///////////////lazy
        for(int i=0;i<v.length();i++){
            exp = new Sum(exp,new Power(v.get(i),new RealNumber(2)));
        }
        exp = new SquareRoot(exp);
        exp = exp.simplify();
        return (RealNumber) exp;
    }
    
    
    
    
    private static Constant sin(RealNumber r){
        return new RealNumber(Math.sin(r.getValue()));
    }
    /*https://www.youtube.com/watch?v=RCG_5op1FOo        use for arcsin too (end)
     * sin(a+bi) = sin(a)/2*(e^(-b)+e^b)  -  icos(a)/2*(e^(-b)-e^b)
     */
    private static Constant sin(Complex complex){
        double eToTheB = Math.exp(complex.getImaginary());
        double eToTheNegB = Math.exp(-complex.getImaginary());
        double realPart = Math.sin(complex.getReal())/2*(eToTheNegB+eToTheB);
        double imaginaryPart = -Math.cos(complex.getReal())/2*(eToTheNegB-eToTheB);
        return new ComplexRI(realPart,imaginaryPart);
    }
    private static Constant sin(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(sin(v.get(i)));
        }
        return new Vector(newList);
    }
    
    
    
    
    private static Constant cos(RealNumber r){
        return new RealNumber(Math.cos(r.getValue()));
    }
    /*https://www.youtube.com/watch?v=RCG_5op1FOo
     * cos(a+bi) = cos(a)/2*(e^(-b)+e^b)  +  isin(a)/2*(e^(-b)-e^b)
     */
    private static Constant cos(Complex complex){
        double eToTheB = Math.exp(complex.getImaginary());
        double eToTheNegB = Math.exp(-complex.getImaginary());
        double realPart = Math.cos(complex.getReal())/2*(eToTheNegB+eToTheB);
        double imaginaryPart = Math.sin(complex.getReal())/2*(eToTheNegB-eToTheB);
        return new ComplexRI(realPart,imaginaryPart);
    }
    private static Constant cos(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(cos(v.get(i)));
        }
        return new Vector(newList);
    }
    
    
    
    
    private static Constant tan(RealNumber r){
        return new RealNumber(Math.tan(r.getValue()));
    }
    /*
     * tan(a+bi)=sin(a+bi)/cos(a+bi)
     *          =(sin(a)m-icos(a)n)/(cos(a)m+isin(a)n)  m=e^(-b)+e^b   n=e^(-b)-e^b
     */
    private static Constant tan(Complex complex){
        double a = complex.getReal();
        double eToTheB = Math.exp(complex.getImaginary());
        double eToTheNegB = Math.exp(-complex.getImaginary());
        double m = eToTheNegB+eToTheB;
        double n = eToTheNegB-eToTheB;
        
        Complex top = new ComplexRI(Math.sin(a)*m,-Math.cos(a)*n);
        Complex bottom = new ComplexRI(Math.cos(a)*m,Math.sin(a)*n);
        
        return divide(top,bottom);
    }
    private static Constant tan(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(tan(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant arcsin(RealNumber r){
        return new RealNumber(Math.asin(r.getValue()));
    }
    private static Constant arcsin(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant arcsin(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(arcsin(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant arccos(RealNumber r){
        return new RealNumber(Math.acos(r.getValue()));
    }
    private static Constant arccos(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant arccos(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(arccos(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant arctan(RealNumber r){
        return new RealNumber(Math.atan(r.getValue()));
    }
    private static Constant arctan(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant arctan(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(arctan(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant sinh(RealNumber r){
        return new RealNumber(Math.sinh(r.getValue()));
    }
    private static Constant sinh(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant sinh(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(sinh(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant cosh(RealNumber r){
        return new RealNumber(Math.cosh(r.getValue()));
    }
    private static Constant cosh(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant cosh(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(cosh(v.get(i)));
        }
        return new Vector(newList);
    }

    private static Constant tanh(RealNumber r){
        return new RealNumber(Math.tanh(r.getValue()));
    }
    private static Constant tanh(Complex complex){
        return Undefined.getInstance();///////////////////todo:
    }
    private static Constant tanh(Vector v){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(tanh(v.get(i)));
        }
        return new Vector(newList);
    }
    
    
    
    private static Constant log(RealNumber r, double base){
        if(r.getValue()<0){
            /*
             * infinite solutions do to theta += 2PIk  use principle value
             * 
             */
            return log(new ComplexRI(r.getValue(),0.0),base);/////////can make faster (b=0)
        }else{
            if(base==Math.E){
                return new RealNumber(Math.log(r.getValue()));
            }else{
                return new RealNumber(Math.log(r.getValue())/Math.log(base));
            }
        }
    }
    //returns principle value   because complex.getTheta() uses principle value (theta between -pi and pi)
    private static Constant log(Complex complex, double base){
        /*
         * log(Z) = log(|Z|)+log(e^(io))    o = theta of complex
         * ln(Z)  = ln(|Z|)+io    base is e
         */
        if(base==Math.E){
            return new ComplexRI(Math.log(complex.getMagnitude()),complex.getTheta());
        }else{
            double logBase = Math.log(base);
            return new ComplexRI(Math.log(complex.getMagnitude())/logBase,complex.getTheta()/logBase);
        }
    }
    private static Constant log(Vector v, double base){
        List<Constant> newList = new ArrayList<>(v.length());
        for(int i=0;i<v.length();i++){
            newList.add(log(v.get(i),base));
        }
        return new Vector(newList);
    }



}
