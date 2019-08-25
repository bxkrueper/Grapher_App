package com.example.differential_equation_grapher.function;

import java.util.Arrays;

public class MultiConditionalExpression extends Expression{

    private Expression[] expressions;
    private MultiCondition condition;

    public MultiConditionalExpression(MultiCondition condition, Expression... expressions) {
        this.expressions = expressions;
        this.condition = condition;
    }

    @Override
    public String toDisplayString() {
        return "condition: " + condition + " expressions: " + Arrays.toString(expressions);
    }

    @Override
    public String toDebugString() {
        return "condition: " + condition + " expressions: " + Arrays.toString(expressions);
    }

    @Override
    public boolean isEqualTo(Object o) {
        if(o instanceof  MultiConditionalExpression){
            MultiConditionalExpression conditionalExpression = (MultiConditionalExpression) o;
            if(condition.isEqualTo(conditionalExpression.condition)){
                for(int i=0;i<expressions.length;i++){
                    if(!expressions[i].equals(conditionalExpression.expressions[i])){
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public Expression copy() {
        return new MultiConditionalExpression(condition,expressions);/////copy fields too?
    }

    @Override
    public Expression simplify() {
        for(int i=0;i<expressions.length;i++){
            expressions[i] = expressions[i].simplify();
        }
        return this;
    }

    @Override
    public boolean computable() {
        for(Expression ex: expressions){
            if(!ex.computable()){
                return false;
            }
        }
        return true;
    }

    @Override
    public Expression substitute(Substitution substitution) {
        for(int i=0;i<expressions.length;i++){
            expressions[i] = expressions[i].substitute(substitution);
        }
        return this;
    }

    @Override
    public Expression getDerivative(Variable v) {
        Expression[] derivativeArray = new Expression[expressions.length];
        for(int i=0;i<derivativeArray.length;i++){
            derivativeArray[i] = expressions[i].getDerivative(v);
        }
        return new MultiConditionalExpression(condition,derivativeArray);
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        int index = condition.indexToChoose(substitution);
        if(index<0 || index>=expressions.length){
            return Undefined.getInstance();
        }

        return expressions[index].evaluate(substitution);
    }
}
