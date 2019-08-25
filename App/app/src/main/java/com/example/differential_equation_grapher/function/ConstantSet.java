package com.example.differential_equation_grapher.function;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//Separate from expression group for use in evaluate possess and constant mediator
//constructors enforce that ConstantSets can't contain other ConstantSets. Their elements are added individually
public class ConstantSet extends ConstantGroup{
    
    public ConstantSet(Collection<Constant> baseCollection){
        super(baseCollection);
    }

    public ConstantSet(Constant ... constants){
        super(constants);
    }

    public ConstantSet(ExpressionGroup expressionGroup,Substitution substitution) {
        super(expressionGroup,substitution);
    }
    
    public ConstantSet(ExpressionGroup expressionGroup) {
        super(expressionGroup);
    }
    @Override
    public Collection<Constant> getEmptyCollection() {
        return new TreeSet<Constant>();
    }
    
//    public Set<Constant> set;
//    public ConstantSet(Set<Constant> baseSet){
//        this.set = new TreeSet<>();
//        addNonSetsFromOtherSet(baseSet);
//    }
//    
//    
//
//    public ConstantSet(Constant ... constants){
//        this.set = new TreeSet<>();
//        addNonSetsFromOtherArray(constants);
//    }
//
//    //adds undefined for non-constants that can't be evaluated, merges constant groups into main set
//    public ConstantSet(ExpressionGroup expressionGroup,Substitution substitution) {
//        set = new TreeSet<>();
//        for(Expression ex: expressionGroup){
//            Constant toAdd = ex.evaluate(substitution);
//            if(toAdd instanceof ConstantSet){
//                addNonSetsFromOtherSet(((ConstantSet) toAdd).set);
//            }else{
//                set.add(toAdd);
//            }
//        }
//    }
//    
//    public ConstantSet(ExpressionGroup expressionGroup) {
//        this(expressionGroup,NullSubstitution.getInstance());
//    }
//    
//    private void addNonSetsFromOtherSet(Set<Constant> baseSet) {
//        for(Constant c: baseSet){
//            if(c instanceof ConstantSet){
//                addNonSetsFromOtherSet(((ConstantSet) c).set);
//            }else{
//                set.add(c);
//            }
//        }
//    }
//    
//    private void addNonSetsFromOtherArray(Constant[] baseSet) {
//        for(Constant c: baseSet){
//            if(c instanceof ConstantSet){
//                addNonSetsFromOtherSet(((ConstantSet) c).set);
//            }else{
//                set.add(c);
//            }
//        }
//    }
//    
//    public Set<Constant> getSet(){
//        return set;
//    }
//
//    
//
//    @Override
//    public String toDisplayString() {
//        List<Constant> list = new LinkedList<>();
//        for(Constant c:set){
//            list.add(c);
//        }
//        return MyAlgs.listToString(list, '{', '}');
//    }
//
//    //if set has on item, return that item
//    @Override
//    public Expression simplify() {
//        Iterator<Constant> iterator = set.iterator();
//
//        if (!iterator.hasNext()) {
//            System.out.println("Constant set is empty");
//            return Undefined.getInstance();
//        }
//
//        Constant element = iterator.next();
//
//        if (iterator.hasNext()) {
//            //Collection contains more than one item.
//            return this;
//        }else{
//            return element;
//        }
//    }
//
//    
//
//    @Override
//    public Iterator<Constant> iterator() {
//        return set.iterator();
//    }
//
//
//    @Override
//    public boolean contains(Constant c2) {
//        return set.contains(c2);
//    }
//
//
//    @Override
//    public int size() {
//        return set.size();
//    }

}
