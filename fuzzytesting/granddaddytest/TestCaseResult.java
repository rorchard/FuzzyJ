package fuzzytesting.granddaddytest;

import nrc.fuzzy.*;

public class TestCaseResult {
    
    static final boolean WEAK = nrc.fuzzy.Parameters.WEAK;
    static final boolean STRONG = nrc.fuzzy.Parameters.STRONG;
    
    static final int NUMBER_OF_FUZZYVALUES = 7;
    static final int NUMBER_OF_CUT_TYPES = 2;
    static final int NUMBER_OF_CUT_LEVELS = 3;
    
    static final int WEAK_CUT = 0;
    static final int STRONG_CUT = 1;
    
    static final int ZERO = 0;
    static final int MID = 1;
    static final int ONE = 2;
    
	static final int FV1 = 0;
	static final int FV2 = 1;
	static final int NOT_FV1 = 2;
	static final int NOT_FV2 = 3;
	static final int UNION = 4;
	static final int INTERSECTION = 5;
	static final int SUM = 6;
    
    nrc.fuzzy.FuzzyValue[] fVs = new nrc.fuzzy.FuzzyValue[NUMBER_OF_FUZZYVALUES];
    double maxMin;

    //pertaining just to fv1
    nrc.fuzzy.IntervalVector[][][] alphas = new nrc.fuzzy.IntervalVector[NUMBER_OF_FUZZYVALUES][NUMBER_OF_CUT_TYPES][NUMBER_OF_CUT_LEVELS];
    
    boolean[] empty = new boolean[NUMBER_OF_FUZZYVALUES];
    boolean[] convex = new boolean[NUMBER_OF_FUZZYVALUES];
    boolean[] normal = new boolean[NUMBER_OF_FUZZYVALUES];
    
  
    /*
     * Constructor
     */ 
    public TestCaseResult(nrc.fuzzy.FuzzyValue value1, nrc.fuzzy.FuzzyValue value2){
        fVs[FV1] = value1;
        fVs[FV2] = value2;
        

        try {

            fVs[NOT_FV1] = value1.fuzzyComplement();
            fVs[NOT_FV2] = value2.fuzzyComplement();
            fVs[UNION] = value1.fuzzyUnion(value2);
			fVs[INTERSECTION] = value1.fuzzyIntersection(value2);
			fVs[SUM] = value1.fuzzySum(value2);
            maxMin = value1.maximumOfIntersection(value2);

        } catch(nrc.fuzzy.IncompatibleFuzzyValuesException e){
            System.out.println("Problem with Value Incompatibility...." + e.getMessage());
            e.printStackTrace();
        } catch(nrc.fuzzy.XValueOutsideUODException e){
            System.out.println("Problem with x value outside UOD...." + e.getMessage());
            e.printStackTrace();
        }    
        
        for(int i=0; i<NUMBER_OF_FUZZYVALUES; i++){
            for(int j=0; j<NUMBER_OF_CUT_TYPES; j++){
                for(int k=0; k<NUMBER_OF_CUT_LEVELS; k++){
                    boolean cutType = (j == WEAK_CUT) ? WEAK : STRONG;
                    
                    switch(k){
                        case ZERO: alphas[i][j][k] = fVs[i].getAlphaCut(cutType, 0.0); break;
                        case MID:  alphas[i][j][k] = fVs[i].getAlphaCut(cutType, (fVs[i].getMaxY() + fVs[i].getMinY())/2); break;
                        case ONE:  alphas[i][j][k] = fVs[i].getAlphaCut(cutType, 1.0); break;
                        default: break;
                    }    
                }
            }
            
            empty[i] = fVs[i].isEmpty();
            convex[i] = fVs[i].isConvex();
            normal[i] = fVs[i].isNormal();
        }    
    }    
    
    
    public String toString(){
        String s = new String();
        
        s = s.concat("Set 1: " + fVs[FV1].getFuzzySet().toString() + "\n");
        s = s.concat("Set 2: " + fVs[FV2].getFuzzySet().toString() + "\n");
        s = s.concat("NOT Set 1: " + fVs[NOT_FV1].getFuzzySet().toString() + "\n");
        s = s.concat("NOT Set 2: " + fVs[NOT_FV2].getFuzzySet().toString() + "\n");
        s = s.concat("MaxMin: " + maxMin + "\n");
        s = s.concat("Union: " + fVs[UNION].getFuzzySet().toString() + "\n");
		s = s.concat("Intersection: " + fVs[INTERSECTION].getFuzzySet().toString() + "\n");
		s = s.concat("Sum: " + fVs[SUM].getFuzzySet().toString() + "\n");
        
        for(int i=0; i<NUMBER_OF_FUZZYVALUES; i++){
            switch(i){
                case FV1: s = s.concat("-> FuzzyValue 1\n"); break;
                case FV2: s = s.concat("-> FuzzyValue 2\n"); break;
                case NOT_FV1: s = s.concat("-> NOT FuzzyValue 1\n"); break;
                case NOT_FV2: s = s.concat("-> NOT FuzzyValue 2\n"); break;
                case UNION: s = s.concat("-> UNION FuzzyValue\n"); break;
				case INTERSECTION: s = s.concat("-> INTERSECTION FuzzyValue\n"); break;
				case SUM: s = s.concat("-> SUM FuzzyValue\n"); break;
                default: break;
            }
                        
            for(int j=0; j<NUMBER_OF_CUT_TYPES; j++){
                for(int k=0; k<NUMBER_OF_CUT_LEVELS; k++){
                    s = s.concat((j == WEAK_CUT) ? "WEAK " : "STRONG ");
                    s = s.concat("AlphaCut @ ");
                    
                    switch(k){
                        case ZERO: s = s.concat("0.0: "); break;
                        case MID:  s = s.concat("mid: "); break;
                        case ONE:  s = s.concat("1.0: "); break;
                    }    
                    
                    if(alphas[i][j][k] == null) s = s.concat("null \n");
                    else s = s.concat("" + alphas[i][j][k].toString() + "\n");
                }
            }    
            s = s.concat("Empty: " + empty[i] + "\n");
            s = s.concat("Normal: " + normal[i] + "\n");
            s = s.concat("Convex: " + convex[i] + "\n");
        }    

        s = s.concat("\n");
        
        return(s);
    }    
        
}

