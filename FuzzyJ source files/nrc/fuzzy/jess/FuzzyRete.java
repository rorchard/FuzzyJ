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

import java.io.*;
import java.util.*;
import java.applet.*;
import jess.awt.*;
import jess.*;

import nrc.fuzzy.*;

/** **********************************************************************
 * The reasoning engine, extended to support fuzzy reasoning. 
 * Executes the built Rete network, and coordinates many
 * other activities.
 * 
 * @author Ernest J. Friedman-Hill
 * @author Bob Orchard
 */

public class FuzzyRete extends Rete implements Serializable
{	
  /**
   * Determines how global contribution is to be done when a fact
   * with fuzzy values is asserted. There are at least 3 options:
   * <br>
   * 1. fuzzyUnion of the FuzzyValues (default) <br>
   * 2. fuzzySum of the FuzzyValues <br>
   * 3. Replace the FuzzyValues (i.e. no global contribution) <br>
   * <br>
   * Other operator may be added by users or as part of the system.
   * 
   */
  private GlobalContributionOperator m_globalContributionOperator = 
                               new UnionGlobalContributionOperator();
	
  /*
    Constructors
    */

  public FuzzyRete()
  {
    super((Object)null);
    setFactory(new FuzzyFactoryImpl());
  }
  
  
// constructor for FuzzyRete peer   
  public FuzzyRete(FuzzyRete fuzzyRete)
  {
    super(fuzzyRete);
    setFactory(new FuzzyFactoryImpl());
  }
  
  
//  FuzzyRete createPeer overrides the super's Rete createPeer  
  public FuzzyRete createPeer()
  {
   return new FuzzyRete(this);
  }
  
  /**
   * @param a 
   */
  public FuzzyRete(Applet a)
  {
    super(a);
    setFactory(new FuzzyFactoryImpl());
  }
  
  // Routines to support the assertion of facts with fuzzy values in the slots
  
  /**
   * Routine to find and return a set of FuzzyValues in a list (ValueVector)
   *
   * @param list The ValueVector to be searched for FuzzyValues.
   * @param fvv  The FuzzyValueVactor in which to store the FuzzyValues that are found.
   */
  private void getFuzzyValuesInList(ValueVector list, FuzzyValueVector fvv)
    throws JessException
  {
    if (list == null) return;
    
    for (int i=0; i<list.size(); i++)
    { 
      Value v = list.get(i);
      if (v.type() == RU.EXTERNAL_ADDRESS)
      {
         Object o = v.externalAddressValue(null);
         if (o instanceof nrc.fuzzy.FuzzyValue)
            fvv.addFuzzyValue((FuzzyValue)o);
      }
      else if (v.type() == RU.LIST)
      {
        // check all the values in here
        ValueVector nextList = v.listValue(null);
        getFuzzyValuesInList( nextList, fvv);
      }
    }
  }
  
  /**
   * Uses getFuzzyValuesInList to find all FuzzyValues in a fact.
   *
   * @param f The fact to search for FuzzyValues.
   * @return The list of FuzzyValues or null if none found.
   *
   */
  private FuzzyValueVector getFuzzyValuesInFact(Fact f)
    throws JessException
  {
    FuzzyValueVector fvv = new FuzzyValueVector();
    
    getFuzzyValuesInList( f, fvv);
    
    if (fvv.size() > 0)
      return fvv;
    else
      return null;
  }
  
  /**
   * Given a list of FuzzyValues, replace the existing FuzzyValues
   * in a list (ValueVector).
   *
   * @param list The list into which FuzzyValues are to be replaced
   * @param fvv The FuzzyValues to replace those in the list.
   * @param nextFvIndex The starting index in the set of FuzzyValues
   *
   */
  private void setFuzzyValuesInList(ValueVector list, FuzzyValueVector fvv, int nextFvIndex)
    throws JessException
  { 
    if (list == null) return;
  
    for (int i=0; i<list.size(); i++)
    { 
      Value v = list.get(i);
      if (v.type() == RU.EXTERNAL_ADDRESS)
      {
         Object o = v.externalAddressValue(null);
         if (o instanceof nrc.fuzzy.FuzzyValue)
         {
            if (nextFvIndex > fvv.size()-1)
               throw new JessException("setFuzzyValuesInList",
                     "Unexpected internal error on fuzzy rule firing. Rule outputs fewer than expected", "");
            FuzzyValue fv = fvv.fuzzyValueAt(nextFvIndex++);
            list.set(new Value(fv), i);
         }
      }
      else if (v.type() == RU.LIST)
      {
        // check all the values in here
        ValueVector nextList = v.listValue(null);
        setFuzzyValuesInList( nextList, fvv, nextFvIndex);
      }
    }
  }
  
  /**
   * Replace a set of FuzzyValues in a fact with a new set.
   *
   * @param f The fact
   * @param fvv The FuzzyValueVector with the FuzzyValues to replace
   *            those found in the fact.
   *
   */
  private void setFuzzyValuesInFact(Fact f, FuzzyValueVector fvv)
    throws JessException
  {
    setFuzzyValuesInList(f, fvv, 0);
  }
  
  /**
   * Finds any Fuzzy matches (antecedent/input pairs) that were made
   * on the LHS of the rule. When a fuzzy match was made (fuzzy-match
   * function) the pairs were stored in the appropriate token (in the
   * special extensionData slot) which ultimately was linked in the 
   * m_token slot of the activation.
   *
   * @param a The rule activation to be searched for Fuzzy matches.
   * @return A Vector with the fuzzy match pairs or null if no match pairs
   */
  public Vector getFuzzyMatchesInActivation(Activation a)
  {
    // the matches (pairs of FuzzyValues) will be in the m_token of the
    // activation at slot m_extensionData of the token (and all parents).
    Vector v = null;
    FuzzyToken t = (FuzzyToken) a.getToken();
    while (t != null)
    { Vector e = t.getExtensionData();
      if (e != null)
      { // keep the fuzzy value pairs we find (unless they are with a 
        // token from a test node).
        for (int i=0; i<e.size(); i++)
        { Object o = e.elementAt(i);
          if (o instanceof nrc.fuzzy.FuzzyValueVector)
          { if (v == null) v = new Vector();
            v.addElement(o);
          }
        }
      }
      t = t.getParent();
    } 
    return v;
  }
  
  

  /**
   * Special preprocessing when asserting a fact with fuzzy values in it.
   *
   * Some special additions for dealing with fuzzy facts
   * and fuzzy inferencing. In particular the FuzzyValues in the asserted
   * fact will be modified if there are fuzzy matches on the LHS of the rule
   * and if there exists an equivalent fact with fuzzy values already, that
   * fact and the new one are combined (fuzzy global contribution).
   *
   * @param f the fact being asserted
   * @exception JessException 
   * @return int 
   */
   
  public int doPreAssertionProcessing(Fact f) throws JessException 
  {
    int i;
    // find any FuzzyValues in the fact being asserted
    FuzzyValueVector fuzzyValues = getFuzzyValuesInFact(f);

    // if there are any fuzzy values in the slots of the fact 
    // being asserted ... then special processing             
    if (fuzzyValues != null)
      {
        Vector fuzzyMatches = null;
        FuzzyValueVector fuzzyOutputs = null;
        // if in an activation and there were fuzzy matches on LHS
        // determine the modified fuzzy outputs
        if ( m_currentActivation != null &&
             (fuzzyMatches = getFuzzyMatchesInActivation(m_currentActivation)) != null 
             )
          {
            // if we don't have a FuzzyRule for the fuzzy values of this 
            // activation OR if the defaultFuzzyRuleExecutor or the 
            // defaultAntecedentCombineOperator has changed then 
            // create a FuzzyRule. The 2nd condition takes
            // care of the case where the user changes the default executor
            // or the antecedent/input pair match value combine operator
            // since the last assert was done (this allows the user to change
            // these defaults during a rule RHS actions and have them take
            // effect immediately in the next assert).
            if ( m_currentActivationFuzzyRule == null ||
                 ( m_currentActivationFuzzyRule.getRuleExecutor().getClass() !=
                   FuzzyRule.getDefaultRuleExecutor().getClass()
                 ) ||
                 ( m_currentActivationFuzzyRule.getAntecedentCombineOperator() !=
                   FuzzyRule.getDefaultAntecedentCombineOperator()
                 )
                )
              { m_currentActivationFuzzyRule = new FuzzyRule();
                for (i = 0; i<fuzzyMatches.size(); i++)
                  { FuzzyValueVector fvv = (FuzzyValueVector)(fuzzyMatches.elementAt(i));
                    m_currentActivationFuzzyRule.addAntecedent(fvv.fuzzyValueAt(0));
                    m_currentActivationFuzzyRule.addInput(fvv.fuzzyValueAt(1));
                  }
              }
            // add the conclusions (the fuzzy values found in this fact)
            // to the rule and fire it -- getting the modified outputs
            // then replace the fuzzy values in the fact with these corrected ones
            m_currentActivationFuzzyRule.removeAllConclusions();
            for (i=0; i<fuzzyValues.size(); i++)
              m_currentActivationFuzzyRule.addConclusion(fuzzyValues.fuzzyValueAt(i));
            
            try
              {
                fuzzyOutputs = m_currentActivationFuzzyRule.execute(); 
              }
            catch (IncompatibleRuleInputsException irie)
              { // should not happen since the patterns and inputs were checked
                // during pattern matching
                throw new JessException("doFuzzyPreAssertionProcessing",
                                        "Unexpected error on fuzzy rule firing, internal problem: " + irie, "");
              }
            
          }

        // global contribution if there is an existing 'identical' (weak equals 
        // compare) fuzzy fact -- otherwise set the outputs just calculated (if any)
        // findFactByFact will use the equals method to compare slots to see if the 
        // fact given is the same as another existing fact. In this case for 
        // the FuzzyValue slots in the fact we want the compare to be a weak one so
        // we temporarily change the behaviour of the FuzzyValue equals method.
        // ???? Do we need to synchronize this so that it only affects this
        // call to findFactByFact and not any other code that might be executing
        // in another thread ???
        Fact iFact;
        synchronized(this)
        {
        	int currentStrength = FuzzyValue.getEqualsStrength();
        	FuzzyValue.setEqualsStrength(FuzzyValue.WEAK_EQUALS, 314159);
        	iFact = findFactByFact(f);
        	FuzzyValue.setEqualsStrength(currentStrength, 314159);
        }
        if (iFact == null)
          { // no existing fact to combine with
            if (fuzzyOutputs != null) // null if no matches on LHS or not in a rule
              setFuzzyValuesInFact(f, fuzzyOutputs);
          }
        else
          { // an existing fact to combine with
            FuzzyValueVector globalFuzzyValues = new FuzzyValueVector();
            FuzzyValueVector iFactFuzzyValues = getFuzzyValuesInFact(iFact);
            if (fuzzyOutputs == null) // null if no matches on LHS or not in a rule
              fuzzyOutputs = fuzzyValues;
            try
              { for (i=0; i<iFactFuzzyValues.size(); i++)
                {   // do correct type of global contribution
                	FuzzyValue globalFV = fuzzyOutputs.fuzzyValueAt(i);
                	if (m_globalContributionOperator != null)
                	   globalFV = m_globalContributionOperator.execute(globalFV,iFactFuzzyValues.fuzzyValueAt(i));
					globalFuzzyValues.addFuzzyValue(globalFV);
                }
              }
            // should not be errors but ...
            catch (IncompatibleFuzzyValuesException ifve)
              {
                throw new JessException("doFuzzyPreAssertionProcessing",
                                        "Unexpected error global contribution, internal problem: " + ifve, "");
              }
            catch (XValueOutsideUODException xvoue)
              {
                throw new JessException("doFuzzyPreAssertionProcessing",
                                        "Unexpected error global contribution, internal problem: " + xvoue, "");
              }
            setFuzzyValuesInFact(f, globalFuzzyValues);
            // retract the existing 'identical' fact!
            retract(iFact);
          }
      }
    return super.doPreAssertionProcessing(f);  
  }
  
  /*
   * Holds the current rule activation during the time a rule is
   * firing. When it is null there is no rule firing.
   */
  Activation m_currentActivation = null;

  /**
   * Get the current rule activation.
   */
  protected Activation getCurrentActivation()
  {
    return m_currentActivation;
  }

  /**
   * Holds a FuzzyRule for current rule activation during 
   * the time a rule is firing. It will be null when there 
   * is no rule firing and will be set if required when an
   * activation is firing (ie. when there asserts during
   * the rule firing that have fuzzy values in the slots).
   */ 
  FuzzyRule m_currentActivationFuzzyRule = null;

  /**
   * Get the current FuzzyRule in the activation.
   */
  public FuzzyRule getCurrentActivationFuzzyRule()
  {
    return m_currentActivationFuzzyRule;
  }


  /**
   * Records the current activation during a rule firing.
   */
  protected void aboutToFire(Activation a)
  {
    m_currentActivation = a;
  }


  /**
   * Clears the current activation when a rule finishes firing.
   */
  protected void justFired(Activation a)
  {
    m_currentActivation = null;
    m_currentActivationFuzzyRule = null;
  }
  
  /**
   *  Sets the type of global contribution to do. Wil normally be one of: <br>
   *  a UnionGlobalContributionOperator - do a fuzzy union of FuzzyValues <br>
   *  a SumGlobalContributionOperator - do a fuzzy Sum of FuzzyValues <br>
   *  null - do no global contribution (replace with new FuzzyValues) <br>
   * 
   * @param op the GlobalContributionOperator that provides the 
   *          required execute method to combine the new and existing
   *          fuzzy values when global contribution is being done.
   * @return true if successful else false (actually can't fail)
   */
  public boolean setFuzzyGlobalContributionOperator( GlobalContributionOperator op)
  { 
	m_globalContributionOperator = op;
	return true;
  }
  
  /**
   *  Gets the current GlobalContributionOperator object that implements
   *  the execute method to do global contribution. Will normally be one of: <br>
   *  a UnionGlobalContributionOperator - do a fuzzy union of FuzzyValues <br>
   *  a SumGlobalContributionOperator - do a fuzzy Sum of FuzzyValues <br>
   *  null - do no global contribution (replace with new FuzzyValues) <br>
   * 
   * @return an GlobalContributionOperator object or null.
   */
  public GlobalContributionOperator getFuzzyGlobalContributionOperator()
  { 
	return m_globalContributionOperator;
  }
  
}
