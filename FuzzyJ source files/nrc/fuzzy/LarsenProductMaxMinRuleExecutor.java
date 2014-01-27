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

/**
 * Implements the FuzzyRuleExecutor interface providing an execute method
 * that accepts a FuzzyRule object and returns a FuzzyValueVector composed
 * of the actual conclusion FuzzyValues for the rule using the
 * Larsen Product inference operator and MaxMin composition. This 
 * method has the rather nice property that the outputs are generated from the
 * conclusions by 'scaling' them by a value (DOF -- Degree of 
 * Fullment of the rule) that is calculated as the minimum (or product etc. 
 * depending on current AntecedentCombineOperator)
 * of the maximum membership values of the intersections of the fuzzy value 
 * antecedent and input pairs. So one can calculate the intersections of the pairs,
 * get the maximum membership value for each intersection and find the minimum of these.
 * Should the rule be fired a second time and the antecedents and inputs have not 
 * changed, it is only necessary to clip the consequents, avoiding some
 * expensive calculations. NOTE: In order to do this optimization it is required 
 * the the executor NOT be shared amongst rules. In effect each rule must have its
 * own unique instance of the executor. This is enforced in Rule creation, since
 * each RuleExecutor must implement the cloneable interface and provide a clone method.
 *
 * @author Bob Orchard
 *
 * @see MamdaniMinMaxMinRuleExecutor
 * @see TsukamotoExecutor
 * @see FuzzyRuleExecutor
 * @see AntecedentCombineOperator
 * @see MinimumAntecedentCombineOperator
 * @see ProductAntecedentCombineOperator
 *
 */

public class LarsenProductMaxMinRuleExecutor extends FuzzyRuleExecutor
                                              implements Serializable
{
    // The degree of matching (or DOF -- Degree of rule Fullfillment)
	// for the antecedents and inputs. This value
    // is maintained from rule execution to rule execution. It is expected that
    // any FuzzyRuleExecutor will not be shared amongst rules (i.e. each
    // FuzzyRule has its own FuzzyRuleExecutor) so that we can maintain
    // state between rule firings as required for any FuzzyRuleExecutor.
    // In this case we can reuse the previous calculation of DOF (Degree of
    // Fulfillment of the rule ... i.e. how well the antecedents match their 
    // corresponding actual fuzzy values) if the method of calculating the DOF
    // (minimum or product etc. -- depending on the AntecedentCombineOperator) and the
    // antecedents and inputs have not changed since the last rule firing.
    // This allows one to change the conclusion(s) of the rule and fire it again
    // with the same inputs/antecedents without the overhead of recalulating
    // DOF each time.
    //
    // DOF is the minimum (or product etc.) of the maximum intersection values of all the
    // antecedent/input fuzzy value pairs.
    private double DOF;

    /**
     * Implements the required cloning of a RuleExecutor.
     *
     * @return the newly cloned RuleExecutor
     */
    public Object clone() 
    {
      try 
      {
        LarsenProductMaxMinRuleExecutor re = (LarsenProductMaxMinRuleExecutor)super.clone();
        re.DOF = this.DOF;
        return re;
      } 
      catch (Exception e) 
      {
        // this shouldn't happen because this is Cloneable
        throw new InternalError();
      }
    }
          
         
    /**
     * This execute method implements rule execution using the 
     * Larsen Product inference operator and Max-Min composition.
     * Classes that implement the FuzzyRuleExecutor interface must provide
     * an execute method that accepts a FuzzyRule object and 
     * returns a FuzzyValueVector composed of the actual output FuzzyValues
     * for the rule. 
     * 
     * @param fuzzyRule the rule that is to be executed (fired)
     * @return A FuzzyValueVector with the FuzzyValues that represent the
     *          outputs of the fired rule.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute( FuzzyRule rule )
        throws IncompatibleRuleInputsException
    {
        boolean recalculateDOF = (rule.isAntecedentsChanged() || rule.isInputsChanged() ||
                                  rule.isAntecendentCombineOperatorChanged());

        return doTheExecute( rule.getAntecedents(), rule.getConclusions(),
                             rule.getInputs(), rule.getAntecedentCombineOperator(),
                             recalculateDOF );
    }
    
    /**
     * This execute method implements rule execution using the 
     * Larsen Product inference operator and Max-Min composition.
     * Classes that implement the FuzzyRuleExecutor interface must provide
     * an execute method that accepts 3 FuzzyValueVector objects and 
     * returns a FuzzyValueVector composed of the actual output FuzzyValues
     * for the rule. 
     *
     * @param antecedents The FuzzyValues that represent a rule's antecedents.
     * @param conclusions The FuzzyValues that represent a rule's conclusions.
     * @param inputs The FuzzyValues that represent a rule's inputs.
     * @return A FuzzyValueVector with the FuzzyValues that represent the
     *          outputs of the fired rule.
     * @exception IncompatibleRuleInputsException
     */
    public FuzzyValueVector execute( FuzzyValueVector antecedents, 
                                     FuzzyValueVector conclusions, 
                                     FuzzyValueVector inputs )
        throws IncompatibleRuleInputsException
    {
        // in this case the DOF must always be re-calulated since there is no rule
        // that stores the antecedents and inputs from one invocation to the next
		// and use the default antecedentCombineOperator without a rule
        return doTheExecute( antecedents, conclusions, inputs,
		                     FuzzyRule.getDefaultAntecedentCombineOperator(), true );
    }
    
    /**
     * Actually do the work of the execute in this private method.
     *
     * @param antecedents The FuzzyValues that represent a rule's antecedents.
     * @param conclusions The FuzzyValues that represent a rule's conclusions.
     * @param inputs The FuzzyValues that represent a rule's inputs.
     * @param combineOperator the operator to use when combining the match values
     *                        for the antecedent/input pairs
     * @param recalculateDOF True if we need to recalculate the DOF since the
     *                       antecedents or inputs of the rule have changed.
     * @return A FuzzyValueVector with the FuzzyValues that represent the
     *          outputs of the fired rule.
     * @exception IncompatibleRuleInputsException
     */
    private FuzzyValueVector doTheExecute( FuzzyValueVector antecedents, 
                                           FuzzyValueVector conclusions, 
                                           FuzzyValueVector inputs,
	                                       AntecedentCombineOperator combineOperator, 
                                           boolean recalculateDOF 
                                         )
        throws IncompatibleRuleInputsException
    {
        int i;
        FuzzyValueVector outputs;
        double tempDOF;

        // antecedents and inputs must be same size and have matching FuzzyVariables
        FuzzyRule.checkAntecedentsAndInputs( antecedents, inputs );
        
        // now we can create the outputs -- if size of conclusions is zero
        // return an empty FuzzyValueVector;
        if (conclusions.size() == 0)
            return new FuzzyValueVector(1);

        outputs = new FuzzyValueVector( conclusions.size() );

        if (recalculateDOF)
        { // we must recalculate DOF
		  int len = antecedents.size();
          DOF = 1.0;
		  if (len > 0)
		  { try
			{ double matchValues[] = new double[len];
		      for (i = 0; i<len; i++)
			  { matchValues[i] = antecedents.fuzzyValueAt(i).maximumOfIntersection(inputs.fuzzyValueAt(i));
			  }
			  DOF = (len == 1)? matchValues[0] :
			  					combineOperator.execute(matchValues);
			}
			catch (IncompatibleFuzzyValuesException e)
			{   tempDOF = 1.0; } // safe to ignore this since we already checked compatibility
		  }
        }

        // now calc actual conclusions using DOF to 'scale' each
        // conclusion fuzzy value
        for (i = 0; i<conclusions.size(); i++)
        {   FuzzyValue conc = conclusions.fuzzyValueAt(i);
            outputs.addFuzzyValue(conc.fuzzyScale(DOF));
        }

        return outputs;
    }
    
    
}
