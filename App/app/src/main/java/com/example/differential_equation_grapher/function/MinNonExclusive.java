package com.example.differential_equation_grapher.function;

import java.util.List;

public class MinNonExclusive extends Min{

    public static final String NAME = "min";

    public MinNonExclusive(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public MinNonExclusive(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        Constant min = getFirstArg().evaluate(substitution);
        if(min==Undefined.getInstance()){
            return Undefined.getInstance();
        }
        for(int i=1;i<getNumberOfArguments();i++){
            Constant canidate = getArg(i).evaluate(substitution);
            if(canidate==Undefined.getInstance()){
                return Undefined.getInstance();
            }
            if(canidate.compareTo(min)==-1){
                min=canidate;
            }
        }
        return min;
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new MinNonExclusive(newArguments);
    }
}
