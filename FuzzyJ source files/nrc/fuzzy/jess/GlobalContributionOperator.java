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

/**
 * An abstract class that can be sub-classed to provide
 * an execute method that performs a desired operation on fuzzy
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
 *
 * @see nrc.fuzzy.jess.GlobalContributionOperatorInterface
 * @see nrc.fuzzy.jess.UnionGlobalContributionOperator
 * @see nrc.fuzzy.jess.SumGlobalContributionOperator
 */
public abstract class GlobalContributionOperator implements GlobalContributionOperatorInterface, 
                        Cloneable, Serializable
{
}

