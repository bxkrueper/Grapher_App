package com.example.differential_equation_grapher.function;

public class Negate extends PrefixExpression{
    
    public Negate(Arguments arguments) {
        super(arguments);
    }
    public Negate(Expression expression) {
        super(new ArgumentSingle(expression));
    }

    @Override
    public String getSymbol() {
        return "-";
    }
    
    @Override
    public String getName() {
        return "Negate";
    }
    
    @Override
    public Expression specialSimplification() {
        if(getFirstArg() instanceof Negate){
            Negate n = (Negate) getFirstArg();
            return n.getFirstArg();
        }else{
            return this;
        }
    }
    
    @Override
    public Expression getDerivative(Variable v) {
        return new Negate(getFirstArg().getDerivative(v));
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Negate(newArguments);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.negate(getFirstArg().evaluate(substitution));
    }
    

}
