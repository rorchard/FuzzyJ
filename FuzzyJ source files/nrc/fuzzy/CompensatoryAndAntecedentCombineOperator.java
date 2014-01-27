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
 * A class that provides a special operator referred to as the 
 * 'compensatory and' operator to combine
 * the fuzzy match values of antecedent/input pairs in a rule 
 * when a rule is fired which has multiple antecedents. <p>
 * The formula for the operator is: <br>
 * <pre>
 *  u = (PROD(u(i)))**(1-gamma) * (1-(PROD(1-u(i))))**gamma
 * 
 *  where PROD(u(i)) means the product of a set of values
 *                   u(i), with i ranging from 1 to m
 *  and gamma is a value between 0 and 1 that controls the 'compensation'.
 *      (the default gamma value is 0.562)
 * </pre>
 * <br>
 * Note that by setting gamma to 0 we get the 
 * equivalent of the ProductAntecedentCombineOperator.
 * <br>
 * This is detailed in FuzzySet Theory and Its Applications, Second
 * Edition, by Zimmermann, Kluwer Acedemic Publishers, 1990.
 * 
 * @author Bob Orchard
 * 
 * @see AntecedentCombineOperator
 * @see MinimumAntecedentCombineOperator
 * @see ProductAntecedentCombineOperator
 * @see FuzzyRule
 */
public class CompensatoryAndAntecedentCombineOperator extends AntecedentCombineOperator 
	implements Serializable 
{
    /**
     * The gamma value controls the output of the 'compensatory and' function.
     * By default it is set to 0.562 which apparantly gives good (expected) results
     * according to a study done. Note that by setting gamma to 0 we get the 
     * equivalent of the ProductAntecedentCombineOperator.
     */
    private double gamma = 0.562;  
    
	/**
	 * Constructor to create an object with the default gamma value of 0.562.
	 */
    
	public CompensatoryAndAntecedentCombineOperator()
	{
		setGamma(0.562);
	}

	/**
	 * Constructor to create an object with the specified gamma value.
	 * Note that by setting gamma to 0 we get the 
     * equivalent of the ProductAntecedentCombineOperator.
	 * 
	 * @param g the gamma value for the object
	 */
    
	public CompensatoryAndAntecedentCombineOperator(double g)
	{
		setGamma(g);
	}

	/**
	 * Set the gamma value used in the Compensatory And calulation
	 * 
	 * @param g the gamma value to be set (must be between 0 and 1)
	 */
	public void setGamma(double g)
	{
		if (g > 1.0) g = 1.0;
		else if (g < 0.0) g = 0.0;
		gamma = g;
	}
    
	/**
	 * Get the gamma value used in the Compensatory And calculation
	 * 
	 * @return the gamma value 
	 */
	public double getGamma()
	{
		return (gamma);
	}
    
	/** Implements the 'compensatory and' of the match values in the double array.
	 * 
	 * @param matchValues
	 * @return the 'compensatory and' of values in the array parameter
	 * 
	 */
	public double execute(double matchValues[])
	{	double cAnd = 0.0;
		int len = matchValues.length;
		if (len < 1) return( 0.0 );
		
		double product = matchValues[0];
		double productcomplement = 1.0-matchValues[0];
		for (int i = 1; i < len; i++)
		{   product *= matchValues[i];
			productcomplement *= 1.0-matchValues[i];
	    } 
	    cAnd = Math.pow(product, 1.0-gamma) * Math.pow(1.0-productcomplement, gamma);
		   	
		return(cAnd);
	}

}
