package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyAlgs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class ConstantGroup extends Constant implements Iterable<Constant>{
    
    private Collection<Constant> collection;
    
    public ConstantGroup(Collection<Constant> baseCollection){
        this.collection = getEmptyCollection();
        addNonGroupsFromOtherCollection(baseCollection);
    }

    public ConstantGroup(Constant ... constants){
        this.collection = getEmptyCollection();
        addNonGroupsFromOtherArray(constants);
    }

    //adds undefined for non-constants that can't be evaluated, merges constant groups into main set
    public ConstantGroup(ExpressionGroup expressionGroup,Substitution substitution) {
        this.collection = getEmptyCollection();
        for(Expression ex: expressionGroup){
            Constant toAdd = ex.evaluate(substitution);
            if(toAdd instanceof ConstantGroup){
                addNonGroupsFromOtherCollection(((ConstantGroup) toAdd).getCollection());
            }else{
                collection.add(toAdd);
            }
        }
    }
    
    public ConstantGroup(ExpressionGroup expressionGroup) {
        this(expressionGroup,NullSubstitution.getInstance());
    }
    
    private void addNonGroupsFromOtherCollection(Collection<Constant> baseCollection) {
        for(Constant c: baseCollection){
            if(c instanceof ConstantGroup){
                addNonGroupsFromOtherCollection(((ConstantGroup) c).getCollection());
            }else{
                collection.add(c);
            }
        }
    }
    
    private void addNonGroupsFromOtherArray(Constant[] baseSet) {
        for(Constant c: baseSet){
            if(c instanceof ConstantGroup){
                addNonGroupsFromOtherCollection(((ConstantGroup) c).getCollection());
            }else{
                collection.add(c);
            }
        }
    }
    

    public static void addConstantToCollection(Constant c, Collection<Constant> outsideCollection){
        if(c instanceof ConstantGroup){
            for(Constant con:(ConstantGroup) c){
                addConstantToCollection(con,outsideCollection);
            }
        }else{
            outsideCollection.add(c);
        }
    }
    

    @Override
    public String toDisplayString() {
        List<Constant> list = new LinkedList<>();
        for(Constant c:collection){
            list.add(c);
        }
        return MyAlgs.listToString(list, '{', '}');
    }

    //if set has on item, return that item
    @Override
    public Expression simplify() {
        Iterator<Constant> iterator = collection.iterator();

        if (!iterator.hasNext()) {
            System.out.println("Constant set is empty");
            return Undefined.getInstance();
        }

        Constant element = iterator.next();

        if (iterator.hasNext()) {
            //Collection contains more than one item.
            return this;
        }else{
            return element;
        }
    }

    

    @Override
    public Iterator<Constant> iterator() {
        return collection.iterator();
    }

    public boolean contains(Constant c2) {
        return collection.contains(c2);
    }

    public int size() {
        return collection.size();
    }
    
    public Collection<Constant> getCollection() {
        return collection;
    }

    @Override
    public Constant getAdditiveIdentity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constant getMultiplictiveIdentity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constant getNaN() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Expression getDerivative(Variable v) {
        return new RealNumber(0);
    }
    
    
    public abstract Collection<Constant> getEmptyCollection();
}
