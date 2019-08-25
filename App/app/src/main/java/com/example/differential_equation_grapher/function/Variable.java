package com.example.differential_equation_grapher.function;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Variable extends Expression implements Comparable<Variable>{

    private char letter;//////////////////////
    private int subscript;//not allowed to be less than -1. -1 means no subscript
    
    public Variable(char letter) {
        this.letter = letter;
        this.subscript = -1;
    }
    public Variable(char letter,int subscript) {
        this.letter = letter;
        if(subscript<0){
            this.subscript = -1;
        }else{
            this.subscript = subscript;
        }
        
    }
    
    /////can just return this?
    @Override
    public Expression copy() {
        return new Variable(letter,subscript);
    }
    
    @Override
    public String toDisplayString() {
        if(subscript==-1){
            return Character.toString(letter);
        }else{
            return Character.toString(letter) + "[" + Integer.toString(subscript) + "]";
        }
    }
    @Override
    public String toDebugString(){
        return toDisplayString();
    }
    
    
    
    
    
    @Override
    public boolean isEqualTo(Object o) {
        if(o instanceof Variable){
            Variable v2 = (Variable) o;
            return this.letter==v2.letter && this.subscript==v2.subscript;
        }else{
            return false;
        }
    }
    
    
    @Override
    public int hashCode(){
        int number = (int) letter;
        
        int hash = 7;
        hash = 31 * hash + subscript;
        hash = 17 * hash + number;
        return hash;
    }
    
    @Override
    public boolean computable(){
        return false;
    }
    
    @Override
    public final Constant evaluate(Substitution substitution) {
        Expression ex = substitute(substitution);
        if(ex instanceof Constant){
            return (Constant) ex;
        }else{
            return Undefined.getInstance();
        }
    }
    //returns its value in the substitution, or itself if its value in the substitution is not there
    @Override
    public Expression substitute(Substitution substitution) {
        Expression val = substitution.get(this);
        if(val==null){
            return this;//variable wasn't defined in substitution
        }else{
            return val.copy().substitute(substitution);//substitute because the value may be an expression with more variables
        }
    }
    
    public final Expression simplify(){
        return this;
    }
    
    
    @Override
    public Expression getDerivative(Variable v) {
        if(this.equals(v)){
            return new RealNumber(1);
        }else{
            return new RealNumber(0);
        }
    }
    
    @Override
    public int compareTo(Variable v2) {
        if((int) letter>(int)v2.letter){
            return 1;
        }else if(letter==v2.letter){
            if(subscript>v2.subscript){
                return 1;
            }else if(subscript==v2.subscript){
                return 0;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
    public char getCharacter() {
        return letter;
    }

}
