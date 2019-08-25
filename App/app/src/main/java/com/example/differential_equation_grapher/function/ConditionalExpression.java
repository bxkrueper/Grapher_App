package com.example.differential_equation_grapher.function;

public class ConditionalExpression extends Expression{

    private Expression trueExpression;
    private Expression falseExpression;
    private Condition condition;

    public ConditionalExpression(Expression trueExpression, Expression falseExpression, Condition condition) {
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
        this.condition = condition;
    }

    @Override
    public String toDisplayString() {
        return "(if " + condition + " then " + trueExpression.toDisplayString() + " else " + falseExpression.toDisplayString() + ")";
    }

    @Override
    public String toDebugString() {
        return "(if " + condition + " then " + trueExpression.toDebugString() + " else " + falseExpression.toDebugString() + ")";
    }

    @Override
    public boolean isEqualTo(Object o) {
        if(o instanceof  ConditionalExpression){
            ConditionalExpression conditionalExpression = (ConditionalExpression) o;
            return trueExpression.equals(conditionalExpression.trueExpression) && falseExpression.equals(conditionalExpression.falseExpression) && condition.isEqualTo(conditionalExpression.condition);
        }else{
            return false;
        }
    }

    @Override
    public Expression copy() {
        return new ConditionalExpression(trueExpression,falseExpression,condition);/////copy fields too?
    }

    @Override
    public Expression simplify() {
        trueExpression = trueExpression.simplify();
        falseExpression = falseExpression.simplify();
        return this;
    }

    @Override
    public boolean computable() {
        return trueExpression.computable() && falseExpression.computable();
    }

    @Override
    public Expression substitute(Substitution substitution) {
        trueExpression = trueExpression.substitute(substitution);
        falseExpression = falseExpression.substitute(substitution);
        return this;
    }

    @Override
    public Expression getDerivative(Variable v) {
        return new ConditionalExpression(trueExpression.getDerivative(v),falseExpression.getDerivative(v),condition);
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        if(condition.passes(substitution)){
            return trueExpression.evaluate(substitution);
        }else{
            return falseExpression.evaluate(substitution);
        }
    }
}
