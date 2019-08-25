package com.example.differential_equation_grapher.function;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SubstitutionMap implements Substitution{
    private Map<Variable,Expression> subMap;
    
    public SubstitutionMap(){
        this.subMap = new HashMap<>();
    }

    @Override
    public void put(Variable variable, Expression expression) {
        subMap.put(variable, expression);
    }

    @Override
    public Expression get(Variable variable) {
        return subMap.get(variable); 
    }
    
    @Override
    public List<Variable> getVariables() {
        List<Variable> list = new LinkedList<>();
        for(Variable v:subMap.keySet()){
            list.add(v);
        }
//        list.sort();
        return list;
    }
    
    
    
    @Override
    public Substitution copy() {
        SubstitutionMap copyMap = new SubstitutionMap();
        copyMap.subMap.putAll(subMap);
        return copyMap;
    }
}
