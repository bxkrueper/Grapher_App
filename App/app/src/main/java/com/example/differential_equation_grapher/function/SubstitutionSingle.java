package com.example.differential_equation_grapher.function;

import java.util.LinkedList;
import java.util.List;

public class SubstitutionSingle implements Substitution{
    
    private Variable var;
    private Expression exp;
    
    public SubstitutionSingle(Variable var,Expression exp){
        this.var = var;
        this.exp = exp;
    }

    @Override
    public void put(Variable variable, Expression expression) {
        this.var = variable;
        this.exp = expression;
    }

    @Override
    public Expression get(Variable variable) {
        if(var.equals(variable)){
            return exp;
        }else{
            return null;
        }
    }
    
    @Override
    public List<Variable> getVariables() {
        List<Variable> list = new LinkedList<>();
        list.add(var);
        return list;
    }
    
    @Override
    public Substitution copy() {
        return new SubstitutionSingle(var,exp);////deep copy?
    }
}
