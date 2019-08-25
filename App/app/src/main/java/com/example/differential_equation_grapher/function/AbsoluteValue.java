package com.example.differential_equation_grapher.function;

public class AbsoluteValue extends PredefinedFunction{
    
    public static final String NAME = "abs";

    public AbsoluteValue(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public AbsoluteValue(Arguments arguments) {
        super(arguments);
    }
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {//todo:////////power of even num
        Expression arg = getFirstArg();
        if(arg instanceof Negate){
            setFirstArg(((Negate) arg).getFirstArg());//negate was pointless, get rid of it
        }
        return this;
    }
    
    @Override
    public PredefinedFunction getSubclassInstance(Arguments newArguments) {
        return new AbsoluteValue(newArguments);
    }
    
    @Override
    public String toDisplayString() {
        return "|" + this.getArguments().getFirstArg() + "|";
    }


    @Override
    public Expression getDerivative(Variable v) {
        Expression derivativeIfFuncPositive = this.getArguments().getFirstArg().getDerivative(v);
        Expression derivativeIfFuncNegative = new Negate(this.getArguments().getFirstArg().getDerivative(v));
        Expression derivativeIfFunc0 = Undefined.getInstance();

        Condition positiveCondition = ConditionFactory.getPositiveCondition(this.getArguments().getFirstArg());
        Condition zeroCondition = ConditionFactory.getEqualsCondition(this.getArguments().getFirstArg(),RealNumber.additiveIdentity);

        Expression derivativeIfFuncNotPositive = new ConditionalExpression(derivativeIfFunc0,derivativeIfFuncNegative,zeroCondition);

        return new ConditionalExpression(derivativeIfFuncPositive,derivativeIfFuncNotPositive,positiveCondition);
    }

    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.abs(getFirstArg().evaluate(substitution));
    }
}
