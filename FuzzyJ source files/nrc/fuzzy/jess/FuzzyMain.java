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
import jess.*;

/**
 * FuzzyMain.java
 *
 * Equivalent to the Jess Main class when the NRC Fuzzy extensions
 * are being used.
 *
 *
 * Created: Tue Nov 09 07:47:44 1999
 * $Id$
 *
 * @author Bob Orchard
 * 
 */

public class FuzzyMain extends Main implements Serializable
{       
  public static void main(String[] argv) 
  {
    FuzzyMain m = new FuzzyMain();
    m.initialize(argv, new FuzzyRete());
    m.execute(true);
  }
    
}
