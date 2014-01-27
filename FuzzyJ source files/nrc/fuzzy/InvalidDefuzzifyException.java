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
 * The <code>InvalidDefuzzifyException</code> class is thrown to indicate that
 * a defuzzify operation on a FuzzySet failed. See FuzzySet methods momentDefuzzify
 * and maximumDefuzzify.
 *
 * @author Bob Orchard
 *
 */

public class InvalidDefuzzifyException extends FuzzySetException 
                                        implements Serializable
{

    
    /**
     * The message constructed to inform the user, to a greater degree, what
     * has gone wrong.
     */
    String message;
    
    
    /**
     * Constructs a new InvalidDefuzzifyException.  
     *
     */
     
    public InvalidDefuzzifyException()
    {   
        message = "The defuzzification could not be performed.";
    }
    
    /**
     * Constructs a new XValuesOutOfOrderException with a specific detail
     * message appended to the default detail message.  
     * 
     * @param message      the specific detail message
     */
     
    public InvalidDefuzzifyException(String message){
        this.message = "The defuzzification could not be performed. " + message;
    }
}
