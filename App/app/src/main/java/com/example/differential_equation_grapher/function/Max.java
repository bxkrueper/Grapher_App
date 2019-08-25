package com.example.differential_equation_grapher.function;

import java.util.List;

public abstract class Max extends PredefinedFunction{
    
    public Max(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public Max(Arguments arguments) {
        super(arguments);
    }
    

    @Override
    public Expression specialSimplification() {
        Constant max = null;
        int indexOfMax = -1;
        for(int i=0;i<getNumberOfArguments();i++){
            Expression canidate = getArg(i);
            if(canidate instanceof Constant){
                Constant canConstant = (Constant) canidate;
                if(max==null || canConstant.compareTo(max)==1){//if it is bigger
                    //delete old maxExpression constant
                    if(max!=null){
                        removeArg(indexOfMax);
                        i--;
                    }
                    //update maxExpression
                    max = canConstant;
                    indexOfMax = i;
                }else if((max!=null && canConstant.compareTo(max)<1)){//if it is smaller
                    //delete current
                    removeArg(i);
                    i--;
                }
            }
            
            
        }
        
        if(getNumberOfArguments()==1){
            return getFirstArg();
        }else{
            return this;
        }
        
    }
    

    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()>=1;
    }
    @Override
    public Expression getDerivative(Variable v) {
        Arguments arguments = getArguments();
        Expression[] derivativeArray = new Expression[arguments.getNumberOfArguments()];
        for(int i=0;i<derivativeArray.length;i++){
            derivativeArray[i] = arguments.getArg(i).getDerivative(v);
        }
        return new MultiConditionalExpression(new MaxExclusiveCondition(this),derivativeArray);
    }

    //derivative is usually discontinuous at ties, so returns index -1 on ties
    private static class MaxExclusiveCondition implements MultiCondition{

        private Max maxExpression;

        public MaxExclusiveCondition(Max maxExpression) {
            this.maxExpression = maxExpression;
        }

        ////////if max is complex number, weird
        @Override
        public int indexToChoose(Substitution substitution) {
            Constant max = maxExpression.getFirstArg().evaluate(substitution);
            int index = 0;
            if(max==Undefined.getInstance()){
                return 0;
            }
            for(int i=1;i<maxExpression.getNumberOfArguments();i++){
                Constant canidate = maxExpression.getArg(i).evaluate(substitution);
                if(canidate==Undefined.getInstance()){
                    return i;
                }
                if(canidate.compareTo(max)==1){
                    max=canidate;
                    index = i;
                }else if(canidate.compareTo(max)==0){
                    index = -1;
                }
            }
            return index;
        }

        @Override
        public boolean isEqualTo(MultiCondition condition) {
            if(condition instanceof MaxExclusiveCondition){
                MaxExclusiveCondition otherCondition = (MaxExclusiveCondition) condition;
                return maxExpression.getArguments().isEqualTo(otherCondition.maxExpression.getArguments());
            }else{
                return false;
            }
        }
    }

}
