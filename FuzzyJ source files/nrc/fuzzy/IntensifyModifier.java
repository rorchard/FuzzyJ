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
 * Concrete implementaion of the <b>intensify</b> modifier. Consider the following example:
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
 *          <td>After <b>intensify</b> modifier applied:
 *              <ul>
 *              <li>the <code>INTENSIFY</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having having performed
 *                  the following modifications on it:
 *                      <ul>
 *                      <li>if (0.0 <= y <= 0.5), y = 2*y<sup>2</sup>
 *                      <li>if (0.5 <  y <= 1.0), y = 1 - 2*(1-y)<sup>2</sup>
 *                      </ul>
 *                  The has the effect of emphasizing the sections of the FuzzyValue
 *                  that have a membership value greater than 0.5, and understating
 *                  any sections of the FuzzyValue with a membership value less than
 *                  or equal to 0.5.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/intensify.gif">
 *          </td>
 *      </tr>
 * </table>
 *
 * @author Bob Orchard
 *
 * @see nrc.fuzzy.ModifierFunction
 * @see nrc.fuzzy.Modifiers
 */
 public class IntensifyModifier extends ModifierFunction implements Serializable
 {
   public IntensifyModifier()
    {
        super( "intensify" );
    }

    public IntensifyModifier(String s)
    {
        super( s );
    }

    /**
     * Returns a new FuzzyValue object which represents the FuzzyValue argument
     * after the application of the <code>intensify</code> modifier.  The intensify 
     * modifier uses the following rules for application of modifiers and operators:
     * <ul>
     * <li>if (0.0 <= y <= 0.5), y = 2*y<sup>2</sup>
     * <li>if (0.5 <  y <= 1.0), y = 1 - 2*(1-y)<sup>2</sup>
     * </ul>
     * Where y is the membership value of each point of the FuzzyValue.
     *
     * @param fv the FuzzyValue to modify with the <code>intensify</code> hedge
     * @return a new FuzzyValue object representing FuzzyValue argument after the 
     *         application of the <code>intensify</code> hedge.  This new FuzzyValue 
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
     * after the application of the <code>intensify</code> modifier.  The intensify 
     * modifier uses the following rules for application of modifiers and operators:
     * <ul>
     * <li>if (0.0 <= y <= 0.5), y = 2*y<sup>2</sup>
     * <li>if (0.5 <  y <= 1.0), y = 1 - 2*(1-y)<sup>2</sup>
     * </ul>
     * Where y is the membership value of each point of the FuzzySet.
     *
     * @param fs the FuzzySet to modify with the <code>intensify</code> hedge
     * @return a new FuzzySet object representing FuzzySet argument after the 
     *         application of the <code>intensify</code> hedge.
     * @see ModifierFunction
     */
    public FuzzySet call(FuzzySet a){
        FuzzySet b = expandSet(a);
        FuzzySet fs = new FuzzySet();
        double x, y;

        for(int i=0; i<b.numPoints; i++){
            x = b.getX(i);

            if(b.getY(i) <= 0.5) y = 2*(Math.pow(b.getY(i), 2));
            else                 y = 1 - 2*(Math.pow((1 - b.getY(i)), 2));

            fs.appendSetPoint(x, y);
        }
        
        fs.simplifySet();
        return(fs);
    }
}
