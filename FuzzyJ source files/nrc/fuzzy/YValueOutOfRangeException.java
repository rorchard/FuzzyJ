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
 * The <code>YValueOutOfRangeException</code> class is thrown to indicate that
 * a y value, or membership value, in a FuzzySet does not fall within strict
 * range of 0.0 to 1.0 inclusive, or [0.0, 1.0].  This exception is thrown 
 * almost exclusively by the 
 * FuzzySet constructors, which in turn affects most methods that construct 
 * a new FuzzySet in the course of their execution.
 *
 * <p>In the construction of a YValueOutOfRangeException, the offending y value,
 * will be stored in the exception.  The method getYValue will return this values.  
 * This value will also appear in the exception message, should the user call
 * the getMessage method.
 *
 * @author Bob Orchard
 * 
 * @see FuzzySetException
 *
 */

public class YValueOutOfRangeException extends FuzzySetException 
                                        implements Serializable
{
    /**
     * The y value whose range is outside the strict acceptable range of
     * 0.0 to 1.0 inclusive, or [0.0, 1.0].
     */
    double yValue;
    
    /**
     * The message constructed to inform the user, to a greater degree, what
     * has gone wrong.
     */
    String message;
    
    
    /**
     * Constructs a new YValueOutOfRangeException without a specific detail
     * message.  The default detail message informs the user that the y values
     * of a FuzzySet must be in the strict range of [0.0, 1.0], and provides the 
     * user with the value of the offending y value.
     *
     * @param yValue the y value with a value outside of the range [0.0, 1.0]
     */
     
    public YValueOutOfRangeException(double yValue){
        this.yValue = yValue;
        
        message = "The Y values in the FuzzySet must be in the range [0.0, 1.0].";

        if(yValue < 0.0){
            message = message + " Y values cannot be less than zero. The y value which " +
                      "fell outside of the acceptable range had a value of " + 
                      yValue + ".";
        } else {
            message = message + " Y values cannot be greater than one. The y value which " +
                      "fell outside of the acceptable range had a value of " + yValue + ".";
        }    
    }
    
    /**
     * Constructs a new YValueOutOfRangeException with a specific detail
     * message appended to the default detail message.  The default detail 
     * message informs the user that the y values
     * of a FuzzySet must be in the strict range of [0.0, 1.0], and provides the 
     * user with the value of the offending y value.
     *
     * @param yValue  the y value with a value outside of the range [0.0, 1.0]
     * @param message the specific detail message
     */
     
    public YValueOutOfRangeException(double yValue, String message){
        this.yValue = yValue;
        
        this.message = "The Y values in the FuzzySet must be in the range [0.0, 1.0].";

        if(yValue < 0.0){
            this.message = this.message + " Y values cannot be less than zero. The y value which " +
                           "fell outside of the acceptable range had a value of " + yValue + ".";
        } else {
            this.message = this.message + " Y values cannot be greater than one. The y value which " +
                           "fell outside of the acceptable range had a value of " + yValue + ".";
        }    

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
