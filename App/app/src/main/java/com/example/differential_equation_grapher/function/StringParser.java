package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StringParser{
    private final String str;
    private int index;
    
    public StringParser(String str){
        this.str = str.trim();
        index=0;
    }
    
    public boolean hasNext(){
        return index<str.length();
    }
    
    public Double parseDouble(){
        char currentChar = str.charAt(index);
        if(!isNumberPiece(currentChar)){
            return null;
        }
        
        int indexEnd = index;
        do{
            indexEnd++;
        }while(indexEnd<str.length() && isNumberPiece(str.charAt(indexEnd)));//////short circuit?
        
        double number;
        try{
            number=Double.parseDouble(str.substring(index, indexEnd));
        }catch(Exception e){
            throw new RuntimeException("trouble parsing double! " + str.substring(index, indexEnd));
        }
        
        index = indexEnd;
        return number;
    }
    
    public Integer parseInteger(){
        char currentChar = str.charAt(index);
        if(!isDigit(currentChar)){
            return null;
        }
        
        int indexEnd = index;
        do{
            indexEnd++;
        }while(indexEnd<str.length() && isDigit(str.charAt(indexEnd)));//////short circuit?
        
        int number;
        try{
            number=Integer.parseInt(str.substring(index, indexEnd));
        }catch(Exception e){
            throw new RuntimeException("trouble parsing integer! " + str.substring(index, indexEnd));
        }
        
        index = indexEnd;
        return number;
    }
    
    public char peek(){
        return str.charAt(index);
    }
    
    public char pop(){
        return str.charAt(index++);
    }
    
//    public boolean parseSymbol(char symbol){
//        char currentChar = str.charAt(index);
//        if(symbol!=currentChar){
//            return false;
//        }else{
//            index++;
//            return true;
//        }
//    }
    
    //if true, advances
    public boolean parseString(String s){
        int endIndex = index+s.length();
        if(endIndex>str.length()){
            return false;
        }
        if(str.substring(index, endIndex).equals(s)){
            index=endIndex;
            return true;
        }else{
            return false;
        }
    }



    public Expression parseParenthesesExpression(char open,char close){
        String subExpression = parseParentheses(open,close);
        if(subExpression==null){
            return null;//no ( at start
        }
        if(subExpression.equals("")){
            throw new RuntimeException("empty Parentheses");
        }
        Expression ex=ExpressionFactory.getExpression(subExpression);
        if(ex==Undefined.getInstance()){
            throw new RuntimeException("trouble parsing expression!" + "_" + subExpression + "_");
        }
        return ex;
        
    }
    
    public List<Expression> parseParameterList(char open,char close){
        String subExpression = parseParentheses(open,close);
        if(subExpression==null){
            return null;//no ( at start
        }
        if(subExpression.equals("")){
            throw new RuntimeException("empty Parentheses");
        }
        return parseParameterList(subExpression,open,close);
    }
    
    //returns what is in the parentheses. expected to use () <> [] or {} for parameters
    //stops at the right closeing parentheses
    private String parseParentheses(char open,char close){
        if(!hasNext()){
            return null;
        }
        char currentChar = str.charAt(index);
        if(currentChar!=open){
            return null;
        }
        
        int indexEnd = index;
        int depth = 1;
        do{
            indexEnd++;
            currentChar = str.charAt(indexEnd);
            if(currentChar==close){
                depth--;
            }else if(currentChar==open){
                depth++;
            }
        }while(indexEnd<str.length()-1 && depth>0);//short circuit is used

        String subString;
        if(depth>0){
            //reached the end of the string without finding the match for the first open parenthesis. (ex: "(1+2"     this is now allowed
            subString = str.substring(index+1,indexEnd+1);
        }else{
            //depth==0 as expected
            subString = str.substring(index+1,indexEnd);//don't have indexEnd plus one here to omit the close parenthesis
        }

        index = indexEnd+1;
        return subString;
    }
    
    //sub expression is without open and close. open and close are for telling what characters affect the depth
    private List<Expression> parseParameterList(String subExpression,char open,char close){
        if(subExpression==null){
            return null;//no open at start
        }
        
        List<Expression> expressionList = new LinkedList<>();
        int startIndex = 0;
        int depth = 0;
        for(int endIndex=0;endIndex<=subExpression.length();endIndex++){
            if(endIndex==subExpression.length() || (subExpression.charAt(endIndex)==','&& depth==0)){
                Expression expressionToAdd = ExpressionFactory.getExpression(subExpression.substring(startIndex, endIndex));
                if(expressionToAdd==Undefined.getInstance()){
                    throw new RuntimeException("trouble parsing expression!" + "_" + subExpression + "_");
                }
                expressionList.add(expressionToAdd);
                startIndex = endIndex+1;
            }else if(subExpression.charAt(endIndex)==open){
                depth++;
            }else if(subExpression.charAt(endIndex)==close){
                depth--;
            }
        }
        
        return expressionList;
    }
    
    
    private boolean isNumberPiece(char ch){
        return (ch>=48 && ch<=57) || ch==46;
    }
    
    private boolean isDigit(char ch){
        return ch>=48 && ch<=57;
    }
    

    @Override
    public String toString() {
        return "StringParser [str=" + str + ", index=" + index + "]";
    }
    
}
