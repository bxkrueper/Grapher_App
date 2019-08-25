package com.example.differential_equation_grapher.function;

import java.util.List;

//returns undefined if there is a tie for first place
public class MaxExclusive extends Max{
    public static final String NAME = "maxex";

    public MaxExclusive(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public MaxExclusive(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        Constant max = getFirstArg().evaluate(substitution);
        Constant toReturn = max;
        if(max==Undefined.getInstance()){
            return Undefined.getInstance();
        }
        for(int i=1;i<getNumberOfArguments();i++){
            Constant canidate = getArg(i).evaluate(substitution);
            if(canidate==Undefined.getInstance()){
                return Undefined.getInstance();
            }
            if(canidate.compareTo(max)==1){
                max=canidate;
                toReturn = max;
            }else if(canidate.compareTo(max)==0){
                toReturn = Undefined.getInstance();
            }
        }
        return toReturn;
    }


    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new MaxExclusive(newArguments);
    }
}
