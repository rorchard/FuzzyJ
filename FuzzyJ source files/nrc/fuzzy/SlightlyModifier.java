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

/**
 * Concrete implementaion of the <b>slightly</b> modifier. Consider the following example:
 * <br><br>
 * <table width=640 align=CENTER border=1>
 *      <tr>
 *          <th>Description</th>
 *          <th>Visual Representation</th>
 *      </tr>
 *
 *      <tr>
 *          <td width=300>Unmodified fuzzy value:
 *              <ul>
 *              <li>this is the unmodified fuzzy value to which the modifier is applied.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/basicFuzzyValue.gif">
 *          </td>
 *      </tr>
 *      <tr>
 *          <td>Slightly:
 *              <ul>
 *              <li>the <code>SLIGHTLY</code> modifier returns the expanded
 *                  FuzzyValue A passed as its argument, having performed
 *                  the following modifications on it:
 *                  <ul>
 *                  <li>intensify [ norm (plus A AND not very A) ]
 *                  </ul>
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/slightly.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2>
 *                  Consider the partial breakdown, shown below, of this
 *                  example.  On the left, directly below, is shown this
 *                  FuzzyValue after applying the <code>plus</code> modifier.
 *                  On the right is this FuzzyValue after applying both the
 *                  <code>not</code> and the <code>very</code> modifiers.
 *                  Below these is depicted <code>plus FuzzyValue AND not
 *                  very FuzzyValue</code>, showing the portion of the FuzzyValue
 *                  that is selected which intuitively matches our linguistic
 *                  understanding of the word "slightly".
 *          </td>
 *      </tr>
 *          <td>After <b>slightly</b> modifier applied:
 *              <ul>
 *              <li>the <code>SLIGHTLY</code> modifier returns the expanded
 *                  FuzzyValue A passed as its argument, having performed
 *                  the following modifications on it:
 *                  <ul>
 *                  <li>intensify [ norm (plus A AND not very A) ]
 *                  </ul>
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/slightly.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2>
 *                  Consider the partial breakdown, shown below, of this
 *                  example.  On the left, directly below, is shown this
 *                  FuzzyValue after applying the <code>plus</code> modifier.
 *                  On the right is this FuzzyValue after applying both the
 *                  <code>not</code> and the <code>very</code> modifiers.
 *                  Below these is depicted <code>plus FuzzyValue AND not
 *                  very FuzzyValue</code>, showing the portion of the FuzzyValue
 *                  that is selected which intuitively matches our linguistic
 *                  understanding of the word "slightly".
 *          </td>
 *      </tr>
 *      <tr>
 *          <td align=MIDDLE>PLUS</td>
 *          <td align=MIDDLE>NOT VERY</td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE valign=TOP height=250>
 *              <img src="NetGraphics/plus_1.gif">
 *              <img src="NetGraphics/not_very_1.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE>
 *              Now, put them together.
 *              <br>PLUS FuzzyValue AND NOT VERY FuzzyValue
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE valign=TOP height=250>
 *              <img src="NetGraphics/plus_1_and_not_very_1.gif">
 *          </td>
 *      </tr>
 * </table> 
 *
 * @author Bob Orchard
  *
 * @see nrc.fuzzy.ModifierFunction
 * @see nrc.fuzzy.Modifiers
 */
 public class SlightlyModifier extends ModifierFunction implements Serializable
 {
   public SlightlyModifier()
    {
        super( "slightly" );
    }

    public SlightlyModifier(String s)
    {
        super( s );
    }

   /**
     * Returns a new FuzzyValue object which represents the FuzzyValue argument
     * after the application of the <code>slightly</code> modifier.  The slightly 
     * modifier uses the following sequence of modifiers and operators:
     * <ul>
     * <li>intensify [ norm (plus A AND not very A) ]
     * </ul>
     * Where A is the FuzzyValue to which the modifier <code>slightly</code> is 
     * being applied.
     *
     * @param fv the FuzzyValue to modify with the <code>slightly</code> hedge
     * @return a new FuzzyValue object representing FuzzyValue argument after the 
     *         application of the <code>slightly</code> hedge.  This new FuzzyValue 
     *         object has the same FuzzyVariable as the FuzzyValue argument.
     * @see ModifierFunction
     */
    public FuzzyValue call(FuzzyValue fv)
    {
        FuzzyValue fvNew = null;
        // executes the 'call' method below for FuzzySet to do the modifier function
        FuzzySet fs = call(fv.getFuzzySet()); 
        try 
         { fvNew = new FuzzyValue(fv.getFuzzyVariable(), fs);
         }
        catch (XValueOutsideUODException e)
         { // we know that the modifers like 'very' do not expand the x value range
           // of the fuzzy set so if get this exception there must be an internal error
           System.err.println("Internal error in Modifier function '"+getName()+
                            "': " + e);
           System.exit(100);
         }

        fvNew.unaryModifyLinguisticExpression(getName(), fv.getLinguisticExpression());
        return(fvNew);
    }    
    
    /**
     * Returns a new FuzzySet object which represents the FuzzySet argument
     * after the application of the <code>slightly</code> modifier.  The slightly 
     * modifier uses the following sequence of modifiers and operators:
     * <ul>
     * <li>intensify [ norm (plus A AND not very A) ]
     * </ul>
     * Where A is the FuzzySet to which the modifier <code>slightly</code> is 
     * being applied.
     *
     * @param fs the FuzzySet to modify with the <code>slightly</code> hedge
     * @return a new FuzzySet object representing FuzzySet argument after the 
     *         application of the <code>slightly</code> hedge.
     * @see ModifierFunction
     */
    public FuzzySet call(FuzzySet a) 
    {
        return(Modifiers.intensify(Modifiers.norm(Modifiers.plus(a).fuzzyIntersection(Modifiers.not(Modifiers.very(a))))));
    }
}
