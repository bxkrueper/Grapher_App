package com.example.differential_equation_grapher.function;

import java.util.List;

public class MaxNonExclusive extends Max{
    public static final String NAME = "max";

    public MaxNonExclusive(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public MaxNonExclusive(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        Constant max = getFirstArg().evaluate(substitution);
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
            }
        }
        return max;
    }


    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new MaxNonExclusive(newArguments);
    }
}
