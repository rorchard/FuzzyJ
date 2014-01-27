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

import java.lang.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * A FuzzyRule holds three sets FuzzyValues for the antecedents, conclusions 
 * and input values of a rule.
 * A rule might be written as follows:
 *
 * <pre>
 *   if antecedent1 and
 *      antecedent2 and
 *          ...
 *      antecedentn
 *   then
 *      conclusion1 and
 *      conclusion2 and
 *          ...
 *      conclusionm
 * </pre>
 *
 * Attaching a set of fuzzy value inputs to the rule that correspond to
 * actual values for the antecedents, a set of actual conclusions can be
 * determined by executing (firing) the rule, using the FuzzyRuleExecutor
 * that is associated with a the rule. The executor will generally 
 * implement one of the common algorithms for fuzzy inferencing such as
 * the Mamdani min inference operator with the Max-Min composition operator.
 * <p>
 * Note that this allows only very simple rules that have only fuzzy values
 * on the left hand side and right hand side. The fuzzy package is integrate with
 * the Jess expert system shell and provides the ability to create much 
 * more complex rules, combining crisp and fuzzy values. It also provides
 * a simpler way to build the rule systems.
 * <p>
 * An very simple example of Java code that implements a rule and its
 * firing follows:
 * <pre>
        // some values used to describe fuzzy terms 
		double xHot[] = {25, 35};
		double yHot[] = {0, 1};
		double xCold[] = {5, 15};
		double yCold[] = {1, 0};
		// A few FuzzyValues
	    FuzzyValue fval = null;
	    FuzzyValue fval2 = null;
	    FuzzyValue fval3 = null;
        FuzzyValue fvals[] = new FuzzyValue[2];
        // define our temperature fuzzy variable
        FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C");
	    temp.addTerm("hot", xHot, yHot, 2);
	    temp.addTerm("cold", xCold, yCold, 2);
	    temp.addTerm("veryHot", "very hot");
	    temp.addTerm("medium", "(not hot and (not cold))");
        // define our pressure fuzzy variable
        FuzzyVariable pressure = new FuzzyVariable("pressure", 0, 10, "kilo-pascals");
	    pressure.addTerm("low", new ZFuzzySet(2.0, 5.0));
	    pressure.addTerm("medium", new PIFuzzySet(5.0, 2.5));
	    pressure.addTerm("high", new SFuzzySet(5.0, 8.0)); 
        // let's try some rules --- 
        FuzzyRule rule1 = new FuzzyRule();
        fval = new FuzzyValue( temp, "hot");
        fval2 = new FuzzyValue( pressure, "low or medium");
        fval3 = new FuzzyValue( temp, "very medium");
        rule1.addAntecedent(fval);
        rule1.addConclusion(fval2);
        rule1.addInput(fval3);
        // execute this simple rule with a single antecedent and 
        // a single consequent using default rule executor --
        // MamdaniMinMaxMinRuleExecutor
        FuzzyValueVector fvv = rule1.execute();
        // show the results
	    fvals[0] = fval;
	    fvals[1] = fval3;
	    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 50, fvals));
	    System.out.println(fval2.plotFuzzyValue("*", 0, 10));
	    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
	    // execute again with a different rule executor -- 
	    // LarsenProductMaxMinRuleExecutor
        fvv = rule1.execute(new LarsenProductMaxMinRuleExecutor());
        // and show results
	    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
	
	the above would produce the following graphs as output
	
	The Antecedent (hot) and the input (very medium) fuzzy values
	
    Fuzzy Value: temperature
    Linguistic Value: hot (*),  very medium (+)

     1.00               +++++++++++         ****************
     0.95
     0.90                                  *
     0.85
     0.80              +           +      *
     0.75
     0.70                                *
     0.65             +             +
     0.60                               *
     0.55
     0.50            +               + *
     0.45
     0.40                             *
     0.35           +                 +
     0.30                            *
     0.25          +                   +
     0.20                           *
     0.15         +                     +
     0.10        +                 *     +
     0.05       +                         +
     0.00+++++++*******************        +++++++++++++++++
         |----|----|----|----|----|----|----|----|----|----|
        0.00     10.00     20.00     30.00     40.00     50.00

    The conclusion fuzzy value.

    Fuzzy Value: pressure
    Linguistic Value: low or medium (*)

     1.00************            ***
     0.95            *          *   *
     0.90             *        *     *
     0.85              *
     0.80                     *       *
     0.75               *
     0.70                    *
     0.65                *             *
     0.60
     0.55                 * *           *
     0.50
     0.45                  *
     0.40                   *            *
     0.35
     0.30
     0.25                                 *
     0.20
     0.15                                  *
     0.10                                   *
     0.05                                    *
     0.00                                     **************
         |----|----|----|----|----|----|----|----|----|----|
        0.00      2.00      4.00      6.00      8.00     10.00

    The output using MamdaniMinMaxMinRuleExecutor
    
    Fuzzy Value: pressure
    Linguistic Value: ??? (*)

     1.00
     0.95
     0.90
     0.85
     0.80
     0.75
     0.70
     0.65
     0.60
     0.55
     0.50
     0.45
     0.40*********************************
     0.35
     0.30
     0.25                                 *
     0.20
     0.15                                  *
     0.10                                   *
     0.05                                    *
     0.00                                     **************
         |----|----|----|----|----|----|----|----|----|----|
        0.00      2.00      4.00      6.00      8.00     10.00

    The conclusion using LarsenProductMaxMinRuleExecutor.
     
    Fuzzy Value: pressure
    Linguistic Value: ??? (*)

     1.00
     0.95
     0.90
     0.85
     0.80
     0.75
     0.70
     0.65
     0.60
     0.55
     0.50
     0.45
     0.40************             *
     0.35            ***       *** ***
     0.30               *     *       *
     0.25                *   *         *
     0.20                 * *           *
     0.15                  **            *
     0.10                                 *
     0.05                                  **
     0.00                                    ***************
         |----|----|----|----|----|----|----|----|----|----|
        0.00      2.00      4.00      6.00      8.00     10.00	
 * </pre>
 *
 * @author Bob Orchard
 *
 * @see nrc.fuzzy.MamdaniMinMaxMinRuleExecutor
 * @see nrc.fuzzy.LarsenProductMaxMinRuleExecutor
 * @see nrc.fuzzy.FuzzyValue
 * @see nrc.fuzzy.FuzzyVariable
 * @see nrc.fuzzy.FuzzySet
 * 
 */

public class FuzzyRule implements Serializable
{
    /** A vector of FuzzyValues that represent the antecedents in the rule */
    private FuzzyValueVector antecedents;
    
    /** A vector of FuzzyValues that represent the conclusions in the rule */
    private FuzzyValueVector conclusions;
    
    /** A vector of FuzzyValues that represent the inputs for the rule */
    private FuzzyValueVector inputs;
    
    /** The FuzzyRuleExecutor that will be associated with a rule when it
     *  is created and no executor is specified */
    private static FuzzyRuleExecutor defaultExecutor = new MamdaniMinMaxMinRuleExecutor();

    /** The FuzzyRuleExecutor that will be used when the rule is executed (fired)
     *  to generate the fuzzy value output vector */
    private FuzzyRuleExecutor executor;

    /** The default operation used to combine the match values for multiple antecendent/input
     * pairs in a rule  (initially it is the minimum of the values)
     */
    private static AntecedentCombineOperator defaultAntecedentCombineOperator = new MinimumAntecedentCombineOperator();

    /** The operation used to combine the match values for multiple antecedent/input
     * pairs in this rule  (minimum or product)
     */
    private AntecedentCombineOperator antecedentCombineOperator;

    /** A flag that is set to true when the antecedents set has changed between
        rule firings */
    private boolean antecedentsChanged;
    
    /** A flag that is set to true when the conclusion set has changed between
     *  rule firings */
    private boolean conclusionsChanged;
    
    /** A flag that is set to true when the input set has changed between
     *  rule firings */
    private boolean inputsChanged;
    
    /** A flag that is set to true when the operation (minimum or product) for
     *  combining antecedent/input match values has changed between rule firings
     */
    private boolean antecedentCombineOperatorChanged;
    
    /**
     * Create a FuzzyRule with a default FuzzyRuleExecutor. Currently the initial
     * default executor is the MamdaniMinMaxMinRuleExecutor. This can be changed with the method 
     * setDefaultRuleExecutor. Note that each FuzzyRule that is constructed actually gets it own 
     * rule executor. To ensure this the default FuzzyRuleExecutor 
     * is copied.  This provides the rule executors with the option to remember 
     * state from one rule execution to another that will possibly allow it to be 
     * more efficient. For example, the MamdaniMinMaxMinRuleExecutor calculates the 
     * DOF (degree of matching/fulfillment) of all of the input/antecedent pairs. If only the outputs
     * change then the DOF is still valid and the overhead of generating the outputs is 
     * considerably reduced.
     * <br>
     * The DOF for all input/antecedent pairs can be calculated in many ways. This is determined by
     * the rule's antecedentCombineOperator setting. Supported operations include the minimum of the
     * matching value for all of the pairs and the product of the values.
     */

    public FuzzyRule()
    {
        antecedents = new FuzzyValueVector();
        conclusions = new FuzzyValueVector();
        inputs = new FuzzyValueVector();
        antecedentCombineOperator = defaultAntecedentCombineOperator;
        try
         { Method m = defaultExecutor.getClass().getMethod("clone",null);
           executor = (FuzzyRuleExecutor)m.invoke(defaultExecutor, null);
         }
        catch (Exception e)
         {
            executor = defaultExecutor;
            System.err.println("Error in FuzzyRule constructor: expects a clone method for FuzzyRuleExecutor (clone not done)\n" + e);
         }
        antecedentsChanged = true;
        conclusionsChanged = true;
        inputsChanged = true;
        antecedentCombineOperatorChanged = true;
    }
    
    /**
     * Create a FuzzyRule with a specified FuzzyRuleExecutor.
     * Note that each FuzzyRule that is constructed actually gets it own
     * rule executor. To ensure this the FuzzyRuleExecutor supplied to this
     * constructor is copied. This provides the rule executors with the option to remember
     * state from one rule execution to another that will possibly allow it to be 
     * more efficient. For example, the MamdaniMinMaxMinRuleExecutor calculates the 
     * DOF (degree of matching/fulfillment) of all of the input/antecedent pairs. If only the outputs
     * change then the DOF is still valid and the overhead of generating the outputs is 
     * considerably reduced.
     * <br>
     * The DOF for all input/antecedent pairs can be calculated in many ways. This is determined by
     * the rule's antecedentCombineOperator setting. Supported operations include the minimum of the
     * matching value for all of the pairs and the product of the values.
     *
     * @param exec the rule executor to use with this rule
     */

    public FuzzyRule( FuzzyRuleExecutor exec )
    {
        antecedents = new FuzzyValueVector();
        conclusions = new FuzzyValueVector();
        inputs = new FuzzyValueVector();
        antecedentCombineOperator = defaultAntecedentCombineOperator;
        // since each rule requires its own unique rule executor
        // object (if state is stored in the object) then perhaps we
        // should make a copy of the FuzzyRuleExecutor here??
        try 
         { Method m = exec.getClass().getMethod("clone",null);
           executor = (FuzzyRuleExecutor)m.invoke(exec, null);        
         }
        catch (Exception e) 
         {
            executor = exec;
            System.err.println("Error in FuzzyRule constructor: expects a clone method for FuzzyRuleExecutor (clone not done)\n" + e);
         }
        antecedentsChanged = true;
        conclusionsChanged = true;
        inputsChanged = true;
        antecedentCombineOperatorChanged = true;
    }

    /**
     * Create a FuzzyRule with a specified FuzzyRuleExecutor and an antecedentCombineOperator.
     * Note that each FuzzyRule that is constructed actually gets it own
     * rule executor. To ensure this the FuzzyRuleExecutor supplied to this
     * constructor is copied. This provides the rule executors with the option to remember
     * state from one rule execution to another that will possibly allow it to be
     * more efficient. For example, the MamdaniMinMaxMinRuleExecutor calculates the
     * DOF (degree of matching/fulfillment) of all of the input/antecedent pairs. If only the outputs
     * change then the DOF is still valid and the overhead of generating the outputs is
     * considerably reduced.
     * <br>
     * The DOF for all input/antecedent pairs can be calculated in many ways. This is determined by
     * the rule's antecedentCombineOperator setting. Supported operations include the minimum of the
     * matching value for all of the pairs and the product of the values.
     *
     * @param exec the rule executor to use with this rule
     * @param combineOperator the operator to use to combine the match values of the
     *        antecedent/input pairs of the rule 
     */

    public FuzzyRule( FuzzyRuleExecutor exec, AntecedentCombineOperator combineOperator)
    {
        antecedents = new FuzzyValueVector();
        conclusions = new FuzzyValueVector();
        inputs = new FuzzyValueVector();
        antecedentCombineOperator = combineOperator;

        // since each rule requires its own unique rule executor
        // object (if state is stored in the object) then perhaps we
        // should make a copy of the FuzzyRuleExecutor here??
        try 
         { Method m = exec.getClass().getMethod("clone",null);
           executor = (FuzzyRuleExecutor)m.invoke(exec, null);        
         }
        catch (Exception e) 
         {
            executor = exec;
            System.err.println("Error in FuzzyRule constructor: expects a clone method for FuzzyRuleExecutor (clone not done)\n" + e);
         }
        antecedentsChanged = true;
        conclusionsChanged = true;
        inputsChanged = true;
        antecedentCombineOperatorChanged = true;
    }

    /**
     * Create a FuzzyRule with a specified antecedentCombineOperator.
     * Note that each FuzzyRule that is constructed actually gets it own
     * rule executor. To ensure this the FuzzyRuleExecutor supplied to this
     * constructor is copied. This provides the rule executors with the option to remember
     * state from one rule execution to another that will possibly allow it to be
     * more efficient. For example, the MamdaniMinMaxMinRuleExecutor calculates the
     * DOF (degree of matching/fulfillment) of all of the input/antecedent pairs. If only the outputs
     * change then the DOF is still valid and the overhead of generating the outputs is
     * considerably reduced.
     * <br>
     * The DOF for all input/antecedent pairs can be calculated in many ways. This is determined by
     * the rule's antecedentCombineOperator setting. Supported operations include the minimum of the
     * matching value for all of the pairs and the product of the values.
     *
     * @param combineOperator the operator to use to combine the match values of the
     *        antecedent/input pairs of the rule 
     */

    public FuzzyRule(AntecedentCombineOperator combineOperator)
    {
        antecedents = new FuzzyValueVector();
        conclusions = new FuzzyValueVector();
        inputs = new FuzzyValueVector();
        antecedentCombineOperator = combineOperator;

        // since each rule requires its own unique rule executor
        // object (if state is stored in the object) then perhaps we
        // should make a copy of the FuzzyRuleExecutor here??
        try
         { Method m = defaultExecutor.getClass().getMethod("clone",null);
           executor = (FuzzyRuleExecutor)m.invoke(defaultExecutor, null);
         }
        catch (Exception e)
         {
            executor = defaultExecutor;
            System.err.println("Error in FuzzyRule constructor: expects a clone method for FuzzyRuleExecutor (clone not done)\n" + e);
         }
        antecedentsChanged = true;
        conclusionsChanged = true;
        inputsChanged = true;
        antecedentCombineOperatorChanged = true;
    }

    /**
     * Add an antecedent FuzzyValue to the to the end of the set of 
     * antecedents for this rule.
     *
     * @param fval The FuzzyValue to add.
     */
    public void addAntecedent( FuzzyValue fval )
    {
        antecedents.addFuzzyValue( fval );
        antecedentsChanged = true;
    }
    
    /** 
     * Insert an antecedent FuzzyValue into the set of 
     * antecedents for this rule at the position indicated.
     *
     * @param fval The FuzzyValue to add.
     * @param i The index (position) at which to add the FuzzyValue (zero based).
     */
    public void insertAntecedentAt( FuzzyValue fval, int i )
    {
        antecedents.insertFuzzyValueAt( fval, i );
        antecedentsChanged = true;
    }
    
    /** 
     * Remove an antecedent FuzzyValue from the set of 
     * antecedents for this rule at the position indicated.
     *
     * @param i The index (position) from which the FuzzyValue should be removed (zero based).
     */
    public void removeAntecedentAt( int i )
    {
        antecedents.removeFuzzyValueAt( i );
        antecedentsChanged = true;
    }
    
    /** 
     * Remove all antecedent FuzzyValues from the set of 
     * antecedents for this rule.
     *
     */
    public void removeAllAntecedents( )
    {
        antecedents = new FuzzyValueVector();
        antecedentsChanged = true;
    }
    
    /**
     * Get the antecedent FuzzyValue from the set of 
     * antecedents for this rule at the position indicated.
     *
     * @param i The index (position) at which to get the FuzzyValue (zero based).
     */
    public FuzzyValue antecedentAt( int i )
    {
        return antecedents.fuzzyValueAt( i );
    }
    
    /**
     * Return the size of the set of antecedents (the number of antecedents in
     * the rule.
     */
    public int antecedentsSize()
    {
        return antecedents.size();
    }
    
    /**
     * Return the set of antecedents in a FuzyValueVector.
     */
    public FuzzyValueVector getAntecedents()
    {
        return antecedents;
    }

    /** 
     * Add a conclusion FuzzyValue to the to the end of the set of 
     * conclusions for this rule.
     *
     * @param fval The FuzzyValue to add.
     */
    public void addConclusion( FuzzyValue fval )
    {
        conclusions.addFuzzyValue( fval );
        conclusionsChanged = true;
    }
    
    /** 
     * Insert a conclusion FuzzyValue into the set of 
     * conclusions for this rule at the position indicated.
     *
     * @param fval The FuzzyValue to add.
     * @param i The index (position) at which to add the FuzzyValue (zero based).
     */
    public void insertConclusionAt( FuzzyValue fval, int i )
    {
        conclusions.insertFuzzyValueAt( fval, i );
        conclusionsChanged = true;
    }
    
    /** 
     * Remove a conclusion FuzzyValue from the set of 
     * conclusions for this rule at the position indicated.
     *
     * @param i The index (position) from which the FuzzyValue should be removed (zero based).
     */
    public void removeConclusionAt( int i )
    {
        conclusions.removeFuzzyValueAt( i );
        conclusionsChanged = true;
    }
    
    /** 
     * Remove all conclusion FuzzyValues from the set of 
     * conclusions for this rule.
     *
     */
    public void removeAllConclusions( )
    {
        conclusions = new FuzzyValueVector();
        conclusionsChanged = true;
    }
    
    /**
     * Get the conclusion FuzzyValue from the set of 
     * conclusions for this rule at the position indicated.
     *
     * @param i The index (position) at which to get the FuzzyValue (zero based).
     */
    public FuzzyValue conclusionAt( int i )
    {
        return conclusions.fuzzyValueAt( i );
    }
    
    /**
     * Return the size of the set of conclusions (the number of conclusions in
     * the rule.
     */
    public int conclusionsSize()
    {
        return conclusions.size();
    }
    
    /**
     * Return the set of conclusions in a FuzzyValueVector.
     */
    public FuzzyValueVector getConclusions()
    {
        return conclusions;
    }

    /** 
     * Add an input FuzzyValue to the to the end of the set of 
     * inputs for this rule.
     *
     * @param fval The FuzzyValue to add.
     */
    public void addInput( FuzzyValue fval )
    {
        inputs.addFuzzyValue( fval );
        inputsChanged = true;
    }
    
    /** 
     * Insert an input FuzzyValue into the set of 
     * inputs for this rule at the position indicated.
     *
     * @param fval The FuzzyValue to add.
     * @param i The index (position) at which to add the FuzzyValue (zero based).
     */
    public void insertInputAt( FuzzyValue fval, int i )
    {
        inputs.insertFuzzyValueAt( fval, i );
        inputsChanged = true;
    }
    
    /** 
     * Remove an input FuzzyValue from the set of 
     * inputs for this rule at the position indicated.
     *
     * @param i The index (position) from which the FuzzyValue should be removed (zero based).
     */
    public void removeInputAt( int i )
    {
        inputs.removeFuzzyValueAt( i );
        inputsChanged = true;
    }
    
    /** 
     * Remove all input FuzzyValues from the set of 
     * inputs for this rule.
     *
     */
    public void removeAllInputs( )
    {
        inputs = new FuzzyValueVector();
        inputsChanged = true;
    }
    
    /**
     * Get the input FuzzyValue from the set of 
     * inputs for this rule at the position indicated.
     *
     * @param i The index (position) at which to get the FuzzyValue (zero based).
     */
    public FuzzyValue inputAt( int i )
    {
        return inputs.fuzzyValueAt( i );
    }
    
    /**
     * Return the set of inputs in a FuzyValueVector.
     */
    public FuzzyValueVector getInputs()
    {
        return inputs;
    }

    /**
     * Return the size of the set of inputs (the number of inputs in
     * the rule.
     */
    public int inputSize()
    {
        return inputs.size();
    }
    
    
    /**
     * Set of change status of the antecedents, conclusions and inputs to false.
     */
    private void setAllChangedFlagsFalse()
    {
        antecedentsChanged = false;
        conclusionsChanged = false;
        inputsChanged = false;
        antecedentCombineOperatorChanged = false;
    }
    
    /**
     * Set of change status of the antecedents, conclusions and inputs to true.
     */
    private void setAllChangedFlagsTrue()
    {
        antecedentsChanged = true;
        conclusionsChanged = true;
        inputsChanged = true;
        antecedentCombineOperatorChanged = true;
    }
    
    /**
     * Get of change status of the antecedent combine operator. The value will be true if
     * the operator is changed between rule firings.
     */
    public boolean isAntecendentCombineOperatorChanged()
    {
        return antecedentCombineOperatorChanged;
    }

    /**
     * Get of change status of the antecedents. The value will be true if
     * any antecedent is added or removed between rule firings.
     */
    public boolean isAntecedentsChanged()
    {
        return antecedentsChanged;
    }

    /**
     * Get of change status of the conclusions. The value will be true if
     * any conclusion is added or removed between rule firings.
     */
    public boolean isConclusionsChanged()
    {
        return conclusionsChanged;
    }
    
    /**
     * Get of change status of the inputs. The value will be true if
     * any input is added or removed between rule firings.
     */
    public boolean isInputsChanged()
    {
        return inputsChanged;
    }
    
    /**
     * Set the rule's executor to the one provided. This allows one to change
     * the method of rule inferencing dynamically -- simple to fire it
     * several times in a row with different executors. Note that changing
     * the rule executor will also set all 'changed' flags to true since
     * the impact is severe enough that any state saved by an executor
     * will not be valid.
     *
     * @param exec The FuzzyRuleExecutor that will now be used to execute
     *             the rule.
     */
    public void setRuleExecutor( FuzzyRuleExecutor exec )
    {
        try 
         { Method m = exec.getClass().getMethod("clone",null);
           executor = (FuzzyRuleExecutor)m.invoke(exec, null);        
         }
        catch (Exception e) 
         {
            executor = exec;
            System.err.println("Error in setRuleExecutor: expects a clone method for FuzzyRuleExecutor (clone not done)\n" + e);
         }
        setAllChangedFlagsTrue();
    }
    
    /**
     * Get the rule's current rule executor. 
     *
     * @return Returns the rule's FuzzyRuleExecutor.
    */
    public FuzzyRuleExecutor getRuleExecutor( )
    {
        return executor;
    }
 

    /**
     * Set the rule's default executor to the one provided. This allows one to change
     * the default method of rule inferencing dynamically. 
     *
     * @param exec The FuzzyRuleExecutor that will now be used to as the 
     *             default when a rule is created and an executor is not specified.
     */
    public static void setDefaultRuleExecutor( FuzzyRuleExecutor exec )
    {
        defaultExecutor = exec;
    }
    
    /**
     * Get the default executor for FuzzyRules. This is the FuzzyRuleExecutor 
     * assigned to the rule when it is create by a constructor with no arguments.
     *
     * @return Returns the default rule Executor for fuzzy rules.
    */
    public static FuzzyRuleExecutor getDefaultRuleExecutor( )
    {
        return defaultExecutor;
    }
    
    /**
     * Set the rule's antecedent combine operator to the one provided.
     * This allows one to change the method of combining the match values of the
     * antecedent/input pairs in a rule. Note that changing
     * the operator will also set all 'changed' flags to true since
     * the impact is severe enough that any state saved by an executor
     * will not be valid.
     *
     * @param combineOperator The operator that will now be used to
     *             combine match values of the antecedent/input pairs in the rule.
     */
    public void setAntecedentCombineOperator( AntecedentCombineOperator combineOperator )
    {
        antecedentCombineOperator = combineOperator;
        setAllChangedFlagsTrue();
    }
    
    /**
     * Get the rule's current antecedent combine operator. 
     *
     * @return Returns the rule's antecedentCombineOperator.
    */
    public AntecedentCombineOperator getAntecedentCombineOperator( )
    {
        return antecedentCombineOperator;
    }

    /**
     * Set the rule's default antecedent combine operator to the one provided.
     *
     * @param combineOperator The operator that will now be used.
     */
    public static void setDefaultAntecedentCombineOperator( AntecedentCombineOperator combineOperator )
    {
        defaultAntecedentCombineOperator = combineOperator;
    }
    
    /**
     * Get the default antecedent combine operator for FuzzyRules. This is the operator 
     * assigned to the rule when it is create by a constructor with no arguments or 
     * with only the rule executor specified.
     *
     * @return Returns the default rule antecedent combine operator.
    */
    public static AntecedentCombineOperator getDefaultAntecedentCombineOperator( )
    {
        return defaultAntecedentCombineOperator;
    }

    /**
     * Test to see if the rule antecedents match the rule inputs
     * with enough overlap to ensure rule will fire with meaninful
     * outputs. The test is that all of the antecedent/input pairs
     * overlap at least to degree as specified by the default
     * tolerance or a specified tolerance.
     *
     * @return Returns true if the antecedent and input pairs of the rule
     *          match within the specified or default tolerance
     * @exception IncompatibleRuleInputsException
     */
    public boolean testRuleMatching()
        throws IncompatibleRuleInputsException
    {
        return doTestRuleMatching( FuzzyValue.getMatchThreshold(), this.getAntecedents(),  this.getInputs());
    }
    
    /**
     * Test to see if the rule antecedents match the rule inputs
     * with enough overlap to ensure rule will fire with meaninful
     * outputs. The test is that all of the antecedent/input pairs
     * overlap at least to degree as specified by the default
     * tolerance or a specified tolerance.
     *
     * @param threshold The threshold for matching fuzzy values
     * @return Returns true if the antecedent and input pairs of the rule
     *          match within the specified or default tolerance
     * @exception IncompatibleRuleInputsException
     */
    public boolean testRuleMatching(double threshold)
        throws IncompatibleRuleInputsException
    {
        return doTestRuleMatching( threshold, this.getAntecedents(),  this.getInputs());
    }


    /**
     * Test to see if the rule antecedents match the rule inputs
     * with enough overlap to ensure rule will fire with meaninful
     * outputs. The test is that all of the antecedent/input pairs
     * overlap at least to degree as specified by the default
     * tolerance or a specified tolerance. In this case the rule's inputs
     * are ignored and the inputs provided in the paramter list are used.
     *
     * @return Returns true if the antecedent and input pairs of the rule
     *          match within the specified or default tolerance
     * @param inputs The inputs to be used in the test for the rule
     * @exception IncompatibleRuleInputsException
     */
    public boolean testRuleMatching(FuzzyValueVector inputs)
        throws IncompatibleRuleInputsException
    {
        return doTestRuleMatching( FuzzyValue.getMatchThreshold(), this.getAntecedents(),  inputs);
    }
    
    /**
     * Test to see if the rule antecedents match the rule inputs
     * with enough overlap to ensure rule will fire with meaninful
     * outputs. The test is that all of the antecedent/input pairs
     * overlap at least to degree as specified by the default
     * tolerance or a specified tolerance.  In this case the rule's inputs
     * are ignored and the inputs provided in the paramter list are used.
     *
     * @param threshold The threshold for matching fuzzy values.
     * @param inputs The inputs to be used in the test for the rule.
     * @return Returns true if the antecedent and input pairs of the rule
     *          match within the specified or default tolerance.
     * @exception IncompatibleRuleInputsException
     */
    public boolean testRuleMatching(double threshold, FuzzyValueVector inputs)
        throws IncompatibleRuleInputsException
    {
        return doTestRuleMatching( threshold, this.getAntecedents(),  inputs);
    }
    
    /**
     * Does the work for the testRuleMatching methods.
     *
     * @param threshold The threshold for matching fuzzy values.
     * @param antecedents The antecedents for the rule.
     * @param inputs The inputs for the rule.
     * @return True if the matching passed the threshold, else false.
     * @exception IncompatibleRuleInputsException
     */
    public boolean doTestRuleMatching(double threshold, FuzzyValueVector antecedents, FuzzyValueVector inputs)
        throws IncompatibleRuleInputsException
    {
        int i;
        boolean matchTest = true;

        // antecedents and inputs must be same size and have matching FuzzyVariables
        if (antecedents.size() != inputs.size())
            throw new IncompatibleRuleInputsException("Number of Inputs must equal number of Antecedents");
        
        for (i = 0; i<antecedents.size(); i++)
        {
            FuzzyValue aFv = antecedents.fuzzyValueAt(i);
            FuzzyValue iFv = inputs.fuzzyValueAt(i);
            try 
            {  if (!aFv.fuzzyMatch(iFv, threshold)) 
               {    matchTest = false;
                    break;
               }
            }
            catch (IncompatibleFuzzyValuesException e)
            { throw new IncompatibleRuleInputsException("Corresponding Inputs and Antecedents must share the same FuzzyVariable");
            }
        }
        
        return matchTest;
    }
    
    /**
     * Execute (fire) the rule. In order to be successful the number of
     * antecedents and inputs must be the same and the corresponding 
     * fuzzy variable types of the antecedent/input pairs must be the same.
     * The method calls the execute method of the associated FuzzyRuleExecutor 
     * that is associated with the rule and returns a vector of the 
     * output FuzzyValues determined by the rule execution.
     *
     * @return A vector of the output FuzzyValues determined by the 
     *          rule execution. If there are no outputs then a vector with
     *          size of zero will result.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute()
        throws IncompatibleRuleInputsException
    {
        FuzzyValueVector fvv = executor.execute( this );
        // once the rule is executed with its rule executor any state
        // saved will be OK until something changes (this relies
        // on each rule having its own copy of a rule executor!!!)
        setAllChangedFlagsFalse();
        return fvv;
    }
               
    /**
     * Execute (fire) the rule. In order to be successful the number of
     * antecedents and inputs must be the same and the corresponding 
     * fuzzy variable types of the antecedent/input pairs must be the same.
     * The method calls the execute method of the sup[plied FuzzyRuleExecutor 
     * that is associated with the rule and returns a vector of the 
     * output FuzzyValues determined by the rule execution.
     *
     * @param exec The rule executor to use rather than the one associated with
     *             the rule.
     * @return A vector of the output FuzzyValues determined by the 
     *          rule execution. If there are no outputs then a vector with
     *          size of zero will result.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute( FuzzyRuleExecutor exec )
        throws IncompatibleRuleInputsException
    {
        // when a different rule executor is used than was originally
        // associated with the rule, any state saved in the rule executor
        // will not be valid
        setAllChangedFlagsTrue();
        return exec.execute( this );
    }
      
    
    /**
     * Execute (fire) the rule. In order to be successful the number of
     * antecedents and inputs must be the same and the corresponding 
     * fuzzy variable types of the antecedent/input pairs must be the same.
     * The method calls the execute method of the associated FuzzyRuleExecutor 
     * that is associated with the rule and returns a vector of the 
     * output FuzzyValues determined by the rule execution. In this case the rule
     * is fired using a specified set of inputs rather than those associated
     * with the rule.
     *
     * @param inputs The rule is executed with the specified inputs (rather than with
     *               the inputs currently associated with the rule)
     * @return A vector of the output FuzzyValues determined by the 
     *          rule execution. If there are no outputs then a vector with
     *          size of zero will result.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute(FuzzyValueVector inputs)
        throws IncompatibleRuleInputsException
    {
        // when different inputs than were originally
        // associated with the rule are used, any state saved in the rule executor
        // will not be valid
        setAllChangedFlagsTrue();
        return executor.execute( this.getAntecedents(), this.getConclusions(), inputs );
    }
               
    /**
     * Execute (fire) the rule. In order to be successful the number of
     * antecedents and inputs must be the same and the corresponding 
     * fuzzy variable types of the antecedent/input pairs must be the same.
     * The method calls the execute method of the sup[plied FuzzyRuleExecutor 
     * that is associated with the rule and returns a vector of the 
     * output FuzzyValues determined by the rule execution.  In this case the rule
     * is fired using a specified set of inputs rather than those associated
     * with the rule.
     *
     * @param exec The rule executor to use rather than the one associated with
     *             the rule.
     * @param inputs The rule is executed with the specified inputs (rather than with
     *               the inputs currently associated with the rule)
     * @return A vector of the output FuzzyValues determined by the 
     *          rule execution. If there are no outputs then a vector with
     *          size of zero will result.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute( FuzzyRuleExecutor exec, FuzzyValueVector inputs)
        throws IncompatibleRuleInputsException
    {
        // when a different rule executor or different inputs than were originally
        // associated with the rule are used, any state saved in the rule executor
        // will not be valid
        setAllChangedFlagsTrue();
        return exec.execute( this.getAntecedents(), this.getConclusions(), inputs );
    }
      
    /**
     * Check that the antecedents and inputs for a rule execution are compatible
     * (same number of them and pairwise they have the same fuzzy variables);
     * This is provided for the FuzzyRuleExecutors, each of which should call 
     * this method to verify that they are compatible.
     *
     * @param antecedents The set of antecedents for a rule
     * @param inputs The set of inputs for a rule
     * @exception IncompatibleRuleInputsException
     */
    static void checkAntecedentsAndInputs( FuzzyValueVector antecedents, 
                                           FuzzyValueVector inputs
                                         )
        throws IncompatibleRuleInputsException
    {
        int i;
        // antecedents and inputs must be same size and have matching FuzzyVariables
        if (antecedents.size() != inputs.size())
            throw new IncompatibleRuleInputsException("Number of Inputs must equal number of Antecedents");

        for (i = 0; i<antecedents.size(); i++)
            if (antecedents.fuzzyValueAt(i).getFuzzyVariable() !=
                inputs.fuzzyValueAt(i).getFuzzyVariable())
                throw new IncompatibleRuleInputsException("Corresponding Inputs and Antecedents must share the same FuzzyVariable");
    }
               
}
