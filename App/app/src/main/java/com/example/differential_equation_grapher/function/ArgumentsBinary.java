package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArgumentsBinary implements Arguments{
    
    private Expression ex1,ex2;
    private boolean computable;//maintained
    
    public ArgumentsBinary(Expression ex1, Expression ex2){
        this.ex1 = ex1;
        this.ex2 = ex2;
        this.computable = expressionsAreComputable(ex1,ex2);
    }

    private boolean expressionsAreComputable(Expression ex12, Expression ex22) {
        return ex1.computable() && ex2.computable();
    }

    @Override
    public void simplify() {
        ex1 = ex1.simplify();
        ex2 = ex2.simplify();
        this.computable = expressionsAreComputable(ex1,ex2);//maintain field
    }

    @Override
    public boolean isComputable() {
        return this.computable;
    }

    @Override
    public void substitute(Substitution substitution) {
        if(computable){
            return;//no variables in here. don't bother
        }else{
            ex1 = ex1.substitute(substitution);
            ex2 = ex2.substitute(substitution);
            this.computable = expressionsAreComputable(ex1,ex2);//maintain field
        }
    }

    @Override
    public Arguments copy() {
        return new ArgumentsBinary(ex1.copy(),ex2.copy());
    }

    @Override
    public Expression getArg(int index) {
        if(index==0){
            return ex1;
        }else if(index==1){
            return ex2;
        }else{
            return null;
        }
    }

    @Override
    public Expression getFirstArg() {
        return ex1;
    }

    @Override
    public Expression getSecondArg() {
        return ex2;
    }

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public boolean isEqualTo(Arguments arg2){
        if(getNumberOfArguments()!=arg2.getNumberOfArguments()){
            return false;
        }
        for(int i=0;i<getNumberOfArguments();i++){
            if(!getArg(i).equals(arg2.getArg(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public void setArg(int index, Expression ex) {
        if(index==0){
            ex1 = ex;
        }else if(index==1){
            ex2 = ex;
        }
    }

    @Override
    public void setFirstArg(Expression ex) {
        ex1 = ex;
    }

    @Override
    public void setSecondArg(Expression ex) {
        ex2 = ex;
    }
    
    @Override
    public void removeArg(int index) {
        if(index==0){
            ex1=null;/////////////////
        }else if(index==1){
            ex2=null;
        }
    }
    
    @Override
    public String toString(){
        return toDisplayString();
    }
    
    @Override
    public String toDisplayString() {
        return "(" + this.getFirstArg().toDisplayString() + "," + this.getSecondArg().toDisplayString() + ")";
    }
    
    @Override
    public String toDebugString() {
        return "(" + this.getFirstArg().toDebugString() + "," + this.getSecondArg().toDebugString() + ")";
    }

    @Override
    public List<Expression> getExList() {
        List<Expression> list = new ArrayList<Expression>(2);
        list.add(ex1);
        list.add(ex2);
        return list;
    }

    @Override
    public boolean isEqualToOrderDoesntMatter(Arguments arg2) {
        if(this.getNumberOfArguments()!=arg2.getNumberOfArguments()){
            return false;
        }
        
        if(ex1.equals(arg2.getFirstArg())){
            return ex2.equals(arg2.getSecondArg());
        }else{
            return ex1.equals(arg2.getSecondArg()) && ex2.equals(arg2.getFirstArg());
        }
    }
    
    @Override
    public Iterator<Expression> iterator() {
        return new ArgumentsBinaryIterator();
    }
    
    private class ArgumentsBinaryIterator implements Iterator<Expression>{
        
        private int index;
        
        public ArgumentsBinaryIterator(){
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index<2;
        }

        @Override
        public Expression next() {
            if(index==0){
                index++;
                return ex1;
            }else if(index==1){
                index++;
                return ex2;
            }else{
                return null;
            }
        }
        
    }

}
