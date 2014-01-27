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


package nrc.fuzzy.jess; 

import jess.*;
import nrc.fuzzy.*;

import java.util.*;
import java.io.*;

/**
 * User-defined functions for fuzzy reasoning with Jess.
 * <P>
 * To use one of these functions from your programs, simply register the
 * package class in your Java mainline:
 *
 * <PRE>
 *    engine.addUserpackage(new nrc.fuzzy.jess.FuzzyFunctions());
 * </pre>
 *
 * and use the FuzzyRete object instead of the Rete object (as is done
 * in the FuzzyMain and FuzzyConsole classes).
 * <p>
 * The functions included in this package are (for details see the FuzzyJ User Guide): <br>
 * (fuzzy-match FuzzyValue1 FuzzyValue2)<br>
 * (fuzzy-rule-similarity) <br>
 * (fuzzy-rule-match-score) <br>
 * (set-default-similarity-operator operator) <br>
 * (get-default-similarity-operator) <br>
 * (set-fuzzy-global-contribution-operator operator) <br>
 * (get-fuzzy-global-contribution-operator) <br>
 * (set-default-fuzzy-rule-executor executor) <br>
 * (get-default-fuzzy-rule-executor) <br>
 * (set-default-antecedent-combine-operator operator) <br>
 * (get-default-antecedent-combine-operator) <br>
 * 
 * @author Bob Orchard
 * @see nrc.fuzzy.jess.FuzzyMain
 * @see nrc.fuzzy.jess.FuzzyRete
 * @see nrc.fuzzy.jess.FuzzyConsole
 */


public class FuzzyFunctions implements Userpackage
{
  /**
   * Called by a Rete object when you add this to an engine.
   */
  public void add(Rete engine)
  {
    engine.addUserfunction(new FuzzyMatch());
    engine.addUserfunction(new FuzzyRuleSimilarity());
	engine.addUserfunction(new FuzzyRuleMatchScore());
	engine.addUserfunction(new setDefaultSimilarityOperator());
	engine.addUserfunction(new getDefaultSimilarityOperator());
	engine.addUserfunction(new setFuzzyGlobalContributionOperator());
	engine.addUserfunction(new getFuzzyGlobalContributionOperator());
	engine.addUserfunction(new setDefaultFuzzyRuleExecutor());
	engine.addUserfunction(new getDefaultFuzzyRuleExecutor());
	engine.addUserfunction(new setDefaultAntecedentCombineOperator());
	engine.addUserfunction(new getDefaultAntecedentCombineOperator());
  
  }

/**
 * Userfunction class: 
 * <p>
 * Implements the Jess user function (fuzzy-match fv1 fv2)
 * <p>
 * where   fv1 and fv2 are both FuzzyValue instances
 * <p>
 * or      one is a FuzzyValue and the other is a string that represents a valid linguistic expression.
 * <p>
 * The function returns true if the 2 FuzzyValues have some overlapping portion that 
 * has a membership value at least as large as the current setting of the FuzzyValue
 * match Threshold (see FuzzyValue static method setMatchThreshold).
 * <p><p>
 * This is used in Jess rules to test FuzzyValues for 'equality' on the LHS. Consider
 * the simple rule below. <b>NOTE:</b> This Jess user function must be used for 
 * matching of FuzzyValues on the LHS of rules and not the FuzzyValue method fuzzyMatch since it interacts
 * with the Jess rete engine, providing the strong connection between Jess and the NRC fuzzy API.
 * <p><pre>
 *    (defrule cold_strong "match on cold temperature and strong flow"
 *      (temp ?t&:(fuzzy-match ?t "cold"))
 *      (flow ?f&:(fuzzy-match ?f "strong"))
 *     =>
 *      (assert (change_hv (new nrc.fuzzy.FuzzyValue ?*hotValveChangeFvar* "Z"))
 *              (change_cv (new nrc.fuzzy.FuzzyValue ?*coldValveChangeFvar* "NB"))
 *      )
 *    )
 * </pre>
 *
 * @author Bob Orchard
 */
public class FuzzyMatch implements Userfunction, Serializable
{
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
    return "fuzzy-match"; 
  }

  /**
   * @param vv a ValueVector with the function arguments, 2 FuzzyValue objects
   * @param context 
   * @exception JessException 
   * @return Value TRUE if the fuzzy values match else FALSE
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {
    // get the required 2 parameters ... both must be fuzzy values
    // and perform the fuzzyMatch of the 2 fuzzy values. 
    boolean matched;
    Value v1, v2;
    Value tempValue;
    Object v1Obj = null, v2Obj = null;
    boolean badArgs = false;

    // must be 2 args
    if (vv.size() != 3)
      badArgs = true;
    else
    { // one or both args must be Fuzzy Values
      // if one is a string and other a fuzzy value call fuzzyMatch
      // with string arg
      v1 = vv.get(1).resolveValue(context);
      v2 = vv.get(2).resolveValue(context);
      if (v1.type() != RU.JAVA_OBJECT && v2.type() != RU.JAVA_OBJECT)
        badArgs = true;
      else if (v1.type() == RU.JAVA_OBJECT && v2.type() == RU.JAVA_OBJECT)
      {
        v1Obj = v1.javaObjectValue(context);
        v2Obj = v2.javaObjectValue(context);
        if (!(v1Obj instanceof nrc.fuzzy.FuzzyValue) || !(v2Obj instanceof nrc.fuzzy.FuzzyValue))
          badArgs = true;
      }
      else 
      {  // at least 1 must be an external address ... hopefully a fuzzy value
        if (v2.type() == RU.JAVA_OBJECT)
        { // switch v1 and v2
          tempValue = v1;
          v1 = v2;
          v2 = tempValue;
        }
        v1Obj = v1.javaObjectValue(context);
        if (!(v1Obj instanceof nrc.fuzzy.FuzzyValue))
            badArgs = true;
        else
          if (v2.type() == RU.SYMBOL || v2.type() == RU.STRING)
          {
              FuzzyVariable fvar = ((FuzzyValue)v1Obj).getFuzzyVariable();
              try
              {
                v2Obj = new FuzzyValue(fvar, v2.stringValue(context));
              }
              catch (InvalidLinguisticExpressionException ifve)
              {
                throw new JessException(getName(),
                          "fuzzyMatch method for FuzzyValue failed (invalid linguistic string expression): " + ifve, "");
              }
          }
          else
            badArgs = true;
      }
    }
    if (badArgs)
    {	 throw new JessException(getName(),
                "Requires 2 arguments (both FuzzyValues or 1 a FuzzyValue and 1 a valid linguistic expression)" ,"");
    }
    try
    { 
      matched = ((FuzzyValue)v1Obj).fuzzyMatch((FuzzyValue)v2Obj);
    }
    catch (IncompatibleFuzzyValuesException ifve)
    {
      throw new JessException(getName(),
                "fuzzyMatch method for FuzzyValue failed (incompatible fuzzy values): " + ifve, "");
    }

    // If the match is successful then also add the pair of matching 
    // fuzzy values (antecedent and input) to the current token
    // (there should be one if we are pattern matching) in the 
    // extensionData slot
    //
    // v2Obj is the antecedent -- normally the string expression in the pattern
    if (matched)
    {    
      FuzzyToken t = (FuzzyToken)context.getToken();
      if (t != null)
      { FuzzyValueVector matchingFuzzyValues = new FuzzyValueVector(2);
       matchingFuzzyValues.addFuzzyValue((FuzzyValue)v2Obj);
       matchingFuzzyValues.addFuzzyValue((FuzzyValue)v1Obj);
       t.addExtensionData(matchingFuzzyValues);
      }
      
      return Funcall.TRUE;
    }
    
    return Funcall.FALSE;
  }
}


/**
 * Userfunction class: 
 * <p>
 * Implements the Jess user function (fuzzy-rule-similarity)
 * <p>
 * The function returns a value between 0.0 and 1.0 which indicates the overall fuzzy
 * similarity of the patterns that matched on the left hand side (LHS) of a rule, if the
 * function is called from the right hand side (RHS) of a rule. If not called from the 
 * RHS of a rule it will always return 0.0. The value is determined by calcualting
 * the minimum (or product -- depends on the current rule's antecedentCombineOperator)
 * similarity of all of the fuzzy matches that were made on the LHS of
 * the rule. If there were no fuzzy matches on the LHS of the rule a value of 1.0 is 
 * returned.
 * <p><p>
 * This is used in Jess rules to determine the similarity with which fuzzy patterns on
 * LHS matched. This can serve as the basis for a certainty factor or degree of
 * confidence for non-fuzzy facts asserted on the RHS of a rule (as in FuzzyCLIPS)
 * or for other user defined purposes. Consider the simple example below.
 * <p><pre>
 * ;; A simple example to test a complete FuzzyJess program (no Java code at all).
 * ;;
 * ;;
 * ;; Note: future versions (beyond 5.0a5) of Jess will allow us to use --
 * ;;
 * ;;             (new FuzzyValue ... )
 * ;;       etc.
 * ;;
 * ;;       will no longer always need to fully qualify the classes!
 * ;;
 * ;; Example as shown will give result ...
 * ;;
 * ;; Jack is tall with degree (similarity) 0.5363321799307958
 * ;; Jack is tall with degree (match) 0.588235294117647
 * ;; Randy is tall with degree (similarity) 1.0
 * ;; Randy is tall with degree (match) 1.0
 * ;; Ralph is tall with degree (similarity) 0.4117647058823532
 * ;; Ralph is tall with degree (match) 0.49999999999999994
 * 
 * 
 * (defglobal ?*heightFvar* = (new nrc.fuzzy.FuzzyVariable "height" 0.0 10.0 "feet"))
 * 
 * (defglobal ?*rlf* = (new nrc.fuzzy.RightLinearFunction))
 * (defglobal ?*llf* = (new nrc.fuzzy.LeftLinearFunction))
 * 
 * (deftemplate person
 *    (slot name)
 *    (slot height)
 * )
 * 
 * (defrule init
 *    (declare (salience 100))
 *   =>
 *    (load-package nrc.fuzzy.jess.FuzzyFunctions)
 *    (?*heightFvar* addTerm "short" (new nrc.fuzzy.RFuzzySet 0.0 5.0 ?*rlf*))
 *    (?*heightFvar* addTerm "medium" (new nrc.fuzzy.TrapezoidFuzzySet 4.0 4.8 5.5 6.0))
 *    (?*heightFvar* addTerm "tall" (new nrc.fuzzy.LFuzzySet 5.5 6.0 ?*llf*))
 * 
 *    (assert (person (name "Ralph")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 5.7 0.1)))
 *         )
 *         (person (name "Timothy")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 4.0 0.1)))
 *         )
 *         (person (name "Randy")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 6.5 0.1)))
 *         )
 *         (person (name "Jack")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 5.75 0.1)))
 *         )
 *     )
 * )
 *
 * (defrule identify-tall-people "determine strength of tallness for a person"
 *   (person (name ?n) (height ?ht&:(fuzzy-match ?ht "tall")))
 *  =>
 *   (printout t ?n " is tall with degree (similarity) " (fuzzy-rule-similarity) crlf)
 *   (printout t ?n " is tall with degree (match) " (fuzzy-rule-match-score) crlf)
 * )
 * </pre>
 * <p>
 * Note that the similar function 'fuzzy-rule-match-score' returns the overall 
 * (minimum or product) fuzzy-match between patterns on the RHS of the rule (depends on the rule's
 * antecedentCombineOperator -- minimum or product of the values). In fuzzyCLIPS
 * this value would be used as the certainty factor for fuzzy facts asserted
 * on the RHS of rules, and the fuzzy-rule-similarity would be used as the
 * certainty factor for non-fuzzy (crisp) facts asserted on the RHS of rules.
 *
 * @author Bob Orchard
 */
public class FuzzyRuleSimilarity implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
    return "fuzzy-rule-similarity"; 
  }

  /**
   * @param vv a ValueVector 
   * @param context 
   * @exception JessException 
   * @return Value the similarity measure for the rule.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {
    int i;
    double theSimilarity = 1.0;
    
    // NO parameters ... 
    // must be 0 args
    if (vv.size() != 1)
      throw new JessException(getName(),
                "Expects no arguments" ,"");

    // Extract the fuzzy patterns in the RHS of the rule (if in a rule) and
    // the fuzzy values that matched those patterns and determine the minimum
    // of the fuzzy similarities of these pairs of fuzzy values. 
    FuzzyRete engine = (FuzzyRete)(context.getEngine());
    Activation activation = engine.getCurrentActivation();

    // if not in RHS of a rule then return 0.0
    if (activation == null)
       return new Value(0.0, RU.FLOAT);  
       
    Vector fuzzyMatches = engine.getFuzzyMatchesInActivation(activation);
    if (fuzzyMatches != null)
    { 
      try
	  { int len = fuzzyMatches.size();
	  	double similarities[] = new double[len];
		AntecedentCombineOperator combineOperator = FuzzyRule.getDefaultAntecedentCombineOperator();
		for (i = 0; i<len; i++)
		{ FuzzyValueVector fvv = (FuzzyValueVector)(fuzzyMatches.elementAt(i));
		  similarities[i] = fvv.fuzzyValueAt(0).similarity(fvv.fuzzyValueAt(1));
		}
		theSimilarity = (len == 1) ? similarities[0] :
									 combineOperator.execute(similarities);
	  }
      catch (IncompatibleFuzzyValuesException ie)
      {
        throw new JessException(getName(),
                  "Internal Rule problem\n" + ie ,"");
      }
    }
    return new Value(theSimilarity, RU.FLOAT);
  }
}


/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (fuzzy-rule-match-score)
 * <p>
 * The function returns a value between 0.0 and 1.0 which indicates the overall fuzzy
 * match scores of the patterns that matched on the left hand side (LHS) of a rule, if the
 * function is called from the right hand side (RHS) of a rule. If not called from the 
 * RHS of a rule it will always return 0.0. The value is determined by calcualting
 * the minimum (or product -- depends on the current rule's antecedentCombineOperator)
 * of all of the fuzzy matches that were made on the LHS of
 * the rule. If there were no fuzzy matches on the LHS of the rule a value of 1.0 is 
 * returned. A fuzzy-match score is the maximum value of the intersection of the 2
 * fuzzy values. This is different than the fuzzy-similarity of 2 fuzzy values, which 
 * provides a more complex (and possibly more useful) measure of the similarity of
 * 2 fuzzy values.
 * <p><p>
 * This is used in Jess rules to determine the minimum match score with which fuzzy patterns on
 * LHS matched. This can serve as the basis for a certainty factor or degree of
 * confidence for fuzzy facts asserted on the RHS of a rule (as in FuzzyCLIPS)
 * or for other user defined purposes. Consider the simple example below.
 * <p><pre>
 * ;; A simple example to test a complete FuzzyJess program (no Java code at all).
 * ;;
 * ;;
 * ;; Note: future versions (beyond 5.0a5) of Jess will allow us to use --
 * ;;
 * ;;             (new FuzzyValue ... )
 * ;;       etc.
 * ;;
 * ;;       will no longer always need to fully qualify the classes!
 * ;;
 * ;; Example as shown will give result ...
 * ;;
 * ;; Jack is tall with degree (similarity) 0.5363321799307958
 * ;; Jack is tall with degree (match) 0.588235294117647
 * ;; Randy is tall with degree (similarity) 1.0
 * ;; Randy is tall with degree (match) 1.0
 * ;; Ralph is tall with degree (similarity) 0.4117647058823532
 * ;; Ralph is tall with degree (match) 0.49999999999999994
 * 
 * 
 * (defglobal ?*heightFvar* = (new nrc.fuzzy.FuzzyVariable "height" 0.0 10.0 "feet"))
 * 
 * (defglobal ?*rlf* = (new nrc.fuzzy.RightLinearFunction))
 * (defglobal ?*llf* = (new nrc.fuzzy.LeftLinearFunction))
 * 
 * (deftemplate person
 *    (slot name)
 *    (slot height)
 * )
 * 
 * (defrule init
 *    (declare (salience 100))
 *   =>
 *    (load-package nrc.fuzzy.jess.FuzzyFunctions)
 *    (?*heightFvar* addTerm "short" (new nrc.fuzzy.RFuzzySet 0.0 5.0 ?*rlf*))
 *    (?*heightFvar* addTerm "medium" (new nrc.fuzzy.TrapezoidFuzzySet 4.0 4.8 5.5 6.0))
 *    (?*heightFvar* addTerm "tall" (new nrc.fuzzy.LFuzzySet 5.5 6.0 ?*llf*))
 * 
 *    (assert (person (name "Ralph")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 5.7 0.1)))
 *         )
 *         (person (name "Timothy")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 4.0 0.1)))
 *         )
 *         (person (name "Randy")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 6.5 0.1)))
 *         )
 *         (person (name "Jack")
 *                 (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
 *                              (new nrc.fuzzy.PIFuzzySet 5.75 0.1)))
 *         )
 *     )
 * )
 *
 * (defrule identify-tall-people "determine strength of tallness for a person"
 *   (person (name ?n) (height ?ht&:(fuzzy-match ?ht "tall")))
 *  =>
 *   (printout t ?n " is tall with degree (similarity) " (fuzzy-rule-similarity) crlf)
 *   (printout t ?n " is tall with degree (match) " (fuzzy-rule-match-score) crlf)
 * )
 * </pre>
 * <p>
 * Note that the similar function 'fuzzy-rule-similarity' returns the overall 
 * similarity score between patterns on the RHS of the rule (depends on the rule's
 * antecedentCombineOperator -- minimum or product of the values). In fuzzyCLIPS
 * this value would be used as the certainty factor for non-fuzzy (crisp) facts asserted
 * on the RHS of rules, and the fuzzy-rule-match-score would be used as the
 * certainty factor for fuzzy facts asserted on the RHS of rules.
 *
 * @author Bob Orchard
 */
public class FuzzyRuleMatchScore implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
    return "fuzzy-rule-match-score"; 
  }

  /**
   * @param vv a ValueVector with the function arguments (0 arguments)
   * @param context 
   * @exception JessException 
   * @return Value the match score for the rule.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {
    int i;
    double theMatchScore = 1.0;
    
    // NO parameters ... 
    // must be 0 args
    if (vv.size() != 1)
      throw new JessException(getName(),
                "Expects no arguments" ,"");

    // Extract the fuzzy patterns in the RHS of the rule (if in a rule) and
    // the fuzzy values that matched those patterns and determine the minimum
    // of the fuzzy match scores of these pairs of fuzzy values. 
    FuzzyRete engine = (FuzzyRete)context.getEngine();
    Activation activation = engine.getCurrentActivation();

    // if not in RHS of a rule then return 0.0
    if (activation == null)
       return new Value(0.0, RU.FLOAT);  
       
    Vector fuzzyMatches = engine.getFuzzyMatchesInActivation(activation);
    if (fuzzyMatches != null)
    { 
      try
	  { int len = fuzzyMatches.size();
	  	double matchScores[] = new double[len];
		AntecedentCombineOperator combineOperator = FuzzyRule.getDefaultAntecedentCombineOperator();
		for (i = 0; i<len; i++)
		{ FuzzyValueVector fvv = (FuzzyValueVector)(fuzzyMatches.elementAt(i));
		  matchScores[i] = fvv.fuzzyValueAt(0).maximumOfIntersection(fvv.fuzzyValueAt(1));
		}
		theMatchScore = (len == 1) ? matchScores[0] : 
									 combineOperator.execute(matchScores);
      }
      catch (IncompatibleFuzzyValuesException ie)
      {
        throw new JessException(getName(),
                  "Internal Rule problem\n" + ie ,"");
      }
    }
    return new Value(theMatchScore, RU.FLOAT);
  }
}

/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (set-default-similarity-operator the-operator)
 * <p>
 * The function takes one argument that specifies the way similarity
 * between 2 Fuzzy Values should be calculated
 * The values can be one of:
 * <p>
 * area - use ratio of aarea of intersection and area of union (see java class SimilarityByAreaOperator).
 * <br>
 * possibility - use possibility and necessity calculations  (this is the default) (see java class SimilarityByPossibilityOperator).
 * <br>
 */
public class setDefaultSimilarityOperator implements Userfunction
{
  private SimilarityOperator areaOp = new SimilarityByAreaOperator();
  private SimilarityOperator possibilityOp = new SimilarityByPossibilityOperator();
	
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
	return "set-default-similarity-operator";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of 'area' or 'possibility'.
   * @param context 
   * @exception JessException 
   * @return Value TRUE if the setting of the default similarity operator is successful
   *         or FALSE if it fails (argument was not in correct range of allowed
   *         values).
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String defaultSimilarityOperatorString;
  	SimilarityOperator defaultSimilarityOperator;

	// must be 1 argument
	if (vv.size() != 2)
	  throw new JessException(getName(),
				"Expects exactly 1 argument (with value area or possibility)" ,"");

	v1 = vv.get(1).resolveValue(context);
	defaultSimilarityOperatorString = v1.stringValue(context).toLowerCase();

    if (defaultSimilarityOperatorString.equals("area"))
    	defaultSimilarityOperator = areaOp;
    else if (defaultSimilarityOperatorString.equals("possibility"))
    	defaultSimilarityOperator = possibilityOp;
    else
	    throw new JessException(getName(),
				"Expects exactly 1 argument (with value area or possibility)" ,"");

	FuzzyValue.setDefaultSimilarityOperator(defaultSimilarityOperator);
	
	return Funcall.TRUE;
  }
}

/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (get-default-similarity-operator)
 * <p>
 * The function takes no arguments and return an indicator of the way fuzzy 
 * similarity comparisons will be done. 
 * The values will be one of:
 * <p>
 * area - use ratio of aera of intersect and are of union (this is the default).
 * <br>
 * possibility - use possibility and necessity calculations (see java class SimilarityByPossibility).
 * <br>
 */
public class getDefaultSimilarityOperator implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
	return "get-default-similarity-operator";
  }

  /**
   * @param vv a ValueVector with the function arguments (0 arguments). 
   * @param context 
   * @exception JessException 
   * @return an RU.SYMBOL with 'area' or 'possibility' as a value
   *         or throws a JessException if it fails (has arguments or
   *         not a recognized operator .... may be an internal error
   *         suggesting that the FuzzyJess function needs to be updated to
   *         reflect new global operators that have been added.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	
	  	String defaultSimilarityOperatorString;
	  	SimilarityOperator defaultSimilarityOperator;

	// must be no arguments
	if (vv.size() != 1)
	  throw new JessException(getName(),
				"Expects no arguments" ,"");

	defaultSimilarityOperator = FuzzyValue.getDefaultSimilarityOperator();

    if (defaultSimilarityOperator instanceof SimilarityByAreaOperator)
    	defaultSimilarityOperatorString = "area";
    else if (defaultSimilarityOperator instanceof SimilarityByPossibilityOperator)
    	defaultSimilarityOperatorString = "possibility";
    else
	    throw new JessException(getName(),
				"Unexpected Global Contribution Operator type found" ,"");
    
	return (new Value(defaultSimilarityOperatorString, RU.SYMBOL));
  }
}


/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (set-fuzzy-global-contribution-operator)
 * <p>
 * The function takes one argument that specifies the way fuzzy global
 * contribution should be done when 'identical' facts with fuzzy values
 * are asserted. The values can be one of:
 * <p>
 * union - combine the fuzzy values using the fuzzy union operation (this is the default).
 * <br>
 * sum - combine the fuzzy values using the fuzzy sum operation.
 * <br>
 * none - do not combine the fuzzy values; replace the old value with the new one.
 * <br>
 */
public class setFuzzyGlobalContributionOperator implements Userfunction
{
  private GlobalContributionOperator unionOp = new UnionGlobalContributionOperator();
  private GlobalContributionOperator sumOp = new SumGlobalContributionOperator();
	
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
	return "set-fuzzy-global-contribution-operator";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of union, sum or none.
   * @param context 
   * @exception JessException 
   * @return Value TRUE if the setting of the global contribution is successful
   *         or FALSE if it fails (argument was not in correct range of allowed
   *         values).
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String globalContributionOperatorString;
  	GlobalContributionOperator globalContributionOperator;

	// must be 1 argument
	if (vv.size() != 2)
	  throw new JessException(getName(),
				"Expects exactly 1 argument (with value union, sum or none)" ,"");

	v1 = vv.get(1).resolveValue(context);
	globalContributionOperatorString = v1.stringValue(context).toLowerCase();

    if (globalContributionOperatorString.equals("union"))
        globalContributionOperator = unionOp;
    else if (globalContributionOperatorString.equals("sum"))
        globalContributionOperator = sumOp;
    else if (globalContributionOperatorString.equals("none"))
        globalContributionOperator = null;
    else
	    throw new JessException(getName(),
				"Expects exactly 1 argument (with value union, sum or none)" ,"");

	FuzzyRete engine = (FuzzyRete)context.getEngine();
    if (engine.setFuzzyGlobalContributionOperator(globalContributionOperator)) 
       return Funcall.TRUE;
    
	return Funcall.FALSE;
  }
}

/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (get-fuzzy-global-contribution-operator)
 * <p>
 * The function takes no arguments and return an indicator of the way fuzzy global
 * contribution should be done when 'identical' facts with fuzzy values
 * are asserted. The values will be one of:
 * <p>
 * union - combine the fuzzy values using the fuzzy union operation (this is the default).
 * <br>
 * sum - combine the fuzzy values using the fuzzy sum operation.
 * <br>
 * none - do not combine the fuzzy values; replace the old value with the new one.
 * <br>
 */
public class getFuzzyGlobalContributionOperator implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName() 
  { 
	return "get-fuzzy-global-contribution-operator";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of union, sum or none.
   * @param context 
   * @exception JessException 
   * @return an RU.ATOM with union, sum or none as a value
   *         or throws a JessException if it fails (has arguments or
   *         not a recognized operator .... may be an internal error
   *         suggesting that the FuzzyJess function needs to be updated to
   *         reflect new global operators that have been added.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	
  	String globalContributionOperatorString;
  	GlobalContributionOperator globalContributionOperator;

	// must be no arguments
	if (vv.size() != 1)
	  throw new JessException(getName(),
				"Expects no arguments" ,"");

	FuzzyRete engine = (FuzzyRete)context.getEngine();
	globalContributionOperator = engine.getFuzzyGlobalContributionOperator();

    if (globalContributionOperator instanceof UnionGlobalContributionOperator)
        globalContributionOperatorString = "union";
    else if (globalContributionOperator instanceof SumGlobalContributionOperator)
        globalContributionOperatorString = "sum";
    else if (globalContributionOperator == null)
        globalContributionOperatorString = "none";
    else
	    throw new JessException(getName(),
				"Unexpected Global Contribution Operator type found" ,"");
    
	return (new Value(globalContributionOperatorString, RU.SYMBOL));
  }
}


/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (set-default-fuzzy-rule-executor)
 * <p>
 * The function takes one argument that specifies which fuzzy rule
 * executor should be used when a rule is fired. The values can be one of:
 * <p>
 * mamdanimin - use the MamdaniMinMaxMin rule executor (this is the default).
 * <br>
 * larsenproduct - use the LarsenProductMaxMin rule executor.
 * <br>
 * tsukamoto - use the Tsukamoto rule executor.
 * <br>
 *
 */
public class setDefaultFuzzyRuleExecutor implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName()
  {
	return "set-default-fuzzy-rule-executor";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of mamdanimin, larsenproduct or tsukamoto.
   * @param context
   * @exception JessException 
   * @return Value TRUE if the setting of the default executor is successful
   *         or throws a JessException if it fails (argument was not in correct range of allowed
   *         values).
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String defaultRuleExecutorString;
  	FuzzyRuleExecutor defaultRuleExecutor;

	  // must be 1 argument
	  if (vv.size() != 2)
	    throw new JessException(getName(),
				"Expects exactly 1 argument (with value mamdanimin, larsenproduct or tsukamoto)" ,"");

	  v1 = vv.get(1).resolveValue(context);
	  defaultRuleExecutorString = v1.stringValue(context).toLowerCase();

    if (defaultRuleExecutorString.equals("mamdanimin"))
        defaultRuleExecutor = new MamdaniMinMaxMinRuleExecutor();
    else if (defaultRuleExecutorString.equals("larsenproduct"))
        defaultRuleExecutor = new LarsenProductMaxMinRuleExecutor();
    else if (defaultRuleExecutorString.equals("tsukamoto"))
        defaultRuleExecutor = new TsukamotoRuleExecutor();
    else
	    throw new JessException(getName(),
				"Expects exactly 1 argument (with value mamdanimin, larsenproduct or tsukamoto)" ,"");

    FuzzyRule.setDefaultRuleExecutor(defaultRuleExecutor);
    
    return Funcall.TRUE;
  }
}


/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (get-default-fuzzy-rule-executor)
 * <p>
 * The function takes no arguments. Returns one of:
 * <p>
 * mamdanimin - the MamdaniMinMaxMin rule executor is the default.
 * <br>
 * larsenproduct - the LarsenProductMaxMin rule executor is the default.
 * <br>
 * tsukamoto -  the Tsukamoto rule executor is the default.
 * <br>
 *
 */
public class getDefaultFuzzyRuleExecutor implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName()
  {
	return "get-default-fuzzy-rule-executor";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of mamdanimin or larsenproduct.
   * @param context
   * @exception JessException 
   * @return an RU.ATOM with mamdanimin, larsenproduct or tsukamoto as a value
   *         or throws a JessException if it fails (has arguments or
   *         not a recognized operator ... may be an internal error
   *         suggesting that the FuzzyJess function needs to be updated to
   *         reflect new rule executors that have been added.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String defaultRuleExecutorString;
  	FuzzyRuleExecutor defaultRuleExecutor;

	// must be no arguments
	if (vv.size() != 1)
	   throw new JessException(getName(),
				"Expects no arguments" ,"");

    defaultRuleExecutor = FuzzyRule.getDefaultRuleExecutor();
    
    if (defaultRuleExecutor instanceof MamdaniMinMaxMinRuleExecutor)
        defaultRuleExecutorString = "mamdanimin";
    else if (defaultRuleExecutor instanceof LarsenProductMaxMinRuleExecutor)
        defaultRuleExecutorString = "larsenproduct";
    else if (defaultRuleExecutor instanceof TsukamotoRuleExecutor)
        defaultRuleExecutorString = "tsukamoto";
    else
	    throw new JessException(getName(),
				"Unexpected FuzzyRuleExecutor", "");
    
    return (new Value(defaultRuleExecutorString, RU.SYMBOL) );
  }
}

/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (set-default-antecedent-combine-operator)
 * <p>
 * The function takes one argument that specifies which operator
 * should be used to combine the antecedent/input match
 * values when a fuzzt rule is fired. The values can be one of:
 * <p>
 * minimum - use the minimum of the match values of the antecedent/input pairs (this is the default).
 * <br>
 * product - use the product of the match values of the antecedent/input pairs.
 * <br>
 * compensatoryAnd - use the 'compensatory and' of the match values of the antecedent/input pairs.
 * <br>
 *
 */
public class setDefaultAntecedentCombineOperator implements Userfunction
{
   final AntecedentCombineOperator minimumCombineOp = new MinimumAntecedentCombineOperator();
   final AntecedentCombineOperator productCombineOp = new ProductAntecedentCombineOperator();
   final AntecedentCombineOperator compensatoryAndCombineOp = new CompensatoryAndAntecedentCombineOperator();
   
  /**
   * @return String the name of the Jess function
   */
  public String getName()
  {
	return "set-default-antecedent-combine-operator";
  }

  /**
   * @param vv a ValueVector with the function arguments (1 argument). The
   *           argument is a symbol or string and must be one of minimum, product
   *           or compensatoryAnd.
   * @param context
   * @exception JessException 
   * @return Value TRUE if the setting of the default combine operator is successful
   *         or throws a JessException if it fails (argument was not in correct range of allowed
   *         values).
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String defaultCombineOperatorString;
	AntecedentCombineOperator defaultCombineOperator;

	// must be 1 argument
	if (vv.size() != 2)
	   throw new JessException(getName(),
				"Expects exactly 1 argument (with value minimum, product or compensatoryAnd)" ,"");

	v1 = vv.get(1).resolveValue(context);
	defaultCombineOperatorString = v1.stringValue(context).toLowerCase();

    if (defaultCombineOperatorString.equals("minimum"))
        defaultCombineOperator = minimumCombineOp;
	else if (defaultCombineOperatorString.equals("product"))
		defaultCombineOperator = productCombineOp;
	else if (defaultCombineOperatorString.equals("compensatoryand"))
		defaultCombineOperator = compensatoryAndCombineOp;
    else
	    throw new JessException(getName(),
				"Expects exactly 1 argument (with value minimum, product or compensatoryAnd)" ,"");

    FuzzyRule.setDefaultAntecedentCombineOperator(defaultCombineOperator);

    return Funcall.TRUE;
  }
}


/**
 * Userfunction class:
 * <p>
 * Implements the Jess user function (get-default-antecedent-combine-operator)
 * <p>
 * The function takes no arguments. Returns one of:
 * <p>
 * minimum - the minimum operator is the current default.
 * <br>
 * product - the product operator is the current default.
 * <br>
 * compensatoryAnd - the 'compensatory and' operator is the current default.
 * <br>
 *
 */
public class getDefaultAntecedentCombineOperator implements Userfunction
{
  /**
   * @return String the name of the Jess function
   */
  public String getName()
  {
	return "get-default-antecedent-combine-operator";
  }

  /**
   * @exception JessException 
   * @return an RU.ATOM with minimum or product as a value
   *         or throws a JessException if it fails (has arguments or
   *         not a recognized operator .... may be an internal error
   *         suggesting that the FuzzyJess function needs to be updated to
   *         reflect new operators that have been added.
   */
  public Value call(ValueVector vv, Context context) throws JessException
  {	Value v1;
  	String defaultAntecedentCombineOperatorString;
  	AntecedentCombineOperator defaultAntecedentCombineOperator;

	// must be no arguments
	if (vv.size() != 1)
	   throw new JessException(getName(),
				 "Expects no arguments" ,"");

    defaultAntecedentCombineOperator = FuzzyRule.getDefaultAntecedentCombineOperator();
    
    if (defaultAntecedentCombineOperator instanceof MinimumAntecedentCombineOperator)
        defaultAntecedentCombineOperatorString = "minimum";
	else if (defaultAntecedentCombineOperator instanceof ProductAntecedentCombineOperator)
		defaultAntecedentCombineOperatorString = "product";
	else if (defaultAntecedentCombineOperator instanceof CompensatoryAndAntecedentCombineOperator)
		defaultAntecedentCombineOperatorString = "compensatoryAnd";
    else
	    throw new JessException(getName(),
				"Unexpected AntecedentCombineOperator", "");
    
    return (new Value(defaultAntecedentCombineOperatorString, RU.SYMBOL) );
  }
}



}
