package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyAlgs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionGroup extends Expression implements Iterable<Expression>{
    
    private List<Expression> list;
    
    public ExpressionGroup(List<Expression> list){
        this.list = list;
    }
    public ExpressionGroup(Expression ...expressions){
        this.list = new ArrayList<>(expressions.length);
        for(Expression e:expressions){
            list.add(e);
        }
    }
    
    public List<Expression> getExpressionList() {
        return list;
    }
    
    @Override
    public boolean isEqualTo(Object o) {
        if(o instanceof ExpressionGroup){
            ExpressionGroup otherGroup = (ExpressionGroup) o;
            return MyAlgs.listsHaveSameTerms(list, otherGroup.list);
        }else{//o is not expression group
            if(list.size()==1){
                return list.get(0).equals(o);
            }else{
                return false;
            }
        }
    }
    
    @Override
    public String toDisplayString() {
        return MyAlgs.listToString(list, '{', '}');
    }
    //based on MyAlgs.listToString, but uses debug string
    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for(Expression ex:list){
            if(first){
                first = false;
            }else{
                sb.append(',');
            }
            sb.append(ex.toDebugString());
        }
        sb.append('}');
        return sb.toString();
    }
    
    public final boolean computable(){
        for(int i=0;i<list.size();i++){
            Expression child = list.get(i);
            if(!child.computable()){
                return false;
            }
        }
        return true;
    }
    
    public final Expression substitute(Substitution substitution){
        //substitute children
        for(int i=0;i<list.size();i++){
            Expression e = list.get(i);
            list.set(i, e.substitute(substitution));
        }
        return this;
    }
    
    @Override
    public Expression simplify() {
        //simplify children
        for(int i=0;i<list.size();i++){
            Expression e = list.get(i);
            list.set(i, e.simplify());
        }
        
      //merge direct children that are expression groups into this one
        for(int i=0;i<list.size();i++){
            Expression e = list.get(i);
            if(e instanceof ExpressionGroup){
                ExpressionGroup eg = (ExpressionGroup) e;
                list.remove(i);
                list.addAll(i, eg.getExpressionList());
                i+=eg.getNumberInGroup();
                i--;//counter next i increment in for loop
            }
        }
        
        //if all constants, return constant group
        if(computable()){
            return new ConstantSet(this);
        }
        
        
        //delete expressions that are equal to each other
        for(int i=0;i<list.size();i++){
            Expression exi = list.get(i);
            for(int j=i+1;j<list.size();j++){
                Expression exj = list.get(j);
                if(exi.equals(exj)){//delete ex at j
                    list.remove(j);
                    j--;//to counter next j++
                }
            }
        }
        
        if(list.size()==1){
            return list.get(0);
        }
        
        return this;
        
    }
    
    
    public int getNumberInGroup() {
        return list.size();
    }
    
    @Override
    public Expression copy() {
        List<Expression> listCopy = new ArrayList<>(list.size());
        for(int i=0;i<list.size();i++){
            Expression e = list.get(i);
            listCopy.add(e);
        }
        
        return new ExpressionGroup(listCopy);
    }
    
    //makes a copy with derivatives
    @Override
    public Expression getDerivative(Variable v) {
        List<Expression> listCopy = new ArrayList<>(list.size());
        for(int i=0;i<list.size();i++){
            Expression e = list.get(i);
            listCopy.add(e.getDerivative(v));
        }
        
        return new ExpressionGroup(listCopy);
    }
    
    
    
    /////keep constant set as field?
    @Override
    public final Constant evaluate(Substitution substitution) {
        return (Constant) new ConstantSet(this,substitution).simplify();
        
    }
    
    
    @Override
    public Iterator<Expression> iterator() {
        return list.iterator();
    }
    
    
}
