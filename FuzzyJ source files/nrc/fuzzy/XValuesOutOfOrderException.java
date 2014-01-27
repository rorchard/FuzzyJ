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
 * The <code>XValuesOutOfOrderException</code> class is thrown to indicate that
 * the x values in a FuzzySet that are not in strictly ascending order.  This
 * exception is thrown almost exclusively by the FuzzySet constructors, which in 
 * turn affects most methods that construct a new FuzzySet in the course of their
 * execution.
 *
 * <p>In the construction of an XValuesOutOfOrderException, the offending x value,
 * and the x value previous to it will be stored in the exception.  The methods
 * getXValue and getPreviousXValue will return these values.  These values will also
 * appear in the exception message, should the user call the getMessage method.
 *
 * @author Bob Orchard
 * 
 * @see FuzzySetException
 *
 */

public class XValuesOutOfOrderException extends FuzzySetException 
                                         implements Serializable
{
    /**
     * The value previous to the x value which is out of order.
     */
    double prevValue;
    
    /**
     * The x value which is out of order.  It has been identified as the x value
     * that is out of order because its value is less than the x value previous
     * to it.
     */
    double currentValue;
    
    /**
     * The message constructed to inform the user, to a greater degree, what
     * has gone wrong.
     */
    String message;
    
    
    /**
     * Constructs a new XValuesOutOfOrderException without a specific detail
     * message.  The default detail message informs the user that the x values
     * of a FuzzySet must be in strictly ascending order, and provides the 
     * user with the value of the offending x value and the x value previous
     * to it.
     *
     * @param prevValue    the x value previous to the x value which is out
     *                     of order
     * @param currentValue the x value which is out of order
     */
     
    public XValuesOutOfOrderException(double prevValue, double currentValue){
        this.prevValue = prevValue;
        this.currentValue = currentValue;
        
        message = "The X values in the FuzzySet must be in strictly ascending order. " +
                  prevValue + " cannot precede " + currentValue + ".";
    }
    
    /**
     * Constructs a new XValuesOutOfOrderException with a specific detail
     * message appended to the default detail message.  The default detail 
     * message informs the user that the x values
     * of a FuzzySet must be in strictly ascending order, and provides the 
     * user with the value of the offending x value and the x value previous
     * to it.
     *
     * @param prevValue    the x value previous to the x value which is out
     *                     of order
     * @param currentValue the x value which is out of order
     * @param message      the specific detail message
     */
     
    public XValuesOutOfOrderException(double prevValue, double currentValue, String message){
        this.prevValue = prevValue;
        this.currentValue = currentValue;
        
        this.message = "The X values in the FuzzySet must be in strictly ascending order. " +
                       prevValue + " cannot precede " + currentValue + ". " + message;
    }
    
    /**
     * Returns the offending x value.  This x value has a value smaller than the
     * x value previous to it, which indicates that the x values must be out of
     * order.
     *
     * @return the out of order x value
     */
    
    public double getXValue(){
        return(currentValue);
    }
    
    /**
     * Returns the x value previous to the x value which is out of order.  This 
     * value will be larger than that returned by the method <code>getXValue</code>.
     *
     * @return the x value previous to the one out of order
     */
     
    public double getPreviousXValue(){
        return(prevValue);
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
