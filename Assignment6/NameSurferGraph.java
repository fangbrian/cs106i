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
import java.awt.Color;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		//	 You fill in the rest //
		internalList = new ArrayList <NameSurferEntry>();	
		currentColor = Color.MAGENTA;
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
		internalList.clear();
		currentColor = Color.magenta;
		update();
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
		drawEntry(entry);
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
		double xWidth = getWidth();
		double yHeight = getHeight();
		int initialRank;
		int nextRank;
		
		if(currentColor == Color.MAGENTA){
			currentColor = Color.black;
		} else if(currentColor == Color.BLACK){
			currentColor = Color.red;
		} else if(currentColor == Color.RED){
			currentColor = Color.blue;
		} else if(currentColor == Color.BLUE){
			currentColor = Color.magenta;
		}
		
		for(int i = 0; i < NDECADES-1; i++){
			initialRank = entry.getRank(i);
			nextRank = entry.getRank(i+1);
			double yRankCoordinateInitial = ((getHeight() - 2*GRAPH_MARGIN_SIZE)/(double)MAX_RANK) * initialRank + GRAPH_MARGIN_SIZE;
			double yRankCoordinateNext = ((getHeight() - 2*GRAPH_MARGIN_SIZE)/(double)MAX_RANK) * nextRank + GRAPH_MARGIN_SIZE;
			if(initialRank == 0){
				yRankCoordinateInitial = yHeight - GRAPH_MARGIN_SIZE;
				GLabel lineLabel = new GLabel(entry.getName() + " *", 
						i*xWidth/NDECADES, yRankCoordinateInitial);
				lineLabel.setColor(currentColor);
				add(lineLabel);
			}else {
				GLabel lineLabel = new GLabel(entry.getName() + Integer.toString(initialRank), 
						i*xWidth/NDECADES, yRankCoordinateInitial);
				lineLabel.setColor(currentColor);
				add(lineLabel);
			}
			if(nextRank == 0){
				yRankCoordinateNext = yHeight - GRAPH_MARGIN_SIZE;
			}
			
			GLine nameLine = new GLine(i*xWidth/NDECADES, yRankCoordinateInitial, (i+1)*xWidth/NDECADES, yRankCoordinateNext);
			nameLine.setColor(currentColor);
			add(nameLine);
		}

	}
	
	
	
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	private ArrayList<NameSurferEntry> internalList;
	private Color currentColor;
}
