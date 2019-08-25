package com.example.differential_equation_grapher.function;

public abstract class PredefinedFunction extends Operation{
    
    public PredefinedFunction(Arguments arguments) {
        super(arguments);
    }
    
    @Override
    public final boolean isEqualTo(Object o) {
        if(o instanceof PredefinedFunction){
            PredefinedFunction p2 = (PredefinedFunction) o;
            if(this.getName().equals(p2.getName())){
                return this.getArguments().isEqualTo(((PredefinedFunction) o).getArguments());
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    //abs and square root override this (ex: |x| instead of abs(x))
    @Override
    public String toDisplayString() {
        return getName() + getArguments().toDisplayString();
    }
    
    
    
    
}
