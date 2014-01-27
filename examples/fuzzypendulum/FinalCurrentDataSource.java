/*
 * @(#)FinalCurrentDataSource.java	1.0 2000/06/10
 *
 * Copyright (c) 1998 National Research Council of Canada.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * the National Research Council of Canada. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with the National Research Council of Canada.
 *
 * THE NATIONAL RESEARCH COUNCIL OF CANADA MAKES NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. NRC SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * Copyright Version 1.0
 *
 */
package examples.fuzzypendulum;

import com.klg.jclass.chart.data.*;
import com.klg.jclass.chart.beans.SimpleChart;
import com.klg.jclass.chart.*;
import nrc.fuzzy.*;


public class FinalCurrentDataSource extends JCDefaultDataSource
{   
    final int NUM_POINTS = 25;

    SimpleChart chart;
    
    FinalCurrentDataSource( SimpleChart c )
    {
        super(null, null, null, null, "Final Current");

        chart = c;
        
        xvalues = new double[1][NUM_POINTS];
        yvalues = new double[1][NUM_POINTS];

        for (int i=0; i<NUM_POINTS; i++)
        {  xvalues[0][i] = i;
           yvalues[0][i] = Double.MAX_VALUE;
        }
        
        chart.setBatched(true);
        JCAxis xaxis = chart.getDataView(0).getXAxis();
        xaxis.setMin(-1.0);
        xaxis.setMax(1.0);
        chart.setBatched(false);
    }
    
    void showFuzzySet( FuzzySet fs )
    {
        int i, size;
 
        if (fs == null)
        {
          size = 1;
          xvalues[0][0] = 0.0;
          yvalues[0][0] = 0.0;
        }
        else
        {
          size = fs.size()+ 2;

          for (i=1; i<size-1; i++)
          {  xvalues[0][i] = fs.getX(i-1);
             yvalues[0][i] = fs.getY(i-1);
          }
        
          xvalues[0][0] = -1.0;
          yvalues[0][0] = fs.getY(0);
          xvalues[0][size-1] = 1.0;
          yvalues[0][size-1] = fs.getY(size-3);
        }
        
        double lastY = (fs == null) ? 0.0 : fs.getY(size-3);
        for (i=size; i<NUM_POINTS; i++)
        { xvalues[0][i] = 1.0;
          yvalues[0][i] = lastY;
        }
        
        fireChartDataEvent(ChartDataEvent.RELOAD_SERIES, 0, 0);
    }
}
