package com.example.differential_equation_grapher.function;

public class SquareRoot extends PredefinedFunction{
    
    public static final String NAME = "sqrt";
    private static final RealNumber powerValue = new RealNumber(0.5);

    public SquareRoot(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public SquareRoot(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public Expression specialSimplification() {
        /////todo: check for power to even
        return this;
    }
    
    
    @Override
    public String toDisplayString() {
        return "\u221A" + "(" + getFirstArg().toDisplayString() + ")";
    }
    @Override
    public Expression getDerivative(Variable v) {
        return new Divide(getFirstArg().getDerivative(v),new Product(new RealNumber(2),this.copy()));
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    @Override
    public Operation getSubclassInstance(Arguments newArguments) {
        return new SquareRoot(newArguments);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.power(getFirstArg().evaluate(substitution),powerValue);
    }
    

}
