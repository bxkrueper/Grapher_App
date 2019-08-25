package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.List;


public class Product extends MultiOperation{
    
    public Product(Arguments args){
        super(args);
    }
    public Product(List<Expression> numeratorList){
        super(new ArgumentsList(numeratorList));
//        ifEmptyPut1();
    }
    public Product(Expression ...expressions){
        super(new ArgumentsList(expressions));
//        ifEmptyPut1();
    }
    
    public int getIndexOfFirstConstant() {
        for(int i=0;i<getNumberOfArguments();i++){
            if(getArg(i) instanceof Constant){
                return i;
            }
        }
        return -1;
    }
//    
//    public boolean remove(Expression ex){
//        boolean removed = expressionList.remove(ex);
//        ifEmptyPut1();
//        return removed;
//    }
//    
//    public Expression remove(int position){
//        Expression removed = expressionList.remove(position);
//        ifEmptyPut1();
//        return removed;
//    }
    
//    private void ifEmptyPut1(){
//        if(expressionList.isEmpty()){
//            expressionList.add(new RealNumber(1));
//        }
//    }
    
    @Override
    public String getName() {
        return "Product";
    }
    
    @Override
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        
        Expression prevEx = null;
        
        for(int i=0;i<getNumberOfArguments();i++){
            
            Expression currentEx = getArg(i);
            
            //add () around lesser priority operations (Sum)
            boolean addPar = false;
            if(currentEx instanceof ChainOperation){
                addPar = ((ChainOperation) currentEx).getPriority()<this.getPriority();
            }
            
            //determine if it should put a *
            boolean putStar = true;
            if(i==0){
                putStar = false;
            }else{
//                if(prevEx instanceof Constant && !(currentEx instanceof Constant)){
//                    if(currentEx instanceof Power){
//                        Power power = (Power) currentEx;
//                        if(!(power.getFirstArg() instanceof Constant)){
//                            putStar=false;
//                        }
//                    }else{
//                        putStar=false;
//                    }
//                }
            }
            
            if(putStar){
                sb.append("*");
            }
            
            
            if(addPar){
                sb.append('(');
            }
            sb.append(currentEx.toDisplayString());
            if(addPar){
                sb.append(')');
            }
            
            prevEx = currentEx;
        }
        return sb.toString();
    }

    @Override
    public Expression specialSimplification() {
        TermList termList = new TermList();
        for(int i=0;i<getNumberOfArguments();i++){
            termList.multiply(getArg(i));
        }
        
        return termList.getSimplifiedExpression();
    }
    
    
    
    
    
    @Override
    public int getPriority() {
        return TokenizerFactory.MULTIPLY_PRIORITY;
    }
    
    //ex: if f= abcx, then f' = abcx'+abc'x+ab'cx+a'bcx
    @Override
    public Expression getDerivative(Variable v) {
        int numArgs = getNumberOfArguments();
        List<Expression> sumList = new ArrayList<>(numArgs);
        for(int i=0;i<numArgs;i++){
            List<Expression> copyList = new ArrayList<>(numArgs);
            for(int j=0;j<numArgs;j++){
                Expression ex = getArg(j);
                if(i==j){
                    copyList.add(ex.getDerivative(v));
                }else{
                    copyList.add(ex.copy());
                }
                
            }
            sumList.add(new Product(copyList));
        }
        return new Sum(sumList);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        Constant product = getFirstArg().evaluate(substitution);
        for(int i=1;i<getNumberOfArguments();i++){
            product = ConstantMediator.multiply(product, getArg(i).evaluate(substitution));
        }
        return product;
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Product(newArguments);
    }
    @Override
    public String getSymbol() {
        return "*";
    }
    
    
    
}