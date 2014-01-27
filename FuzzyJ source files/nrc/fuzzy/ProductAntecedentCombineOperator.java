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
 * A class that provides a 'product' operator to combine
 * the fuzzy match values of antecedent/input pairs in a rule 
 * when a rule is fired which has multiple antecedents. 
 * 
 * @author Bob Orchard
 * 
 * @see AntecedentCombineOperator
 * @see MinimumAntecedentCombineOperator
 * @see CompensatoryAndAntecedentCombineOperator
 * @see FuzzyRule
 */
public class ProductAntecedentCombineOperator extends AntecedentCombineOperator 
	implements Serializable 
{

	/** Implements the product of the match values in the double array.
	 * 
	 * @param matchValues
	 * @return the product of values in the array parameter
	 * 
	 */
	public double execute(double matchValues[])
	{	double product = 0.0;
		int len = matchValues.length;
		if (len < 1) return( 0.0 );
		
		product = matchValues[0];
		for (int i = 1; i < len; i++)
		   product *= matchValues[i];
		   	
		return(product);
	}

}
