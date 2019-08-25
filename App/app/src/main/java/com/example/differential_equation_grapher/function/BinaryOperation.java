package com.example.differential_equation_grapher.function;

public abstract class BinaryOperation extends ChainOperation{
    
    public BinaryOperation(Arguments args) {
        super(args);
    }
    
    @Override
    public final boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==2;
    }
    
    protected boolean useParenthesesForPrint(Expression ex){
        if(ex instanceof ChainOperation){//////////////////////don't use instanceof?
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toDisplayString() {
        Expression ex1 = getFirstArg();
        Expression ex2 = getSecondArg();
        StringBuilder sb = new StringBuilder();
        if(useParenthesesForPrint(ex1)){
            sb.append("(");
            sb.append(ex1.toDisplayString());
            sb.append(")");
        }else{
            sb.append(ex1.toDisplayString());
        }
        sb.append(getSymbol());
        if(useParenthesesForPrint(ex2)){
            sb.append("(");
            sb.append(ex2.toDisplayString());
            sb.append(")");
        }else{
            sb.append(ex2.toDisplayString());
        }
        
        return sb.toString();
    }
    @Override
    public final boolean isEqualTo(Object o) {
        if(o instanceof BinaryOperation){
            BinaryOperation b2 = (BinaryOperation) o;
            if(!this.getSymbol().equals(b2.getSymbol())){
                return false;
            }
            return this.getFirstArg().equals(b2.getFirstArg()) && this.getSecondArg().equals(b2.getSecondArg());
        }else{
            return false;
        }
    }
    
}
