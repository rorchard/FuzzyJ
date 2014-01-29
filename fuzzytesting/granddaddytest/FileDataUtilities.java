package fuzzytesting.granddaddytest;

import nrc.fuzzy.*;
import java.util.StringTokenizer;

public class FileDataUtilities {

    protected static final int MIN = Integer.MIN_VALUE;
    protected static final int MAX = Integer.MAX_VALUE;

    public static nrc.fuzzy.FuzzyVariable testVar;

    static 
    { try 
      {
    	 testVar = new nrc.fuzzy.FuzzyVariable("TEST", MIN, MAX, "noUnits");
      } 
      catch (InvalidFuzzyVariableNameException e) {}
      catch (InvalidUODRangeException e) {}
    }


    public static nrc.fuzzy.FuzzyValue convertToFuzzyValue(String s){
        if(s.startsWith("{")) s = s.substring(1);
        if(s.endsWith("}"))   s = s.substring(0, s.length()-1);

        s = s.trim();

        StringTokenizer st = new StringTokenizer(s, " /");
        nrc.fuzzy.DoubleVector x = new nrc.fuzzy.DoubleVector();
        nrc.fuzzy.DoubleVector y = new nrc.fuzzy.DoubleVector();

        while(st.hasMoreTokens()){
            x.addDouble((new Double(st.nextToken())).doubleValue());
            y.addDouble((new Double(st.nextToken())).doubleValue());
        }

        try {
            return(new nrc.fuzzy.FuzzyValue(testVar, x.todoubleArray(), y.todoubleArray(), x.size()));
        } catch(nrc.fuzzy.XValuesOutOfOrderException e){
            System.out.println("Trouble in paradise...");
            return(null);
        } catch(nrc.fuzzy.YValueOutOfRangeException e){
            System.out.println("Trouble in paradise...");
            return(null);
        } catch(nrc.fuzzy.XValueOutsideUODException e){
            System.out.println("Trouble in paradise...");
            return(null);
        }    
    }
}