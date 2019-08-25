package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.List;

public class Vector extends Constant{
    
    private List<Constant> list;
    
    public Vector(List<Constant> list){
        this.list = list;
    }
    public Vector(Constant ... array){
        this.list = new ArrayList<>(array.length);
        for(Constant item: array){
            list.add(item);
        }
    }
    
    public int length(){
        return list.size();
    }
    
    public Constant get(int i){
        return list.get(i);
    }

    @Override
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder("<");
        boolean first = true;
        for(Expression ex:list){
            if(first){
                first = false;
            }else{
                sb.append(',');
            }
            sb.append(ex.toDisplayString());
        }
        sb.append('>');
        return sb.toString();
    }
    
    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public Expression copy() {
        List<Constant> newList = new ArrayList<>(list.size());
        for(Constant item: list){
            newList.add((Constant) item.copy());
        }
        return new Vector(newList);
    }

    @Override
    public Constant getAdditiveIdentity() {
        List<Constant> newList = new ArrayList<>(list.size());
        for(int i=0;i<newList.size();i++){
            list.add(RealNumber.additiveIdentity);
        }
        return new Vector(newList);
    }

    @Override
    public Constant getMultiplictiveIdentity() {
        List<Constant> newList = new ArrayList<>(list.size());
        for(int i=0;i<newList.size();i++){
            list.add(RealNumber.multiplictiveIdentity);
        }
        return new Vector(newList);
    }

    @Override
    public Constant getNaN() {
        List<Constant> newList = new ArrayList<>(list.size());
        for(int i=0;i<newList.size();i++){
            list.add(RealNumber.nan);
        }
        return new Vector(newList);
    }

    @Override
    public Expression getDerivative(Variable v) {
        return new RealNumber(0.0);
    }
    
    
    
    
}
