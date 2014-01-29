package fuzzytesting.granddaddytest;

import java.io.*;
import java.util.StringTokenizer;
import nrc.fuzzy.*;



public class Parser1 {

    static final int LENGTH = 100;
    static final int EOF = -1;
    
    static private FuzzyVariable FVAR;


    /***************************************************************************************
     *
     * CONSTRUCTOR
     *
     ***************************************************************************************/

    Parser1()
    {
        try {FVAR = new FuzzyVariable("TEST", 0.0, 1.0, "noUnits");}
        catch (Exception e) {}
    }

    /***************************************************************************************
     *
     * METHODs TO RUN THE SHOW
     *
     ***************************************************************************************/



    nrc.fuzzy.FuzzyValue[] getTestFuzzyValues(File file) throws java.io.FileNotFoundException {
        return(getFuzzyValues(file));
    }

    nrc.fuzzy.FuzzyValue[] getVerifiedFuzzyValues(File file) throws java.io.FileNotFoundException {
        return(getFuzzyValues(file));
    }

    nrc.fuzzy.FuzzyValue[] getFuzzyValues(File file) throws java.io.FileNotFoundException {
        FileReader in = new FileReader(file);

        try {
            int length = extractNumFuzzyValues(file);
            nrc.fuzzy.FuzzyValue[] fuzzyValues = new nrc.fuzzy.FuzzyValue[length];
            int index = 0;

            String line = readNextLine(in);

            while(line != null && index < length){
                if(line.startsWith("{ ")){
                    nrc.fuzzy.FuzzyValue fv = convertToFuzzyValue(line);
                    fuzzyValues[index++] = fv;
                }

                line = readNextLine(in);
            }

            in.close();
            return(fuzzyValues);

        } catch(java.io.IOException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }

    /**
     *
     */

    void compareFiles(File verifiedFile, File resultFile, File outFile){
        try {
            FileWriter out = new FileWriter(outFile);

            System.out.println("" + outFile.canWrite());

            FileReader expected = new FileReader(verifiedFile);
            FileReader results = new FileReader(resultFile);

            String expLine = readNextTextLine(expected);
            String resultLine = readNextTextLine(results);

            nrc.fuzzy.StringVector expTest = new nrc.fuzzy.StringVector(50);
            nrc.fuzzy.StringVector resultTest = new nrc.fuzzy.StringVector(50);

            boolean difference = false;

            while(expLine != null && resultLine != null){

                if(expLine.equals(resultLine)){
                    if(expLine.startsWith("Test")){
                        if(difference){
                            // dump last compared tests that failed
                            dumpFailedTest(out, expTest, resultTest);

                            difference = false;
                        }

                        expTest.removeAllStrings();
                        resultTest.removeAllStrings();

                        expTest.addString("EXPECTED ->\n");
                        resultTest.addString("RESULTS ->\n");
                    }

                    expTest.addString("   " + expLine);
                    resultTest.addString("   " + resultLine);
                } else {
                    expTest.addString("** " + expLine);
                    resultTest.addString("** " + resultLine);

                    difference = true;
                }

                expLine = readNextTextLine(expected);

                resultLine = readNextTextLine(results);
            }

            if (difference) 
                // dump last compared tests that failed
                dumpFailedTest(out, expTest, resultTest);
            
            expected.close();
            results.close();
            out.close();
        } catch(java.io.FileNotFoundException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
        } catch(java.io.IOException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 
     */
     
     public void dumpFailedTest(FileWriter out, StringVector expTest, StringVector resultTest)
     {
        try
          {
            for(int i=0; i<expTest.size(); i++){
                out.write(expTest.StringAt(i));
            }

            out.write("\n");

            for(int i=0; i<resultTest.size(); i++){
                out.write(resultTest.StringAt(i));
            }

            out.write("\n");
          }
        catch (java.io.IOException e)
          {
            System.out.println("" + e.getMessage());
            e.printStackTrace();
          }
     }
     

    /**
     *
     */

    void convertOldData(File oldDataFile, File newDataFile) throws java.io.IOException {
        FileReader in = new FileReader(oldDataFile);
        FileWriter out = new FileWriter(newDataFile);
        
        String line = readNextLine(in);

        while(line != null && line.length() > 0){

            if(isNumber(line.charAt(0))){
                out.write("{ ");

                StringTokenizer st = new StringTokenizer(line, " ");
                String x;
                String y;

                while(st.hasMoreTokens()){
                    x = st.nextToken();

                    //remove carriage return if this number is the last
                    //of the set
                    y = st.nextToken();
                    if(Character.isWhitespace(y.charAt(y.length()-1))){
                        y = y.substring(0, y.length()-1);
                    }

                    out.write(y + "/" + x + " ");
                }

                out.write("}\n");
            }

            else {
                out.write(line);
            }

            line = readNextLine(in);
        }
    }

    /***************************************************************************************
     *
     * OTHER METHODs
     *
     ***************************************************************************************/

    /*
     * Extracts the number of the last test results committed to the results
     * file, and returns it.
     */

    protected int extractNumFuzzyValues(File file) throws java.io.IOException {
        try {
            FileReader in = new FileReader(file);
            int testNumber = 0;

            String line = readNextLine(in);

            while(line != null){
                if(line.startsWith("Test")){
                    testNumber = getTestNumber(line)*2;

                } else if (line.startsWith("FuzzySet")){
                    testNumber = getTestNumber(line);
                }

                line = readNextLine(in);
            }

            in.close();
            return(testNumber);

        } catch(java.io.FileNotFoundException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
            return(0);
        }
    }

    /**
     *
     */

    String readNextLine(FileReader in) throws java.io.IOException {
        StringBuffer sb = new StringBuffer();
        int c = in.read();

        while(c != EOF && !endOfLine((char)c)){
            sb.append((char)c);
            c = in.read();
        }

        sb.append((char)c);

        if(sb.length() == 1 && c == EOF) return(null);
        else return(sb.toString());
    }

    /**
     *
     */

    String readNextTextLine(FileReader in) throws java.io.IOException {
        String s;

        do {
            s = removeWhiteSpace(readNextLine(in));
            if(s == null) return(s);
        } while(s.length() == 0 || (s.length() == 1 && s.charAt(0) == '\n'));

        return(s);
    }


    /**
     *
     */

    String removeWhiteSpace(String line){
        if(line == null) return(null);

        StringBuffer sb = new StringBuffer(line);

        for(int i=0; i < sb.length(); i++){
            if(endOfLine(sb.charAt(i))){
                sb.setCharAt(i, '\n');
            }
        }

        return(sb.toString());
    }

    /**
     *
     */

    public nrc.fuzzy.FuzzyValue convertToFuzzyValue(String s)
    {
        double minX, maxX;
        int i;
        
        s = s.substring(s.indexOf("{")+1, s.indexOf("}"));
        s = s.trim();

        StringTokenizer st = new StringTokenizer(s, " /");
        nrc.fuzzy.DoubleVector x = new nrc.fuzzy.DoubleVector();
        nrc.fuzzy.DoubleVector y = new nrc.fuzzy.DoubleVector();

        while(st.hasMoreTokens()){
            y.addDouble((new Double(st.nextToken())).doubleValue());
            x.addDouble((new Double(st.nextToken())).doubleValue());
        }
        
        // normalize all x values to be between 0.0 and 1.0 so we
        // can compare these fuzzy values with each other .. must have
        // the same fuzzy variable ... we could have used a wide
        // UOD for the fuzzy variable BUT this made more sense.
        minX = maxX = x.doubleAt(0);
        for (i=1; i<x.size(); i++)
        {   double val = x.doubleAt(i);
            if (val < minX) minX = val;
            if (val > maxX) maxX = val;
        }
        minX = Math.floor(minX);
        maxX = Math.ceil(maxX);
        if(minX == maxX)
        {  minX -=1;  maxX +=1;
        }
        for (i=0; i<x.size(); i++)
        {   x.setDoubleAt((x.doubleAt(i)-minX)/(maxX-minX),i);
        }

        try {

            nrc.fuzzy.FuzzySet fs = new nrc.fuzzy.FuzzySet(x.todoubleArray(), y.todoubleArray(), x.size());

            try
             { return new FuzzyValue(FVAR, fs);
             }
            catch(nrc.fuzzy.XValueOutsideUODException e) 
             {
               System.out.println("Trouble in paradise...");
               e.printStackTrace();
               return(null);
             }
            catch (Exception e)
             {
               System.out.println("Trouble in paradise...");
               e.printStackTrace();
                return null;
             }

        } catch(nrc.fuzzy.XValuesOutOfOrderException e){
            System.out.println("Trouble in paradise...");
            return(null);
        } catch(nrc.fuzzy.YValueOutOfRangeException e){
            System.out.println("Trouble in paradise...");
            return(null);
        }
    }

    /**
     *
     */

    int getTestNumber(String s){
        int testNumber = 0;
        int index=0;
        char c;

        while(index < s.length()){

            c = s.charAt(index++);

            if(c == '#'){
                StringBuffer temp = new StringBuffer();
                c = s.charAt(index++);

                while(index < s.length() && Character.isDigit((char)c)) {
                    temp.append(c);
                    c = s.charAt(index++);
                }

                testNumber = (new Integer(temp.toString())).intValue();
            }
        }

        return(testNumber);
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
