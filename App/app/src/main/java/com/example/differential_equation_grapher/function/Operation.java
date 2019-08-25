package com.example.differential_equation_grapher.function;

import java.util.List;

//this Expression has other expressions in its arguments that are used to evaluate the answer in evaluate()

public abstract class Operation extends Expression{
    
    private Arguments arguments;
    
    public Operation(Arguments arguments){
        this.arguments = arguments;
        if(argumentsAreCompatable(this.arguments)){

        }else{
            throw new RuntimeException("bad arguments for operation: " + this.getClass() + " arguments: " + arguments.toDebugString());
        }
    }
    
    public abstract boolean argumentsAreCompatable(Arguments arguments);
    public final int getNumberOfArguments(){
        return arguments.getNumberOfArguments();
    }
    
    public final Expression copy(){
        return getSubclassInstance(arguments.copy());
    }
    
    public final Expression simplify(){
        arguments.simplify();
        if(arguments.isComputable()){
            return evaluate(NullSubstitution.getInstance());
        }else{
            return specialSimplification();
        }
    }
    
    public final Arguments getArguments(){
        return arguments;
    }
    
    public final Expression getFirstArg(){
        return arguments.getFirstArg();
    }
    public final Expression getSecondArg(){
        return arguments.getSecondArg();
    }
    public final Expression getArg(int index){
        return arguments.getArg(index);
    }
    
    public final void removeArg(int index){
        arguments.removeArg(index);
    }
    
    public final void setFirstArg(Expression newExpression){
        arguments.setFirstArg(newExpression);
    }
    public final void setSecondArg(Expression newExpression){
        arguments.setSecondArg(newExpression);
    }
    public final void setArg(int index,Expression newExpression){
        arguments.setArg(index,newExpression);
    }
    
    public final boolean computable(){
        return arguments.isComputable();
    }
    
    public final Expression substitute(Substitution substitution){
        arguments.substitute(substitution);
        return this;
    }
    
    @Override
    public String toDebugString(){
        return getName() + getArguments().toDebugString();
    }
    
    //children are already simplified by this point, but it is not computable.
    //Example: 0+x returns just x
    public abstract Expression specialSimplification();
    
    
    
//    @Override
//    public final Constant evaluate(Substitution substitution) {
//        return ConstantMediator.evaluate(getArguments(), this,substitution);
//    }
//    public abstract Constant operateConstant(Arguments args);//args should be just constants here
    
    public abstract String getName();
    
    public abstract Expression getSubclassInstance(Arguments newArguments);//returns a new instance of that class using these arguments
}
