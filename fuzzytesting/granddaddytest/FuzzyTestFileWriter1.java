package fuzzytesting.granddaddytest;

import nrc.fuzzy.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;



public class FuzzyTestFileWriter1 {
    static final int EOF = -1;

    FileWriter out;

    int testNumber;

    /**************
     * Constructor.
     **************/

    FuzzyTestFileWriter1(File outFile, boolean append) {

        try {
            out = new FileWriter(outFile.toString(), append);

            testNumber = extractLastTestNumber(outFile) + 1;

        } catch(java.io.FileNotFoundException e){
            System.out.println("Errant file..." + e.getMessage());
            e.printStackTrace();
        } catch(java.io.IOException e){
            System.out.println("IOException..." + e.getMessage());
            e.printStackTrace();
        }
    }

    FuzzyTestFileWriter1(File outFile) {

        try {
            out = new FileWriter(outFile);

            testNumber = extractLastTestNumber(outFile) + 1;

        } catch(java.io.FileNotFoundException e){
            System.out.println("Errant file..." + e.getMessage());
            e.printStackTrace();
        } catch(java.io.IOException e){
            System.out.println("IOException..." + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * 
     */

    public void addVerifiedTestCase(nrc.fuzzy.FuzzyValue value1, nrc.fuzzy.FuzzyValue value2) {

        try {
            out.write("Test #");
            out.write("" + testNumber + "\n");

            out.write("" + value1.getFuzzySet().toString() + "\n");
            out.write("" + value2.getFuzzySet().toString() + "\n");
            out.write("\n");
            out.flush();

            testNumber++;
        } catch(java.io.IOException e){
            System.out.println("uh oh...");
            e.printStackTrace();
        }
    }

    /**
     *
     */
     
   public void addVerifiedTestResult(TestCaseResult testCase) {
        addTestResult(testCase);
    }    

    /**
     *
     */
     
    public void addTestResult(TestCaseResult testCase) { 
        try {
            out.write("Test #");
            out.write("" + testNumber + "\n");
            out.write(testCase.toString());
            out.flush();

            testNumber++;
        } catch(java.io.IOException e){
            System.out.println("uh oh...");
            e.printStackTrace();
        }
    }    
    
    /*
     * Extracts the number of the last test results committed to the results
     * file, and returns it.
     */

    protected int extractLastTestNumber(File file) throws java.io.IOException {
        try {

            FileReader in = new FileReader(file);
            int c = 0;
            int testNumber = 0;

            while(c != EOF){

                c = in.read();

                if(c == '#'){
                    StringBuffer temp = new StringBuffer();
                    c = in.read();

                    while(Character.isDigit((char)c)){
                        temp.append((char)c);
                        c = in.read();
                    }

                    testNumber = (new Integer(temp.toString())).intValue();
                }
            }

            in.close();

            return(testNumber);

        } catch(java.io.FileNotFoundException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
            return(0);
        }

    }

    /*
     * Closes the file.
     */

    public void finishChangesToFile(){
        try {
            out.flush();
            out.close();
            System.out.println("The file is now closed...");
        } catch(java.io.IOException e){
            e.printStackTrace();
        }
    }
}
