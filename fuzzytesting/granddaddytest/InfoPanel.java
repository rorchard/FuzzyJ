package fuzzytesting.granddaddytest;

import java.awt.Panel;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.CheckboxGroup;
import symantec.itools.awt.RadioButtonGroupPanel;
import symantec.itools.multimedia.ImageViewer;
import java.net.URL;

class InfoPanel extends java.awt.Panel {

    //constants and variables for this infopanel layout
    static final int COMPONENT_WIDTH = 60;
    static final int COMPONENT_HEIGHT = 28;
    static final int SPACE = 4;
    
    int columnOneX;
    int columnTwoX;
    
    int rowOneY;
    int rowTwoY;
    int rowThreeY;
    int rowFourY;
        
   	java.awt.Panel panel2;
	java.awt.Label label2;
	java.awt.Panel panel3;
	java.awt.Label label3;
	java.awt.Panel panel4;
	java.awt.Label label5;
	java.awt.Panel panel1;
	java.awt.Label label1;
	java.awt.Panel panel5;
	java.awt.Label label6;
	java.awt.Label label8;
	java.awt.Panel panel8;
	java.awt.Panel panel9;
	java.awt.Label labelMaxMin;
	symantec.itools.multimedia.ImageViewer imageViewerEmpty;
	symantec.itools.multimedia.ImageViewer imageViewerNormal;
	symantec.itools.multimedia.ImageViewer imageViewerConvex;
	
    InfoPanel() {
		setLayout(null);
		setBounds(0,340,100,108);
		        
        columnOneX = 8;
        columnTwoX = columnOneX + COMPONENT_WIDTH;
        
        rowOneY = 8;
        rowTwoY = rowOneY + COMPONENT_HEIGHT + SPACE;
        rowThreeY = rowTwoY + COMPONENT_HEIGHT + SPACE;
        rowFourY = rowThreeY + COMPONENT_HEIGHT + SPACE;
        		
		panel2 = new java.awt.Panel();
		panel2.setLayout(null);
		panel2.setBounds(columnOneX,rowOneY,50,28);
		panel2.setBackground(new Color(0));
		add(panel2);

		label2 = new java.awt.Label("Empty",Label.CENTER);
		label2.setBounds(2,2,46,24);
		label2.setBackground(new Color(12632256));
		panel2.add(label2);

		panel3 = new java.awt.Panel();
		panel3.setLayout(null);
		panel3.setBounds(columnOneX,rowTwoY,50,28);
		panel3.setBackground(new Color(0));
		add(panel3);

		label3 = new java.awt.Label("Convex",Label.CENTER);
		label3.setBounds(2,2,46,24);
		label3.setBackground(new Color(12632256));
		panel3.add(label3);

		panel4 = new java.awt.Panel();
		panel4.setLayout(null);
		panel4.setBounds(columnOneX,rowThreeY,50,28);
		panel4.setBackground(new Color(0));
		add(panel4);

		label5 = new java.awt.Label("Normal",Label.CENTER);
		label5.setBounds(2,2,46,24);
		label5.setBackground(new Color(12632256));
		panel4.add(label5);

		panel8 = new java.awt.Panel();
		panel8.setLayout(null);
		panel8.setBounds(columnOneX,rowFourY,56,28);
		panel8.setBackground(new Color(0));
		add(panel8);

		label8 = new java.awt.Label("Max Min",Label.CENTER);
		label8.setBounds(2,2,52,24);
		label8.setBackground(new Color(12632256));
		panel8.add(label8);

		labelMaxMin = new java.awt.Label("");
		labelMaxMin.setBounds(columnTwoX,rowFourY,30,27);
		add(labelMaxMin);

		imageViewerEmpty = new symantec.itools.multimedia.ImageViewer();
		try {
			imageViewerEmpty.setImageURL(FileAndUrlResolution.getFileUrl("graphics/thumbsup.gif"));
		}
		catch(java.beans.PropertyVetoException e) { }
		imageViewerEmpty.setBounds(columnTwoX,rowOneY,32,29);
		add(imageViewerEmpty);

		imageViewerNormal = new symantec.itools.multimedia.ImageViewer();
		try {
			imageViewerNormal.setImageURL(FileAndUrlResolution.getFileUrl("graphics/thumbsup.gif"));
		}
		catch(java.beans.PropertyVetoException e) { }
		imageViewerNormal.setBounds(columnTwoX,rowThreeY,32,29);
		add(imageViewerNormal);

		imageViewerConvex = new symantec.itools.multimedia.ImageViewer();
		try {
			imageViewerConvex.setImageURL(FileAndUrlResolution.getFileUrl("graphics/thumbsup.gif"));
		}
		catch(java.beans.PropertyVetoException e) { }
		imageViewerConvex.setBounds(columnTwoX,rowTwoY,32,29);
		add(imageViewerConvex);
    }
    
    void setMaxMinLabel(String maxMin){
        labelMaxMin.setText(maxMin);
    }     
}
