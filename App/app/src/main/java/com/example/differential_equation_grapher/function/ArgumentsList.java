package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyAlgs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArgumentsList implements Arguments{
    
    private List<Expression> exList;
    private boolean computable;//maintained
    
    public ArgumentsList(List<Expression> exList){
        this.exList = exList;
        this.computable = listIsComputable(this.exList);
    }

    public ArgumentsList(Expression[] expressions) {
        this.exList = new ArrayList<>(expressions.length);
        for(Expression ex:expressions){
            exList.add(ex);
        }
        this.computable = listIsComputable(this.exList);
    }

    private boolean listIsComputable(List<Expression> listToCheck) {
        for(int i=0;i<listToCheck.size();i++){
            Expression argument = listToCheck.get(i);
            if(!argument.computable()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void simplify() {
        for(int i=0;i<exList.size();i++){
            exList.set(i, exList.get(i).simplify());
        }
        this.computable = listIsComputable(this.exList);//maintain field
    }

    @Override
    public boolean isComputable() {
        return this.computable;
    }

    @Override
    public void substitute(Substitution substitution) {
        if(computable){
            return;//no variables in here. don't bother
        }else{
            for(int i=0;i<exList.size();i++){
                exList.set(i, exList.get(i).substitute(substitution));
            }
            this.computable = listIsComputable(this.exList);//maintain field
        }
    }

    @Override
    public Arguments copy() {
        List<Expression> copyList = new ArrayList<>(exList.size());
        for(Expression ex:exList){
            copyList.add(ex.copy());
        }
        return new ArgumentsList(copyList);
    }

    @Override
    public Expression getArg(int index) {
        return exList.get(index);
    }

    @Override
    public Expression getFirstArg() {
        return exList.get(0);
    }

    @Override
    public Expression getSecondArg() {
        return exList.get(1);
    }
    
    @Override
    public void setArg(int index, Expression ex) {
        exList.set(index, ex);
    }

    @Override
    public void setFirstArg(Expression ex) {
        exList.set(0, ex);
    }

    @Override
    public void setSecondArg(Expression ex) {
        exList.set(1, ex);
    }
    
    @Override
    public void removeArg(int index) {
        exList.remove(index);
    }

    @Override
    public int getNumberOfArguments() {
        return exList.size();
    }

    @Override
    public boolean isEqualTo(Arguments arg2){
        if(getNumberOfArguments()!=arg2.getNumberOfArguments()){
            return false;
        }
        for(int i=0;i<getNumberOfArguments();i++){
            if(!getArg(i).equals(arg2.getArg(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(){
        return toDisplayString();
    }
    
    @Override
    public String toDisplayString() {
        return MyAlgs.listToString(exList, '(', ')');
    }

    //based on MyAlgs.listToString, but uses debug string
    @Override
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        boolean first = true;
        for(Expression ex:exList){
            if(first){
                first = false;
            }else{
                sb.append(',');
            }
            sb.append(ex.toDebugString());
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public boolean isEqualToOrderDoesntMatter(Arguments arg2) {
        return MyAlgs.listsHaveSameTerms(exList, arg2.getExList());
    }

    @Override
    public List<Expression> getExList() {
        return exList;
    }

    @Override
    public Iterator<Expression> iterator() {
        return exList.iterator();
    }

    

    

    

}
