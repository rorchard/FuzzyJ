/*
 * @(#)CurrentTraceDataSource.java	1.0 2000/06/10
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


public class CurrentTraceDataSource extends JCDefaultDataSource
{
    final int NUM_POINTS = 80;
    
    SimpleChart chart;
    int nextValueIndex = 0;
    
    CurrentTraceDataSource( SimpleChart c )
    {
        super(null, null, null, null, "Current Trace");

        int i;
        chart = c;
        
        xvalues = new double[1][NUM_POINTS];
        yvalues = new double[1][NUM_POINTS];
        
        for (i=0; i<NUM_POINTS; i++)
        {  xvalues[0][i] = i;
           yvalues[0][i] = Double.MAX_VALUE;
        }
        nextValueIndex = 0;

        chart.setBatched(true);
        JCAxis xaxis = chart.getDataView(0).getXAxis();
        xaxis.setMin(0.0);
        xaxis.setMax((double)NUM_POINTS);
        chart.setBatched(false);
    }
    
    void addNextValue( double v )
    {
        int i;
        
        if (nextValueIndex < NUM_POINTS)
           // add to non-filled array at end
           yvalues[0][nextValueIndex++] = v;
        else
        {  // add to end after shifting 1 point to the left
           for (i=1; i<NUM_POINTS; i++)
               yvalues[0][i-1] = yvalues[0][i];
               
           yvalues[0][NUM_POINTS-1] = v;
        }
        
        fireChartDataEvent(ChartDataEvent.RELOAD_SERIES, 0, 0);
    }
}