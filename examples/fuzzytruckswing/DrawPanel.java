package examples.fuzzytruckswing;

import javax.swing.*;
import java.beans.*;
import java.awt.*;

public class DrawPanel extends javax.swing.JPanel
{
	// variables for the size of the truck 
	public static final int TRUCK_WIDTH = 16;
	public static final int TRUCK_LENGTH = 32;
	public static final int CAB_WIDTH = 6;

	// keep track of the truck coords for redrawing.
	Polygon truckPoly, cabPoly;
	
	TruckSimulation Truck;

	public DrawPanel(TruckSimulation t)
	{
	    super();
		//{{INIT_CONTROLS
		//}}

		//{{REGISTER_LISTENERS
		//}}
		
		Truck = t;
	}
	
	//{{DECLARE_CONTROLS
	//}}

    public void paintComponent(Graphics g)
    {
        if (Truck.tracingOn == false) super.paintComponent( g );
		if (Truck.recompute == true) computeTruckCoords();
		drawTruck(g);
		drawDock(g);
	}

	// given the coordinates and the angle of the truck, 
	// compute the coordinates of the vertices of the truck
	// and cab.
	public void computeTruckCoords() 
	{   double x = size().width * Truck.Xt * .01;
		double y = size().height * Truck.Yt * .01;
		double theta = Truck.Phit * Math.PI / 180.0;
		int tx[] = new int[5];
		int ty[] = new int[5];
		int cx[] = new int[4];
		int cy[] = new int[4];
		
        // coords for the truck
        double truckWidthBy2 = TRUCK_WIDTH / 2.0;
        double truckWidthBy2TimesSine = truckWidthBy2 * Math.sin(theta);
        double truckWidthBy2TimesCos = truckWidthBy2 * Math.cos(theta);
        double truckLengthTimesSine = (TRUCK_LENGTH * Math.sin(theta));
        double truckLengthTimesCos = (TRUCK_LENGTH * Math.cos(theta));
		tx[0] = (int)Math.round(x - truckWidthBy2TimesSine);
		ty[0] = (int)Math.round(y - truckWidthBy2TimesCos);
		tx[1] = (int)Math.round(x + truckWidthBy2TimesSine);
		ty[1] = (int)Math.round(y + truckWidthBy2TimesCos);

		tx[2] = (int)Math.round(x + truckWidthBy2TimesSine - truckLengthTimesCos);
		ty[2] = (int)Math.round(y + truckWidthBy2TimesCos + truckLengthTimesSine);

		tx[3] = (int)Math.round(x - truckWidthBy2TimesSine - truckLengthTimesCos);
		ty[3] = (int)Math.round(y - truckWidthBy2TimesCos + truckLengthTimesSine);

		tx[4] = tx[0];
		ty[4] = ty[0];
        truckPoly = new Polygon(tx,ty,5);

        // coords for the truck cab
        double truckWidthBy4 = TRUCK_WIDTH / 4.0;
        double truckWidthBy4TimesSine = truckWidthBy4 * Math.sin(theta);
        double truckWidthBy4TimesCos = truckWidthBy4 * Math.cos(theta);
        double cabWidthTimesSine = (CAB_WIDTH * Math.sin(theta));
        double cabWidthTimesCos = (CAB_WIDTH * Math.cos(theta));

		cx[0] = tx[2] - (int)Math.round(truckWidthBy4TimesSine);
		cy[0] = ty[2] - (int)Math.round(truckWidthBy4TimesCos);

		cx[1] = cx[0] - (int)Math.round(cabWidthTimesCos);
		cy[1] = cy[0] + (int)Math.round(cabWidthTimesSine);

 		cx[2] = tx[3] + (int)Math.round(truckWidthBy4TimesSine - cabWidthTimesCos);
		cy[2] = ty[3] + (int)Math.round(truckWidthBy4TimesCos + cabWidthTimesSine);

		cx[3] = tx[3] + (int)Math.round(truckWidthBy4TimesSine);
 		cy[3] = ty[3] + (int)Math.round(truckWidthBy4TimesCos);

        cabPoly = new Polygon(cx,cy,4);

		Truck.recompute = false;
	}

	// called from paint(), this will draw all of the old truck
	// coords.
	private void drawTruck(Graphics g) 
	{
		g.setColor(Color.yellow);
        g.fillPolygon(truckPoly);
		g.setColor(Color.red);
        g.fillPolygon(cabPoly);
		g.setColor(Color.black);
        g.drawPolygon(truckPoly);
        g.drawPolygon(cabPoly);
	}


	// draw the dock where the truck drives to.
	public void drawDock(Graphics g) {
		g.setColor(Color.black);
		g.setPaintMode();
		g.drawLine(size().width/2, 0, size().width/2, 8);
	}
}