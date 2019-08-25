package com.example.differential_equation_grapher.function;

public class Tanh extends TrigFunction{

    public static final String NAME = "tanh";

    public Tanh(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Tanh(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {
        // TODO Auto-generated method stub
        return this;
    }
    
    
    
    
    //d(inside) * (1-tanh(inside)^2)
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Product(inside.getDerivative(v),new Sum(RealNumber.multiplictiveIdentity,new Negate(new Power(new Tanh(inside.copy()),new RealNumber(2)))));
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Tanh(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.tanh(getFirstArg().evaluate(substitution));
    }
    
}
