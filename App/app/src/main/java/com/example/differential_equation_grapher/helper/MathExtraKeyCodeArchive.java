package com.example.differential_equation_grapher.helper;

public class MathExtraKeyCodeArchive implements ExtraKeyCodeArchive {
    public final static int START_CODE = 1000;//this value is far away from any codes i would want to use normally;

    private final String[] codeStrings;

    private static MathExtraKeyCodeArchive instance;
    private MathExtraKeyCodeArchive(){
        codeStrings = new String[]{
                "ln("//1000
                ,"log[10]("//1001
                ,"arcsin("//1002   put arc stuff before normal functions to avoid backspace taking the normal functions first
                ,"arccos("//1003
                ,"arctan("//1004
                ,"max("//1005
                ,"min("//1006
                ,"floor("//1007
                ,"ceil("//1008
                ,"csc("//1009
                ,"sec("//1010
                ,"cot("//1011
                ,"sin("//1012
                ,"cos("//1013
                ,"tan("//1014
                ,"der("//1015
                ,"sinh("//1016
                ,"cosh("//1017
                ,"tanh("//1018
                ,"\u221A("//1019   (square root)
        };
    }

    public static MathExtraKeyCodeArchive getInstance(){
        if(instance==null){
            instance = new MathExtraKeyCodeArchive();
        }
        return instance;
    }

    @Override
    public String[] getCodeStrings(){
        return codeStrings;
    }
    @Override
    public int getStartCode(){
        return START_CODE;
    }


    //returns a direct character translation if no codes match
    @Override
    public String codeToString(int keyCode){
        int index = keyCode-START_CODE;
        if(index<0||index>=codeStrings.length){
            //no replacements found for this keyCode
            return Character.toString((char) keyCode);
        }else{
            return codeStrings[index];
        }
    }

}
