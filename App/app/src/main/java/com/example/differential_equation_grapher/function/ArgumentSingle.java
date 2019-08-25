package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArgumentSingle implements Arguments{
    
    private Expression expression;
    private boolean computable;//maintained
    
    public ArgumentSingle(Expression expression){
        this.expression = expression;
        this.computable = expression.computable();
    }

    @Override
    public void simplify() {
        expression = expression.simplify();
        this.computable = expression.computable();//maintain field
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
            expression = expression.substitute(substitution);
            this.computable = expression.computable();//maintain field
        }
    }

    @Override
    public Arguments copy() {
        return new ArgumentSingle(expression.copy());
    }

    @Override
    public Expression getArg(int index) {
        if(index==0){
            return expression;
        }else{
            return null;
        }
    }

    @Override
    public Expression getFirstArg() {
        return expression;
    }

    @Override
    public Expression getSecondArg() {
        return null;
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
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
            expression = ex;
        }
    }

    @Override
    public void setFirstArg(Expression ex) {
        expression = ex;
    }

    @Override
    public void setSecondArg(Expression ex) {//do nothing
    }
    
    @Override
    public void removeArg(int index) {
        if(index==0){
            expression=null;/////////////////
        }
    }

    
    @Override
    public String toString(){
        return toDisplayString();
    }
    
    @Override
    public String toDisplayString() {
        return "(" + this.getFirstArg().toDisplayString() + ")";
    }
    
    @Override
    public String toDebugString() {
        return "(" + this.getFirstArg().toDebugString() + ")";
    }
    
    @Override
    public List<Expression> getExList() {
        List<Expression> list = new ArrayList<Expression>(1);
        list.add(expression);
        return list;
    }

    @Override
    public boolean isEqualToOrderDoesntMatter(Arguments arg2) {
        if(this.getNumberOfArguments()!=arg2.getNumberOfArguments()){
            return false;
        }
        
        return expression.equals(arg2.getFirstArg());
    }

    @Override
    public Iterator<Expression> iterator() {
        return new ArgumentSingleIterator();
    }
    
    private class ArgumentSingleIterator implements Iterator<Expression>{
        
        private boolean hasNext;
        
        public ArgumentSingleIterator(){
            this.hasNext = true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Expression next() {
            if(hasNext){
                hasNext = false;
                return expression;
            }else{
                return null;
            }
        }
        
    }
}
