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
 * Concrete implementation of the <b>above</b> modifier. Consider the following example:
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
 *          <td>After <b>Above</b> modifier applied:
 *              <ul>
 *              <li>the <code>ABOVE</code> modifier identifies the first x value
 *                  at which the maximum value is reached. All membership values
 *                  below this point are set to zero and all membership values above
 *                  this value are set to 1-y. For convex fuzzy sets this gives 
 *                  an intuitive result.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/above.gif">
 *          </td>
 *      </tr>
 * </table>
 *
 * @author Bob Orchard
 *
 * @see nrc.fuzzy.ModifierFunction
 * @see nrc.fuzzy.Modifiers
 */
 public class AboveModifier extends ModifierFunction implements Serializable
 {
   public AboveModifier()
    {
        super( "above" );
    }

    public AboveModifier(String s)
    {
        super( s );
    }

    /**
     * Creates a new FuzzyValue by applying the <b>above</b> modifier to an
     * existing FuzzyValue.
     * Above identifies the first x value
     * at which the maximum value is reached. All membership values
     * below this point are set to zero and all membership values above
     * this value are set to 1-y. For convex fuzzy sets this gives 
     * an intuitive result.  
     * @param fv the fuzzy value to which the below modifier is applied.
     *
     * @param fv the fuzzy value to which the above modifier is applied.
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
     * Creates a new FuzzySet by applying the <b>above</b> modifier to an
     * existing FuzzySet.
     * Above identifies the first x value
     * at which the maximum value is reached. All membership values
     * below this point are set to zero and all membership values above
     * this value are set to 1-y. For convex fuzzy sets this gives 
     * an intuitive result.  
     *
     * @param fs the fuzzy set to which the above modifier is applied.
     * @see ModifierFunction
     */
    public FuzzySet call(FuzzySet a)
    {
        FuzzySet fs = new FuzzySet();
        double maxY = 0, maxPos = 0;
        double x, y;

        for(int i=0; i<a.numPoints; i++){
            if(a.getY(i) > maxY){
                maxY = a.getY(i);
                maxPos = i;
            }
        }

        for(int i=0; i<a.numPoints; i++){
            x = a.getX(i);
            y = (i > maxPos) ? 1.0 - a.getY(i) : 0.0;

            fs.appendSetPoint(x, y);
        }
        
        fs.simplifySet();
        return(fs);
    }
}
