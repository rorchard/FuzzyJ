/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 *  http://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006 National Research Council of Canada 
 * 
 * This software was initially developed at the National Research Council of Canada (NRC).
 *
 * THE NATIONAL RESEARCH COUNCIL OF CANADA MAKES NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * THE NATIONAL RESEARCH COUNCIL OF CANADA SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 *
 */


package nrc.fuzzy;

import java.util.*;
import java.io.*;

/**
 * This class defines a FuzzyVariable, which consists of a <b>name</b> (for example, temperature)
 * the <b>units</b> of the variable if required (for example, degrees C), the <b>universe of discourse</b>
 * for the variable (for example a range from 0 to 100), and a <b>set of fuzzy terms</b> that will be used
 * when describing the fuzzy variable. The fuzzy terms are described
 * using a term name such as hot, along with a FuzzySet that represents that term.
 * The fuzzy variable terms along with a set of fuzzy modifiers, the operators
 * 'and' and 'or' (fuzzy set intersection and union respectively) and the left and
 * right parentheses provide the basis for a grammar
 * that allows one to write fuzzy linguistic expressions that
 * describe fuzzy concepts in an english-like manner. For example,
 * <pre><code>     (very hot or warm) and slightly cold
 * </code></pre>
 * consists of the terms hot, warm and cold, along with the fuzzy modifiers
 * very and slightly.
 * <p> A FuzzyVariable is normally used in the creation of a FuzzyValue which
 * associates a FuzzySet with a FuzzyVariable. This allows the FuzzySets to
 * be described using the linguistic expressions definable with the FuzzyVaraible terms
 * and restricted by the universe of discourse for the variable. 
 * <p>
 * NOTE: linguistic expressions are case insensitive!!
 * <p>
 * NOTE ALSO: All names for terms must not have spaces, ) or ( in them. They must
 * also not be AND or OR. They might be the same as some existing 
 * modifier and then would override the modifier (not generally 
 * a good thing to do).
 * <p>
 * The following incomplete set of code shows a simple example of creating 
 * a FuzzyVariable and a FuzzyValue:
 * <pre><code>
 *    double xHot[] = {25, 35};
 *    double yHot[] = {0, 1};
 *    double xCold[] = {5, 15};
 *    double yCold[] = {1, 0};
 *    FuzzyValue fval = null;
 *    ...
 *    FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C");
 *    ...
 *    temp.addTerm("hot", xHot, yHot, 2);
 *    temp.addTerm("cold", xCold, yCold, 2);
 *    // note: once a term is added it can be used to define other terms
 *    temp.addTerm("medium", "not hot and not cold");
 *    ...
 *    fval = new FuzzyValue(temp, "very hot or cold");
 *    System.out.println(fval);
 * </code></pre>
 * The output from this would be:
 * <pre><code>
 * FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
 * Linguistic Expression -> very hot or cold
 * FuzzySet              -> { 1/5 0/15 0/25 0.01/26 0.04/27 0.09/28 0.16/29 0.25/30
 *                            0.36/31 0.49/32 0.64/33 0.81/34 1/35  } 
 * </code></pre>
 *
 * @author Bob Orchard
 *
 * @see FuzzyValue
 * @see FuzzySet
 * @see Modifiers
 * @see ModifierFunction
 * @see FuzzyScanner
 * @see FuzzyValue
 */

public class FuzzyVariable  implements Serializable
{
    /** The string name of the fuzzy variable */
    String name;
    /** The units (such as Degrees C) for the varible */
    String units;
    /** The Universe of Discourse (x value range) for the variable */
    private double[] UOD = new double[2];
    /** The fuzzy terms described as FuzzyValues are stored in this hash table */
    private Hashtable fuzzyTerms;

    /**
     * Create a FuzzyVariable (with no Fuzzy Terms) specifying units
     *
     * @param n the name of the fuzzy variable (such as temperature)
     * @param UODlower the lower limit of the Universe of Discourse
     * @param UODlower the upper limit of the Universe of Discourse
     * @param u the units for the variable (such as Degrees C)
     * @exception InvalidFuzzyVariableNameException
     * @exception InvalidUODRangeException
     */
    public FuzzyVariable( String n, double UODlower, double UODupper, String u)
        throws InvalidFuzzyVariableNameException, InvalidUODRangeException
    {
        /* need to check for lower < upper!!! and String must NOT be empty for the name!! */
        if (n.length() < 1) 
           throw new InvalidFuzzyVariableNameException("FuzzyVariable name must not be empty");
        name = n;
        if (UODlower >= UODupper)
           throw new InvalidUODRangeException("Universe of Discourse limits invalid");

        UOD[0] = UODlower;
        UOD[1] = UODupper;
        units = u;
        fuzzyTerms = new Hashtable();
    }

    /**
     * Create a FuzzyVariable (with no Fuzzy Terms) not specifying the units (defaults to empty string)
     *
     * @param n the name of the fuzzy variable (such as temperature)
     * @param UODlower the lower limit of the Universe of Discourse
     * @param UODlower the upper limit of the Universe of Discourse
     * @exception InvalidUODRangeException
     * @exception InvalidFuzzyVariableNameException
     */
    public FuzzyVariable( String n, double UODlower, double UODupper)
        throws InvalidFuzzyVariableNameException, InvalidUODRangeException
    {
        this(n, UODlower, UODupper, "");
    }

    /**
     * Create a FuzzyVariable (with Fuzzy Terms) specifying units
     *
     * @param n the name of the fuzzy variable (such as temperature)
     * @param UODlower the lower limit of the Universe of Discourse
     * @param UODlower the upper limit of the Universe of Discourse
     * @param u the units for the variable (such as Degrees C)
     * @param terms array of names of terms to be added to the FuzzyVariable
     * @param fuzzySets array of FuzzySets of terms to be added to the FuzzyVariable
     * @param numTerms number of terms to be added
     * @exception InvalidFuzzyVariableNameException
     * @exception InvalidUODRangeException
     * @exception XValueOutsideUODException
     */
    public FuzzyVariable( String n, double UODlower, double UODupper, String u,
                          String terms[], FuzzySet fuzzySets[], int numTerms
                        )
        throws InvalidFuzzyVariableNameException, InvalidUODRangeException,
               XValueOutsideUODException, InvalidFuzzyVariableTermNameException
    {
        this(n, UODlower, UODupper, u);
        
        for (int i=0; i<numTerms; i++)
        {
            addTerm(terms[i], fuzzySets[i]);
        }
    }

    /**
     * Create a FuzzyVariable (with Fuzzy Terms) not specifying the units (defaults to empty string)
     *
     * @param n the name of the fuzzy variable (such as temperature)
     * @param UODlower the lower limit of the Universe of Discourse
     * @param UODlower the upper limit of the Universe of Discourse
     * @param terms array of names of terms to be added to the FuzzyVariable
     * @param fuzzySets array of FuzzySets of terms to be added to the FuzzyVariable
     * @param numTerms number of terms to be added
     * @exception InvalidUODRangeException
     * @exception InvalidFuzzyVariableNameException
     * @exception XValueOutsideUODException
     */
    public FuzzyVariable( String n, double UODlower, double UODupper,
                          String terms[], FuzzySet fuzzySets[], int numTerms
                        )
        throws InvalidFuzzyVariableNameException, InvalidUODRangeException,
               XValueOutsideUODException, InvalidFuzzyVariableTermNameException
    {
        this(n, UODlower, UODupper, "", terms, fuzzySets, numTerms);
    }

 
    
    
    /**
     * Retrieve the FuzzyVariable's name.
     */
    public String getName(){
        return(name);
    }

    /**
     * Retrieve the FuzzyVariable's Units string
     */
    public String getUnits(){
        return(units);
    }

    /**
     * Retrieve the minimum value of the Universe of Discourse of the FuzzyVariable
     */
    public double getMinUOD(){
        return(UOD[0]);
    }

    /**
     * Retrieve the maximum value of the Universe of Discourse of the FuzzyVariable
     */
    public double getMaxUOD(){
        return(UOD[1]);
    }

    /**
     * Return the fuzzy value that represents a fuzzy term for the fuzzy variable.
     *
     * @param term the term name to be accessed (eg hot)
     * @return the FuzzyValue that represents the term or null if not a term
     */
    public FuzzyValue findTerm(String term)
    {
        return (FuzzyValue)fuzzyTerms.get(term.toLowerCase());
    }

    /**
     * Return an enumeration that contains the set of fuzzy values
     * that represent the fuzzy terms of the fuzzy variable.
     *
     * @return an enumeration of FuzzyValues that represent the terms 
     *          of the FuzzyVariable
     */
    public Enumeration findTerms()
    {
        return fuzzyTerms.elements();
    }

    /**
     * Return an enumeration that contains the names of the terms of the fuzzy variable.
     *
     * @return an enumeration of Strings that contain the names of the terms 
     *          of the FuzzyVariable
     */
    public Enumeration findTermNames()
    {
        return fuzzyTerms.keys();
    }

    /**
     * Removes the fuzzy term identified by the term name from the fuzzy variable.
     *
     * @param term the term name to be accessed (eg hot)
     * @return the FuzzyValue that represents the term that was removed or null if not a term
     */
    public FuzzyValue removeTerm(String term)
    {
        return (FuzzyValue)fuzzyTerms.remove(term.toLowerCase());
    }

    /**
     * Remove all terms from the fuzzy variable.
     *
     */
    public void removeTerms()
    {
        fuzzyTerms.clear();
    }


    /**
     * Support routine to do the actual adding of the term to the internal hashtable.
     * Will replace any existing term with the same name. Remember that linguistic
     * expressions are case insensitive.
     * <p>
     * All names for terms must not have spaces, ) or ( in them. They must
     * also not be AND or OR. They might be the same as some existing 
     * modifier and then would override the modifier (not generally 
     * a good thing to do).
     *
     * @param term the name of the term being added
     * @param fval the FuzzyValue that represents the term
     */
    private void addTheTerm( String term, FuzzyValue fval )
        throws InvalidFuzzyVariableTermNameException
    {
        /* store a fuzzy value in the Term Hashtable */
        if (fval != null)
        {
            String lcTerm = term.toLowerCase();
            // the term name must not have spaces, (, or ) in them
            // the term name must not be 'and' or 'or'
            if (lcTerm.equals("and")) throw new InvalidFuzzyVariableTermNameException("term name cannot be 'and'");
            if (lcTerm.equals("or"))  throw new InvalidFuzzyVariableTermNameException("term name cannot be 'or'");
            if (lcTerm.indexOf(' ') != -1) throw new InvalidFuzzyVariableTermNameException("term name cannot contain a space");
            if (lcTerm.indexOf('(') != -1) throw new InvalidFuzzyVariableTermNameException("term name cannot contain a '('");
            if (lcTerm.indexOf(')') != -1) throw new InvalidFuzzyVariableTermNameException("term name cannot contain a ')'");
            
            if (fuzzyTerms.get(lcTerm) != null)
               fuzzyTerms.remove(lcTerm);

            fuzzyTerms.put(lcTerm, fval);
            fval.setLinguisticExpression(term);
        }
    }

    /**
     * Add a term description to the FuzzyVariable. The description consists of
     * a term name and a fuzzy set.
     * If a term with the same name already exists it will replace
     * that term with this new one.
     *
     * @param term the name of the term being added
     * @param fset the FuzzySet that represents the term
     * @exception XValueOutsideUODException
     */
    public FuzzyValue addTerm( String term, FuzzySet fset )
        throws XValueOutsideUODException, InvalidFuzzyVariableTermNameException

    {
        FuzzyValue fval = new FuzzyValue(this, fset);         
        addTheTerm(term, fval);

        return fval;
    }

    /**
     * Add a term description to the FuzzyVariable. The description consists of
     * a term name and a fuzzy set described by 2 arrays.
     * If a term with the same name already exists it will replace
     * that term with this new one.
     *
     * @param term the name of the term being added
     * @param x the x values of a FuzzySet that represents the term
     * @param y the y values of a FuzzySet that represents the term
     * @param numPoints the number of points in these x and y arrays
     * @exception XValuesOutOfOrderException
     * @exception YValueOutOfRangeException
     * @exception XValueOutsideUODException
     */
    public FuzzyValue addTerm( String term, double[] x, double[] y, int numPoints )
       throws XValuesOutOfOrderException, YValueOutOfRangeException,
              XValueOutsideUODException, InvalidFuzzyVariableTermNameException
    {
        FuzzyValue fval = new FuzzyValue(this, x, y, numPoints);
        addTheTerm(term, fval);

        return fval;
    }

    /**
     * Add a term description to the FuzzyVariable. The description consists of
     * a term name and a fuzzy set described by an array of (x,y) points.
     * If a term with the same name already exists it will replace
     * that term with this new one.
     *
     * @param term the name of the term being added
     * @param xsetPoints the x,y pairs of a FuzzySet that represents the term
     * @param numPoints the number of points in the setPoints arrays
     * @exception XValuesOutOfOrderException
     * @exception YValueOutOfRangeException
     * @exception XValueOutsideUODException
     *
     * @see SetPoint
     */
    public FuzzyValue addTerm( String term, SetPoint[] setPoints, int numPoints )
       throws XValuesOutOfOrderException, YValueOutOfRangeException,
              XValueOutsideUODException, InvalidFuzzyVariableTermNameException
    {
        FuzzyValue fval =  new FuzzyValue(this, setPoints, numPoints);
        addTheTerm(term, fval);

        return fval;
    }

    /**
     * Add a term description to the FuzzyVariable. The description consists of
     * a term name and a fuzzy set described by a linguistic expression.
     * If a term with the same name already exists it will replace
     * that term with this new one.
     *
     * @param term the name of the term being added
     * @param linguisticExpr the english-like string that specifies a fuzzy value.
     *        Uses the existing defined terms of the FuzzyVariable along with
     *        defined fuzzy modifiers and the operators 'and' and 'or'.
     * @exception InvalidLinguisticExpressionException
     *
     * @see Modifiers
     */
    public FuzzyValue addTerm( String term, String linguisticExpr ) 
        throws InvalidLinguisticExpressionException, InvalidFuzzyVariableTermNameException
    {
        FuzzyValue fval = new FuzzyValue(this, linguisticExpr);
        addTheTerm(term, fval);

        return fval;
    }
    
    /**
     * Returns the String representation of the FuzzyVariable. 
     *
     * <p>For example:
     * <p><pre>
     * FuzzyVariable -> temperature [ 0.0, 100.0 ] C
     * Terms:
     *   hot -> { 0/25 1/35 }
     *   medium -> { 0/5 1/15 1/25 0/35 }
     *   cold -> { 1/5 0/15 }
     *   
     * 
     * </pre>
     *
     * @return the String representation of the FuzzyVariable 
     */
    
    public String toString(){
        String s;
        
        s = "FuzzyVariable -> " +
            this.getName() +
            " [ " + this.getMinUOD() +
            ", " + this.getMaxUOD() +
            " ] " + this.getUnits() + "\nTerms:\n";

        for (Enumeration e = fuzzyTerms.keys(); e.hasMoreElements();)
        {
            String key = (String)e.nextElement();
            s = s + "  " + key + " -> " + 
               ((FuzzyValue)(fuzzyTerms.get(key))).getFuzzySet() +
               "\n";
        }
        
        return(s);
    }    

    

}
