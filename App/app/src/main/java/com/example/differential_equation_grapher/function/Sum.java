package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Sum extends MultiOperation{

    public Sum(Arguments args){
        super(args);
    }
    public Sum(List<Expression> numeratorList){
        super(new ArgumentsList(numeratorList));
    }
    public Sum(Expression ...expressions){
        super(new ArgumentsList(expressions));
    }
    
    @Override
    public String getName() {
        return "Sum";
    }
    
    @Override
    public String toDisplayString() {
        Arguments args = getArguments();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<args.getNumberOfArguments();i++){
            String nextExString = args.getArg(i).toDisplayString();
            if(i!=0 && nextExString.charAt(0)!='-'){
                sb.append("+");
            }
            sb.append(nextExString);
            
        }
        return sb.toString();
    }
    

    @Override
    public Expression specialSimplification() {
        SumTermList termList = new SumTermList();
        for(int i=0;i<getNumberOfArguments();i++){
            termList.add(getArg(i));
        }
        
        return termList.getSimplifiedExpression();
    }
    
    
    @Override
    public Expression getDerivative(Variable v) {
        List<Expression> exListCopy = new ArrayList<>();
        for(int i=0;i<getNumberOfArguments();i++){
            exListCopy.add(getArg(i).getDerivative(v));
        }
        return new Sum(exListCopy);
    }
    
    

    @Override
    public int getPriority() {
        return TokenizerFactory.SUM_PRIORITY;
    }
    
    
    @Override
    public Constant evaluate(Substitution substitution) {
        Constant sum = getFirstArg().evaluate(substitution);
        for(int i=1;i<getNumberOfArguments();i++){
            sum = ConstantMediator.add(sum, getArg(i).evaluate(substitution));
        }
        return sum;
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Sum(newArguments);
    }
    @Override
    public String getSymbol() {
        return "+";
    }
    
    
    
    
    
    //constants are separated from the expressions when made into terms so they can be added or multiplied to
    //ex: adding 2*x to 1*x will make 3*x
    private static class SumTermList{
        
        private List<Term> termList;
        private Constant constant;
        
        
        public SumTermList(){
            this.termList = new LinkedList<>();
            this.constant = new RealNumber(0);
        }
        
        public void add(Expression ex){
            Term term;
            if(ex instanceof Constant){//just merge into constant
                Constant exConstant = (Constant) ex;
                constant = constant.add(exConstant);
                return;
            }else if (ex instanceof Negate){//separate the negate
                term = new ConstantExpressionTerm(new RealNumber(-1),((Negate) ex).getFirstArg());
            }else if (ex instanceof Sum){
                Sum sum = (Sum) ex;
                for(Expression exInSum:sum.getArguments()){
                    add(exInSum);
                }
                return;
            }else if (ex instanceof Product){
                Product product = (Product) ex;
                term = new ProductTerm(product);
                
            }else{//other like function
                term = new ConstantExpressionTerm(ex);
            }
            
            mergeTerm(term);
        }

        private void mergeTerm(Term term) {
            for(int i=0;i<termList.size();i++){
                Term listTerm = termList.get(i);
                if(term.getExpressionWithoutConstant().equals(listTerm.getExpressionWithoutConstant())){
                    listTerm.addToConstant(term.getConstant());
                    return;
                }
            }
            termList.add(term);
        }
        
        public Expression getSimplifiedExpression(){
            if(termList.size()==0){
                return constant;
            }
            
            List<Expression> newSumList = new ArrayList<>();
            
            //add terms in termList
            for(Term term:termList){
                Expression completeExpression = term.getCompleteExpression();
                if(!completeExpression.equals(term.getConstant().getAdditiveIdentity())){
                    newSumList.add(completeExpression);
                }
                
            }
            
            //add constant to list
            if(constant.equals(constant.getAdditiveIdentity())){
                //don't add to list
            }else{
                newSumList.add(constant);//add to end
            }
            
            
            if(newSumList.size()==0){
                return new RealNumber(0);
            }else if(newSumList.size()==1){
                return newSumList.get(0);
            }else{
                return new Sum(newSumList);
            }
        }
        
    }
    
    //a term is a unit to be added like 2 or 3*x    constants are kept seperate for merging and are put back
    //with getCompleteExpression
    private static abstract class Term{
        
        public final Expression getCompleteExpression(){
            if(getConstant().equals(getConstant().getAdditiveIdentity())){//constant is 0
                return getConstant();
            }else if(getConstant().equals(getConstant().getMultiplictiveIdentity())){//constant is 1
                return getExpressionWithoutConstant();
            }else if(getConstant().equals(getConstant().getMultiplictiveIdentity().negate())){//constant is -1
                return new Negate(getExpressionWithoutConstant());
            }else{
                return new Product(getConstant(),getExpressionWithoutConstant());
            }
        }
        public abstract Constant getConstant();
        public abstract Expression getExpressionWithoutConstant();
        
        public abstract void addToConstant(Constant constant2);
        
    }
    
    private static class ConstantExpressionTerm extends Term{
        
        private Expression expression;
        private Constant constant;
        
        public ConstantExpressionTerm(Constant constant,Expression expression){
            this.expression = expression;
            this.constant = constant;
        }
        public ConstantExpressionTerm(Expression expression){
            this(new RealNumber(1),expression);
        }
        
        
        
        public Constant getConstant(){
            return constant;
        }
        @Override
        public Expression getExpressionWithoutConstant() {
            return expression;
        }
        
        public void addToConstant(Constant constant2) {
            constant = constant.add(constant2);
        }
        
        @Override
        public String toString(){
            return constant.toString() + "*" + expression.toString();
        }
        
    }
    
    private static class ProductTerm extends Term{
        
        private Product product;
        private Constant constant;
        
        public ProductTerm(Product product){
            Arguments args = product.getArguments();
            this.constant = new RealNumber(1);
            List<Expression> nonConstantList = new ArrayList<>(args.getNumberOfArguments());
            for(Expression ex:args){
                if(ex instanceof Constant){
                    constant = constant.multiply((Constant) ex);
                }else{
                    nonConstantList.add(ex);
                }
            }
            this.product = new Product(nonConstantList);
        }
        
        public Constant getConstant(){
            return constant;
        }
        @Override
        public Expression getExpressionWithoutConstant() {
            return product;
        }
        
        public void addToConstant(Constant constant2) {
            constant = constant.add(constant2);
        }
        
        @Override
        public String toString(){
            return constant.toString() + "*" + product.toString();
        }

        
    }

    

    

    

    

    


}
