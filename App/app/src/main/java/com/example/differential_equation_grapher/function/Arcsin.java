package com.example.differential_equation_grapher.function;

public class Arcsin extends PredefinedFunction{

    public static final String NAME = "arcsin";

    public Arcsin(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Arcsin(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {
        if(getFirstArg() instanceof Sin){
            return ((Sin) getFirstArg()).getFirstArg().copy();
        }else{
            return this;
        }
    }

    //(d of inside) * 1/sqrt(1-inside^2)  only between -1 and 1
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        Expression sqrt = new SquareRoot(new Sum(RealNumber.multiplictiveIdentity,new Negate(new Power(inside.copy(),new RealNumber(2)))));
        return new Divide(inside.getDerivative(v),sqrt);
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Arcsin(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.arcsin(getFirstArg().evaluate(substitution));
    }

}
