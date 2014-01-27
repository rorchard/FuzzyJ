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
 * Concrete implementaion of the <b>not</b> modifier, which will give the
 * complement of a FuzzyValue/FuzzySet. Consider the following example:
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
 *          <td>Not:
 *              <ul>
 *              <li>the <code>NOT</code> modifier returns the complement of the
 *                  FuzzyValue passed as its argument.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/not.gif">
 *          </td>
 *      </tr>
 * </table>
 *
 * @author Bob Orchard
 *
 * @see nrc.fuzzy.ModifierFunction
 * @see nrc.fuzzy.Modifiers
 */
 public class NotModifier extends ModifierFunction implements Serializable
 {
    public NotModifier()
    {
        super( "not" );
    }

    public NotModifier(String s)
    {
        super( s );
    }

    /**
     * Takes the complement of the FuzzyValue argument.  Mathematically (NOT),
     * u(x) = 1 - u(x), where u(x) gives the membership value, u being the
     * membership function.
     *
     * @param fv the FuzzyValue to return the complement of
     * @return a new FuzzyValue object representing the complement of the
     *         FuzzyValue argument.  This new FuzzyValue object has the
     *         same FuzzyVariable as the FuzzyValue argument.
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
     * Takes the complement of the FuzzySet argument.  Mathematically (NOT),
     * u(x) = 1 - u(x), where u(x) gives the membership value, u being the
     * membership function.
     *
     * @param fs the FuzzySet to return the complement of
     * @return a new FuzzySet object representing the complement of the
     *         FuzzySet argument.
     * @see ModifierFunction
     */
    public FuzzySet call(FuzzySet a)
    {
        FuzzySet fs = new FuzzySet();

        for(int i=0; i < a.numPoints; i++){
            fs.appendSetPoint(a.getX(i), 1.0 - a.getY(i));
        }

        fs.simplifySet();
        return(fs);
    }
}

