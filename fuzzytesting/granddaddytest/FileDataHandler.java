package fuzzytesting.granddaddytest;

import java.io.*;
import nrc.fuzzy.*;
import java.util.Vector;

public class FileDataHandler {

    static final int MAX = Integer.MAX_VALUE;
    static final int MIN = Integer.MIN_VALUE;

    static nrc.fuzzy.FuzzyVariable testVar;
    
    static final String IN_FILE_NAME = "testcases.dat";

    static final int LENGTH = 100;
    static final int EOF = -1;

    static 
    { try 
      {
    	 testVar = new nrc.fuzzy.FuzzyVariable("TEST", MIN, MAX, "noUnits");
      } 
      catch (InvalidFuzzyVariableNameException e) {}
      catch (InvalidUODRangeException e) {}
    }

    File data;
    FileReader in;

    TestFuzzyValueVector fuzzyValues;
    int numFuzzyValues;


    /***************************************************************************************
     *
     * CONSTRUCTOR
     *
     ***************************************************************************************/

    FileDataHandler(){
        fuzzyValues = new TestFuzzyValueVector();
        numFuzzyValues = 0;
    }


    /***************************************************************************************
     *
     * METHOD TO RUN THE SHOW
     *
     ***************************************************************************************/

    TestFuzzyValueVector getDefaultFuzzyValues(){
        TestFuzzyValue testFuzzyValue = null;
     
        try {
            openInFile();

            testFuzzyValue = readNextSet();

        } catch(java.io.IOException e) {
            System.out.println("Uhh Ohhhh... Houston, we have a problem...");
            System.out.println(""+e.getMessage());
            testFuzzyValue = null;
        } catch(nrc.fuzzy.XValuesOutOfOrderException e){
            System.out.println("What a pain...");
        }    

        while(testFuzzyValue != null){
    
            try {
                fuzzyValues.addTestFuzzyValue(testFuzzyValue);
                testFuzzyValue = readNextSet();
            } catch(java.io.IOException e) {
                System.out.println("Uhh Ohhhh... Houston, we have another problem...");
                System.out.println(""+e.getMessage());
                testFuzzyValue = null;
            } catch(CloneNotSupportedException e) {
                System.out.println("Uhh Ohhhh... Houston, we have another problem...");
                System.out.println(""+e.getMessage());
                testFuzzyValue = null;
            } catch(nrc.fuzzy.XValuesOutOfOrderException e){
                System.out.println("What a pain...");
            }    
        }

        try {
            in.close();
        } catch(IOException e) {
            System.out.println("You can't leave me here....." + e.getMessage());
        }

        return(fuzzyValues);
    }


    /***************************************************************************************
     *
     * METHODS FOR DEALING WITH THE IN FILE
     *
     ***************************************************************************************/

    public void openInFile() throws IOException {
        data = new File(FileAndUrlResolution.getFullFileName(IN_FILE_NAME));
        in = new FileReader(data);
    }

    public TestFuzzyValue readNextSet() throws IOException, nrc.fuzzy.XValuesOutOfOrderException {

        nrc.fuzzy.DoubleVector set = null;
        int testNum = 0;
        int c;

        c = in.read();

        while(c != EOF && set == null){

            //for any comment lines
            if(c == ';')
                while(c != EOF && !endOfLine(c))
                    c = in.read();

            //for any blank lines
            if(isWhiteSpace(c))
                while(isWhiteSpace(c))
                    c = in.read();

            if(isChar(c)){
                StringBuffer input = new StringBuffer(LENGTH);

                while(c != EOF && !endOfLine(c)){
                    if(isNumber(c)){
                        input.append((char) c);
                    }
                    c = in.read();
                }

                testNum = (new Integer(input.toString())).intValue();
            }

            if(isNumber(c)){

                StringBuffer input;
                set = new nrc.fuzzy.DoubleVector();

                while(c != EOF && !endOfLine(c)){
                    input = new StringBuffer(LENGTH);

                    while(c != EOF && !isWhiteSpace(c)){
                        input.append((char) c);
                        c = in.read();
                    }

                    if(!endOfLine(c)) c = in.read();

                    if(input.length() != 0)
                        set.addDouble((new Double(input.toString())).doubleValue());
                }

                c = in.read();
            }
        }

        if(set != null && set.size() != 0 && testNum != 0) {
            try {
                return(new TestFuzzyValue(testVar, set, testNum));
            } catch(nrc.fuzzy.XValuesOutOfOrderException e){
                System.out.println("We ain't in Kansas anymore...");
                return(null);
            } catch(nrc.fuzzy.YValueOutOfRangeException e){
                System.out.println("The sun'il come out, tumorra...");
                return(null);
            }    
        } else {
            return(null);
        }    
    }

    /***************************************************************************************
     *
     * METHODS FOR IDENTIFYING THE TYPE OF LINE OR CHARACTER BEING DEALT WITH
     *
     ***************************************************************************************/

    private boolean isChar(int c){
        return(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
    }

    private boolean isNumber(int c){
        return(('0' <= c && c <= '9') || c == '.' || c == '-' || c == '+');
    }

    private boolean isWhiteSpace(int c){
        return((c == ' ') ||
               (c == '\n') ||
               (c == '\r') ||
               (c == '\t') ||
               (c == '\f') ||
               (c == '\b') ||
               (c == ','));
    }

    private boolean endOfLine(int c){
        return((c == '\n') || (c == '\r') || (c == '\f'));
    }
}
