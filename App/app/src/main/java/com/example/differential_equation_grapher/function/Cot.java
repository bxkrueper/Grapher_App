package com.example.differential_equation_grapher.function;

public class Cot extends TrigFunction{

    public static final String NAME = "cot";

    public Cot(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Cot(Arguments arguments) {
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

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Cot(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }


    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Product(getFirstArg().getDerivative(v),new Negate(new Power(new Csc(inside.copy()),new RealNumber(2))));
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.divide(RealNumber.multiplictiveIdentity,ConstantMediator.tan(getFirstArg().evaluate(substitution)));
    }

}
