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
 * A class that provides an operator to calculate
 * the similarity between 2 fuzzy values. 
 * 
 * Given two fuzzy values, f1 and f2, similarity is defined as:
 * <pre>
 * similarity = if    necessity(f1,f2) > 0.5   
 *              then  possibility(f1,f2)   
 *              else  (necessity(f1,f2) + 0.5) * possibility(f1,f2) 
 *
 * where 
 *
 *   necessity(f1,f2) = 1 - possibility(not f1,f2)
 *
 * and
 *
 *   possibility(f1,f2) = max(min(u  (x),u  (x)) 
 *                         x       f1     f2  
 * </pre>
 * <p>
 * The default similarity operator is the SimilarityByPossibilityOperator. 
 * This can be changed using the static setDefaultSimilarityOperator method of the FuzzyValue class.
 * <p>
 * 
 * @author Bob Orchard
 * 
 * @see SimilarityOperator
 * @see SimilarityOperatorInterface
 * @see SimilarityByAreaOperator
*/

public class SimilarityByPossibilityOperator extends SimilarityOperator
       implements Serializable
{
  /**
   * SIMILARITY: a method to calculate the similarity measure of two fuzzy sets
   * Given two fuzzy values, f1 and f2, similarity is defined as:
   * <pre>
   * similarity = if    necessity(f1,f2) > 0.5   
   *              then  possibility(f1,f2)   
   *              else  (necessity(f1,f2) + 0.5) * possibility(f1,f2) 
   *
   * where 
   *
   *   necessity(f1,f2) = 1 - possibility(not f1,f2)
   *
   * and
   *
   *   possibility(f1,f2) = max(min(u  (x),u  (x)) 
   *                         x       f1     f2  
   * </pre>
   * <p>
   * This version of a similarity function is useful in some cases but breaks
   * down when there is some overlap but large parts not overlapping. For example
   * if we have the fuzzy values with LeftLinearFuzzySet(0.499999, 0.500001)
   * and RightLinearFuzzySet(0.499999, 0.500001) we'd expect a similarity to
   * be close to 0.0 but it is 0.25 in this case. So if we are using triangular
   * or trapezoid fuzzy sets this measure may be OK. This is a measure that was used in the
   * FuzzyCLIPS system and a better approach might be to use the more useful 
   * measure, similarityByArea.
   * <p>
   * Note: we must check that the 2 fuzzy values have the same Fuzzy variable 
   * <p>
   * Note: If the fuzzy values are the same (equals) then returns a 1.0
   *
   * @param fuzzyValue1
   * @param fuzzyValue2
   * @return double that is the result of applying the operator to the 2 values (the similarity measure)
   * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
   *                identical fuzzy variables the operation cannot be done
   * 
   */
  public double similarity( FuzzyValue fv1, FuzzyValue fv2 )
       throws IncompatibleFuzzyValuesException
  {
	  double nec, poss;
	  
	  // problem with this method and the fuzzy sets are the same and there is
	  // a vertical line in the fuzzy set ... gives a value of 0.5 rather than
	  // the expected 1.0. This is because the set and its complement have
	  // a common vertical line in their intersection so necessity results in
	  // 0.0 (1.0 - 1.0)
	  // ... so we will check for identical fuzzy values 1st
	  
	  if (fv1.equals(fv2)) 
		  return( 1.0 );
	  
	  nec = necessity(fv1, fv2);
	  poss = possibility(fv1, fv2);
	  
	  if (nec > 0.5)
		  return( poss );
	  else
		  return( (nec + 0.5) * poss );
  }
 
  /***
   * POSSIBILITY: possibility measure of two fuzzy sets.
   *  
   * Given two fuzzy values, f1 and f2, possibility is defined as:
   * <pre>
   * possibility(f1,f2) = max(min(u  (x),u  (x)) 
   *                       x       f1     f2  
   * </pre>
   * This is effectively the maximum value of the intersection 
   * of the 2 fuzzy values.
   * <p>
   * 
   * @param fv1 fuzzy value 
   * @param fv2 fuzzy value 
   * @return The possibility value for the 2 fuzzy values
   * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
   *                              identical fuzzy variables the operation cannot be done
   */
   private double possibility(FuzzyValue fv1, FuzzyValue fv2)
      throws IncompatibleFuzzyValuesException
   {        
      return( fv1.maximumOfIntersection(fv2) );
   }

  /**
   * NECESSITY; the necessity measure of two fuzzy sets.
   *
   * Given two fuzzy values, f1 and f2, necessity is defined as:
   * <pre>
   *   necessity(f1,f2) = 1 - possibility(not f1,f2)
   * </pre>
   * <p>
   *
   * @param fv1 fuzzy value
   * @param fv2 fuzzy value
   * @return The necessity value for the 2 fuzzy values
   * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
   *                              identical fuzzy variables the operation cannot be done
   */
  private double necessity(FuzzyValue fv1, FuzzyValue fv2)
      throws IncompatibleFuzzyValuesException
  {
     FuzzyValue fc;
     double nc;
     
     fc = fv1.fuzzyComplement();
     nc = 1.0 - possibility(fc, fv2);
     
     return(nc);
  }

}
