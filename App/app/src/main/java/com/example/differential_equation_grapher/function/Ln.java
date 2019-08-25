package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyMath;

public class Ln extends Log{
    
    public static final String NAME = "ln";
    
    public Ln(Arguments arguments) {
        super(arguments,Math.E);
    }
    public Ln(Expression expression) {
        this(new ArgumentSingle(expression));
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        sb.append(getFirstArg().toDisplayString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        sb.append(getFirstArg().toDisplayString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Ln(newArguments);
    }

//    @Override
//    public Expression specialSimplification() {
//        return this;
//    }
    
    @Override
    public Expression getDerivative(Variable v) {
        Expression fx = getFirstArg().copy();
        
        Expression answerWithNoZeroCondition = new Divide(fx.getDerivative(v),fx);
        Expression answer = new ConditionalExpression(answerWithNoZeroCondition,Undefined.getInstance(),ConditionFactory.getPositiveCondition(fx));
        return answer;
    }
    
    

}
