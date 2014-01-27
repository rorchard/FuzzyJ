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
 * Concrete implementaion of the <b>norm</b> modifier which normalizes
 * a FuzzyValue/FuzzySet. Consider the following example:
 * <br><br>
 * <table width=640 align=CENTER border=1>
 *      <tr>
 *          <th>Description</th>
 *          <th>Visual Representation</th>
 *      </tr>
 *
 *      <tr>
 *          <td valign=TOP>Norm:
 *              <ul>
 *              <li>the <code>NORM</code> modifier returns the normalized FuzzyValue.
 *                  Normalizing the FuzzyValue refers to scaling it so that at least
 *                  one point on the FuzzyValue has a membership value of 1.0.
 *                  FuzzyValue passed as its argument.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <center><b>Before Normalization</b></center>
 *              <br><img src = "NetGraphics/not_normal.gif">
 *              <center><b>After Normalization</b></center>
 *              <br><img src = "NetGraphics/normal.gif">
 *          </td>
 *      </tr>
 * </table>
 *
 * @author Bob Orchard
 *
 * @see nrc.fuzzy.ModifierFunction
 * @see nrc.fuzzy.Modifiers
 */
 public class NormModifier extends ModifierFunction implements Serializable
 {
   public NormModifier()
    {
        super( "norm" );
    }

    public NormModifier(String s)
    {
        super( s );
    }

    /**
     * Returns a new FuzzyValue object which represents the normalized FuzzyValue
     * argument.  Normalization involves identifying the point in the FuzzyValue
     * with the highest membership value and multiplying all the membership values
     * in the FuzzyValue by a scale factor such that this highest point then has
     * a membership value of 1.0.
     *
     * @param fv the FuzzyValue to normalize
     * @return a new FuzzyValue object which represents the FuzzyValue argument
     *         after it has been normalized.  This new FuzzyValue 
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
     * Returns a new FuzzySet object which represents the normalized FuzzySet
     * argument.  Normalization involves identifying the point in the FuzzySet
     * with the highest membership value and multiplying all the membership values
     * in the FuzzySet by a scale factor such that this highest point then has
     * a membership value of 1.0.
     *
     * @param fs the FuzzySet to normalize
     * @return a new FuzzySet object which represents the FuzzySet argument
     *         after it has been normalized. 
     * @see ModifierFunction
     */
    public FuzzySet call(FuzzySet a){
        FuzzySet fs = new FuzzySet();

        double maxY = 0;

        for(int i=0; i<a.numPoints; i++){
            maxY = (a.getY(i) > maxY) ? a.getY(i) : maxY;
        }

        double scaleFactor = 1.0 / maxY;

        for(int i=0; i<a.numPoints; i++){
            fs.appendSetPoint(a.getX(i), scaleFactor * a.getY(i));
        }
        
        fs.simplifySet();
        return(fs);
    }

 }
