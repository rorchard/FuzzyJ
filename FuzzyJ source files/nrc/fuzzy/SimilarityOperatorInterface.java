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

/**
 * An interface for classes that provides an operator to calculate
 * the similarity between 2 fuzzy values. By default
 * the SimilarityByArea operator (ratio of area of intersection
 * to area of union of the fuzzy values) is used.
 * 
 * @author Bob Orchard
 * 
 * @see SimilarityByAreaOperator
 * @see SimilarityByPossibilityOperator
*/

public interface SimilarityOperatorInterface 
{
	/**
	 * Classes that implement the SimilarityOperatorInterface must provide
	 * a similarity method that accepts 2 FuzzyValue objects and returns the 
	 * measure of the similarity between them.
	 * 
	 * @param fuzzyValue1
	 * @param fuzzyValue2
	 * @return double that is the result of applying the operator to the 2 values (the similarity measure)
	 * 
	 */
  public double similarity( FuzzyValue fuzzyValue1, FuzzyValue fuzzyValue2 )
  		 throws IncompatibleFuzzyValuesException;
 
}
