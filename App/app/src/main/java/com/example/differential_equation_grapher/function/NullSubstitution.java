package com.example.differential_equation_grapher.function;

import java.util.LinkedList;
import java.util.List;

//Substitution with nothing in it
public class NullSubstitution implements Substitution{
    
    private static NullSubstitution instance;
    
    private NullSubstitution(){
        
    }
    
    public static NullSubstitution getInstance(){
        if(instance==null){
            instance=new NullSubstitution();
        }
        return instance;
    }

    @Override
    public void put(Variable variable, Expression expression) {
    }

    @Override
    public Expression get(Variable variable) {
        return null;
    }

    @Override
    public List<Variable> getVariables() {
        return new LinkedList<Variable>();
    }

    @Override
    public Substitution copy() {
        return this;//immutable
    }

}
