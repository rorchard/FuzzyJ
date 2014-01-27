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
 * The <code>NoXValueForMembershipException</code> class is thrown to indicate that
 * an X value corresponding to a membership value could not be found for the
 * given FuzzySet/FuzzyValue.  This exception is thrown exclusively by the 
 * FuzzySet getXForMembership method.
 *
 * <p>In the construction of a NoXValueForMembershipException, the offending membership value,
 * will be stored in the exception. This value will also appear in the exception message, 
 * should the user call the getMessage method.
 *
 * @author Bob Orchard
 * 
 * @see FuzzySetException
 *
 */

public class NoXValueForMembershipException extends FuzzySetException 
                                        implements Serializable
{
    /**
     * The y value for which an x value could not be found
     */
    double yValue;
    
    /**
     * The message constructed to inform the user, to a greater degree, what
     * has gone wrong.
     */
    String message;
    
    
    /**
     * Constructs a new NoXValueForMembershipException without a specific detail
     * message.  The default detail message informs the user that the y value
     * did not have a corresponding x value in the FuzzySet .
     *
     * @param yValue the y value with no corresponding x value for the fuzzy set
     */
     
    public NoXValueForMembershipException(double yValue){
        this.yValue = yValue;
        
        message = "The membership value specified had no corresponding x value in the FuzzySet.\n" +
                  "All values in the FuzzySet are either below or above the membership value: " +
                  yValue + " (or the FuzzySet was empty).";
    }
    
    /**
     * Constructs a new NoXValueForMembershipException with a specific detail
     * message. The default detail message informs the user that the y value
     * did not have a corresponding x value in the FuzzySet .
     *
     * @param yValue the y value with no corresponding x value for the fuzzy set
     * @param message the specific detail message
     */
     
    public NoXValueForMembershipException(double yValue, String message){
        this.yValue = yValue;
        
        this.message = "The membership value specified had no corresponding x value in the FuzzySet.\n" +
                       "All values in the FuzzySet are either below or above the membership value: " +
                       yValue + " (or the FuzzySet was empty).";

        this.message = this.message + "  " + message;
    }
    
    /**
     * Returns the offending y value.  This y value has a value outside the
     * acceptable range of [0.0, 1.0].
     *
     * @return the y value outside the acceptable range
     */
    
    public double getYValue(){
        return(yValue);
    }
    
    /**
     * Returns the detail message to the user.
     *
     * @return the String detail message to inform the user of some of the particulars
     *         of the situation that caused the exception to be thrown
     */
    
    public String getMessage(){
        return(message);
    }    
}
