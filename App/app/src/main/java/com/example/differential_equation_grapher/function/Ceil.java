package com.example.differential_equation_grapher.function;

import java.util.List;

public class Ceil extends PredefinedFunction{

    public static final String NAME = "ceil";

    public Ceil(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public Ceil(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }




    @Override
    public Expression specialSimplification() {
        return this;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        Constant roundTo;
        if(getNumberOfArguments()==1){
            roundTo = RealNumber.multiplictiveIdentity;//1
        }else{
            roundTo = (Constant) getSecondArg();
        }
        return ConstantMediator.ceil(getFirstArg().evaluate(substitution),roundTo);
    }


    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Ceil(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        if(arguments.getNumberOfArguments()==1){
            return true;
        }else if(arguments.getNumberOfArguments()==2){
            return getSecondArg()instanceof Constant;
        }else{
            return false;
        }
    }
    @Override
    public Expression getDerivative(Variable v) {
        Constant roundTo;
        if(getNumberOfArguments()==1){
            roundTo = RealNumber.multiplictiveIdentity;//1
        }else{
            roundTo = (Constant) getSecondArg();
        }
        Expression derivativeIfInteger = Undefined.getInstance();
        Expression derivativeIfNotInteger = RealNumber.additiveIdentity;

        Condition multipleOfCondition = ConditionFactory.getMultipleOfCondition(this.getArguments().getFirstArg(),roundTo);

        return new ConditionalExpression(derivativeIfInteger,derivativeIfNotInteger,multipleOfCondition);
    }
}
