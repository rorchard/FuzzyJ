package fuzzytesting.granddaddytest;

import symantec.itools.awt.ImageButton;
import java.net.URL;
import java.io.File;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyVetoException;
import nrc.fuzzy.*;

public class GrandDaddyController  
    implements java.awt.event.ActionListener
{

    static final String VERIFIED_RESULTS_FILE = "verified_results.dat";
    static final String VERIFIED_CASES_FILE = "verified_cases.dat";
    static final String TEST_FUZZY_VALUES = "test_values.dat";

    static String[] modifiers;
    static final int NUMBER_OF_MODIFIERS = 11;
    static final int NUMBER_OF_INFO_PANELS = 7;
    
    static final int NUMBER_OF_CUT_TYPES = 2;
    static final int NUMBER_OF_CUT_LEVELS = 3;
    
	static final int FV1 = 0;
	static final int FV2 = 1;
	static final int NOT_FV1 = 2;
	static final int NOT_FV2 = 3;
	static final int UNION = 4;
	static final int INTERSECTION = 5;
	static final int SUM = 6;
	
	static final int ZERO = 0;
	static final int MID = 1;
	static final int ONE = 2;
	
	FuzzyTestFileWriter verifiedCases;
	FuzzyTestFileWriter verifiedResults;
    Parser parser; 
    
	TestingFrame frame;
	statusFrame status;
	TestCaseResult testCase;

	nrc.fuzzy.FuzzyValue[] fuzzyValues;
	int index;
	
    java.net.URL nextArrow, prevArrow;
    

	
	/****************************************************************************************
	 *
	 * CONSTRUCTOR(s) / DESTRUCTOR
	 *
	 ****************************************************************************************/

 	GrandDaddyController(){
 	    

 	    frame = new TestingFrame();
 	    status = new statusFrame();
 	    
        frame.getNextValueButton().addActionListener(this);
        frame.getPrevValueButton().addActionListener(this);
        frame.getSubmitButton().addActionListener(this);
        frame.getResultsVerifiedButton().addActionListener(this);
        frame.getTestResultsButton().addActionListener(this);
        frame.getRunTestsButton().addActionListener(this);
        frame.getNextPairButton().addActionListener(this);

 	    frame.setVisible(true);
        	
		nextArrow = FileAndUrlResolution.getFileUrl("graphics/arrowR.gif");
		prevArrow = FileAndUrlResolution.getFileUrl("graphics/new_left_arrow.gif");

        try {
            parser = new Parser();    	                                               
    		fuzzyValues = parser.getTestFuzzyValues(new File(FileAndUrlResolution.getFullFileName(TEST_FUZZY_VALUES)));
    		index = 0;
    		
        	verifiedCases = new FuzzyTestFileWriter(new File(FileAndUrlResolution.getFullFileName(VERIFIED_CASES_FILE)), true);
        	verifiedResults = new FuzzyTestFileWriter(new File(FileAndUrlResolution.getFullFileName(VERIFIED_RESULTS_FILE)), true);
        	
        } catch(java.io.FileNotFoundException e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
/*        } catch(FilesOutOfSyncException e){
            System.out.println("" + e.getMessage());
            System.out.println("DEFINITELY A CAUSE FOR CONCERN...");*/
        }    
        
        // set higher precision for output of fuzzy set values and alpha cut values
        Interval.setToStringPrecision(9);
        FuzzySet.setToStringPrecision(9);
        
        nextFuzzyValue();
 	}
 	
 	
 	static {
 	    modifiers = new String[NUMBER_OF_MODIFIERS];
        modifiers[0] = "not"; 
        modifiers[1] = "slightly";
        modifiers[2] = "somewhat";
        modifiers[3] = "more_or_less";
        modifiers[4] = "plus";
        modifiers[5] = "very";
        modifiers[6] = "extremely";
        modifiers[7] = "intensify";
        modifiers[8] = "above";
        modifiers[9] = "below";
        modifiers[10] = "norm";
    }    


	/****************************************************************************************
	 *
	 * MAIN
	 *
	 ****************************************************************************************/

 	static public void main(String args[]) 
 		{ // args is either empty (the default directory will work) or it has a string identifying
		  // where the directory is for the xxxxx.gif files (arrowR.gif, etc.) and the data files
 		  // (results.out, compare.dat, etc.). It might look something like:
 		  //
 		  //  "file:////Users/boborchard/fuzzytesting/granddaddytest/"
		  // 
		  String appDir = "";
		  if (args.length > 0)
		  {	appDir = args[0];
		  }
		  
		  FileAndUrlResolution.applicationDirectory = appDir;

		new GrandDaddyController();
	}


	/****************************************************************************************
	 *
	 * ACTION CONTROL METHOD
	 *
	 ****************************************************************************************/

    public void actionPerformed(java.awt.event.ActionEvent event) {
		Object object = event.getSource();
		if (object == frame.imageButtonNextValue) 
    		nextFuzzyValue();
		else if (object == frame.imageButtonPrevValue) 
		    prevFuzzyValue();
		else if (object == frame.submitButton)
		    displayTestCase();
		else if (object == frame.resultsVerifiedButton)
		    storeResults();
		else if (object == frame.testResultsButton)
		    showResults();
		else if (object == frame.presetTestsButton)
		    runTests();
		else if (object == frame.nextPairButton)
		    loadNextPair();
	}
	

	/****************************************************************************************
	 *
	 * METHODS
	 *
	 ****************************************************************************************/

    /**
     *
     */

    void storeResults()
    {
        verifiedCases.addVerifiedTestCase(testCase.fVs[FV1], testCase.fVs[FV2]);
        verifiedResults.addVerifiedTestResult(testCase);
        frame.resultsVerifiedButton.setEnabled(false);
    }  
    
    /**
     *
     */

    void loadNextPair()
    {
        // take value in the Fuzzy Value 2 text field and add 1 
        // to it then put this value in Fuzzy value 1 text field
        // and the value + 2 in the fuzzy value 2 text field
        int fv1Num = 0;
        String fv2 = frame.getFV2TextField();
        try
          { fv1Num = Integer.valueOf(fv2).intValue()+1;
            if (fv1Num > fuzzyValues.length) 
                fv1Num = fv1Num % fuzzyValues.length;
          }
        catch (NumberFormatException e)
          { // if no number in the text field then do nothing
            fv1Num = 1;
          }
        frame.setFV1TextField(String.valueOf(fv1Num));
        frame.setFV2TextField(String.valueOf(++fv1Num));
        
        // finally use these new values: calc values and show on the panel
        displayTestCase();
    }  
    
    /**
     *
     */
    
    void showResults()
    {
        status.setLocation(100,100);
        status.show();
        if (testCase == null)
		  status.statusTextArea.setText("");
        else
          status.statusTextArea.setText(testCase.toString());
    }  
    
    /**
     *
     */
     
    void runTests()
    { 
        status.setLocation(100,100);
        status.show();
        status.statusTextArea.setText("");

        try 
        {
            FuzzyTestFileWriter out = new FuzzyTestFileWriter(new File(FileAndUrlResolution.getFullFileName("results.dat")));
            nrc.fuzzy.FuzzyValue[] fuzzyValues = parser.getFuzzyValues(new File(FileAndUrlResolution.getFullFileName("verified_cases.dat")));
            TestCaseResult testCase;
            
            for(int i=0; i<fuzzyValues.length; i+=2)
            {
                status.statusTextArea.append("Doing Test #" + ((i/2)+1) + "\n");
                testCase = new TestCaseResult(fuzzyValues[i], fuzzyValues[i+1]);
                out.addTestResult(testCase);
            }
            
            out.finishChangesToFile();
            
            int diffs = parser.compareFiles(new File(FileAndUrlResolution.getFullFileName(VERIFIED_RESULTS_FILE)), 
                                             new File(FileAndUrlResolution.getFullFileName("results.dat")), 
                                             new File(FileAndUrlResolution.getFullFileName("compare.out")));
            status.statusTextArea.append("\nNumber of Tests with differences = " + diffs + "\n");
        } 
        catch(java.io.FileNotFoundException e)
        {
            System.out.println("" + e.getMessage());
            e.printStackTrace();
        }    
    }    
        
        
    
    /**
     *
     */
    
    void nextFuzzyValue(){
        nrc.fuzzy.FuzzyValue fv = fuzzyValues[index];
        
        frame.seeNextFuzzyValue(todoubleArray(fv));
        frame.setFuzzyValueNumber(++index);
        
        index%=fuzzyValues.length;
    }
    
    /**
     *
     */
    
    void prevFuzzyValue()
    {
       if (index <= 1) 
          index = fuzzyValues.length-2+index;
       else 
          index -= 2;
        
       nextFuzzyValue();
    }
    
    /**
     *
     */
    
    void displayTestCase() {
        
        FuzzyValue fv1 = convertUserInputToFuzzyValue(1);
        FuzzyValue fv2 = convertUserInputToFuzzyValue(2);
         
        if (fv1 == null || fv2 == null)
           return;
 
        frame.resultsVerifiedButton.setEnabled(true);
           
        testCase = new TestCaseResult(fv1, fv2);
        
        frame.displayFuzzyValues(testCase.fVs);

        for(int i=0; i < NUMBER_OF_INFO_PANELS; i++){
            frame.setMaxMin("" + testCase.maxMin, i);
                        
            for(int j=0; j < NUMBER_OF_CUT_TYPES; j++){
                for(int k=0; k < NUMBER_OF_CUT_LEVELS; k++){
                    if(testCase.alphas[i][j][k] == null) {
                        frame.setAlphaCut(testCase.fVs[i], null, i, j, k);
                    } 
                    
                    else {
                        nrc.fuzzy.FuzzySet fs = new nrc.fuzzy.FuzzySet();

                        for(int z=0; z < testCase.alphas[i][j][k].size(); z++){
                            double y;
                            
                            switch(k){
                                case ZERO: y = 0.0; break;
                                case MID:  y = (testCase.fVs[i].getMaxY() + testCase.fVs[i].getMinY())/2; break;
                                default: case ONE:  y = 1.0; break;
                            }    
                            
                            fs.insertSetPoint(testCase.alphas[i][j][k].intervalAt(z).getLowX(), 0);
                            fs.insertSetPoint(testCase.alphas[i][j][k].intervalAt(z).getLowX(), y);
                            fs.insertSetPoint(testCase.alphas[i][j][k].intervalAt(z).getHighX(), y);
                            fs.insertSetPoint(testCase.alphas[i][j][k].intervalAt(z).getHighX(), 0);
                            
                        }    
                        
                        try {
                        
                            nrc.fuzzy.FuzzyValue fv = new nrc.fuzzy.FuzzyValue(fv1.getFuzzyVariable(), fs);
                            frame.setAlphaCut(testCase.fVs[i], fv, i, j, k);
                            
                        } catch(nrc.fuzzy.XValueOutsideUODException e){
                            System.out.println("UOD Error creating AlphaCut FuzzyValues...");
                            e.printStackTrace();
                        }    

                    }    
                }
            }    

            frame.setEmptyIndicator(testCase.empty[i], i);
            frame.setConvexIndicator(testCase.convex[i], i);
            frame.setNormalIndicator(testCase.normal[i], i);

        }
    }    
    
    /**
     *
     */
    
    nrc.fuzzy.FuzzyValue convertUserInputToFuzzyValue(int whichValue){
        
        try {
            
            String fv = frame.getFVTextField(whichValue).getText();

            int[] mods = extractModifiers(fv);
            int testNum = extractTestNum(fv);
            
            if (testNum <= 0 || testNum > fuzzyValues.length)
               return null;

            nrc.fuzzy.FuzzyValue fuzzyValue = fuzzyValues[testNum-1].copyFuzzyValue();
            fuzzyValue = applyModifiers(fuzzyValue, mods);
            
            return(fuzzyValue);

        } catch(CloneNotSupportedException e){
            System.out.println("" + e.getMessage());
            return(null);
        }    
    }
    
    /**
     *
     */
    
    int[] extractModifiers(String fuzzyValue){
        int[][] mods = new int[NUMBER_OF_MODIFIERS][2];
        int index = 0;
        int modifierCount = 0;
        
        for(int i=0; i < NUMBER_OF_MODIFIERS; i++){
            index = fuzzyValue.indexOf(modifiers[i]);

            if(index != -1){
                mods[i][0] = index;
                mods[i][1] = i;
                modifierCount++;
            } else {    
                mods[i][0] = -1;
            }    
        }
        
        int[] modsToApply = new int[modifierCount];
        int max;
        int maxIndex;
        
        for(int j=0; j < modifierCount; j++){
            max = 0;
            maxIndex = 0;
            
            for(int i=0; i < NUMBER_OF_MODIFIERS; i++){
                if(mods[i][0] != -1 && mods[i][0] >= max){
                    max = mods[i][0];
                    maxIndex = i;
                }
            }
 
            mods[maxIndex][0] = -1;
            modsToApply[j] = mods[maxIndex][1];
        }    
                
        return(modsToApply);
    }
    
    /**
     *
     */
    
    nrc.fuzzy.FuzzyValue applyModifiers(nrc.fuzzy.FuzzyValue fv, int[] mods){
        for(int i=0; i<mods.length; i++){
            switch(mods[i]){
                case 0: fv = nrc.fuzzy.Modifiers.not(fv); break;
                case 1: fv = nrc.fuzzy.Modifiers.slightly(fv); break;
                case 2: fv = nrc.fuzzy.Modifiers.somewhat(fv); break;
                case 3: fv = nrc.fuzzy.Modifiers.more_or_less(fv); break;
                case 4: fv = nrc.fuzzy.Modifiers.plus(fv); break;
                case 5: fv = nrc.fuzzy.Modifiers.very(fv); break;
                case 6: fv = nrc.fuzzy.Modifiers.extremely(fv); break;
                case 7: fv = nrc.fuzzy.Modifiers.intensify(fv); break;
                case 8: fv = nrc.fuzzy.Modifiers.above(fv); break;
                case 9: fv = nrc.fuzzy.Modifiers.below(fv); break;
                case 10: fv = nrc.fuzzy.Modifiers.norm(fv); break;
                default: break;
            }
        }

        return(fv);
    }    
            
    /**
     *
     */
    
    int extractTestNum(String fuzzyValue){
        StringBuffer sb = new StringBuffer();
        char c;
        
        if(fuzzyValue != null && fuzzyValue.length() != 0){
            for(int i=0; i<fuzzyValue.length(); i++){
                c = fuzzyValue.charAt(i);
                
                if(Character.isDigit(c)) sb.append(c);
            }
            
            return((new Integer(sb.toString())).intValue());
        } else {
            return(0);
        }    
    }    
    
    double[] todoubleArray(nrc.fuzzy.FuzzyValue fv){
        double[] array = new double[fv.size()*2];
        
        for(int i=0, j=0; i<fv.size(); i++, j+=2){
            array[j] = fv.getX(i);
            array[j+1] = fv.getY(i);
        }
        
        return(array);
    }    
    
}