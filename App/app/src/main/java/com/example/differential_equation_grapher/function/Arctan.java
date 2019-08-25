package com.example.differential_equation_grapher.function;

public class Arctan extends PredefinedFunction{

    public static final String NAME = "arctan";

    public Arctan(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Arctan(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {
        if(getFirstArg() instanceof Tan){
            return ((Tan) getFirstArg()).getFirstArg().copy();
        }else{
            return this;
        }
    }

    //(d of inside) * 1/(1+inside^2)
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Divide(inside.getDerivative(v),new Sum(RealNumber.multiplictiveIdentity,new Power(inside.copy(),new RealNumber(2))));
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Arctan(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.arctan(getFirstArg().evaluate(substitution));
    }

}
