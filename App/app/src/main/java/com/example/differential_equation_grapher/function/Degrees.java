package com.example.differential_equation_grapher.function;

//converts to radians
public class Degrees extends PostfixExpression{

    private static final RealNumber MULTIPLIER = new RealNumber(Math.PI/180);

    public Degrees(Arguments arguments) {
        super(arguments);
    }
    public Degrees(Expression expression) {
        super(new ArgumentSingle(expression));
    }

    @Override
    public String getSymbol() {
        return "\u00B0";
    }

    @Override
    public String getName() {
        return "Degrees";
    }

    @Override
    public Expression specialSimplification() {
        Expression arg = getFirstArg();
        if(arg instanceof Constant){
            return ConstantMediator.multiply(MULTIPLIER,(Constant) arg);
        }else{
            return this;
        }
    }

    @Override
    public Expression getDerivative(Variable v) {
        return new Product(MULTIPLIER,getFirstArg().getDerivative(v));
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Degrees(newArguments);
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.multiply(getFirstArg().evaluate(substitution),MULTIPLIER);
    }


}
