//package com.example.differential_equation_grapher.function;
//
//import com.example.differential_equation_grapher.myAlgs.MyAlgs;
//
//import java.util.List;
//import java.util.Set;
//
//public class ConditionalFunction implements Function{
//    private Function trueFunction;
//    private Function falseFunction;
//    private Condition condition;
//
//    //trueFunction's sub map is used, falseFunction's map is linked to true function's map
//
//
//    public ConditionalFunction(Expression trueExpression, Expression falseExpression,Condition condition,Substitution subMap){
//        this.trueFunction = new BasicFunction(trueExpression);
//        this.trueFunction.setSubstitution(subMap);
//        this.falseFunction = new BasicFunction(falseExpression,trueFunction.getSubstitution());
//        this.condition = condition;
//
//    }
//    public ConditionalFunction(Expression trueFunction, Expression falseFunction,Condition condition){
//        this(trueFunction,falseFunction,condition,new SubstitutionMap());
//    }
//    public ConditionalFunction(String trueExString,String falseExString,Condition condition){
//        this(ExpressionFactory.getExpression(trueExString),ExpressionFactory.getExpression(falseExString),condition);
//    }
//    public ConditionalFunction(String trueExString,String falseExString,Condition condition,Substitution subMap){
//        this(ExpressionFactory.getExpression(trueExString),ExpressionFactory.getExpression(falseExString),condition,subMap);
//    }
//
//    @Override
//    public Expression getExpression() {
//        return new ExpressionGroup(trueFunction.getExpression(),falseFunction.getExpression());//////save this value and return it
//    }
//
//    @Override
//    public Substitution getSubstitution() {
//        return trueFunction.getSubstitution();
//    }
//
//    @Override
//    public void setSubstitution(Substitution sub) {
//        this.trueFunction.setSubstitution(sub);
//        this.falseFunction.setSubstitution(trueFunction.getSubstitution());
//    }
//
//    @Override
//    public Constant evaluate() {
//        if(condition.passes()){
//            return trueFunction.evaluate();
//        }else{
//            return falseFunction.evaluate();
//        }
//    }
//
//    @Override
//    public void setVariable(Variable variable, Expression expression) {
//        trueFunction.setVariable(variable,expression);
//    }
//
//    @Override
//    public void setVariable(Variable variable, double realNumber) {
//        trueFunction.setVariable(variable,realNumber);
//    }
//
//    @Override
//    public void setVariable(char variable, int subscript, Expression expression) {
//        trueFunction.setVariable(variable,subscript,expression);
//    }
//
//    @Override
//    public void setVariable(char variable, int subscript, double realNumber) {
//        trueFunction.setVariable(variable,subscript,realNumber);
//    }
//
//    @Override
//    public void setVariable(char variable, Expression expression) {
//        trueFunction.setVariable(variable,expression);
//    }
//
//    @Override
//    public void setVariable(char variable, double realNumber) {
//        trueFunction.setVariable(variable,realNumber);
//    }
//
//    //returns null if variable is not there
//    @Override
//    public Expression getVariableValue(Variable variable) {
//        return trueFunction.getVariableValue(variable);
//    }
//
//    @Override
//    public Set<Variable> getVariablesInExpression() {
//
//    }
//
//    @Override
//    public List<Variable> getVariablesInFunctionArgs() {
//        return trueFunction.getVariablesInFunctionArgs();
//    }
//
//    @Override
//    public boolean allVariablesDefined() {
//
//    }
//
//    public boolean variablesComputable(){
//        return trueFunction.variablesComputable() && falseFunction.variablesComputable();
//    }
//
//    @Override
//    public String toDisplayString() {
//        return trueFunction.toDisplayString() + " if " + condition.toString() + " else " + falseFunction.toDisplayString();
//    }
//    @Override
//    public String toDebugString() {
//        return trueFunction.toDebugString() + " if " + condition.toString() + " else " + falseFunction.toDebugString();
//    }
//
//    @Override
//    public String toString(){
//        return toDisplayString();
//    }
//
//    @Override
//    public void substitute() {
//        trueFunction.substitute();
//        falseFunction.substitute();
//    }
//
//    @Override
//    public void simplify() {
//        trueFunction.simplify();
//        falseFunction.simplify();
//    }
//
//    @Override
//    public Function getDerivative(Variable v) {
//
//    }
//}
