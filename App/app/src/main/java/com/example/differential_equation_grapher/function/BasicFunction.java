package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyAlgs;

import java.util.List;
import java.util.Set;


public class BasicFunction implements Function{

    private Expression expression;
    private Substitution subMap;
    
    public BasicFunction(Expression ex,Substitution subMap){
        this.expression = ex;
        this.subMap = subMap;
    }
    public BasicFunction(Expression ex){
        this(ex,new SubstitutionMap());
    }
    public BasicFunction(String exString){
        this(ExpressionFactory.getExpression(exString));
    }
    public BasicFunction(String exString,Substitution subMap){
        this(ExpressionFactory.getExpression(exString),subMap);
    }
    
    @Override
    public Expression getExpression() {
        return expression;
    }
    
    @Override
    public Substitution getSubstitution() {
        return subMap;
    }
    
    @Override
    public void setSubstitution(Substitution sub) {
        this.subMap = sub;
    }

    @Override
    public Function copy() {
        return new BasicFunction(expression.copy(),subMap.copy());
    }

    @Override
    public Constant evaluate() {
        Expression evaluated = expression.evaluate(subMap);
        if(evaluated instanceof Constant){
            return (Constant) evaluated;
        }else{
            return Undefined.getInstance();
        }
    }

    @Override
    public void setVariable(Variable variable, Expression expression) {
        subMap.put(variable, expression);
    }
    @Override
    public void setVariable(Variable variable, double realNumber) {
        setVariable(variable,new RealNumber(realNumber));
    }
    @Override
    public void setVariable(char variable, int subscript, Expression expression) {
        setVariable(new Variable(variable,subscript),expression);
    }
    @Override
    public void setVariable(char variable, int subscript, double realNumber) {
        setVariable(new Variable(variable,subscript),realNumber);
    }
    @Override
    public void setVariable(char variable, Expression expression) {
        setVariable(new Variable(variable),expression);
    }
    @Override
    public void setVariable(char variable, double realNumber) {
        setVariable(new Variable(variable),realNumber);
    }

    //returns null if variable is not there
    @Override
    public Expression getVariableValue(Variable variable) {
        return subMap.get(variable);
    }
    
    @Override
    public Set<Variable> getVariablesInExpression() {
        GetAllVariableTypesVisitor varVisitor = new GetAllVariableTypesVisitor();
        expression.getVisitedBy(varVisitor);
        return varVisitor.getSet();
    }
    
    @Override
    public List<Variable> getVariablesInFunctionArgs() {
        return subMap.getVariables();
    }
    
    @Override
    public boolean allVariablesDefined() {
        Set<Variable> expressionVarSet = getVariablesInExpression();
        for(Variable v:expressionVarSet){
            if(!(getVariableValue(v) instanceof Constant)){
                return false;
            }
        }
        return true;
    }
    
    public boolean variablesComputable(){
        Set<Variable> expressionSet = getVariablesInExpression();
        List<Variable> argList = getVariablesInFunctionArgs();
        for(Variable v:expressionSet){
            if(!argList.contains(v)){
                return false;
            }
            if(!(getVariableValue(v) instanceof Constant)){
                return false;
            }
            if((getVariableValue(v) instanceof Undefined)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toDisplayString() {
        return "f" + MyAlgs.listToString(subMap.getVariables(), '(', ')') + "=" + expression.toDisplayString();
    }
    @Override
    public String toDebugString() {
        return "f" + MyAlgs.listToString(subMap.getVariables(), '(', ')') + "=" + expression.toDebugString();
    }
    
    @Override
    public String toString(){
        return toDisplayString();
    }

    @Override
    public void substitute() {
        expression = expression.substitute(subMap);
        //////clear vars from map that were substituted
    }

    @Override
    public void simplify() {
//        System.out.println(expression);
        expression = expression.simplify();
//        System.out.println(expression);
    }
    
    @Override
    public Function getDerivative(Variable v) {
        Function derivative = new BasicFunction(expression.getDerivative(v).simplify());
        for(Variable var:subMap.getVariables()){
            derivative.setVariable(var, subMap.get(var));
        }
        return derivative;
    }




}
