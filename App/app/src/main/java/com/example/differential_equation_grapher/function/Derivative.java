package com.example.differential_equation_grapher.function;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

//arguments: (expression,variable)
public class Derivative extends PredefinedFunction{

    public static final String NAME = "der";

    private Variable derivingVariable;

    public Derivative(List<Expression> exList) {
        this(new ArgumentsList(exList));
    }
    public Derivative(Arguments arguments) {
        super(arguments);
        if(getNumberOfArguments()==1){
            derivingVariable = getOnlyVariableInExpresion();
        }else{
            if(getSecondArg() instanceof Variable){
                this.derivingVariable = (Variable) getSecondArg();
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }




    @Override
    public Expression specialSimplification() {
        return getFirstArg().getDerivative(derivingVariable).simplify();
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return getFirstArg().getDerivative(derivingVariable).simplify().evaluate(substitution);
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Derivative(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        if(getNumberOfArguments()==1){
            Variable onlyVariable = getOnlyVariableInExpresion();
            return onlyVariable!=null;
        }else if(getNumberOfArguments()==2){
            return (getSecondArg() instanceof Variable);
        }else{
            return false;
        }
    }
    @Override
    public Expression getDerivative(Variable v) {
        //need to simplify after taking derivative (taking the derivative of an unsimplified power expression turned out messy and wrong)
        Expression innerDerivative = getFirstArg().getDerivative(derivingVariable).simplify();//simplified form of this operation with respect to the deriving variable
        return innerDerivative.getDerivative(v);
    }

    //returns null if there is more than one variable in the expression
    //if there are 0, default to x. Doesn't matter
    private Variable getOnlyVariableInExpresion(){
        GetAllVariableTypesVisitor varVisitor = new GetAllVariableTypesVisitor();
        getFirstArg().getVisitedBy(varVisitor);
        Set<Variable> set = varVisitor.getSet();
        if(set.size()==0){
            return new Variable('x');
        }else if(set.size()==1){
            Iterator<Variable> iterator = set.iterator();
            return iterator.next();
        }else{
            return null;
        }
    }



}