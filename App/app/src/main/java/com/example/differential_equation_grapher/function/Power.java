package com.example.differential_equation_grapher.function;

public class Power extends BinaryOperation{

    public Power(Arguments args){
        super(args);
    }
    public Power(Expression ex1, Expression ex2) {
        super(new ArgumentsBinary(ex1,ex2));
    }

    @Override
    public Expression specialSimplification() {
        if(getFirstArg() instanceof Constant){
            Constant c1 = (Constant) getFirstArg();
            if(c1.equals(c1.getAdditiveIdentity())){
                return c1;
            }
            if(c1.equals(c1.getMultiplictiveIdentity())){
                return c1;
            }
        }
        
        if(getSecondArg() instanceof Constant){
            Constant c2 = (Constant) getSecondArg();
            if(c2.equals(c2.getAdditiveIdentity())){
                return c2.getMultiplictiveIdentity();
            }
            if(c2.equals(c2.getMultiplictiveIdentity())){
                return getFirstArg();
            }
        }

        if(getFirstArg() instanceof Power){
            Power mainBase = (Power) getFirstArg();
            Expression firstArgBase = mainBase.getFirstArg();
            Expression firstArgExponent = mainBase.getSecondArg();
            Expression exponent = new Product(firstArgExponent,getSecondArg());
            exponent.simplify();
            return new Power(firstArgBase,exponent);
        }
        
        return this;
    }
    
    @Override
    public String getSymbol() {
        return "^";
    }
    
    @Override
    public String getName() {
        return "Power";
    }

    @Override
    public int getPriority() {
        return TokenizerFactory.POWER_PRIORITY;
    }

    //d/dx of f(x)^g(x) = f(x)^g(x) * (g(x)f'(x)/f(x)+ln(f(x))g'(x))
    @Override
    public Expression getDerivative(Variable v) {
        Expression fx = getFirstArg().copy();
        Expression gx = getSecondArg().copy();
        Expression dfx = fx.getDerivative(v);
        Expression dgx = gx.getDerivative(v);
        
        Expression firstPart = this.copy();//f(x)^g(x)
        Expression secondPart = new Divide(new Product(gx,dfx),fx);//g(x)f'(x)/f(x)
        Expression thirdPart = new Product(new Ln(fx.copy()),dgx);//ln(f(x))g'(x)  don't use same fx as in second part
        
        Expression answer = new Product(firstPart,new Sum(secondPart,thirdPart));
        
        return answer;
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Power(newArguments);
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.power(getFirstArg().evaluate(substitution),getSecondArg().evaluate(substitution));
    }
    

}
