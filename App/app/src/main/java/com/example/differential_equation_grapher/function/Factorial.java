package com.example.differential_equation_grapher.function;

public class Factorial extends PostfixExpression{
    
    public Factorial(Arguments arguments) {
        super(arguments);
    }
    public Factorial(Expression expression) {
        super(new ArgumentSingle(expression));
    }

    @Override
    public String getSymbol() {
        return "!";
    }
    
    @Override
    public String getName() {
        return "Factorial";
    }
    
    @Override
    public Expression specialSimplification() {
        return this;
    }
    
    @Override
    public Expression getDerivative(Variable v) {
        return Undefined.getInstance();
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Factorial(newArguments);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.gamma(getFirstArg().evaluate(substitution));
    }
    

}
