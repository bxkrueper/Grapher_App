package com.example.differential_equation_grapher.function;

import java.util.List;

public class Divide extends BinaryOperation{

    public Divide(Arguments args){
        super(args);
    }
    public Divide(Expression ex1, Expression ex2) {
        super(new ArgumentsBinary(ex1,ex2));
    }

    @Override
    public Expression specialSimplification() {
        TermList termList = new TermList();
        
        termList.multiply(getFirstArg());
        termList.multiply(getSecondArg(),true);
        
        return termList.getSimplifiedExpression();
    }
    
    @Override
    public String getSymbol() {
        return "/";
    }
    
    @Override
    public String getName() {
        return "Divide";
    }

    @Override
    public int getPriority() {
        return TokenizerFactory.DIVIDE_PRIORITY;
    }
    
    @Override
    public Expression getDerivative(Variable v) {
        Expression high = getFirstArg().copy();
        Expression low = getSecondArg().copy();
        Expression dhigh = high.getDerivative(v);
        Expression dlow = low.getDerivative(v);
        
        Expression left = new Product(low,dhigh);
        Expression right = new Product(high,dlow);
        
        Expression top = new Sum(left,new Negate(right));
        Expression bottom = new Power(low,new RealNumber(2));
        
        return new Divide(top,bottom);
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Divide(newArguments);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.divide(getFirstArg().evaluate(substitution),getSecondArg().evaluate(substitution));
    }
    

}


//public class Divide  extends BinaryExpression{
//
//    public Divide(Expression ex1, Expression ex2) {
//        super(ex1,ex2);
//    }
//    
//    @Override
//    public Expression copy() {
//        return new Divide(this.getEx1().copy(),this.getEx2().copy());
//    }
//
//    @Override
//    public Expression specialSimplify() {
//        //make sure it is just one big division
//        //(a/b)/(c/d)
//        if((getEx1() instanceof Divide) && (getEx2() instanceof Divide)){
//            Divide div1 = (Divide) getEx1();
//            Divide div2 = (Divide) getEx2();
//            Expression a = div1.getEx1();
//            Expression b = div1.getEx2();
//            Expression c = div2.getEx1();
//            Expression d = div2.getEx2();
//            
//            setEx1(new Product(a,d).simplify());
//            setEx2(new Product(b,c).simplify());
//        }else if(getEx1() instanceof Divide){//(a/b)/c
//            Expression c = getEx2();
//            Divide div = (Divide) getEx1();
//            Expression a = div.getEx1();
//            Expression b = div.getEx2();
//            
//            setEx1(a);
//            setEx2(new Product(b,c).simplify());
//        } else if(getEx2() instanceof Divide){//a/(b/c)
//            Expression a = getEx1();
//            Divide div = (Divide) getEx2();
//            Expression b = div.getEx1();
//            Expression c = div.getEx2();
//            
//            setEx1(new Product(a,c).simplify());
//            setEx2(b);
//        }
//        
//        
//        //Cancel. Products are simplified at this point
//        if((getEx1() instanceof Product) && (getEx2() instanceof Product)){
//            Product product1 = (Product) getEx1();
//            Product product2 = (Product) getEx2();
//            cancelLikeTerms(product1, product2);
//            setEx1(product1.simplify());////////these simplifies are only for returning an only child. also returns 1 if empty
//            setEx2(product2.simplify());
//            //don't return yet
//        }else if(getEx1() instanceof Product){
//            Product numerator = (Product) getEx1();
//            if(numerator.contains(getEx2())){
//                numerator.remove(getEx2());
//                return getEx1();
//            }
//        }else if(getEx2() instanceof Product){
//            Product denominator = (Product) getEx2();
//            if(denominator.contains(getEx1())){
//                denominator.remove(getEx1());
//                setEx1(new RealNumber(1));
//            }
//        }
//        
//        if((getEx1() instanceof Constant) && (getEx2() instanceof Constant)){
//            Constant c1 = (Constant) getEx1();
//            Constant c2 = (Constant) getEx2();
//            return c1.divide(c2);
//        }else if(getEx1() instanceof Constant){
//            Constant c1 = (Constant) getEx1();
//            if(c1.equals(c1.getAdditiveIdentity())){
//                return c1;
//            }
//        }else if(getEx2() instanceof Constant){
//            Constant c2 = (Constant) getEx2();
//            if(c2.equals(c2.getAdditiveIdentity())){
//                return Undefined.getInstance();
//            }
//            if(c2.equals(c2.getMultiplictiveIdentity())){
//                return getEx1();
//            }
//            if(getEx1() instanceof Product){
//                Product numerator = (Product) getEx1();
//                int constantPosition = numerator.getIndexOfFirstConstant();
//                if(constantPosition==-1){
//                    //do nothing
//                }else{
//                    Constant constantInNum = (Constant) numerator.getChild(constantPosition);
//                    numerator.setChild(constantPosition,constantInNum.divide(c2));
//                }
//            }
//        }
//        
//        if(getEx1().equals(getEx2())){
//            return new RealNumber(1.0);
//        }
//        
//        
//        
//        
//        
//        return this;
//    }
//    
//    @Override
//    public String getSymbol() {
//        return "/";
//    }
//    
//    public int getProirity(){
//        return 2;
//    }
//    
//    @Override
//    public Constant operate(Constant c1, Constant c2) {
//        return ConstantMediator.divide(c1, c2);
//    }
//    
//    public static Tokenizer getTokenizer(){
//        return new Tokenizer(){
//
//            @Override
//            public Token tryTokenize(StringParser sp) {
//                if(sp.parseString("/")){
//                    return new BinaryToken(){
//
//                        @Override
//                        public int getPriority() {
//                            return 2;
//                        }
//
//                        @Override
//                        public BinaryExpression makeExpression(Expression ex1, Expression ex2) {
//                            return new Divide(ex1,ex2);
//                        }
//                        
//                    };
//                }else{
//                    return null;
//                }
//            }
//            
//        };
//    }
//
//    @Override
//    public int getPriority() {
//        return 2;
//    }
//    
//    
//    public static void cancelLikeTerms(Product numerator, Product denominator) {
//        for(int i=0;i<numerator.getNumberOfChildren();i++){
//            Expression ex = numerator.getChild(i);
//            if(denominator.contains(ex)){
//                numerator.remove(i);
//                i--;
//                denominator.remove(ex);
//            }
//        }
//        
//        int numConstantPosition = numerator.getIndexOfFirstConstant();
//        int denConstantPosition = denominator.getIndexOfFirstConstant();
//        if(numConstantPosition==-1 || denConstantPosition==-1){
//            //do nothing
//        }else{//merge denominator's constant into numerator's constant
//            Constant constantInNum = (Constant) numerator.getChild(numConstantPosition);
//            Constant constantInDen = (Constant) denominator.getChild(denConstantPosition);
//            denominator.remove(constantInDen);
//            numerator.setChild(numConstantPosition,constantInNum.divide(constantInDen));
//        }
//    }
//
//    @Override
//    public Expression getDerivative(Variable v) {
//        Expression high = getEx1().copy();
//        Expression low = getEx2().copy();
//        Expression dhigh = high.getDerivative(v);
//        Expression dlow = low.getDerivative(v);
//        
//        Expression left = new Product(low,dhigh);
//        Expression right = new Product(high,dlow);
//        
//        Expression top = new Sum(left,new Negate(right));
//        Expression bottom = new Power(low,new RealNumber(2));
//        
//        return new Divide(top,bottom);
//    }
//
//}
