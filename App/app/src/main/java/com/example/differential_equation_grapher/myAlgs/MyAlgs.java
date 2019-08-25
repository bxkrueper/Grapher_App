package com.example.differential_equation_grapher.myAlgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyAlgs {

    /////////java has a shuffle method
    public static <T> void shuffleArray(T[] array) {
        Random rand = new Random();
        for(int i=0;i<array.length;i++){
            swap(array,i,rand.nextInt(array.length));
        }
    }
    
    public static <T> void swap(T[] array, int index1, int index2){
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
    
    public static <T> String listToString(List<T> list,char open,char close) {
        StringBuilder sb = new StringBuilder();
        sb.append(open);
        boolean first = true;
        for(T ex:list){
            if(first){
                first = false;
            }else{
                sb.append(',');
            }
            sb.append(ex.toString());
        }
        sb.append(close);
        return sb.toString();
    }
    
    public static <T,K> boolean listsHaveSameTerms(List<T> list1,List<K> list2){
        if(list1.size()!=list2.size()){
            return false;
        }
        
        //make copies of lists
        List<T> exListCopy = new ArrayList<>(list1.size());
        List<K> exList2Copy = new ArrayList<>(list2.size());
        for(T ex:list1){
            exListCopy.add(ex);
        }
        for(K ex:list2){
            exList2Copy.add(ex);
        }
        
        //go through list 1 copy and delete each element from both it and list 2
        //if list 2 doesn't have it, return false
        //if list 2 has elements left at the end, return false
        for(int i=0;i<exListCopy.size();i++){
            T ex = exListCopy.get(i);
            if(exList2Copy.contains(ex)){
                exListCopy.remove(i);
                i--;
                exList2Copy.remove(ex);
            }else{
                return false;
            }
        }
        
        return exList2Copy.isEmpty();
    }

}
