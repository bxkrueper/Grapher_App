package com.example.differential_equation_grapher.function;

import java.util.List;

//returns undefined if there is a tie for first place
public class MinExclusive extends Min{

    public static final String NAME = "minex";

    public MinExclusive(List<Expression> exList) {
        super(new ArgumentsList(exList));
    }
    public MinExclusive(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        Constant min = getFirstArg().evaluate(substitution);
        Constant toReturn = min;
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
                toReturn = min;
            }else if(canidate.compareTo(min)==0){
                toReturn = Undefined.getInstance();
            }
        }
        return toReturn;
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new MinExclusive(newArguments);
    }
}
