package fuzzytesting.granddaddytest;

import graph.*;
import java.awt.*;
import nrc.fuzzy.*;

class GraphHandler extends java.awt.Panel {
    
    static int viewPanelWidth;
    static int viewPanelHeight;
    static int tabPanelWidth;
    static int tabPanelHeight;

	graph.Graph2D myGraph;
    graph.DataSet cyanDataSet;
    graph.DataSet blueDataSet;
    graph.DataSet redDataSet;
    graph.Axis xaxis;
    graph.Axis yaxis;
    Graphics graphics;

    graph.Markers markers;

    boolean firstRun;


	/**********************************************************************************************
	 *
	 * CONSTRUCTOR
	 *
	 **********************************************************************************************/

	GraphHandler(int width, int height){
    	myGraph = new graph.Graph2D();
    	myGraph.drawzero = false;
    	myGraph.drawgrid = false;
    	myGraph.clearAll = true;
    	myGraph.borderRight = 20;
    	myGraph.setDataBackground(new Color(245, 230, 200));
    	myGraph.setBackground(new Color(12632256));
    	myGraph.setForeground(Color.black);
    	myGraph.setVisible(true);

        myGraph.setBounds( 0, 0, width, height);

        add(myGraph);
        
        graphics = null;
        firstRun = true;
    }
    
	/***************************************************************************************
	 *
	 * GRAPH STUFF FOR THREE GRAPHS...
	 *
	 ***************************************************************************************/

	void setGraph(nrc.fuzzy.FuzzyValue fuzzyValue1, nrc.fuzzy.FuzzyValue fuzzyValue2, nrc.fuzzy.FuzzyValue fuzzyValue3) {

        setGraph(convertFuzzyValue(fuzzyValue1), 
	             convertFuzzyValue(fuzzyValue2), 
	             convertFuzzyValue(fuzzyValue3));
	}
	
	void setGraph(double[] fuzzyValue1, double[] fuzzyValue2, double[] fuzzyValue3){
	    
	    if(firstRun){
	        initGraph(fuzzyValue1, fuzzyValue2, fuzzyValue3);
	        return;
	    }    

        setCyanDataSet(fuzzyValue1);
        setBlueDataSet(fuzzyValue2);
        setRedDataSet(fuzzyValue3);

        attachCyanDataSet();
        attachBlueDataSet();
        attachRedDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
    }
    
    void initGraph(double[] fuzzyValue1, double[] fuzzyValue2, double[] fuzzyValue3){

        firstRun = false;

        int x[] = new int[12];
        int y[] = new int[12];
        boolean d[] = new boolean[12];
        x[0]=-1; y[0]= 2; d[0]=false;
        x[1]= 1; y[1]= 2; d[1]=true;
        x[2]= 2; y[2]= 1; d[2]=true;
        x[3]= 2; y[3]=-1; d[3]=true;
        x[4]= 1; y[4]=-2; d[4]=true;
        x[5]=-1; y[5]=-2; d[5]=true;
        x[6]=-2; y[6]=-1; d[6]=true;
        x[7]=-2; y[7]= 1; d[7]=true;
        x[8]=-1; y[8]= 2; d[8]=true;

        markers = new graph.Markers();
        markers.AddMarker(9,9,d,x,y);

        x[0]=-2; y[0]= 4; d[0]=false;
        x[1]= 2; y[1]= 4; d[1]=true;
        x[2]= 0; y[2]= 4; d[2]=false;
        x[3]= 0; y[3]=-4; d[3]=true;
        x[4]=-2; y[4]=-4; d[4]=false;
        x[5]= 2; y[5]=-4; d[5]=true;
        x[6]=-4; y[6]= 0; d[6]=false;
        x[7]= 4; y[7]= 0; d[7]=true;
        x[8]=-4; y[8]= 2; d[8]=false;
        x[9]=-4; y[9]=-2; d[9]=true;
        x[10]=4; y[10]= 2; d[10]=false;
        x[11]=4; y[11]=-2; d[11]=true;

        markers.AddMarker(8,12,d,x,y);

     	myGraph.setMarkers(markers);

        if(xaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));
        if(yaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));

        setCyanDataSet(fuzzyValue1);
        setBlueDataSet(fuzzyValue2);
        setRedDataSet(fuzzyValue3);

	    attachCyanDataSet();
	    attachBlueDataSet();
	    attachRedDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
	}


	/***************************************************************************************
	 *
	 * GRAPH STUFF FOR TWO GRAPHS...
	 *
	 ***************************************************************************************/

	void setGraph(nrc.fuzzy.FuzzyValue fuzzyValue1, nrc.fuzzy.FuzzyValue fuzzyValue2) {
	    setGraph(convertFuzzyValue(fuzzyValue1), convertFuzzyValue(fuzzyValue2));
	}
	
	void setGraph(double[] fuzzyValue1, double[] fuzzyValue2){
	    
	    if(firstRun){
	        initGraph(fuzzyValue1, fuzzyValue2);
	        return;
	    }    

        setBlueDataSet(fuzzyValue1);
        setRedDataSet(fuzzyValue2);

        attachBlueDataSet();
        attachRedDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
    }
    
    void initGraph(double[] fuzzyValue1, double[] fuzzyValue2){

        firstRun = false;

        int x[] = new int[12];
        int y[] = new int[12];
        boolean d[] = new boolean[12];
        x[0]=-1; y[0]= 2; d[0]=false;
        x[1]= 1; y[1]= 2; d[1]=true;
        x[2]= 2; y[2]= 1; d[2]=true;
        x[3]= 2; y[3]=-1; d[3]=true;
        x[4]= 1; y[4]=-2; d[4]=true;
        x[5]=-1; y[5]=-2; d[5]=true;
        x[6]=-2; y[6]=-1; d[6]=true;
        x[7]=-2; y[7]= 1; d[7]=true;
        x[8]=-1; y[8]= 2; d[8]=true;

        markers = new graph.Markers();
        markers.AddMarker(9,9,d,x,y);

        x[0]=-2; y[0]= 4; d[0]=false;
        x[1]= 2; y[1]= 4; d[1]=true;
        x[2]= 0; y[2]= 4; d[2]=false;
        x[3]= 0; y[3]=-4; d[3]=true;
        x[4]=-2; y[4]=-4; d[4]=false;
        x[5]= 2; y[5]=-4; d[5]=true;
        x[6]=-4; y[6]= 0; d[6]=false;
        x[7]= 4; y[7]= 0; d[7]=true;
        x[8]=-4; y[8]= 2; d[8]=false;
        x[9]=-4; y[9]=-2; d[9]=true;
        x[10]=4; y[10]= 2; d[10]=false;
        x[11]=4; y[11]=-2; d[11]=true;

        markers.AddMarker(8,12,d,x,y);

     	myGraph.setMarkers(markers);

        if(xaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));
        if(yaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));

        setBlueDataSet(fuzzyValue1);
        setRedDataSet(fuzzyValue2);

	    attachBlueDataSet();
	    attachRedDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
	}

	/***************************************************************************************
	 *
	 * GRAPH STUFF FOR ONE GRAPH...
	 *
	 ***************************************************************************************/

	void setGraph(nrc.fuzzy.FuzzyValue fuzzyValue) {
	    setGraph(convertFuzzyValue(fuzzyValue));
	}
	
	
	void setGraph(double[] fuzzyValue){
	    
	    if(firstRun){
	        initGraph(fuzzyValue);
	        return;
	    }    

        setBlueDataSet(fuzzyValue);
        attachBlueDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
    }
    
    void initGraph(double[] fuzzyValue){

        firstRun = false;

        int x[] = new int[12];
        int y[] = new int[12];
        boolean d[] = new boolean[12];
        x[0]=-1; y[0]= 2; d[0]=false;
        x[1]= 1; y[1]= 2; d[1]=true;
        x[2]= 2; y[2]= 1; d[2]=true;
        x[3]= 2; y[3]=-1; d[3]=true;
        x[4]= 1; y[4]=-2; d[4]=true;
        x[5]=-1; y[5]=-2; d[5]=true;
        x[6]=-2; y[6]=-1; d[6]=true;
        x[7]=-2; y[7]= 1; d[7]=true;
        x[8]=-1; y[8]= 2; d[8]=true;

        markers = new graph.Markers();
        markers.AddMarker(9,9,d,x,y);

        x[0]=-2; y[0]= 4; d[0]=false;
        x[1]= 2; y[1]= 4; d[1]=true;
        x[2]= 0; y[2]= 4; d[2]=false;
        x[3]= 0; y[3]=-4; d[3]=true;
        x[4]=-2; y[4]=-4; d[4]=false;
        x[5]= 2; y[5]=-4; d[5]=true;
        x[6]=-4; y[6]= 0; d[6]=false;
        x[7]= 4; y[7]= 0; d[7]=true;
        x[8]=-4; y[8]= 2; d[8]=false;
        x[9]=-4; y[9]=-2; d[9]=true;
        x[10]=4; y[10]= 2; d[10]=false;
        x[11]=4; y[11]=-2; d[11]=true;

        markers.AddMarker(8,12,d,x,y);

     	myGraph.setMarkers(markers);

        if(xaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));
        if(yaxis == null) setXAxisTitleText("", new Font("Helvetica", Font.PLAIN, 14));

        setBlueDataSet(fuzzyValue);
	    attachBlueDataSet();

	    if(myGraph.isShowing()){
	        graphics = myGraph.getGraphics();
    	    repaintAll();
    	}    
	}


    /***************
     * ATTACH METHODS
     ***************/

	void attachCyanDataSet(){
	    myGraph.attachDataSet(cyanDataSet);
     	xaxis.attachDataSet(cyanDataSet);
 	    yaxis.attachDataSet(cyanDataSet);
 	}
 	
    void attachBlueDataSet(){
	    myGraph.attachDataSet(blueDataSet);
     	xaxis.attachDataSet(blueDataSet);
 	    yaxis.attachDataSet(blueDataSet);

        resetAxesLabels();
 	}
 	
 	void resetAxesLabels(){
        int xmax = (int)(Math.ceil(xaxis.getDataMax()));
        int xmin = (int)(Math.floor(xaxis.getDataMin()));
        
        if(xmin == xmax){
            xmin -=1;
            xmax +=1;
        }
        
        xaxis.setManualRange(true);
        xaxis.force_end_labels = true;
	    xaxis.maximum = xmax;
	    xaxis.minimum = xmin;

        yaxis.setManualRange(true);
        yaxis.force_end_labels = true;
	    yaxis.maximum = 1.0;
	    yaxis.minimum = 0.0;
	}    
	    
 	void attachRedDataSet(){
 	    if(redDataSet != null){
    	    myGraph.attachDataSet(redDataSet);
     	    xaxis.attachDataSet(redDataSet);
     	    yaxis.attachDataSet(redDataSet);
     	}    
 	}
 	

    /***************
     * SET METHODS
     ***************/

    void setCyanDataSet(double[] johnny){
        if(cyanDataSet != null){
            cyanDataSet.deleteData();
	        myGraph.detachDataSet(cyanDataSet);
	    }    
	    if(johnny == null) return;
	    cyanDataSet = myGraph.loadDataSet(johnny, johnny.length/2);
	    myGraph.detachDataSet(cyanDataSet);
   	    cyanDataSet.linecolor = Color.cyan;
        cyanDataSet.marker = 9;
        cyanDataSet.markerscale = 1.0;
      	cyanDataSet.markercolor = Color.cyan;
//       	cyanDataSet.legend(80, 35, "Set1");
       	cyanDataSet.legendColor(Color.black);
    }
    
    void setBlueDataSet(double[] johnny){
        if(blueDataSet != null){
            blueDataSet.deleteData();
	        myGraph.detachDataSet(blueDataSet);
	    }    
	    if(johnny == null) return;
	    blueDataSet = myGraph.loadDataSet(johnny, johnny.length/2);
	    myGraph.detachDataSet(blueDataSet);
       	blueDataSet.linecolor = Color.blue;
       	blueDataSet.marker = 9;
       	blueDataSet.markerscale = 1.0;
       	blueDataSet.markercolor = Color.blue;
//       	blueDataSet.legend(80, 45, "Set2");
       	blueDataSet.legendColor(Color.black);
    }
    
	void setRedDataSet(double[] johnny){
	    if(redDataSet != null){
	        redDataSet.deleteData();
	        myGraph.detachDataSet(redDataSet);
	    }    
	    if(johnny == null) return;
	    redDataSet = myGraph.loadDataSet(johnny, johnny.length/2);
	    myGraph.detachDataSet(redDataSet);
      	redDataSet.linecolor = Color.red;
       	redDataSet.marker = 9;
       	redDataSet.markerscale = 1.0;
       	redDataSet.markercolor = Color.red;
//       	redDataSet.legend(80, 35, "Result");
       	redDataSet.legendColor(Color.black);
    }
    
    /***************
     * OTHER METHODS
     ***************/
     
	void setXAxisTitleText(String s, Font f){
 	    xaxis = myGraph.createAxis(graph.Axis.BOTTOM);
 	    xaxis.setTitleText(s);
 	    xaxis.setTitleFont(f);
	}    
	
	void setYAxisTitleText(String s, Font f){
 	    yaxis = myGraph.createAxis(graph.Axis.LEFT);
 	    yaxis.setTitleText(s);
 	    yaxis.setTitleFont(f);
 	    yaxis.setTitleColor(Color.black);
 	    yaxis.setTitleRotation(90);

        yaxis.setManualRange(true);
	    yaxis.maximum = 1;
	    yaxis.minimum = 0;
	}    
	
	void setLabelFont(Font f){
	    xaxis.setLabelFont(f);
	    yaxis.setLabelFont(f);
	}    

	void repaintAll(){
        myGraph.update(graphics);
        myGraph.paint(graphics);
    }
    
    double[] convertFuzzyValue(nrc.fuzzy.FuzzyValue linda)
    {
        if(linda == null) 
            return(null);
        int len = linda.size();
        if (len == 0)
            return(null);
            
        double minUOD = linda.getMinUOD();
        double maxUOD = linda.getMaxUOD();
        double minX = linda.getX(0);
        double maxX = linda.getX(len-1);
        int j = 0;
        int extraPoints = 0;
        
        if (minUOD < minX) extraPoints++;
        if (maxUOD > maxX) extraPoints++;

        double[] set = new double[(len+extraPoints)*2];
           
        if (minUOD < minX)
        {
            set[j++] = minUOD;
            set[j++] = linda.getY(0);
        }
        
        for(int i=0; i<len; i++){
            set[j++] = linda.getX(i);
            set[j++] = linda.getY(i);
        }

        if (maxUOD > maxX)
        {
            set[j++] = maxUOD;
            set[j++] = linda.getY(len-1);
        }

        return(set);
    }    
    
}
