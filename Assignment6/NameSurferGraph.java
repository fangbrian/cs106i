/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		//	 You fill in the rest //
		internalList = new ArrayList <NameSurferEntry>();
		
	}
	
	private void drawGrid(){
		int x_width = getWidth();
		int decade = 1900;
		for (int i = 0; i < NDECADES; i++){
			GLine xgrid = new GLine(i*x_width/NDECADES, 0 , i*x_width/NDECADES, getHeight());
			add(xgrid);
			decade = 1900 + (i*10);
			GLabel decadeLabel = new GLabel(Integer.toString(decade));
			double height = decadeLabel.getHeight();
			decadeLabel.setLocation( i*x_width/NDECADES, getHeight() - GRAPH_MARGIN_SIZE + height);
			add(decadeLabel);

		}
		GLine upperMargin = new GLine(0, GRAPH_MARGIN_SIZE, x_width, GRAPH_MARGIN_SIZE);
		GLine lowerMargin = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, x_width, getHeight() - GRAPH_MARGIN_SIZE);
		add(upperMargin);
		add(lowerMargin);


	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		//	 You fill this in //

	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		// You fill this in //
		internalList.add(entry);
	}
	
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		//	 You fill this in //
		removeAll();
		drawGrid();
		drawEntries();

	}
	
	private void drawEntries(){
		if(internalList.size() > 0){
			for(int i = 0; i < internalList.size(); i++){
				drawEntry(internalList.get(i));
			}
		}
	}
	
	private void drawEntry(NameSurferEntry entry){
		int xWidth = getWidth();
		int yHeight = getHeight();
		
		
	}
	
	
	
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	private GRect rect;
	private ArrayList<NameSurferEntry> internalList;
}
