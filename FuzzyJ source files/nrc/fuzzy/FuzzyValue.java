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

import java.io.*;
import java_cup.runtime.Symbol;

/**
 * The <code>FuzzyValue</code> class represents a fuzzy value, which is an association of a
 * <B>FuzzyVariable</B>, a <B>FuzzySet</B>, and the <B>linguistic
 * expression</B> which describe the FuzzyValue. 
 *
 * <p>The FuzzyVariable provides a set of Terms that can be used to describe 
 * concepts being developed for the variable.
 * For example, temperature may be a fuzzy variable, with terms such as
 * hot, cold and warm being used to describe concepts such as 'hot or cold'.
 *
 * <p>The linguistic expression is formed using the terms of the FuzzyVariable
 * along with the operators 'or' and 'and' and the set of supplied
 * modifiers such as 'very', 'not' and 'slightly'. These english-like expressions 
 * are used to describe the required fuzzy concepts for the variable.
 *
 * <p>For example, let us consider the FuzzyVariable temperature, with terms  
 * <code>cold, warm, </code> and <code>hot</code>.  It is decided that we
 * would like a FuzzyValue that represents the concept "slightly cold AND warm"
 * for the temperature variable. This string is parsed, and based on the 
 * parse a suitable FuzzySet is created for the FuzzyValue, and the 
 * linguistic expression is stored as "slightly cold AND warm".
 * See the documentation for the FuzzyScanner class for more detailed 
 * information on the grammar and parsing. 
 *
 * <p>However, it is also possible to create a FuzzyValue by providing the 
 * data it requires to create its FuzzySet.  It this case, no linguistic 
 * expression has been used, and the <code>linguisticExpression</code>
 * is assigned the default String value which, unless changed, is "???".
 *
 * <p>The FuzzySet in a FuzzyValue contains a set of points.  This set of points 
 * is the mathematical representation of the fuzzy concept being expressed for 
 * this FuzzyValue. 
 * <p> The code below shows the creation of a FuzzyVariable and a FuzzyValue:
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
 * </code></pre><p>
 * NOTE: Although the range of allowed membership values is [0, 1], it
 * is possible to form FuzzySets of FuzzyValues with membership values > 1.0. There are
 * instances where this is desirable. For example in some approaches it is
 * preferred to collect the outputs of fuzzy rules by doing a 'fuzzySum' of the
 * resultant FuzzyValues (global contribution of rules). The fuzzySum operation
 * can result in FuzzySets with membership values > 1.0. When a FuzzyValue is created
 * with a FuzzyValue constructor (that does not use an existing FuzzyValue or an 
 * existing FuzzySet) the values are restricted to being between
 * 0 and 1. However, the fuzzySum operation and the FuzzySet methods appendSetPoint and
 * insertSetPoint can be used to override this restriction.
 * 
 *
 * @author Bob Orchard
 *
 * @see FuzzyVariable
 * @see FuzzySet
 * @see FuzzyScanner
 */
 
public class FuzzyValue implements java.lang.Cloneable, Serializable 
{
    /**
     * The default linguistic expression (normally set to '???') that is
     * used if no definitive and sensible linguistic expression exists.
     */
    static final private String DEFAULT_LE = "???";

    /**
     * Holds the FuzzyVariable associated with the FuzzyValue.  The fuzzy variable 
     * determines the context within which a fuzzy value has meaning. It provides
     * the language (the variable itself, such as temperature, as well as the
     * terms such as hot or cold, and the universe of discourse for the variable)
     * used to describe a fuzzy concept.
     */
    FuzzyVariable fuzzyVariable;
    
    /**
     * Contains the linguistic expression, or the english phrase which linguistically
     * describes the FuzzyValue.  For example, if the FuzzyVariable was temperature, the
     * linguistic term might be any of the following: cold, warm, hot, 
     * not cold AND somewhat warm, very warm AND more_or_less hot, etc.  The linguistic 
     * term very often defines the FuzzyValue, and the FuzzySet contained within the 
     * FuzzyValue is then created by applying the modifiers and operations in the 
     * linguistic expression to pre-existing terms.
     */
    String linguisticExpression;
    
    /**
     * Contains the FuzzySet, or the set of points that give the FuzzyValue
     * mathematical meaning.
     */
    FuzzySet fuzzySet;
    
    /**
     * This flag (normally false) when set to true will cause FuzzySets 
     * to be 'clipped' by vertical lines at the extreme X values of 
     * the Universe of Discourse (UOD) when a FuzzyValue is constructed.
     * This means that they will not raise the XValueOutsideUODException
     * exception since they have been fixed to make sure that no X values
     * lie outside the UOD. Users should be cautious when using this since 
     * it may result in unanticipated results. On the other hand it can
     * be useful in certain instances. Alternatively the user can just
     * apply the FuzzySet function confineToXBounds to any FuzzySet being
     * passed to the FuzzyValue constructor.
     * <p> Consider the PIFuzzySet that might be used to represent a value
     * that is close to some X. If X is too near one of the bounds of the 
     * UOD such that some, perhaps small, part of the PI fuzzy set lies outside
     * the UOD, then the creation of a fuzzy value using this fuzzy set will
     * fail with the XValueOutsideUODException exception. If this flag is set to
     * true before constructing the FuzzyValue then the FuzzySet will be modified
     * to lie within the UOD.
     * 
     * @see #isConfineFuzzySetsToUOD
     * @see #setConfineFuzzySetsToUOD
     */
     private static boolean confineFuzzySetsToUOD = false;

     /**
      * The defaultSimilarityOperator holds a SimilarityOperator that is used 
      * when the similarity method is called on a FuzzyValue. This can be changed with the 
      * SetDefaultSimilarityOperator method. The normal default operator is the
      * SimilarityByPossibilityOperator.
      */
      private static SimilarityOperator defaultSimilarityOperator = new SimilarityByPossibilityOperator();
      
    /**
     * This value is used to determine a threshold level for the matching of
     * fuzzy values. When used with a rule based system such as Jess and the
     * FuzzyJess extensions it influences the matching of fuzzy facts with 
     * fuzzy patterns in a rule's LHS. When fuzzy values are compared 
     * with the fuzzyMatch method, the comparison is considered to be successful
     * if there is any overlap (intersection) between the two fuzzy sets 
     * involved in the comparison. This allows the match to succeed only if
     * the maximum of the intersection set has a membership value greater than
     * or equal to this threshold. The default matchThreshold is 0.0. When the 
     * matchThreshold is 0.0 the maximum of the intersection set must be greater
     * than 0.0. 
     */

    private static double matchThreshold = 0.0;
    
    /**
     * Use this value with setEqualsStrength method to set a weak 'equals' compare of FuzzyValues.
     */
    public static final int WEAK_EQUALS = 0;
    
    /**
     * Use this value with setEqualsStrength method to set a strong 'equals' compare of FuzzyValues.
     */
    public static final int STRONG_EQUALS = 1;
    
    /**
     * A private value that is known only to developers who need to modify the way 
     * the method equals compares FuzzyValues
     */
    private static final int EQUALS_STRENGTH_PASSWORD = 314159; 
    
    /**
     * This value is used to determine how the equals method will determine
     * the equality of 2 FuzzyValues. If set to WEAK_EQUALS then the
     * FuzzyValues are considered to be equal if the have the same FuzzyVariable
     * associated with them. If set to STRONG_EQUALS (the default) then they must have
     * the same FuzzyVariable AND their FuzzySets must be equal as well.
     * <p>
     * NOTE: Only change this from the default in special cases and if you are 
     * confident of the ramifications of doing so. This is used in FuzzyJess
     * with care to deal with Global Contribution for FuzzyValues so that if it finds  
     * a fact (using findFactByFact) that has identical (equals) non-fuzzy slots and 
     * any slots with FuzzyValues have the same FuzzyVariable (WEAK equals) it can 
     * perform the Global Contribution operation. For this to happen the equals method 
     * is temporarily switched to do a weak compare ... equivalent to equalsStar.
     * Should be controlled using the setEqualsStrength and getEqualsStrength methods.
     */
    
    private static int EqualsStrength = STRONG_EQUALS; // default

    /*==================================================================
     *
     * CONSTRUCTORS
     *
     *================================================================*/

    /**
     * Constructs a FuzzyValue with the <code>fuzzyVariable</code> variable set equal
     * to the FuzzyVariable argument, and the <code>linguisticExpression</code> variable
     * initialized to the passed linguistic expression String argument.
     *
     * <p>To initialize the FuzzySet of the FuzzyValue, the linguistic expression 
     * argument is parsed and the respective modifiers and operators are applied to
     * the FuzzyValue terms refered to in the linguistic expression.  The result of
     * the modifier application and operations is then assigned to the FuzzySet of
     * this FuzzyValue.
     *
     * @param fuzzyVariable         the FuzzyVariable that the FuzzyValue belongs to,
     *                              or is defined in terms of.
     * @param linguisticExpression the linguistic expression defining the FuzzySet.
     *                              It is important that the linguistic expression parameter
     *                              contains no ambiguities in intention.  For example, 
     *                              the String "not cold AND very warm" could be interpreted
     *                              as either "(not cold) AND (very warm)", or 
     *                              "not (cold AND very warm)".  The use of parentheses is
     *                              of utmost importance, as the absence of parentheses required
     *                              to prevent ambiguities in meaning will lead to 
     *                              unpredictable results.
     *
     *                              <p>The parsing of the linguistic expression results in the
     *                              specified modifiers and operators applied to the specified terms.
     *                              The end product is the FuzzySet that belongs to the new FuzzyValue.
     * @exception InvalidLinguisticExpressionException if the parse/evaluation of the linguistic expression fails
     */

    public FuzzyValue(FuzzyVariable fuzzyVariable, String linguisticExpression) 
        throws InvalidLinguisticExpressionException
    {
        /* create a parsing object */
        FuzzyParser parser_obj = 
            new FuzzyParser(new FuzzyScanner(fuzzyVariable, linguisticExpression));
        FuzzyValue fval = null;
        
        try
        {
         fval = (FuzzyValue)(parser_obj.parse().value);
        }
        catch (Exception e)
        {
        	System.out.println("Parsing Linguistic Expression: " + linguisticExpression +
        	                   ", FuzzyVar = " + fuzzyVariable.toString());
          throw new InvalidLinguisticExpressionException(e + "\n Parsing Linguistic Expression: " + linguisticExpression);
        } 
        
        this.fuzzySet = fval.getFuzzySet();
        this.linguisticExpression = linguisticExpression;
        this.fuzzyVariable = fuzzyVariable;
        // note: no need to check for X values outside of UOD in this one
        //       since fuzzy set created from fuzzy terms of the fuzzy variable
    }

    /**
     * Constructs a new FuzzyValue object which has a reference to the FuzzyVariable argument
     * and which contains the FuzzySet argument.  Because the FuzzySet is already in existence
     * when the FuzzyValue is created, a linguistic expression is not required to build the 
     * FuzzySet.  Therefore, there is no reference which defines the FuzzySet linguistically, and 
     * as a result the linguistic expression of a FuzzyValue constructed in this a manner will be
     * the default linguistic expression, ie. "???".
     *
     * @param fuzzyVariable         the FuzzyVariable that the FuzzyValue belongs to,
     *                              or is defined in terms of.
     * @param fuzzySet              the FuzzySet to be assigned to the FuzzyValue
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     */

    public FuzzyValue(FuzzyVariable fuzzyVariable, FuzzySet fuzzySet)
        throws XValueOutsideUODException
    {
        if (confineFuzzySetsToUOD)
           try 
           {  fuzzySet.confineToXBounds(fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD());
           }
           catch (XValuesOutOfOrderException e)
           { // can't happen since min and max UOD already checked
           }
        else if (isAnyXValueOutsideUOD(fuzzySet, fuzzyVariable))
                throw new XValueOutsideUODException();
        this.linguisticExpression = DEFAULT_LE;
        this.fuzzyVariable = fuzzyVariable;
        this.fuzzySet = fuzzySet;
    }
    
    /**
     * Constructs a new FuzzyValue object from an existing FuzzyValue. This is basically a copy.
     *
     * @param fuzzyValue         the FuzzyValue to copy 
     *                              range of the universe of discourse
     */

    public FuzzyValue(FuzzyValue fuzzyValue)
    {
        this.linguisticExpression = fuzzyValue.linguisticExpression;
        this.fuzzyVariable = fuzzyValue.fuzzyVariable;
        this.fuzzySet = new FuzzySet(fuzzyValue.fuzzySet);
    }
    
    /**
     * Constructs a new FuzzyValue object which has a reference to the FuzzyVariable argument
     * and creates a FuzzySet from the two arrays of double values. Because the FuzzySet is 
     * created from double arrays, a linguistic expression is not required to build the 
     * FuzzySet.  Therefore, there is no reference which defines the FuzzySet linguistically, and 
     * as a result the linguistic expression of a FuzzyValue constructed in this a manner will be
     * the default linguistic expression, ie. "???". 
     *
     * @param fuzzyVariable the FuzzyVariable that the FuzzyValue belongs to,
     *                      or is defined in terms of.
     * @param x             the double array containing the x values of the points that are to 
     *                      constitute the FuzzySet.
     * @param y             the double array containing the y values of the points that are to 
     *                      constitute the FuzzySet.
     * @param numPoints     the number of points that are contained within the two arrays.
     *                      In other words, it would be logically expected that the length of the
     *                      x values array would be equal to the y values array, which would also
     *                      be equal to the number of points that are to be inserted into the FuzzySet.
     *
     * @exception XValuesOutOfOrderException if the x values in the x double array are not in
     *                      strictly ascending order.
     * @exception YValueOutOfRangeException if the y values are not within the range [0.0, 1.0]
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     */

    public FuzzyValue(FuzzyVariable fuzzyVariable, double[] x, double[] y, int numPoints) 
        throws XValuesOutOfOrderException, YValueOutOfRangeException,
               XValueOutsideUODException
    {
        this.linguisticExpression = DEFAULT_LE;
        this.fuzzyVariable = fuzzyVariable;
        this.fuzzySet = new FuzzySet(x, y, numPoints);
        if (confineFuzzySetsToUOD) 
           try 
           {  fuzzySet.confineToXBounds(fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD());
           }
           catch (XValuesOutOfOrderException e)
           { // can't happen since min and max UOD already checked
           }
        else if (isAnyXValueOutsideUOD(fuzzySet, fuzzyVariable))
                throw new XValueOutsideUODException();

    }
        
    /**
     * Constructs a new FuzzyValue object which has a reference to the FuzzyVariable argument
     * and creates a FuzzySet from the array of SetPoints. Because the FuzzySet is 
     * created from a SetPoint array, a linguistic expression is not required to build the 
     * FuzzySet.  Therefore, there is no reference which defines the FuzzySet linguistically, and 
     * as a result the linguistic expression of a FuzzyValue constructed in this a manner will be
     * the default linguistic expression, ie. "???". 
     *
     * @param fuzzyVariable the FuzzyVariable that the FuzzyValue belongs to,
     *                      or is defined in terms of.
     * @param setPoints     the array of SetPoints that are to constitute the FuzzySet.
     * @param numPoints     the number of points that are contained in the SetPoint array.
     *
     * @exception XValuesOutOfOrderException if the x values in the x double array are not in
     *                      strictly ascending order.
     * @exception YValueOutOfRangeException if the y values are not within the range [0.0, 1.0]
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     */

    public FuzzyValue(FuzzyVariable fuzzyVariable, SetPoint[] setPoints, int numPoints) 
        throws XValuesOutOfOrderException, YValueOutOfRangeException,
               XValueOutsideUODException
    {
        this.linguisticExpression = DEFAULT_LE;
        this.fuzzyVariable = fuzzyVariable;
        this.fuzzySet = new FuzzySet(setPoints, numPoints);
        if (confineFuzzySetsToUOD) 
           try 
           {  fuzzySet.confineToXBounds(fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD());
           }
           catch (XValuesOutOfOrderException e)
           { // can't happen since min and max UOD already checked
           }
        else if (isAnyXValueOutsideUOD(fuzzySet, fuzzyVariable))
                throw new XValueOutsideUODException();
    }


    /*************************************************************************************
     *
     * METHODS
     *
     *************************************************************************************/

    /**
     * Control how to compare two FuzzyValues for fuzzy equality with the 'equals' method. Normally for 
     * the two FuzzyValues to be 'equal' (using the equals method), they must 
     * have the same FuzzyVariable and their fuzzy sets must also be equal. 
     * Note that this 'definition' of equality is useful and natural in most contexts. However,
     * sometimes a weaker condition is useful (where they just need to have the same FuzzyVariable).
     * This is true in the FuzzyJess implementation so that Global contribution can be done
     * automatically. In this case we want to combine facts that have identical non-fuzzy slots and 
     * fuzzy slots with the same FuzzyVariable. Since Jess uses the 'equals' method to compare the slots
     * when retrieving facts (findFactByFact) we can temporarily change the behaviour of
     * the FuzzyValue equals method to do a weak comparison. This should only be used with
     * care since it can have a considerable effect. Normally one would use the equalsStar
     * method to do a weak compare. 
     * <p>
     * So the actual compare done by equals can be changed with the method setEqualsStrength. 
     * Normally this will be a temporary change and it should be done in a synchronized manner.
     * E.g.:
     * <p>
     * <code>
     * synchronized (this)
     * {
     *   int currentStrength = FuzzyValue.getEqualsStrength();
     *   FuzzyValue.setEqualsStrength(FuzzyValue.WEAK_EQUALS, PASSWORD);
     *   ...
     *   some code that requires a WEAK compare temporarily 
     *   ...
     *   FuzzyValue.setEqualsStrength(currentStrength, PASSWORD);
     * }
     * </code>
     * <p>
     * The PASSWORD is private and only know to developers with access to the code. In
     * other words it is not to be used in general. If the FuzzyJess code were not in a 'separate'
     * package (nrc.fuzzy.jess) and were in the nrc.fuzzy package all of this would have been
     * nicely hidden from the general user in private methods.
     * 
     * @param strength should be one of FuzzyValue.WEAK_EQUALS or FuzzyValue.STRONG_EQUALS. If
     *        it is neither of these then nothing will be done.
     * @param equalsStrengthPassword should be the required password to use this method. If 
     *        it is incorrect then nothing will be done.
     * @return none
     */

    public static void setEqualsStrength(int strength, int equalsStrengthPassword) 
    {
        if (equalsStrengthPassword == EQUALS_STRENGTH_PASSWORD &&
            (strength == WEAK_EQUALS || strength == STRONG_EQUALS) 
           )
           EqualsStrength = strength;
    }

    /**
     * Returns the value of the current setting for the strength with which the equals method
     * compares FuzzyValues. Will always be one of FuzzyValue.WEAK_EQUALS or FuzzyValue.STRONG_EQUALS
     * and except in exceptional circumstances will be the default, FuzzyValue.STRONG_EQUALS.
     * @return returns FuzzyValue.WEAK_EQUALS or FuzzyValue.STRONG_EQUALS.
     */
    public static int getEqualsStrength() 
    {
        return EqualsStrength;
    }


    /**
     * Compare two FuzzyValues for a fuzzy equality. For the two FuzzyValues to
     * be 'equal' they must have the same FuzzyVariable and their fuzzy sets must also be equal. 
     * Note that this 'definition' of equality is useful and natural in most contexts. 
     * <p>
     * However, in some exceptional circumstances a weaker condition is useful 
     * (they just need to have the same FuzzyVariable).
     * This is true in the FuzzyJess implementation so that Global contribution can be done
     * automatically. In this case we want to combine facts that have identical non-fuzzy slots and 
     * fuzzy slots with the same FuzzyVariable. Since Jess uses the 'equals' method to compare the slots
     * when retrieving facts (with findFactByFact) we can temporarily change the behaviour of
     * the FuzzyValue equals method to do a weak comparison. This should only be used with
     * care since it can have a considerable effect. Normally one would use the equalsStar
     * method to do a weak compare. See setEqualsStrength and getEqualsStrength. 
     * <p>
     * NOTE: This is a change in version 1.10 and later versions of FuzzyJ (previously 
     * equals did a weak compare).
     *
     * @param v the object to compare to the FuzzyValue.
     * @return true if the FuzzyValues are associated with the same FuzzyVariable and
     * identical FuzzySets (default) OR just the same FuzzyVariable (depends on the setting
     * of EqualsStrength).
     */

    public boolean equals(Object v) 
    {
     if (v instanceof FuzzyValue)
     {
    	FuzzyVariable fvFVar = ((FuzzyValue) v).getFuzzyVariable();
        if (EqualsStrength == WEAK_EQUALS)  
            return fuzzyVariable.equals(fvFVar);
        else // any other value for now is STRONG
			return (fuzzyVariable.equals(fvFVar) && 
					(((FuzzyValue) v).getFuzzySet()).equals(this.fuzzySet) 
				   );
     }
     else
        return false;
    }

    /**
     * An alternative weak method for comparing two FuzzyValues. To be 'equalsStar' the two FuzzyValues  
     * must only have the same FuzzyVariable. This may have use in some limited
     * circumstances. Use this method rather than trying to change the way equals compares
     * the FuzzyValues with setEqualsStrength and getEqualsStrength.
     * <p>
     * NOTE: This is a change in version 1.10 and later versions of FuzzyJ (previously 
     * equalsStar did a strong compare).
     *
     * @see nrc.fuzzy.FuzzySet
     *
     * @param v the object to compare to the FuzzyValue.
     * @return true if the FuzzyValues are associated with the same FuzzyVariable.
     */

    public boolean equalsStar(Object v) 
    {
     if (v instanceof FuzzyValue)
     {
        return (fuzzyVariable.equals(((FuzzyValue) v).getFuzzyVariable()));
      }
      else
         return false;
    }


    /**
     * Return a hashcode value for the FuzzyValue.
     * 
     * @return an integer hashCode value determined by the FuzzyVariable
     *         associated with the FuzzyValue. This is consistent with
     *         equals method.
     */
    public int hashCode() 
    {
         return fuzzyVariable.hashCode();
    }


    /**
     * Returns a copy of this FuzzyValue.
     *
     * @return a new FuzzyValue object which is the copy of this FuzzyValue.
     * @exception CloneNotSupportedException if this class does not implement the Cloneable
     *                                       interface (which it does, so don't worry about it).
     */
     
    public FuzzyValue copyFuzzyValue() throws CloneNotSupportedException 
    {
        return((FuzzyValue)this.clone());
    }

    /**
     * Modifies the linguistic expression when a unary operation
     * (such as very, more_or_less, etc.) is applied to produce
     * a new fuzzy value. If the linguistic expression is the 
     * default linguistic expression (for unknown linguistic 
     * values -- normally ???) then just leave it as the default 
     * linguistic expression.
     *
     * @param operator the operator (very, more_or_less, etc.) that was applied
     * @param oldLinguisticExpression the linguistic expression from the fuzzy value that was modified
     */

    public void unaryModifyLinguisticExpression(String operator, String oldLinguisticExpression)
    {
        if (oldLinguisticExpression != DEFAULT_LE)
           this.linguisticExpression = operator + " (" + oldLinguisticExpression + ")";
    }

    /**
     * Modifies the linguistic expression when a binary operation
     * (such union or intersection) of 2 fuzzy values is done to produce
     * a new fuzzy value. If either of the linguistic expressions is the 
     * default linguistic expression (for unknown linguistic 
     * values -- normally ???) then just set if to the default 
     * linguistic expression.
     *
     * @param op the operator (and, or, etc.) that was applied
     * @param oldLinguisticExpression1 the linguistic expression from the 1st fuzzy value
     * @param oldLinguisticExpression2 the linguistic expression from the 2nd fuzzy value
     */

    public void binaryModifyLinguisticExpression
       (String op, String oldLinguisticExpression1, String oldLinguisticExpression2)
    {
        if (oldLinguisticExpression1 == DEFAULT_LE || oldLinguisticExpression2 == DEFAULT_LE)
           this.linguisticExpression =  DEFAULT_LE;
        else
           this.linguisticExpression 
               = "(" + oldLinguisticExpression1 +
                 ") " + op + " (" + oldLinguisticExpression2 + ")";
    }

    /**
     * Assigns a new FuzzySet to the FuzzyValue. Note that
     * the linguistic expression is no longer valid and will be set to
     * the DEFAULT_LE. Also NOTE that this method will likely be removed 
     * ... fuzzyValues should be created with constructors and the unary
     * (modifiers) and binary (union, intersection etc.) supplied.
     *
     * @param fuzzySet the FuzzySet to be assigned.
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     */

    public void assignFuzzySet(FuzzySet fuzzySet)
        throws XValueOutsideUODException
    {
        if (confineFuzzySetsToUOD) 
           try 
           {  fuzzySet.confineToXBounds(fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD());
           }
           catch (XValuesOutOfOrderException e)
           { // can't happen since min and max UOD already checked
           }
        else if (isAnyXValueOutsideUOD(fuzzySet, this.fuzzyVariable))
             throw new XValueOutsideUODException();
        this.fuzzySet = fuzzySet;
        this.linguisticExpression = DEFAULT_LE;
    }
    
    /************************************************************************************
     *
     * METHODS WHICH CALL FUZZYSET METHODS
     *
     ************************************************************************************/


    /**
     * Takes the complement of the <code>FuzzyValue</code>. 
     * More specifically, it takes the compliment of the y values of the SetPoints of the FuzzySet.
     * Mathematically (NOT), u(x) = 1 - u(x), or y = 1 - y.
     * <p>
     * This assumes that all of the membership values of the fuzzy set
     * are <= 1.0 and >= 0.0. If values lie outside this then values >1.0
     * will be set to 0.0 and those < 0.0 will be set to 1.0
     * 
     * @return a new FuzzyValue object that represents the complement of this FuzzyValue.
     */

    public FuzzyValue fuzzyComplement()
    {   
        FuzzyValue fv = null;
        try 
        { fv =  new FuzzyValue(this.fuzzyVariable, fuzzySet.fuzzyComplement());
        }
        catch (XValueOutsideUODException e)
        {} // can't happen!!
        
        fv.unaryModifyLinguisticExpression("not", this.linguisticExpression);
        return(fv);
    }


    /**
     * Scale this <code>FuzzyValue</code>.
     *
     * Returns the FuzzyValue which is scaled by:
     * <br><pre>
     *    yvalue/maxYValueOfSet   
     * </pre><br>
     * Effectively this adjusts the set so that the maximum membership 
     * value of the set has the value y and all other membership values
     * are scaled accordingly. If the membership values are all less than y
     * then a copy of the FuzzySet is returned (ie. it scales values down 
     * but never up). 
     *
     * @param y scaling factor
     * @return a newly constructed FuzzySet containing the scaled version of this FuzzySet
     */
    public FuzzyValue fuzzyScale( double y )
    {   
        FuzzyValue fv;
        
        try
        { fv =  new FuzzyValue(this.fuzzyVariable, fuzzySet.fuzzyScale(y));
          fv.linguisticExpression = DEFAULT_LE;
          return(fv);
        }
        catch (XValueOutsideUODException e)
        // cannot happen -we are using this FuzzyValue and scaling doesn't change x values
        { return(this); 
        }
     }


    /**
     * Normalizes the <code>FuzzyValue</code>. 
     * Normalization involves identifying the point in the FuzzyValue
     * with the highest membership value and multiplying all the membership values
     * in the FuzzyValue by a scale factor such that this highest point then has
     * a membership value of 1.0.
     *
     * @return a new FuzzyValue object that represents the normalization of this FuzzyValue.
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     */

    public FuzzyValue fuzzyNormalize()
        throws XValueOutsideUODException
    {   
        FuzzyValue fv =  new FuzzyValue(this.fuzzyVariable, fuzzySet.fuzzyNormalize());
        fv.unaryModifyLinguisticExpression("norm", this.linguisticExpression);
        return(fv);
    }


    /**
     * Returns the support of this FuzzyValue.  The support set of a FuzzyValue (FuzzySet)
     * is the set of x values that have a membership value other than zero. Note that 
     * this is identical to a STRONG alpha cut with alpha value of 0.
     *
     * @return an IntervalVector object which represents the support set of this FuzzyValue.
     */

    public IntervalVector getSupport(){
        return(fuzzySet.getSupport(fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD()));
    }

    /**
     * Returns the alpha cut of the FuzzyValue at the membership level specified by
     * the <code>alpha</code> argument.  Formally, a distinction is made betweeen two 
     * types of alpha cuts, the <i>strong</i> and the <i>weak</i> alpha cut.
     *
     * <p>The alpha cut of a FuzzyValue (FuzzySet) is the set of all x values in the
     * Universe of Discourse for which the membership value of the FuzzyValue (FuzzySet)
     * is greater than (STRONG cut) or greater than or equal to (WEAK cut) 
     * the <code>alpha</code> argument.
     *
     * @param cutType Parameters.STRONG for a strong alpha cut or Parameters.WEAK for a weak alpha cut
     * @param alpha the double membership value at which the alpha cut is taken.  The
     *              resulting set is the set of all elements for which the 
     *              membership value is greater than or equal to the alpha value.
     *
     * @return      the IntervalVector object which represents the alpha cut of this FuzzyValue
     *              at the double value of the <code>alpha</code> argument.
     *
     */

    public IntervalVector getAlphaCut(boolean cutType, double alpha) {
            
        return(fuzzySet.getAlphaCut(cutType, alpha, fuzzyVariable.getMinUOD(), fuzzyVariable.getMaxUOD()));
    }

    /**
     * Returns the membership value of the FuzzyValue at the specified x value.
     *
     * @param x the double x value at which the membership value requested will
     *          be interpolated
     *
     * @return  the value -1.0 if the FuzzySet of this FuzzyValue has zero points;
     *          otherwise, the membership value of the FuzzySet interpolated at
     *          the given x value
     *
     * @exception XValueOutsideUODException if the x value passed to the
     *            method is outside the Universe of Discourse for this
     *            FuzzyValue (ie. for the FuzzyVariable to which this
     *            FuzzyValue belongs)
     */

    public double getMembership(double x) 
        throws XValueOutsideUODException
    {
            if (isXValueOutsideUOD(x, this.fuzzyVariable)){
                throw new XValueOutsideUODException("Not a valid x value.");
            } else {
                return(fuzzySet.getMembership(x));
            }
    }

    /**
     * Returns the 1st X value with the specified membership value in the FuzzySet
     * This is done by interpolation. Note that if there are
     * multiple x values with this membership value 
     * then the only the 1st X value is returned.
     * <br>
     * Note that this is most often used to get the X value corresponding to
     * a membership value in a FuzzySet that is strictly increasing from 0.0 to 1.0
     * or strictly decreasing from 1.0 to 0.0 (e.g. an SFuzzySet or a ZFuzzySet).
     *
     * @param membership  the membership value at which to find the
     *          X value of the FuzzySet
     * @return  the 1st X value of the FuzzySet with the specified membership value
     *          or exception if the set does not have this membership value
     *
     */

    public double getXforMembership(double m) 
           throws NoXValueForMembershipException
    {
        return(fuzzySet.getXforMembership(m));
    }


    /**
     * Returns the horizontal intersection of the FuzzyValue at the
     * membership level specified by the y parameter.  Please see the
     * diagram below for a visual depiction. If y is < 0 then y is set to 0.
     * <p>
     * <img src="NetGraphics/horizontalIntersection.gif">
     *
     * @param y the y value with which the set is being intersected
     *
     * @return a new FuzzyValue object representing the horizontal
     *         intersection for the FuzzyValue with the y value
     *         argument
     */

    public FuzzyValue horizontalIntersection(double y) 
    {   
        FuzzyValue fv;

        try
        { fv =  new FuzzyValue(this.fuzzyVariable, fuzzySet.horizontalIntersection(y)); }
        catch (XValueOutsideUODException e)
        { return null; } // cannot happen since fuzzy value is already OK
        
        fv.linguisticExpression = DEFAULT_LE;
        return(fv);
    }

    /**
     * Returns the horizontal union of the FuzzySet at the
     * membership level specified by the y parameter.  Please see the
     * diagram below for a visual depiction.
     * If y is < 0 then y is set to 0.
     * <p>
     * <img src="NetGraphics/horizontalUnion.gif">
     *
     * @param y the y value with which the set is being unioned
     *
     * @return a new FuzzySet object representing the horizontal
     *         union for the FuzzySet with the y value
     *         argument
     */
    public FuzzyValue horizontalUnion(double y) 
    {   
        FuzzyValue fv;

        try
        { fv =  new FuzzyValue(this.fuzzyVariable, fuzzySet.horizontalUnion(y)); }
        catch (XValueOutsideUODException e)
        { return null; } // cannot happen since fuzzy value is already OK
        
        fv.linguisticExpression = DEFAULT_LE;
        return(fv);
    }


    /**
     * Perform the fuzzy intersection of 2 fuzzy values. 
     * The fuzzy variables of the 2 fuzzy values must
     * be the same or the operation is not valid.
     * For a visual depiction of the intersection set and more information, please see the
     * documentation for the FuzzySet method <code>fuzzyIntersection(FuzzySet otherSet)</code>.
     * @param linguisticExpression a linguistic expression that defines the other fuzzyValue
     *                   to be used in the intersection operation.
     * @return a new FuzzyValue that is the intersection of this FuzzyValue and the one
     *                              passed as a parameter
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
     */

    public FuzzyValue fuzzyIntersection(String linguisticExpression) 
           throws InvalidLinguisticExpressionException, XValueOutsideUODException, IncompatibleFuzzyValuesException
    {
        FuzzyValue fval = new FuzzyValue( fuzzyVariable, linguisticExpression );
        return(fuzzyIntersection(fval));
    }


    /**
     * Perform the fuzzy intersection of 2 fuzzy values. 
     * The fuzzy variables of the 2 fuzzy values must
     * be the same or the operation is not valid.
     * For a visual depiction of the intersection set and more information, please see the
     * documentation for the FuzzySet method <code>fuzzyIntersection(FuzzySet otherSet)</code>.
     * @param otherValue the 2nd FuzzyValue for the operation
     * @return a new FuzzyValue that is the intersection of this FuzzyValue and the one
     *                              passed as a parameter
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public FuzzyValue fuzzyIntersection(FuzzyValue otherValue)
        throws XValueOutsideUODException, IncompatibleFuzzyValuesException
    {
        if (this.fuzzyVariable != otherValue.fuzzyVariable)
           throw new IncompatibleFuzzyValuesException("FuzzyValues must have the same FuzzyVariables to do an Intersection");
           
        FuzzyValue fv = new FuzzyValue(this.fuzzyVariable,
                                       this.fuzzySet.fuzzyIntersection(otherValue.fuzzySet));
        
        fv.binaryModifyLinguisticExpression("and", 
                                            this.linguisticExpression, 
                                            otherValue.linguisticExpression);
        return(fv);
    }

	/**
	 * Perform the fuzzy union of 2 fuzzy values. 
	 * The fuzzy variables of the 2 fuzzy values must
	 * be the same or the operation is not valid.
	 * For a visual depiction of the union and more information, please see the
	 * documentation for the FuzzySet method <code>fuzzyUnion(FuzzySet otherSet)</code>.
	 * @param linguisticExpression a linguistic expression that defines the other fuzzyValue
	 *                   to be used in the union operation.
	 * @return a new FuzzyValue that is the union of this FuzzyValue and the one
	 *                              passed as a parameter
	 * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
	 *                              range of the universe of discourse
	 * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
	 *                              identical fuzzy variables the operation cannot be done
	 * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
	 */

	public FuzzyValue fuzzyUnion(String linguisticExpression) 
		   throws InvalidLinguisticExpressionException, XValueOutsideUODException, IncompatibleFuzzyValuesException
	{
		FuzzyValue fval = new FuzzyValue( fuzzyVariable, linguisticExpression );
		return(fuzzyUnion(fval));
	}

	/**
	 * Perform the fuzzy union of 2 fuzzy values. 
	 * The fuzzy variables of the 2 fuzzy values must
	 * be the same or the operation is not valid.
	 * For a visual depiction of the union and more information, please see the
	 * documentation for the FuzzySet method <code>fuzzyUnion(FuzzySet otherSet)</code>.
	 * @param otherValue the 2nd FuzzyValue for the operation
	 * @return a new FuzzyValue that is the union of this FuzzyValue and the one
	 *                              passed as a parameter
	 * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
	 *                              range of the universe of discourse
	 * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
	 *                              identical fuzzy variables the operation cannot be done
	 */

	public FuzzyValue fuzzyUnion(FuzzyValue otherValue) 
		   throws XValueOutsideUODException, IncompatibleFuzzyValuesException
	{
		if (this.fuzzyVariable != otherValue.fuzzyVariable)
		   throw new IncompatibleFuzzyValuesException("FuzzyValues must have the same FuzzyVariables to do a union");
           
		FuzzyValue fv = new FuzzyValue(this.fuzzyVariable,
									   this.fuzzySet.fuzzyUnion(otherValue.fuzzySet));

		fv.binaryModifyLinguisticExpression("or", 
											this.linguisticExpression, 
											otherValue.linguisticExpression);
		return(fv);
	}

	/**
	 * Returns the sum of this FuzzyValue with the FuzzyValue argument.
	 * The sum of 2 fuzzy sets is a set where the membership (y) value at every x 
	 * position is the sum of the memberships values of the two sets
	 * at the corresponding x values. <br>
	 * The fuzzy variables of the 2 fuzzy values must
	 * be the same or the operation is not valid.
	 * <p>
	 * NOTE WELL: The sum can lead to FuzzySets with membership values 
	 * greater than 1.0. This is sometimes used to collect 
	 * output of fuzzy rules (global contribution) rather than doing the union.
	 * 
	 * @param linguisticExpression a linguistic expression that defines the other fuzzyValue
	 *                   to be used in the sum operation.
	 * @return a new FuzzyValue that is the sum of this FuzzyValue and the one
	 *                              passed as a parameter
	 * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
	 *                              range of the universe of discourse
	 * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
	 *                              identical fuzzy variables the operation cannot be done
	 * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
	 */

	public FuzzyValue fuzzySum(String linguisticExpression) 
		   throws InvalidLinguisticExpressionException, XValueOutsideUODException, IncompatibleFuzzyValuesException
	{
		FuzzyValue fval = new FuzzyValue( fuzzyVariable, linguisticExpression );
		return(fuzzySum(fval));
	}

	/**
	 * Returns the sum of this FuzzyValue with the FuzzyValue argument.
	 * The sum of 2 fuzzy sets is a set where the membership (y) value at every x 
	 * position is the sum of the memberships values of the two sets
	 * at the corresponding x values. <br>
	 * The fuzzy variables of the 2 fuzzy values must
	 * be the same or the operation is not valid.
	 * <p>
	 * NOTE WELL: The sum can lead to FuzzySets with membership values 
	 * greater than 1.0. This is sometimes used to collect 
	 * output of fuzzy rules (global contribution) rather than doing a union.
	 * 
	 * @param otherValue the 2nd FuzzyValue for the sum operation.
	 * 
	 * @return a new FuzzyValue that is the sum of this FuzzyValue and the one
	 *                              passed as a parameter
	 * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
	 *                              range of the universe of discourse
	 * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
	 *                              identical fuzzy variables the operation cannot be done
	 */

	public FuzzyValue fuzzySum(FuzzyValue otherValue) 
		   throws XValueOutsideUODException, IncompatibleFuzzyValuesException
	{
		if (this.fuzzyVariable != otherValue.fuzzyVariable)
		   throw new IncompatibleFuzzyValuesException("FuzzyValues must have the same FuzzyVariables to do a sum");
           
		FuzzyValue fv = new FuzzyValue(this.fuzzyVariable,
									   this.fuzzySet.fuzzySum(otherValue.fuzzySet));

		fv.binaryModifyLinguisticExpression("sum", 
											this.linguisticExpression, 
											otherValue.linguisticExpression);
		return(fv);
	}

    /**
     * Find the minimum membership values between 2 FuzzyValues at all x values
     * and then find the maximum of these minimums (in effect take the intersection
     * of the 2 FuzzyValues and find the maximum membership value of this).
     * @param linguisticExpression a linguistic expression that defines the other fuzzyValue
     *                   to be compared to this FuzzyValue
     * @return the maximum membership value of the intersection between the 2 FuzzyValues
     * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public double maximumOfIntersection(String linguisticExpression)
        throws InvalidLinguisticExpressionException, IncompatibleFuzzyValuesException
    {
        FuzzyValue fval = new FuzzyValue( fuzzyVariable, linguisticExpression );
        return(maximumOfIntersection(fval));
    }

    /**
     * Find the minimum membership values between 2 FuzzyValues at all x values
     * and then find the maximum of these minimums (in effect take the intersection
     * of the 2 FuzzyValues and find the maximum membership value of this).
     * @param otherValue the 2nd FuzzyValue for the operation
     * @return the maximum membership value of the intersection between the 2 FuzzyValues
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public double maximumOfIntersection(FuzzyValue otherValue)
        throws IncompatibleFuzzyValuesException
    {
        if (this.fuzzyVariable != otherValue.fuzzyVariable)
           throw new IncompatibleFuzzyValuesException("FuzzyValues must have the same FuzzyVariables to do a MaxMin calculation");
           
        return(fuzzySet.maximumOfIntersection(otherValue.fuzzySet));
    }

     /**
      *
      *  Moment defuzzification defuzzifies a fuzzy set returning a
      *  floating point (double value) that represents the fuzzy set.
      *
      *  It calculates the first moment of area of a fuzzy set about the
      *  y axis.  The set is subdivided into different shapes by partitioning
      *  vertically at each point in the set, resulting in rectangles, 
      *  triangles, and trapezoids.  The centre of gravity (moment) and area of
      *  each subdivision is calculated using the appropriate formulas
      *  for each shape.  The first moment of area of the whole set is
      *  then:
      *  <b><pre>
      *      sum of ( moment(i) * area(i) )          <-- top
      *      ------------------------------
      *          sum of (area(i))                    <-- bottom
      *  </pre></b>
      *  If the total area is 0 then throw an exception since the moment
      *  is not defined.
      *
      * @return the floating value that is the first moment of the fuzzy set
      *
      * @exception InvalidDefuzzifyException there is no valid moment for the fuzzy set
      *                 (generally the area is 0 under the fuzzy set graph)
      * @exception XValuesOutOfOrderException occurs when the MinUOD is
      *                 greater than or equal to the MaxUOD parameter (this
      *                 should never happen).
      */   
    public double momentDefuzzify()
        throws InvalidDefuzzifyException, XValuesOutOfOrderException
    {
        return fuzzySet.momentDefuzzify(getMinUOD(), getMaxUOD());
    }

    /**
     *  Center of Area (COA) defuzzification defuzzifies a fuzzy set returning a
     *  floating point (double value) that represents the fuzzy set.
     *
     *  It calculates the x value that spilts the fuzzy set so that there
     *  is an equal area on either side of the x value.
     *  The set is subdivided into different shapes by partitioning
     *  vertically at each point in the set, resulting in rectangles, 
     *  triangles, and trapezoids.  
     *  <br>
     *  If the total area is 0 then throw an exception since the center
     *  of area is not defined.
     *
     * @return the floating value that is the center of area (COA) of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there is no valid COA for the fuzzy set
     *                 (generally the area is 0 under the fuzzy set graph)
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *                 greater than or equal to the xMax parameter.
     */   
   public double centerOfAreaDefuzzify()
       throws InvalidDefuzzifyException, XValuesOutOfOrderException
   {
    return fuzzySet.centerOfAreaDefuzzify(getMinUOD(), getMaxUOD());
   }


    /**
     *  Finds the weighted average of the values of a fuzzy set as the 
     *  defuzzification value. This is slightly different than the 
     *  maximumDefuzzify since tha maximumDefuzzify uses only points that
     *  have the same membership value (the one that is the maximum in 
     *  the set of points). The weightedAverageDefuzzify uses all of the 
     *  points with non-zero membership values and calculates the average 
     *  of the x values using the membership values as weights in this average.
     * 
     *  NOTE: This doesn't always work well because there can be x ranges
     *         where the y value is constant at the max value and other 
     *         places where the max is only reached for a single x value.
     *         When this happens the single value gets too much of a say
     *         in the defuzzified value. So use moment defuzzify in cases
     *         like this.
     *
     * <br><pre>
     *             /------\                   /\
     *            /        \                 /  \
     *         --/          \---------------/    \-------------
     *                 ^       ^
     *                 |       |
     *                 |       | gives this as the weighted average
     *                 | this is more reasonable???
     *
     * <br></pre>
     * However, it is especially effective if the output has a number of singleton
     * points in it. This might be, for example, when one has only singleton
     * values describing the output fuzzy values (Takagi-Sugeno-Kang zero order type rules).
     * In this case one gets a result that would be expected and one that would 
     * result in an exception with the momentDefuzzify (area is 0) and a poor 
     * result with the maximumDefuzzify (since only the values with the max 
     * membership are used - 20 in the example below).
     *
     * <br><pre>
     *    
     *      1.0                 |
     *                          |
     *      0.5        |        |
     *                 |        |        |
     *         --------|--------|--------|--------
     *         0      10        20       30      40
     *                        ^
     *                        |
     *                        | gives 18.57 as the weighted average   
     * 
     *      (10*0.5 + 20*1 + 30*0.25) / (0.5 + 1 + 0.25) = 18.57
     *
     * <br></pre>
     * Centre of gravity (moment) defuzzify is likely more useful most of the time.
     *       
     * @return the floating value that is the weighted average of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     */
     
    public double weightedAverageDefuzzify()
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
        return fuzzySet.weightedAverageDefuzzify(getMinUOD(), getMaxUOD());
    }

    /**
     *  Finds the mean of maxima of a fuzzy set as the defuzzification value.
     *   
     *  NOTE: This doesn't always work well because there can be x ranges
     *         where the y value is constant at the max value and other 
     *         places where the max is only reached for a single x value.
     *         When this happens the single value gets too much of a say
     *         in the defuzzified value. So use moment defuzzify in cases
     *         like this.
     *
     * <br><pre>
     *             /------\                   /\
     *            /        \                 /  \
     *         --/          \---------------/    \-------------
     *                 ^       ^
     *                 |       |
     *                 |       | gives this as the mean of maximum
     *                 | this is more reasonable???
     *
     * <br></pre>
     * Centre of gravity (moment) defuzzify is likely more useful most of the time.
     *       
     * @return the floating value that is the mean of the maximum values
     *          of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     */
     
    public double maximumDefuzzify()
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
        return fuzzySet.maximumDefuzzify(getMinUOD(), getMaxUOD());
    }

    /**
     * Determine if 2 FuzzyValues 'match' or intersect with a membership
     * of at least that specified by the FuzzyValue class variable matchThreshold.
     *
     * @param linguisticExpression a linguistic expression that defines the other fuzzyValue
     *                   to be compared to this FuzzyValue
     * @return true if the fuzy values overlap else false
     * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public boolean fuzzyMatch(String linguisticExpression)
        throws InvalidLinguisticExpressionException, IncompatibleFuzzyValuesException
    {
        return fuzzyMatch(linguisticExpression, matchThreshold);
    }
    
    /**
     * Determine if 2 FuzzyValues 'match' or intersect with a membership
     * of at least that specified by parameter 'threshold'. 
     * The threshold is restricted to a value between 0 and 1.
     *
     * @param otherValue a linguistic expression that defines the other fuzzyValue
     *                   to be compared to this FuzzyValue
     * @param threshold determines the value above which a match is considered to be true,
     *                  over-riding the class variable matchThreshold value.
     * @return true if the fuzy values overlap else false
     * @exception InvalidLinguisticExpressionException if the linguistic expression is not valid
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public boolean fuzzyMatch(String linguisticExpression, double threshold)
        throws InvalidLinguisticExpressionException, IncompatibleFuzzyValuesException
    {
        FuzzyValue fval = new FuzzyValue( fuzzyVariable, linguisticExpression );
        return fuzzyMatch(fval, threshold);
    }
    
    /**
     * Determine if 2 FuzzyValues 'match' or intersect with a membership
     * of at least that specified by the FuzzyValue class variable matchThreshold.
     * The threshold is restricted to a value between 0 and 1.
     *
     * @param otherValue the 2nd FuzzyValue for the operation
     * @return true if the fuzy values overlap else false
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public boolean fuzzyMatch(FuzzyValue otherValue)
        throws IncompatibleFuzzyValuesException
    {
        return fuzzyMatch(otherValue, matchThreshold);
    }
    
    
    /**
     * Determine if 2 FuzzyValues 'match' or intersect with a membership
     * of at least that specified by parameter threshold.
     * The threshold is restricted to a value between 0 and 1.
     *
     * @param otherValue the 2nd FuzzyValue for the operation.
     * @param threshold determines the value above which a match is considered to be true,
     *                  over-riding the class variable matchThreshold value.
     * @return true if the fuzy values overlap else false
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public boolean fuzzyMatch(FuzzyValue otherValue, double threshold)
        throws IncompatibleFuzzyValuesException
    {
        if (this.fuzzyVariable != otherValue.fuzzyVariable)
           throw new IncompatibleFuzzyValuesException("FuzzyValues must have the same FuzzyVariables to do a fuzzyMatch");

        if (threshold > 1.0) 
            threshold = 1.0;
        else if (threshold < 0.0) 
            threshold = 0.0;

        // fast test first ... the test returns true if the sets definitely DO NOT intersect
        // and we only do this if the threshold is 0.0 (otherwise we need to get the
        // value of the maximum of the intersections) ... BUT this is a very common
        // case
        if (threshold == 0.0 && getFuzzySet().noIntersectionTest(otherValue.getFuzzySet()) )
            return false;

        double maxmin = maximumOfIntersection(otherValue);
        
        if (threshold == 0.0)
            return( maxmin > 0.0 ? true : false );
        else
            return( maxmin >= threshold ? true : false );
    }


    /**
     * SIMILARITY: calculates a similarity measure between two fuzzy values using the
     * default similarity operator. The original default similarity operator
     * is the SimilarityByPossibilityOperator. This can be changed using the setDefaultSimilarityOperator
     * method of the FuzzyValue class.
     * <p>
     * Note: the 2 fuzzy values must have the same Fuzzy variable 
     *
     * @param fv the fuzzy value with which this fuzzy value is compared
     * @return The similarity value for the 2 fuzzy values
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     * @see SimilarityByAreaOperator
     * @see SimilarityByPossibilityOperator 
     */
    public double similarity(FuzzyValue fv)
        throws IncompatibleFuzzyValuesException
    {
       double result = defaultSimilarityOperator.similarity(this, fv);
       return( result );
    }

    /**
     * SIMILARITY: calculates a similarity measure between two fuzzy values using a
     * specified similarity operator. 
     * <p>
     * Note: the 2 fuzzy values must have the same Fuzzy variable 
     *
     * @param fv the fuzzy value with which this fuzzy value is compared
     * @param simOp the SimilarityOperator to use to calculate the similarity
     * @return The similarity value for the 2 fuzzy values
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     * @see SimilarityByAreaOperator
     * @see SimilarityByPossibilityOperator 
     */
    public double similarity(FuzzyValue fv, SimilarityOperator simOp)
        throws IncompatibleFuzzyValuesException
    {
       double result = simOp.similarity(this, fv);
       return( result );
    }

    /**
     * Set the default similarity operator that will be used by the FuzzyValue
     * similarity method. 
     *
     * @param simOp is the similarity operator to be used when the
     * FuzzyValue method similarity is called.
     */

    public static void setDefaultSimilarityOperator( SimilarityOperator simOp )
    {
    	defaultSimilarityOperator = simOp;
    }

    /**
     * Get the default similarity operator that is used by the FuzzyValue
     * similarity method. 
     *
     * @returns the similarity operator to be used when the
     * FuzzyValue method similarity is called.
     */

    public static SimilarityOperator getDefaultSimilarityOperator()
    {
    	return( defaultSimilarityOperator );
    }



    /**
     * If param is true FuzzySets will be confined to lie within the 
     * Universe of Discourse (UOD) when FuzzyValues are constructed. 
     * Normally this is false.
     *
     * @param b true if f FuzzySets will be confined to lie within the
     *          UOD when constructing FuzzyValues.
     */

    public static void setConfineFuzzySetsToUOD( boolean b)
    {
        confineFuzzySetsToUOD = b;
    }

    /**
     * Returns true if FuzzySets will be confined to lie within the 
     * Universe of Discourse (UOD) when FuzzyValues are constructed. 
     * Normally this is false.
     *
     * @return true if f FuzzySets will be confined to lie within the
     *         UOD when constructing FuzzyValues.
     */

    public static boolean isConfineFuzzySetsToUOD(){
        return(confineFuzzySetsToUOD);
    }

    /**
     * Returns true if this FuzzyValue is empty; in other words, if this FuzzyValue has a 
     * null FuzzySet, or a FuzzySet which doesn't contain any points.
     *
     * @return true if this FuzzyValue is empty
     */

    public boolean isEmpty(){
        return(fuzzySet == null || fuzzySet.isEmpty());
    }

    /**
     * Returns true if this FuzzyValue is convex.  The definition of convex is that
     * the membership function of a convex FuzzyValue (FuzzySet) does not go "up-and-down"
     * more than once.
     *
     * @return true if this FuzzyValue is convex
     */

    public boolean isConvex(){
        return(fuzzySet.isConvex());
    }

    /**
     * Returns true if this FuzzyValue is normal.  The definition of normal is that 
     * there is at least one point in the universe of discourse where the membership 
     * function reaches unity (in terms of this fuzzy package, unity is equal to 1.0).
     *
     * @return true if this FuzzyValue is normal
     */

    public boolean isNormal(){
        return(fuzzySet.isNormal());
    }

    /**
     * Returns the size of the FuzzyValue; in other words, the number of points 
     * contained in the FuzzySet.
     *
     * @return the int value specifying the size of the FuzzyValue, or the number
     *         of points contained in the FuzzyValue.
     */

    public int size(){
        return(fuzzySet.size());
    }
    
    /**
     * Returns the x value of the point in the FuzzyValue at the specified index.  As usual,
     * the indices begin at 0, so the x value of the first point in the FuzzyValue can be
     * accessed with the following method call: fuzzyValueInstanceName.getX(0)
     *
     * @param index the 0 based index into the fuzzy set points of the fuzzy value.
     * @return the double x value of the point in the FuzzyValue at the specified index. 
     */
     
    public double getX(int index){
        return(fuzzySet.getX(index));
    }    

    /**
     * Returns the y value of the point in the FuzzyValue at the specified index.  As usual,
     * the indices begin at 0, so the y value of the first point in the FuzzyValue can be
     * accessed with the following method call: fuzzyValueInstanceName.getY(0)
     *
     * @param index the 0 based index into the fuzzy set points of the fuzzy value.
     * @return the double y value of the point in the FuzzyValue at the specified index. 
     */
     
    public double getY(int index){
        return(fuzzySet.getY(index));
    }    
    
    /**
     * Returns the maximum membership value of the FuzzySet.
     *
     * @return the double value representing the maximum membership value
     *         of the FuzzySet
     */
    public double getMaxY(){
        return(fuzzySet.getMaxY());
    }    
    
    /**
     * Returns the minimum membership value of the FuzzySet.
     *
     * @return the double value representing the minimum membership value
     *         of the FuzzySet
     */
    public double getMinY(){
        return(fuzzySet.getMinY());
    }    
    
    /**
     * Returns the String representation of the FuzzyValue. 
     *
     * <p>For example:
     * <p><pre>
     * FuzzyVariable         -> TEMPERATURE [0, 60] Degree Celsius
     * Linguistic Expression -> HOT 
     * FuzzySet              -> { 0/30 1/45 0/60}
     * </pre>
     *
     * @return the String representation of the FuzzyValue 
     */
    
    public String toString(){
        String s;
        
        s = "FuzzyVariable         -> " +
            fuzzyVariable.getName() +
            " [ " + fuzzyVariable.getMinUOD() +
            ", " + fuzzyVariable.getMaxUOD() +
            " ] " + fuzzyVariable.getUnits() +
            "\nLinguistic Expression -> " +
            linguisticExpression +
            "\nFuzzySet              -> " +
            fuzzySet.toString();
        
        return(s);
    }    

    /************************************************************************************
     *
     * GET, SET and Testing METHODS
     *
     ************************************************************************************/

    /**
     * Returns the FuzzyVariable to which this FuzzyValue belongs.
     *
     * @return the FuzzyVariable to which this FuzzyValue belongs, and within whose 
     *         whose definition the FuzzyValue has meaning.
     */

    public FuzzyVariable getFuzzyVariable(){
        return(fuzzyVariable);
    }
        
    /**
     * Returns the FuzzySet that is contained within this FuzzyValue.
     *
     * @return the FuzzySet belonging to this FuzzyValue
     */
    
    public FuzzySet getFuzzySet(){
        return(fuzzySet);
    }    

    /**
     * Returns the units of the FuzzyVariable to which this FuzzyValue belongs, 
     * and therefore the units of this FuzzyValue.
     *
     * @return the String value that represents the units of this FuzzyValue.
     */

    public String getUnits(){
        return(fuzzyVariable.getUnits());
    }

    /**
     * Returns the x value which represents the minimum x value in the Universe
     * of Discourse of the FuzzyVariable to which this FuzzyValue belongs, and
     * therefore the mininum x value of the Universe of Discourse of this FuzzyValue.
     */

    public double getMinUOD(){
        return(fuzzyVariable.getMinUOD());
    }

    /**
     * Returns the x value which represents the maximum x value in the Universe
     * of Discourse of the FuzzyVariable to which this FuzzyValue belongs, and
     * therefore the maxinum x value of the Universe of Discourse of this FuzzyValue.
     */

    public double getMaxUOD(){
        return(fuzzyVariable.getMaxUOD());
    }

    /**
     * Sets the String which represents the linguistic expression of this FuzzyValue.
     * Note: be careful when using this since the expression should be a true representation
     *       for the FuzzyValue. A common place to use this might be if a FuzzyValue
     *       is created with no linguistic expression (using a specified Fuzzyset
     *       for example) and the user wanted to add one for that FuzzySet.
     *
     * @param lexpr the linguistic expression to replace the current one
     */

    public void setLinguisticExpression(String lexpr){
        linguisticExpression = lexpr;
    }

    /**
     * Returns the String which represents the linguistic expression of this FuzzyValue.
     *
     * @return the String which represents the linguistic expression of this FuzzyValue.
     */

    public String getLinguisticExpression(){
        return(linguisticExpression);
    }

    /**
     * Checks to see if all of the X values of a fuzzy set are within the 
     * range of the universe of discourse (UOD) of a fuzzy variable.
     *
     * @param fs the fuzzy set to be checked
     * @param fvar the fuzzy variable with the UOD definition
     * @return true if any X value is outside of the UOD range.
     */
     
    public boolean isAnyXValueOutsideUOD(FuzzySet fs, FuzzyVariable fvar) 
    {
        double minUOD = fvar.getMinUOD();
        double maxUOD = fvar.getMaxUOD();
        
        // since the fuzzy set x values are 'guaranteed' to be in order
        // we can do a simple test
        int size = fs.size();
        // if empty set then all x values inside UOD
        if (size < 1)
           return false;
        if (fs.getX(0) < minUOD || fs.getX(size-1) > maxUOD)
           return true;
           
        return false;
    }


    /**
     * Returns true if the x value passed to the method is outside of the 
     * Universe of Discourse.
     *
     * @param x the x value being tested to be within the range of the UOD
     * @param fvar the fuzzy variable that defines the UOD
     * @return true if the x value argument passed is outside of the Universe of 
     *         Discourse.
     */
     
    public boolean isXValueOutsideUOD(double x, FuzzyVariable fvar)
    {
        return(x < fvar.getMinUOD() || x > fvar.getMaxUOD());
    }
    
    
    /**
     * Returns the current matchThreshold which is used when comparing fuzzy
     * values with the fuzzyMatch method.
     *
     * @return the matchThreshold.
     */
     
    public static double getMatchThreshold()
    {
        return(matchThreshold);
    }
    
    
    
    /**
     * Sets the current default matchThreshold which is used when comparing fuzzy
     * values with the fuzzyMatch method.
     * 
     * @param value the new value for matchThreshold. If the value is > 1.0
     *              set it to 1.0; if it is < 0.0 set it to 0.0.
     */
     
    public static void setMatchThreshold(double value)
    {
        if (value > 1.0) matchThreshold = 1.0;
        else if (value < 0.0) matchThreshold = 0.0;
        else matchThreshold = value;
    }
    
    
    
    

    /*======================================================
     *
     * METHODS TO SUPPORT THE PLOTTING OF FUZZY VALUES
     *
     *====================================================*/

  /**
   * Create a string to be used as the heading for FuzzyValue plots.
   */
  private static StringBuffer constructPlotHeading(FuzzyValue fvals[], String plotChars)
  {
    int i;
    char plotChar = '*';
    StringBuffer result =  new StringBuffer();
    
    if (fvals.length < 1) return result;
    
    result.append("Fuzzy Value: " + fvals[0].getFuzzyVariable().getName());
    result.append("\nLinguistic Value: ");
    
    for (i=0; i < fvals.length; i++)
    {
        if (i>0) 
           result.append(",  ");
        result.append(fvals[i].getLinguisticExpression() + " (");
        if (plotChars.length() > i) 
           plotChar = plotChars.charAt(i);
        result.append(plotChar + ")");
        
    }
    result.append("\n");
    return result;
  }

  /**     
   * Plots the fuzzy value in an ascii format producing a String that can be displayed.
   * The string must be displayed using a non-proportional spaced font. 
   * Consider the following example:
   * <pre><code>
   *   FuzzyValue fval;
   *   FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C");
   *     ...
   *   fval = new FuzzyValue(temp, "very hot or cold");
   *   System.out.println(fval.plotFuzzyValue("+", 0, 40));
   *   
   * will display 
   *
   *   Fuzzy Value: temperature
   *   Linguistic Value: very hot or cold (+)
   *   
   *    1.00+++++++                                     +++++++
   *    0.95       +
   *    0.90                                           +
   *    0.85        +
   *    0.80         +
   *    0.75                                          +
   *    0.70          +
   *    0.65
   *    0.60           +                             +
   *    0.55            +
   *    0.50                                        +
   *    0.45             +
   *    0.40              +                        +
   *    0.35
   *    0.30               +                      +
   *    0.25
   *    0.20                +                    +
   *    0.15                 +                  +
   *    0.10                                   +
   *    0.05                  +               +
   *    0.00                   +++++++++++++++
   *        |----|----|----|----|----|----|----|----|----|----|
   *       0.00      8.00     16.00     24.00     32.00     40.00
   * 
   * @param plotchar  character to use for plotting (normally *, +, ., etc)
   * @param low       lower x value in plot
   * @param high      upper x value in plot
   * 
   */
  public String plotFuzzyValue( String plotChar, double lowX, double highX)
  {
    StringBuffer result = new StringBuffer(500);
    
    FuzzyValue fvals[] = new FuzzyValue[1];
    fvals[0] = this;
    result.append(FuzzyValue.constructPlotHeading(fvals, plotChar));
    result.append(this.getFuzzySet().plotFuzzySet(plotChar, lowX, highX));
    
    return result.toString();
  }


  /**     
   * Plots the fuzzy value in an ascii format producing a String that can be displayed.
   * The string must be displayed using a non-proportional spaced font. 
   * The low X and high X values of the plot are upper and lower values of the
   * universe of discourse for the FuzzyValue.
   * <p> See <code>plotFuzzyValue( String plotChars, double lowX, double highX )</code>
   * for an example.
   *
   * @param plotchar  character to use for plotting (normally *, +, ., etc)
   * 
   */
  public String plotFuzzyValue( String plotChar)
  {
    StringBuffer result = new StringBuffer(500);
    double lowX, highX;
    
    FuzzyValue fvals[] = new FuzzyValue[1];
    fvals[0] = this;
    result.append(FuzzyValue.constructPlotHeading(fvals, plotChar));
    
    lowX = this.getMinUOD();
    highX = this.getMaxUOD();
    result.append(this.getFuzzySet().plotFuzzySet(plotChar, lowX, highX));
    
    return result.toString();
  }

  /**     
   * Plots the given fuzzy values in an ascii format producing a String that can be displayed.
   * The string must be displayed using a non-proportional spaced font. 
   * The low X and high X values of the plot are upper and lower values of the
   * universe of discourse for the FuzzyValues. 
   * The string of plotting characters are used in sequence to plot the corresponding
   * fuzzy value. If the string is empty the character '*' is used. If the string
   * has fewer characters than fuzzy values, the last character is reused.
   * All FuzzyValues must be from the same FuzzyVariable. 
   * <p> See <code>plotFuzzyValues( String plotChars, double lowX, double highX, FuzzyValue fvals[])</code>
   * for an example.
   *
   * @param plotChars  characters to use for plotting (normally *, +, ., etc)
   * 
   */
  public static String plotFuzzyValues( String plotChars, FuzzyValue fvals[])
  {
    double lowX, highX;
    
    if (fvals.length < 1)
       return "";
       
    FuzzyVariable fvar = fvals[0].getFuzzyVariable();
    
    lowX = fvar.getMinUOD();
    highX = fvar.getMaxUOD();
        
    return plotFuzzyValues( plotChars, lowX, highX, fvals);
  }

  /**     
   * Plots the fuzzy values in an ascii format producing a String that can be displayed.
   * The string must be displayed using a non-proportional spaced font. 
   * The string of plotting characters are used in sequence to plot the corresponding
   * fuzzy value. If the string is empty the character '*' is used. If the string
   * has fewer characters than fuzzy values, the last character is reused.
   * All FuzzyValues must be from the same FuzzyVariable. 
   * Consider the following example:
   * <pre><code>
   *   FuzzyValue fval2, fval3;
   *   FuzzyValue fvals[] = new FuzzyValue[2];
   *   FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C");
   *     ...
   *   fval2 = new FuzzyValue(temp, "very hot or cold");
   *   fval3 = new FuzzyValue(temp, "medium");
   *   fvals[0] = fval2;
   *   fvals[1] = fval3;
   *   System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 40, fvals));
   *   
   * will display 
   *
   *   Fuzzy Value: temperature
   *   Linguistic Value: very hot or cold (*),  medium (+)
   *   
   *    1.00*******            +++++++++++++            *******
   *    0.95       *          +             +
   *    0.90                                           *
   *    0.85        *        +               +
   *    0.80         *      +                 +
   *    0.75                                          *
   *    0.70          *    +                   +
   *    0.65
   *    0.60           *  +                     +    *
   *    0.55            *+                       +
   *    0.50                                        *
   *    0.45            +*                        +
   *    0.40           +  *                        +
   *    0.35
   *    0.30          +    *                      * +
   *    0.25
   *    0.20         +      *                    *   +
   *    0.15        +        *                  *     +
   *    0.10                                   *
   *    0.05       +          *               *        +
   *    0.00+++++++            ***************          +++++++
   *        |----|----|----|----|----|----|----|----|----|----|
   *       0.00      8.00     16.00     24.00     32.00     40.00 
   * </code></pre>
   *
   * @param plotChars  characters to use for plotting (normally *, +, ., etc)
   * @param low       lower x value in plot
   * @param high      upper x value in plot
   */
  public static String plotFuzzyValues( String plotChars, double lowX, double highX, FuzzyValue fvals[])
  {
    StringBuffer result = new StringBuffer(500);
    int i;
    
    if (fvals.length < 1)
       return "";
       
    FuzzyVariable fvar = fvals[0].getFuzzyVariable();
    for (i=0; i<fvals.length; i++)
       if (fvals[i].getFuzzyVariable() != fvar)
          return "";
           
    result.append(FuzzyValue.constructPlotHeading(fvals, plotChars));
    
    FuzzySet fsets[] = new FuzzySet[fvals.length];
    for (i=0; i<fvals.length; i++)
        fsets[i] = fvals[i].getFuzzySet();
    result.append(FuzzySet.plotFuzzySets(plotChars, lowX, highX, fsets));
    
    return result.toString();
  }


}
