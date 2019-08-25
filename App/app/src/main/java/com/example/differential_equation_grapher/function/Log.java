package com.example.differential_equation_grapher.function;


import com.example.differential_equation_grapher.myAlgs.MyMath;

public class Log extends PredefinedFunction{
    
    public static final String NAME = "log";

    private double base;
    
    public Log(Arguments arguments, double base) {
        super(arguments);
        this.base = base;
    }
    public Log(Expression expression, double base) {
        this(new ArgumentSingle(expression),base);
    }

    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append('[');
        sb.append(MyMath.toNiceString(base,5));
        sb.append("](");
        sb.append(getFirstArg().toDisplayString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append('[');
        sb.append(MyMath.toNiceString(base,5));
        sb.append("](");
        sb.append(getFirstArg().toDebugString());
        sb.append(")");
        return sb.toString();
    }


    @Override
    public Expression specialSimplification() {
        return this;
    }

    //d/dx of logb(f(x)) is f'(x)/(ln(b)*f(x))   (super class is taking care of f'(x))
    @Override
    public Expression getDerivative(Variable v) {
        Expression fx = getFirstArg().copy();

        RealNumber lnb = new RealNumber(Math.log(base));//java log is actually ln
        Expression derIfPositive = new Divide(fx.getDerivative(v),new Product(lnb,fx));

        Expression answer = new ConditionalExpression(derIfPositive,Undefined.getInstance(),ConditionFactory.getPositiveCondition(fx));
        return answer;
    }
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Log(newArguments,base);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.log(getFirstArg().evaluate(substitution),base);
    }


}
