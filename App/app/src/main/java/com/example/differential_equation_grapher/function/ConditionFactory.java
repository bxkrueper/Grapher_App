package com.example.differential_equation_grapher.function;

import com.example.differential_equation_grapher.myAlgs.MyMath;

public class ConditionFactory {
    public static PositiveCondition getPositiveCondition(Expression expression){
        return new PositiveCondition(expression);
    }

    public static EqualsCondition getEqualsCondition(Expression expression, Expression expression2){
        return new EqualsCondition(expression,expression2);
    }

    public static GreaterThanCondition getGreaterThanCondition(Expression expression, Expression expression2){
        return new GreaterThanCondition(expression,expression2);
    }

    public static Condition getLessThanCondition(Expression expression, Expression expression2) {
        return new LessThanCondition(expression,expression2);
    }

    public static Condition getMultipleOfCondition(Expression num, Expression roundTo) {
        return new MultipleOfCondition(num,roundTo);
    }

    public static Condition getBetweenInclusiveCondition(Expression expression, Expression lower, Expression higher) {
        return new BetweenInclusiveCondition(expression,lower,higher);
    }


    private static class PositiveCondition implements Condition{
        private Expression expression;

        public PositiveCondition(Expression expression) {
            this.expression = expression;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer = expression.evaluate(substitution);
            if(answer instanceof Number){
                return ((Number) answer).getReal()>0;
            }else{
                return false;
            }
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof PositiveCondition){
                PositiveCondition otherCondition = (PositiveCondition) condition;
                return expression.equals(otherCondition.expression);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return expression.toDisplayString() + ">0";
        }
    }

    private static class EqualsCondition implements Condition{
        private Expression expression;
        private Expression expression2;

        public EqualsCondition(Expression expression, Expression expression2) {
            this.expression = expression;
            this.expression2 = expression2;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer = expression.evaluate(substitution);
            return answer.equals(expression2.evaluate(substitution));
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof EqualsCondition){
                EqualsCondition otherCondition = (EqualsCondition) condition;
                return expression.equals(otherCondition.expression) && expression2.equals(otherCondition.expression2);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return expression.toDisplayString() + "==" + expression2.toDisplayString();
        }
    }

    private static class GreaterThanCondition implements Condition{
        private Expression expression;
        private Expression expression2;

        public GreaterThanCondition(Expression expression,Expression expression2) {
            this.expression = expression;
            this.expression2 = expression2;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer1 = expression.evaluate(substitution);
            Constant answer2 = expression2.evaluate(substitution);

            return answer1.compareTo(answer2)==1;
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof GreaterThanCondition){
                GreaterThanCondition otherCondition = (GreaterThanCondition) condition;
                return expression.equals(otherCondition.expression) && expression2.equals(otherCondition.expression2);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return expression.toDisplayString() + " > " + expression2.toDisplayString();
        }
    }

    private static class LessThanCondition implements Condition{
        private Expression expression;
        private Expression expression2;

        public LessThanCondition(Expression expression,Expression expression2) {
            this.expression = expression;
            this.expression2 = expression2;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer1 = expression.evaluate(substitution);
            Constant answer2 = expression2.evaluate(substitution);

            return answer1.compareTo(answer2)==-1;
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof LessThanCondition){
                LessThanCondition otherCondition = (LessThanCondition) condition;
                return expression.equals(otherCondition.expression) && expression2.equals(otherCondition.expression2);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return expression.toDisplayString() + " < " + expression2.toDisplayString();
        }
    }

    private static class MultipleOfCondition implements Condition{
        private Expression expression;
        private Expression roundTo;

        public MultipleOfCondition(Expression expression,Expression roundTo) {
            this.expression = expression;
            this.roundTo = roundTo;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer = expression.evaluate(substitution);
            Constant answerDivRoundTo;
            if(roundTo instanceof Constant){
                answerDivRoundTo = ConstantMediator.divide(answer,(Constant) roundTo);
            }else{
                return false;
            }
            //to pass, the real part of answerDivRoundTo should be an integer
            if(answerDivRoundTo instanceof Number){
                Number number = (Number) answerDivRoundTo;
                double real = number.getReal();
                return MyMath.isInteger(real);
            }else{
                return false;
            }
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof MultipleOfCondition){
                MultipleOfCondition otherCondition = (MultipleOfCondition) condition;
                return expression.equals(otherCondition.expression)&&roundTo.equals(otherCondition.roundTo);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return expression.toDisplayString() + "is multiple of " + roundTo.toDisplayString();
        }
    }


    private static class BetweenInclusiveCondition implements Condition{
        private Expression expression;
        private Expression lowerBound;
        private Expression upperBound;

        public BetweenInclusiveCondition(Expression expression,Expression lowerBound,Expression upperBound) {
            this.expression = expression;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public boolean passes(Substitution substitution) {
            Constant answer = expression.evaluate(substitution);
            Constant lowerConstant = lowerBound.evaluate(substitution);
            Constant upperConstant = upperBound.evaluate(substitution);
            if(lowerConstant.compareTo(answer)==1){//if lower constant is bigger
                return false;
            }
            if(upperConstant.compareTo(answer)==-1){//if upper constant is smaller
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqualTo(Condition condition) {
            if(condition instanceof BetweenInclusiveCondition){
                BetweenInclusiveCondition otherCondition = (BetweenInclusiveCondition) condition;
                return expression.equals(otherCondition.expression)&&lowerBound.equals(otherCondition.lowerBound)&&upperBound.equals(otherCondition.upperBound);
            }else{
                return false;
            }
        }

        @Override
        public String toString() {
            return lowerBound.toDisplayString() + "<=" + expression.toDisplayString() + "<=" + upperBound.toDisplayString();
        }
    }
}
