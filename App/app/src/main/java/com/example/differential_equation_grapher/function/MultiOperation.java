package com.example.differential_equation_grapher.function;

public abstract class MultiOperation extends ChainOperation{
    
    public MultiOperation(Arguments arguments) {
        super(arguments);
    }

    @Override
    public final boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()>=1;
    }
    
    //order does not matter
    @Override
    public final boolean isEqualTo(Object o) {
        if(o instanceof MultiOperation){
            MultiOperation p2 = (MultiOperation) o;
            if(this.getName().equals(p2.getName())){
                return this.getArguments().isEqualToOrderDoesntMatter((p2).getArguments());
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    

    

}
