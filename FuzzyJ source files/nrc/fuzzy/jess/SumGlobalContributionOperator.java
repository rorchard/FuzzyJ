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

package nrc.fuzzy.jess;

import java.io.*;
import nrc.fuzzy.*;

/**
 * A class that provides a 'sum' operator to combine fuzzy
 * values when doing global contribution. For example, when a
 * temperature fuzzy value is asserted by more than 1 rule,
 * the existing fuzzy value and the new fuzzy value are combined
 * to form a new fuzzy value that represents the desired way to
 * combine both pieces of information. This will normally be the union
 * of the 2 fuzzy values but can be set to other operations such as
 * the summ of the fuzzy values (or it can also be set to perform
 * no global combining and just replace the exiting value with the new one).
 *
 * @author Bob Orchard
 * @see nrc.fuzzy.jess.GlobalContributionOperatorInterface
 * @see nrc.fuzzy.jess.GlobalContributionOperator
 * @see nrc.fuzzy.jess.UnionGlobalContributionOperator
 *
 */
public class SumGlobalContributionOperator extends GlobalContributionOperator
       implements Serializable
{

	/**
	 * Class that implements the Sum operator via an
	 * execute method that accepts 2 fuzzy values and
	 * returns a fuzzy value that is the result of doing the fuzzy sum.
   *
	 * @param newFuzzyValue  should be the new FuzzyValue being asserted
	 * @param existingFuzzyValue  should be the existing FuzzyValue
	 * @return FuzzyValue that is the result of doing a fuzzy sum of the 2 values
	 * 
	 */

  public FuzzyValue execute( FuzzyValue newFuzzyValue, FuzzyValue existingFuzzyValue )
          throws IncompatibleFuzzyValuesException, XValueOutsideUODException
	{
		return ( newFuzzyValue.fuzzySum(existingFuzzyValue) );
	}

}
