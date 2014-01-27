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
 *  NOT YET Implemented!! The methods are empty prototypes.
 *
 */
class FuzzyNumber extends FuzzySet implements Serializable {

    final static int NUM_POINTS = 3;

    FuzzyNumber(){}

    FuzzyNumber(double number, double uncertainty){
        numPoints = NUM_POINTS;
        set = new SetPoint[numPoints];

        set[0] = new SetPoint(number - uncertainty, 0);
        set[1] = new SetPoint(number, 1);
        set[2] = new SetPoint(number + uncertainty, 0);
    }

    public FuzzyNumber add(FuzzyNumber other){

        return(new FuzzyNumber());
    }

    public FuzzyNumber subtract(FuzzyNumber other){

        return(new FuzzyNumber());
    }

    public FuzzyNumber multiply(FuzzyNumber other){

        return(new FuzzyNumber());
    }

    public FuzzyNumber divide(FuzzyNumber other){

        return(new FuzzyNumber());
    }
}
