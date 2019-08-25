package com.example.differential_equation_grapher.function;

//used by Product and Divide for simplification
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TermList{
        
    private List<Term> termList;
    private Constant constant;
    
    
    public TermList(){
        this.termList = new LinkedList<>();
        this.constant = new RealNumber(1);
    }
    
    public void multiply(Expression ex){
        multiply(ex,false);
    }
    public void multiply(Expression ex,boolean inverse){//inverse: if true, divide
        Term term;
        if(ex instanceof Constant){//just merge into constant
            Constant exConstant = (Constant) ex;
            constant = inverse?constant.divide(exConstant):constant.multiply(exConstant);
            return;
        }else if (ex instanceof Negate){//separate the negate
            constant = constant.negate();
            multiply(((Negate) ex).getFirstArg(),inverse);
            return;
        }else if (ex instanceof Power){
            Power pow = (Power) ex;
            if(inverse){
                term = new Term(pow.getFirstArg(),new Negate(pow.getSecondArg()));
            }else{
                term = new Term(pow.getFirstArg(),pow.getSecondArg());
            }
        }else if (ex instanceof Product){
            Product product = (Product) ex;
            for(Expression exInProduct:product.getArguments()){
                multiply(exInProduct,inverse);
            }
            return;
        }else if (ex instanceof Divide){
            Divide div = (Divide) ex;
            multiply(div.getFirstArg(),inverse);
            multiply(div.getSecondArg(),!inverse);
            return;
        }else{//other like function or sum
            if(inverse){
                term = new Term(ex,new RealNumber(-1));
            }else{
                term = new Term(ex);
            }
        }
        
        mergeTerm(term);
    }

    private void mergeTerm(Term term) {
        for(int i=0;i<termList.size();i++){
            Term listTerm = termList.get(i);
            if(term.compareBase(listTerm)){
                listTerm.addToPower(term.getPower());
                return;
            }
        }
        termList.add(term);
    }
    
    //bases have been merged as they have been added
    public Expression getSimplifiedExpression(){
        if(termList.size()==0){
            return constant;
        }
        
        List<Expression> numList = new ArrayList<>();
        List<Expression> denList = new ArrayList<>();
        
        //if constant is 0, just return 0
        if(constant.equals(constant.getAdditiveIdentity())){//if constant==0
            return constant;
        }
        
        //add constant to list
        numList.add(0,constant);
        
        //add terms in termList
        for(Term term:termList){
            Expression base = term.getBase();
            Expression simplifiedPower = term.getPower().simplify();
            
            if(simplifiedPower instanceof RealNumber){
                RealNumber rnPower = (RealNumber) simplifiedPower;
                double rnPowerValue = rnPower.getValue();
                if(rnPowerValue==0){
                    //don't add to list
                }else if(rnPowerValue==1){
                    numList.add(base);
                }else if(rnPowerValue==-1){
                    denList.add(base);
                }else if(rnPowerValue<0){
                    RealNumber negSimplifiedPower = new RealNumber(-rnPowerValue);
                    denList.add(new Power(base,negSimplifiedPower));
                }else{//rnPowerValue>0
                    numList.add(new Power(base,simplifiedPower));
                }
            }else{
                if(simplifiedPower instanceof Negate){
                    denList.add(new Power(base,((Negate) simplifiedPower).getFirstArg()));
                }else{
                    numList.add(new Power(base, simplifiedPower));
                }
            }
            
        }
        //by now numList.size()>=1 and denList.size>=0
        
        //make numerator and denomenator
        Expression numerator;
        Expression denomenator;
        if(numList.size()==1){
            numerator = numList.get(0);
        }else{//numList.size()>1
            //remove constant if it is 1 or -1
            if(constant.equals(constant.getMultiplictiveIdentity())){//if constant == 1
                numList.remove(0);
            }else if(constant.equals(constant.getMultiplictiveIdentity().negate())){//if constant == -1, remove and negate the new first expression
                numList.remove(0);
                numList.set(0, new Negate(numList.get(0)));
            }
            //check if the size is 1 again
            if(numList.size()==1){
                numerator = numList.get(0);
            }else{
                numerator = new Product(numList);
            }
        }
        if(denList.size()==0){
            return numerator;
        }else if(denList.size()==1){
            denomenator = denList.get(0);
        }else{
            denomenator = new Product(denList);
        }
        return new Divide(numerator,denomenator);
        
    }
    
    
    private static class Term{
        
        private Expression base;
        private Expression power;
        
        public Term(Expression base,Expression power){
            this.base = base;
            this.power = power;
        }
        public Term(Expression base){
            this(base,new RealNumber(1));
        }
        
        public Expression getBase(){
            return base;
        }
        public Expression getPower(){
            return power;
        }
        
        public boolean compareBase(Term otherTerm) {
            return this.base.equals(otherTerm.base);
        }
        
        public void addToPower(Expression power2) {
            power = new Sum(power,power2);
        }
        
        @Override
        public String toString(){
            return base.toString() + "^" + power.toString();
        }
    }
}
