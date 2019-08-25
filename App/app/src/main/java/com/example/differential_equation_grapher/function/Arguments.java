package com.example.differential_equation_grapher.function;

import java.util.List;

public interface Arguments extends Iterable<Expression>{

    void simplify();

    boolean isComputable();

    void substitute(Substitution substitution);

    Arguments copy();
    
    List<Expression> getExList();
    
    Expression getArg(int index);
    Expression getFirstArg();
    Expression getSecondArg();
    
    void setArg(int index, Expression ex);
    void setFirstArg(Expression ex);
    void setSecondArg(Expression ex);
    
    void removeArg(int index);
    
    int getNumberOfArguments();
    
    boolean isEqualTo(Arguments arg2);
    boolean isEqualToOrderDoesntMatter(Arguments arg2);

    //subclasses should override toString
    public String toDisplayString();
    public String toDebugString();

    

    

    

}
