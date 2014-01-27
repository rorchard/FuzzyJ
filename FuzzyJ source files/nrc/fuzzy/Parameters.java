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
 * The <code>Parameters</code> class is a class which contains only 
 * class variables which are used in multiple classes in 
 * the fuzzy package.
 *
 * @author Bob Orchard
 *
 */
 
public class Parameters implements Serializable
{    
    /**
     * A constant used to flag the Weak AlphaCut.
     */
     
    public static final boolean WEAK = true; 

    /**
     * A constant used to flag the Strong AlphaCut.
     */
     
    public static final boolean STRONG = false; 

    /**
     * A class variable which specifies the tolerance given for floating point value
     * comparisons in some situations.
     */
    static double FUZZY_TOLERANCE = 0.00000001;
    
    /**
     * Sets the tolerance used in many of the Fuzzy Logic calculations.
     */ 
    public static void setFuzzyTolerance(double tolerance){
        FUZZY_TOLERANCE = tolerance;
    }  
  
    /**
     * Gets the tolerance used in many of the Fuzzy Logic calculations.
     */ 
    public static double getFuzzyTolerance(){
        return FUZZY_TOLERANCE;
    }    
}
