package com.example.differential_equation_grapher.myAlgs;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MyMath {
    
    private static Random rand;
    
    public static Random getRand(){
        if(rand==null){
            rand = new Random();
        }
        return rand;
    }
    
    public static int getRandomInt(int lowest, int highestPlus1) {
        return getRand().nextInt(highestPlus1)+lowest;
    }
    //lowest is 0
    public static int getRandomInt(int highestPlus1) {
        return getRand().nextInt(highestPlus1);
    }
    
    public static long roundToNearestMultipleOf(long num,long multiple){
        double div = (double)num/multiple;
        long rounded = Math.round(div);
        return rounded*multiple;
    }
    
    //if multiple is negative, it rounds up
    public static double roundDownToNearestMultipleOf(double number,double multiple){
        double ans = number/multiple;
        ans = Math.floor(ans);
        ans *= multiple;
        return ans;
    }
    //if multiple is negative, it rounds down
    //haven't fully tested
    public static double roundUpToNearestMultipleOf(double number,double multiple){
        double ans = number/multiple;
        ans = Math.ceil(ans);
        ans *= multiple;
        return ans;
    }

    //not tested
    public static double roundToNearestMultipleOf(double number,double multiple){
        return roundDownToNearestMultipleOf(number+multiple/2,multiple);
    }
    
    
    
    //if number is not an integer, round to specified sig figs but don't cut off digits left of decimal unless it needs scientific notation
    public static String toNiceString(double num,int sigFigs){
        if(num==Math.PI){
            return "\u03C0";
        }
        if(num==Math.E){
            return "e";
        }
        if(num==Double.POSITIVE_INFINITY){
            return "\u221E";
        }
        if(num==Double.NEGATIVE_INFINITY){
            return "-\u221E";
        }
        
        double absNum = Math.abs(num);
        if(absNum>=1E9 || (absNum<1E-4 && absNum>0)){
            return toScientificNotation(num,sigFigs);
        }
        
        int intVal = (int) num;
        if(intVal==num){
            return String.format("%,d",intVal);
        }
        
        String comma = toCommaStringDecSigFigs(num,sigFigs);
//        System.out.println("comma: " + comma);
        return chopOffDecZeros(comma);
        
    }
    
    public static String toScientificNotation(double num, int sigFigs) {
        sigFigs--;
        switch(sigFigs){
        case 0:
            return String.format("%.0e",num);
        case 1:
            return String.format("%.1e",num);
        case 2:
            return String.format("%.2e",num);
        case 3:
            return String.format("%.3e",num);
        case 4:
            return String.format("%.4e",num);
        case 5:
            return String.format("%.5e",num);
        case 6:
            return String.format("%.6e",num);
        case 7:
            return String.format("%.7e",num);
        case 8:
            return String.format("%.8e",num);
        case 9:
            return String.format("%.9e",num);
        case 10:
            return String.format("%.10e",num);
        case 11:
            return String.format("%.11e",num);
        case 12:
            return String.format("%.12e",num);
        case 13:
            return String.format("%.13e",num);
        case 14:
            return String.format("%.14e",num);
        case 15:
            return String.format("%.15e",num);
        default:
            System.out.println("MyMath: toScientificNotation: sig figs too precise! num: " + num + " sigFigs: " + sigFigs );
            return "###";
        }
    }
    
    //removes hanging zeros at the end of the string. keeps the decimal point
    public static String chopOffDecZeros(String numString){
        int indexOfDecimal = numString.lastIndexOf(".");
        if(indexOfDecimal==-1){
            return numString;
        }
        
        int index = numString.length()-1;
        while(numString.charAt(index)=='0'){
            index--;
        }
        //index is now pointing to the last non zero digit right of the decimal or the decimal
        return numString.substring(0, index+1);
    }
    
    //rounds to specified sig figs
    public static String toCommaStringSigFigs(double num, int sigFigs){
        int powerOfTenRange = powerOfTenRange(num);
//        System.out.println("Power of 10 range: " + powerOfTenRange);
        int roundRange = powerOfTenRange-(sigFigs-1);// 0 for this value is a perfect integer, 1 rounds to nearest 10, -1 rounds to nearest tenth...
//        System.out.println("round range: " + roundRange);
        if(roundRange<0){
            return toCommaStringDecSigFigs(num,sigFigs);
        }else if(roundRange==0){//round to nearest integer
            return String.format("%,d",Math.round(num));
        }else{//round to appropriate multiple of 10
            return String.format("%,d",roundToNearestMultipleOf((long) num,Math.round(Math.pow(10, roundRange))));
        }
    }

    //shows all nums left of decimal. only shows decimals up to given sig figs
    //ex: 12.3456 returns 12.3
    public static String toCommaStringDecSigFigs(double num, int sigFigs){
//        System.out.println("num in comma: " + num);
        int powerOfTenRange = powerOfTenRange(num);
//        System.out.println("powerOfTenRange: " + powerOfTenRange);
        int digitsToRight = Math.max(sigFigs-(powerOfTenRange+1),0);
//        System.out.println("digitsToRight: " + digitsToRight);
        
        switch(digitsToRight){
        case 0:
            if((int) num == num){
                return String.format("%,.0f",num);
            }else{
                return String.format("%,.0f",num)+".";
            }
        case 1:
            return String.format("%,.1f",num);
        case 2:
            return String.format("%,.2f",num);
        case 3:
            return String.format("%,.3f",num);
        case 4:
            return String.format("%,.4f",num);
        case 5:
            return String.format("%,.5f",num);
        case 6:
            return String.format("%,.6f",num);
        case 7:
            return String.format("%,.7f",num);
        case 8:
            return String.format("%,.8f",num);
        case 9:
            return String.format("%,.9f",num);
        case 10:
            return String.format("%,.10f",num);
        case 11:
            return String.format("%,.11f",num);
        case 12:
            return String.format("%,.12f",num);
        case 13:
            return String.format("%,.13f",num);
        case 14:
            return String.format("%,.14f",num);
        case 15:
            return String.format("%,.15f",num);
        default:
            System.out.println("MyMath: toCommaString: num too small");
            return "###";
        }
    }
    
    //returns the direction of (x2,y2) from (x,y)
//    public final static double getDirection(double x, double y, double x2, double y2){
//        if (y<y2)
//            return Math.acos((x2-x)/Math.hypot(x-x2, y-y2));
//        else if (y>y2)
//            return Math.acos((x-x2)/Math.hypot(x-x2, y-y2))+Math.PI;
//        else if(x>x2)
//            return Math.PI;
//        else
//            return 0;
//    }
    //more efficient. doesn't calculate hypot
    //returns the direction of (x2,y2) from (x,y) in radians from the +x axis   between 0 and 2PI
    public final static double getDirection(double x1, double y1, double x2, double y2){
        double xlength = Math.abs(x2-x1);
        double ylength = Math.abs(y2-y1);
        if(x2>x1){
            if(y2>y1){//first quadrant
                return Math.atan(ylength/xlength);
            }else if(y2==y1){//+x axis
                return 0.0;
            }else{//y2<y1   forth quadrant
                return Math.atan(xlength/ylength) + Math.PI*1.5;
            }
            
            
            
            
        }else if(x2==x1){
            if(y2>y1){//+y axis
                return Math.PI/2;
            }else if(y2==y1){//same point
                return 0.0;
            }else{//y2<y1   -y axis
                return Math.PI*1.5;
            }
            
            
            
            
        }else{//x2<x1
            if(y2>y1){//second quadrant
                return Math.atan(xlength/ylength) + Math.PI/2;
            }else if(y2==y1){//-x axis
                return Math.PI;
            }else{//y2<y1    third quadrant
                return Math.atan(ylength/xlength) + Math.PI;
            }
        }
    }
    
    //returns the direction of (x,y) from (0,0) in radians from the +x axis   between 0 and 2PI
    //java's Math.tan2(y,x) does the same thing, but -PI<returns<=PI   remember y is first there
    public final static double getDirectionFromOrigin(double x, double y){
        double xlength = Math.abs(x);
        double ylength = Math.abs(y);
        if(x>0){
            if(y>0){//first quadrant
                return Math.atan(ylength/xlength);
            }else if(y==0){//+x axis
                return 0.0;
            }else{//y<0   forth quadrant
                return Math.atan(xlength/ylength) + Math.PI*1.5;
            }
            
            
            
            
        }else if(x==0){
            if(y>0){//+y axis
                return Math.PI/2;
            }else if(y==0){//on (0,0)
                return 0.0;
            }else{//y<0   -y axis
                return Math.PI*1.5;
            }
            
            
            
            
        }else{//x<0
            if(y>0){//second quadrant
                return Math.atan(xlength/ylength) + Math.PI/2;
            }else if(y==0){//-x axis
                return Math.PI;
            }else{//y<0    third quadrant
                return Math.atan(ylength/xlength) + Math.PI;
            }
        }
    }
    
    //returns angle between -PI and PI (includes pi)
    public final static double getPrincipleDirection(double x1, double y1, double x2, double y2){
        return Math.atan2(y2-y1, x2-x1);
    }
    
    public final static double getPrincipleDirectionFromOrigin(double x, double y){
        return Math.atan2(y, x);
    }
    
    
    
    
    
    public static String base10ToX(double num10, int base,int maxDecPlaces)
    {
      /* test
      public static double rotateXofXYaboutCDbyAngle(double x, double y, double c, double d, double angle)
      {
          return (x-c)*Math.cos(angle)-(y-d)*Math.sin(angle)+c;
      }

      public static double rotateYofXYaboutCDbyAngle(double x, double y, double c, double d, double angle)
      {
          return (x-c)*Math.sin(angle)+(y-d)*Math.cos(angle)+d;
      }
      */
        if (base > 64)
          return "base too big!";
        StringBuilder answer = new StringBuilder("");
        boolean isNeg = false;
        if (num10<0)
        {
            num10 *= -1;
            isNeg = true;
        }

        int intPart = (int)num10;
        while (intPart != 0)
        {
            answer.insert(0,MyMath.convertToDigit(intPart % base));
            intPart /= base;
        }
        if (num10 < 1)
          answer.insert(0,'0');
        if (isNeg)
          answer.insert(0,'-');
        if (num10==(int)num10)
          return answer.toString();


        answer.append('.');


        double decPart = num10%1;
        int numDecPlaces = 0;
        while (decPart != 0 && numDecPlaces<maxDecPlaces)
        {
            decPart *= base;
            numDecPlaces ++;
            answer.append(MyMath.convertToDigit((int)decPart));
            if (decPart>=1)
                decPart %= 1;
        }
        return answer.toString();
    }

    public static double toBase10(String str,int base)
    {
        boolean isNeg = false;
        if (str.charAt(0)=='-')
        {
            str = str.substring(1,str.length());
            isNeg = true;
        }
        double answer = 0;
        int decPos = str.indexOf('.');
        if (decPos == -1)
          decPos = str.length();
        int power = 0;
        for (int pos = decPos - 1;pos >= 0;pos--,power++)
        {
            if ((int) str.charAt(pos) == 226)
              answer += 62*Math.pow(base,power);
            else if ((int) str.charAt(pos) == 223)
              answer += 63*Math.pow(base,power);
            else if(str.charAt(pos)==Character.toUpperCase(str.charAt(pos)))
              answer += Character.getNumericValue(str.charAt(pos))*Math.pow(base,power);
            else
               answer += (Character.getNumericValue(str.charAt(pos))+26)*Math.pow(base,power);


//          System.out.println("decPos: " + decPos + "\n" +
//          "power: " + power + "\n" +
//          "pos: " + pos + "\n" +
//          "answer: " + answer + "\n" +
//          "added: " + Character.getNumericValue(str.charAt(pos))*Math.pow(base,power) + "\n");
        }

        power = -1;
        for (int pos = decPos + 1;pos < str.length();pos++,power--)
        {
            if ((int) str.charAt(pos) == 224)
              answer += 62*Math.pow(base,power);
            else if ((int) str.charAt(pos) == 225)
              answer += 63*Math.pow(base,power);
            else if(str.charAt(pos)==Character.toUpperCase(str.charAt(pos)))
              answer += Character.getNumericValue(str.charAt(pos))*Math.pow(base,power);
            else
               answer += (Character.getNumericValue(str.charAt(pos))+26)*Math.pow(base,power);
        }
        if(isNeg)
          return answer * -1;
        else
        return answer;
    }

      public static double logBaseX(double num,double base)
      {
          return Math.log(num)/Math.log(base);
      }

      public static String convertToDigit(int num)
      {
          num = Math.abs(num);
          if (num <=9)
              return Integer.toString(num);
          if (num <=35)
              return Character.toString((char)(num+55));
          if (num <=61)
              return Character.toString((char)(num+61));
          if (num ==62)
              return Character.toString((char)(226));
          if (num ==63)
              return Character.toString((char)(223));
          return "[" + num + "]";
      }





    public static long nPr(int n, int r){
      long result = 1;
      for (int i=n;i>n-r;i--){
        result = result*i;
      }
    return result;
    }

    public static int gcf(int a, int b){
      int r;
      //makes sure a is the larger value
      if (a<b){
        r=a;
        a=b;
        b=r;
      }

      r=a%b;
      while (r!=0){
        a=b;
        b=r;
        r=a%b;
      }
      return b;
    }

    public static int lcm(int a, int b)
    {
      return a*b/MyMath.gcf(a,b);
    }

    public static byte digit(String str, int d){
      return digit(Double.parseDouble(str),d);
    }

    public static byte digit(double n, int d){
      return (byte)(Math.floor(Math.abs(n)/Math.pow(10,d))%10);
    }
    
    //returns 1 for 10-99.999   2 for 100-999.9999  and so on
    public static int powerOfTenRange(double num){
        num=Math.abs(num);
        if(num<1){//Math.log10<0
            return (int) (Math.log10(num)-1);
        }else{
            return (int) Math.log10(num);
        }
        
    }
    
    //may still have floating point errors
    public static double roundToSigFig(double num, int sigFigs){//0.4999999,5
      int powerOfTenRange = powerOfTenRange(num);
//      System.out.println("powerOfTenRange= " + powerOfTenRange);//-1
      long longNum = Math.round(num*Math.pow(10, -powerOfTenRange+(sigFigs-1)));
//      System.out.println("longNum= " + longNum);//50,000
      return longNum*Math.pow(10, powerOfTenRange-(sigFigs-1));
    }
    
    //magnitudDifference is a positive number (expected to be 3-10 or so) that tells how close a number has to be to a nice number
    public static boolean isCloseToNiceNumber(double num, int magnitudDifference){
//        System.out.println("num= " + num);//0.4999999
        int powerOfTenRange = powerOfTenRange(num);
//        System.out.println("powerOfTenRange= " + powerOfTenRange);//-1
        double standardizedNum = Math.round(num*Math.pow(10, -powerOfTenRange+magnitudDifference))/Math.pow(10, magnitudDifference);
//        System.out.println("standardizedNum= " + standardizedNum);//5
        double calculatedRoundedNumber = standardizedNum*Math.pow(10, powerOfTenRange);
//        System.out.println("calculatedRoundedNumber= " + calculatedRoundedNumber);//0.5 or close
        
        double difference = Math.abs(num-calculatedRoundedNumber);
//        System.out.println("difference: " + difference);
        double threashold = Math.pow(10, powerOfTenRange-magnitudDifference);
//        System.out.println("threashold: " + threashold);
        return difference<threashold;
    }

    //second column: how many times that number occurs
    public static long[][] primeFactorPowerArray(long n){
      long[][] array = new long[15][2];
      byte rownum = -1;

      if (n%2 == 0)
      {
        array[++rownum][0] = 2;
        while (n%2 == 0){
          array[rownum][1]++;
          n/=2;
        }
      }

      for (long guess = 3;guess<=Math.sqrt(n);guess+=2){
        if (n%guess == 0){
          array[++rownum][0] = guess;
          while (n%guess == 0){
            array[rownum][1]++;
            n/=guess;
          }
        }

      }
      if (n!=1){
        array[++rownum][0] = n;
        array[rownum][1] = 1;
      }

      return array;
    }
    
    
    
    public static List<Long> primeFactorList(long n){
        List<Long> list = new LinkedList<>();

          while (n%2 == 0){
            list.add((long) 2);
            n/=2;
          }

        for (long guess = 3;guess<=Math.sqrt(n);guess+=2){
            while (n%guess == 0){
                list.add((long) guess);
                n/=guess;
            }

        }
        if (n!=1){
            list.add((long) n);
        }

        return list;
      }

    //>0
    public static List<Long> findAllFactors(long n){
        List<Long> list = new LinkedList<>();
        int sqrt = (int) Math.sqrt(n);//rounded sqrt
        for(int i=1;i<=sqrt;i++){
            if(n%i==0){
                list.add((long)i);
            }
        }
        List<Long> list2 = new LinkedList<>();
        for(long l:list){
            list2.add(0,(long)n/l);
        }
        if(list2.get(0)==sqrt){
            list2.remove(new Long(sqrt));
        }
        list.addAll(list2);
        return list;
    }


    public static String primeFactorString(long[][] pfarray){
      byte rownum = 0;
      String pfstring = "";
      DecimalFormat dfcommas = new DecimalFormat("#,###");
      while (pfarray[rownum][0]!=0){
        if (rownum!=0) pfstring = pfstring + "*";
        pfstring = pfstring + dfcommas.format(pfarray[rownum][0]);

        if (pfarray[rownum][1]>1){
        pfstring = pfstring + "^" + pfarray[rownum][1];
        }
        if (rownum==15) break;
        rownum++;
      }
      return pfstring;
    }


    public static String primeFactorString(long n){
      return primeFactorString(primeFactorPowerArray(n));
    }

    public static boolean isPrime(long n){
      if (n<2) return false;
      if ((n%2==0)&&(n!=2)) return false;
      for (int i=3;i<=Math.sqrt(n);i+=2){
        if (n%i==0) return false;
      }
      return true;
    }

    public static boolean isInteger(double real) {
        return Math.round(real)==real;
    }

    public static double radiansToDegrees(double radians) {
        final double CONVERSTION_FACTOR = 180/Math.PI;
        return radians*CONVERSTION_FACTOR;
    }
    public static double degreesToRadians(double degrees) {
        final double CONVERSTION_FACTOR = Math.PI/180;
        return degrees*CONVERSTION_FACTOR;
    }
}
