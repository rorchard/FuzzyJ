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
import java.awt.*;
import java.awt.event.*;
import jess.*;
import jess.awt.Console;

/**
 * FuzzyConsole.java
 *
 * Equivalent to the Jess Console class when the NRC Fuzzy extensions
 * are being used.
 *
 * Created: Tue Nov 09 07:47:44 1999
 * $Id$
 *
 * @author Bob Orchard
 * 
 */

public class FuzzyConsole extends Console implements Serializable
{   
  /**
   * Creates a FuzzyConsole which is just a Jess Console associated
   * with a FuzzyRete Object instead of a Rete object.
   * 
   * @param name A string 'name' that identifies the Console object.
   * 
   */
      
  public FuzzyConsole(String name)
  {
    super(name, new FuzzyRete());
  }

  /**
   * Trivial main() to display this frame
   * @param argv Arguments passed to execute().
   */
  
  public static void main(String[] argv) 
  {
    final FuzzyConsole c = new FuzzyConsole("Fuzzy Jess Console");
        
    // ###
    c.addWindowListener( new WindowAdapter() 
        {
            public void windowClosing( WindowEvent event )
            {   System.exit( 0 );
            }
        } 
    );
    c.execute(argv);                                                                      
  }

}
