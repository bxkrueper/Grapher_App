package com.example.differential_equation_grapher.function;

import java.util.List;

public abstract class Min extends PredefinedFunction{

    public Min(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public Min(Arguments arguments) {
        super(arguments);
    }


    @Override
    public Expression specialSimplification() {
        Constant min = null;
        int indexOfMin = -1;
        for(int i=0;i<getNumberOfArguments();i++){
            Expression canidate = getArg(i);
            if(canidate instanceof Constant){
                Constant canConstant = (Constant) canidate;
                if(min==null || canConstant.compareTo(min)==-1){//if it is smaller
                    //delete old min constant
                    if(min!=null){
                        removeArg(indexOfMin);
                        i--;
                    }
                    //update max
                    min = canConstant;
                    indexOfMin = i;
                }else if((min!=null && canConstant.compareTo(min)>-1)){//if it is bigger
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
        return new MultiConditionalExpression(new MinExclusiveCondition(this),derivativeArray);
    }

    //derivative is usually discontinuous at ties, so returns index -1 on ties
    private static class MinExclusiveCondition implements MultiCondition{

        private Min minExpression;

        public MinExclusiveCondition(Min minExpression) {
            this.minExpression = minExpression;
        }

        ////////if min is complex number, weird
        @Override
        public int indexToChoose(Substitution substitution) {
            Constant min = minExpression.getFirstArg().evaluate(substitution);
            int index = 0;
            if(min==Undefined.getInstance()){
                return 0;
            }
            for(int i=1;i<minExpression.getNumberOfArguments();i++){
                Constant canidate = minExpression.getArg(i).evaluate(substitution);
                if(canidate==Undefined.getInstance()){
                    return i;
                }
                if(canidate.compareTo(min)==-1){
                    min=canidate;
                    index = i;
                }else if(canidate.compareTo(min)==0){
                    index = -1;
                }
            }
            return index;
        }

        @Override
        public boolean isEqualTo(MultiCondition condition) {
            if(condition instanceof MinExclusiveCondition){
                MinExclusiveCondition otherCondition = (MinExclusiveCondition) condition;
                return minExpression.getArguments().isEqualTo(otherCondition.minExpression.getArguments());
            }else{
                return false;
            }
        }
    }




}
