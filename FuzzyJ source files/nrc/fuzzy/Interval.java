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

import java.text.NumberFormat;
import java.io.*;

/**
 * The <code>Interval</code> class is used to represent an interval of 
 * the real number line. It consists of the low value of the interval, the high
 * value of the interval and a flag for each of these two endpoints that
 * indicates whether the endpoint is closed (includes the endpoint) or open
 * (does not include the endpoint). Often we will see an interval represented as:
 * <pre><code>
 *    [5, 10]  - all values from 5 to 10 including 5 and 10
 *    (5, 10]  - all values from 5 to 10 excluding 5 and including 10
 *    [5, 10)  - all values from 5 to 10 including 5 and excluding 10
 *    (5, 10)  - all values from 5 to 10 excluding 5 and 10
 * </code></pre>
 * We use intervals to represent the support and the alpha-cut of a FuzzyValue.
 *
 * @author Bob Orchard
 *
 * @see FuzzyValue
 */

public class Interval  implements Serializable
{
    protected double lowX;
    protected boolean openFlagLow;

    protected double highX;
    protected boolean openFlagHigh;
    
    /**
     * A constant which represents the default precision of the 
     * <code>toString</code> method of Interval.
     * This precision can be changed using the <code>setToStringPrecision</code>
     * method.
     */
    protected static int toStringPrecision = 2;
    
    protected NumberFormat nf;
    

    Interval()
    {
        lowX = 0;
        openFlagLow = true;

        highX = 0;
        openFlagHigh = true;
    }

    Interval(double lowX, boolean openFlagLow, double highX, boolean openFlagHigh)
    {
        this.lowX = lowX;
        this.openFlagLow = openFlagLow;
        this.highX = highX;
        this.openFlagHigh = openFlagHigh;
    }

    public void setLowX(double lowX){
        this.lowX = lowX;
    }

    public double getLowX(){
        return(lowX);
    }

    public void setLowOpenFlag(boolean flag){
        openFlagLow = flag;
    }

    public boolean getLowOpenFlag(){
        return(openFlagLow);
    }

    public void setHighX(double highX){
        this.highX = highX;
        }

    public double getHighX(){
        return(highX);
    }

    public void setHighOpenFlag(boolean flag){
        openFlagHigh = flag;
    }

    public boolean getHighOpenFlag(){
        return(openFlagHigh);
    }
    
    /**
     * Sets the precision, in terms of the number of decimal places that will
     * be printed for each double value, of the <code>toString</code> method.
     *
     * @param numDecimalPlaces the desired number of decimal places for each
     *                         double value printed by the <code>toString</code>
     *                         method
     */

    public static void setToStringPrecision(int numDecimalPlaces){
        toStringPrecision = numDecimalPlaces;
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(toStringPrecision);
        
        sb.append((getLowOpenFlag()) ? "(" : "[")
  	      .append(nf.format(getLowX()))
          .append(", ")
  	      .append(nf.format(getHighX()))
          .append((getHighOpenFlag()) ? ")" : "]")
          .append("  ");
          
       return(sb.toString());
    }    
}
