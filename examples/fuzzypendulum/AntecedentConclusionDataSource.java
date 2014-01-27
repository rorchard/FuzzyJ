/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 *  http://mozilla.org/MPL/2.0/. 
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

package examples.fuzzypendulum;

import com.klg.jclass.chart.data.*;
import com.klg.jclass.chart.beans.SimpleChart;
import com.klg.jclass.chart.*;
import nrc.fuzzy.*;
import java.awt.*;
import java.util.*;


public class AntecedentConclusionDataSource extends JCDefaultDataSource
{   
    final int NUM_POINTS = 15;

    SimpleChart chart;
    
    AntecedentConclusionDataSource( SimpleChart c, double xmin, double xmax )
    {
        super(null, null, null, null, "Rule Graph");

        chart = c;
        
        xvalues = new double[2][NUM_POINTS];
        yvalues = new double[2][NUM_POINTS];
        
        for (int i=0; i<NUM_POINTS; i++)
        {  xvalues[0][i] = 0.0;
           yvalues[0][i] = 0.0;
           xvalues[1][i] = 0.01;
           yvalues[1][i] = 0.0;
        }
        
        chart.setBatched(true);
        JCAxis xaxis = chart.getDataView(0).getXAxis();
        xaxis.setMin(xmin);
        xaxis.setMax(xmax);
        chart.setBatched(false);
    }
    
    void showFuzzySets( FuzzySet fs, double intersectionYValue )
    {
        int i, j;
        double x1, x2, y1, y2;
        
        if (fs == null)
        { 
          for (i=0; i<NUM_POINTS; i++)
          {  xvalues[0][i] = 0.0;
             yvalues[0][i] = 0.0;
             xvalues[1][i] = 0.01;
             yvalues[1][i] = 0.0;
          }
          
          fireChartDataEvent(ChartDataEvent.RELOAD_SERIES, 0, 0);
          return;
        }
        
        FuzzySet intersectFS = fs.horizontalIntersection(intersectionYValue);
        JCAxis xaxis = chart.getDataView(0).getXAxis();
        
        // find number of different x values in the 2 fuzzy sets
        int size1 = fs.size();
        int size2 = intersectFS.size();
        Vector vX = new Vector(size1 + size2);
        Vector vY1 = new Vector(size1 + size2);
        Vector vY2 = new Vector(size1 + size2);
        boolean fs1Done = false;
        boolean fs2Done = false;
        
        i = j = 0;
        
        x1 = fs.getX(0);
        x2 = intersectFS.getX(0);
        y1 = fs.getY(0);
        y2 = intersectFS.getY(0);
        while (x1 < x2)
        { vX.addElement(new Double(fs.getX(i)));
          vY1.addElement(new Double(fs.getY(i)));
          if (y2>y1) 
             vY2.addElement(new Double(y1));
          else 
             vY2.addElement(new Double(y2));
          x1 = fs.getX(++i);
          y1 = fs.getY(i);
        }
        
        if (i >= size1) fs1Done = true;
        if (j >= size2) fs2Done = true;

        while (!fs1Done || !fs2Done)
        {   if (fs1Done)
            {  vX.addElement(new Double(intersectFS.getX(j)));
               vY1.addElement(new Double(fs.getY(size1-1)));
               vY2.addElement(new Double(intersectFS.getY(j++)));
            }
            else if (fs2Done)
            {  vX.addElement(new Double(fs.getX(i)));
               vY1.addElement(new Double(fs.getY(i++)));
               vY2.addElement(new Double(intersectFS.getY(size2-1)));
            }
            else
            { x1 = fs.getX(i);
              x2 = intersectFS.getX(j);
              if (x1 == x2)
              {  vX.addElement(new Double(x1));
                 vY1.addElement(new Double(fs.getY(i)));
                 vY2.addElement(new Double(intersectFS.getY(j)));
                 i++; j++;
              }
              else if (x1 < x2)
              {  vX.addElement(new Double(x1));
                 vY1.addElement(new Double(fs.getY(i)));
                 if (j>0) vY2.addElement(new Double(intersectFS.getY(j-1)));
                 else vY2.addElement(new Double(intersectFS.getY(j)));
                 i++;
              }
              else
              {  vX.addElement(new Double(x2));
                 vY1.addElement(new Double(intersectFS.getY(j)));
                 vY2.addElement(new Double(intersectFS.getY(j)));
                 j++;
              }
            }
            if (i >= size1) fs1Done = true;
            if (j >= size2) fs2Done = true;
        }

        int size = vX.size()+2;
        
        for (i=1; i<size-1; i++)
        {  xvalues[0][i] = ((Double)vX.elementAt(i-1)).doubleValue();
           yvalues[0][i] = ((Double)vY1.elementAt(i-1)).doubleValue();
           xvalues[1][i] = ((Double)vX.elementAt(i-1)).doubleValue();
           yvalues[1][i] = ((Double)vY2.elementAt(i-1)).doubleValue();
        }
        
        xvalues[1][0] = xaxis.getMin();
        yvalues[1][0] = yvalues[1][1];
        xvalues[1][size-1] = xaxis.getMax();
        yvalues[1][size-1] = yvalues[1][size-2];
        xvalues[0][0] = xaxis.getMin();
        yvalues[0][0] = yvalues[0][1];
        xvalues[0][size-1] = xaxis.getMax();
        yvalues[0][size-1] = yvalues[0][size-2];

        double lastY0 = yvalues[0][size-2];
        double lastY1 = yvalues[1][size-2];
        for (i=size; i<NUM_POINTS; i++)
        { 
          xvalues[0][i] = xaxis.getMax();
          yvalues[0][i] = lastY0;
          xvalues[1][i] = xaxis.getMax();
          yvalues[1][i] = lastY1;
        }
        
//    NOT sure why this can't be done here ... perhaps should have used a JCEditiableDataSource
//    and would have been able to do this? It would also have made the updating of values easier??
//        chart.getDataView(0).getSeries(0).getStyle().setFillColor(Color.red);
     //   chart.getDataView(0).getSeries(0).getStyle().setFillStyle(new JCFillStyle(Color.cyan, JCFillStyle.SOLID));
     //   chart.getDataView(0).getSeries(1).getStyle().setFillColor(Color.red);

        chart.setBatched(true);
        fireChartDataEvent(ChartDataEvent.RELOAD_SERIES, 0, 0);
        fireChartDataEvent(ChartDataEvent.RELOAD_SERIES, 1, 0);
        chart.setBatched(false);
    }
}
