package com.example.differential_equation_grapher.function;

public class Arccos extends PredefinedFunction{

    public static final String NAME = "arccos";

    public Arccos(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Arccos(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {
        if(getFirstArg() instanceof Cos){
            return ((Cos) getFirstArg()).getFirstArg().copy();
        }else{
            return this;
        }
    }

    //(d of inside) * -1/sqrt(1-inside^2)
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        Expression sqrt = new SquareRoot(new Sum(RealNumber.multiplictiveIdentity,new Negate(new Power(inside.copy(),new RealNumber(2)))));
        return new Negate(new Divide(inside.getDerivative(v),sqrt));
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Arccos(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.arccos(getFirstArg().evaluate(substitution));
    }

}
