package com.example.differential_equation_grapher.function;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

//not used for evaluate(). Alternative group that uses list instead of set and has a set order
public class ConstantList extends ConstantGroup{
    
    public ConstantList(Collection<Constant> baseCollection){
        super(baseCollection);
    }

    public ConstantList(Constant ... constants){
        super(constants);
    }

    public ConstantList(ExpressionGroup expressionGroup,Substitution substitution) {
        super(expressionGroup,substitution);
    }
    
    public ConstantList(ExpressionGroup expressionGroup) {
        super(expressionGroup);
    }
    
    
    
    @Override
    public Collection<Constant> getEmptyCollection() {
        return new LinkedList<Constant>();
    }
    
    public Constant getAtIndex(int i){
        List<Constant> list = (List<Constant>) getCollection();
        return list.get(i);
    }
    
}
