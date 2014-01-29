package fuzzytesting.granddaddytest;

import nrc.fuzzy.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;

public class FileWriterForFuzzyTestResults {
    static final String FILE_NAME = "expected.dat";
    static final int EOF = -1;

    int testNumber;

    File expected;

    FileWriter out;

    /*
     * Constructor.
     */

    FileWriterForFuzzyTestResults(){
        try {
            expected = new File(FileAndUrlResolution.getFullFileName(FILE_NAME));

            testNumber = extractLastTestNumber() + 1;

            out = new FileWriter(expected.toString(), true);
        } catch(java.io.FileNotFoundException e){
            System.out.println("Errant file..." + e.getMessage());
            e.printStackTrace();
        } catch(java.io.IOException e){
            System.out.println("IOException..." + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Adds another test result to a temporary file, which will then be concatenated
     * to the exprected test results file.
     */

    public void addTestResults(TestCaseResult testCase){
        try {
            out.write("Test #");
            out.write("" + testNumber + "\n");
            out.write(testCase.toString());

            testNumber ++;
        } catch(java.io.IOException e){
            System.out.println("IOException..." + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Extracts the number of the last test results committed to the results
     * file, and returns it.
     */

    protected int extractLastTestNumber() throws java.io.IOException {
        try {
            FileReader in = new FileReader(expected);
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
            out.close();
        } catch(java.io.IOException e){
            System.out.println("IOException..." + e.getMessage());
            e.printStackTrace();
        }
    }

}
