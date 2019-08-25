package com.example.differential_equation_grapher.function;

import java.util.Set;
import java.util.TreeSet;

public class GetAllVariableTypesVisitor implements ExpressionVisitor{
    Set<Variable> varSet;
    
    public GetAllVariableTypesVisitor(){
        this.varSet = new TreeSet<>();
    }
    
    //call after variable visits expression
    public Set<Variable> getSet() {
        return varSet;
    }

    @Override
    public void visit(Expression expression) {
        if(expression instanceof Variable){//variables don't have children
            varSet.add((Variable) expression);
        }else if(expression instanceof Operation){
            Operation op = (Operation) expression;
            for(Expression ex:op.getArguments()){
                ex.getVisitedBy(this);
            }
        }
        else if(expression instanceof ExpressionGroup){
            ExpressionGroup eg = (ExpressionGroup) expression;
            for(Expression ex:eg){
                ex.getVisitedBy(this);
            }
        }
    }
}
