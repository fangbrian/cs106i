/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
	    // You fill this in, along with any helper methods //
		nameDataBase = new NameSurferDataBase("a.txt");
		
		nameField = new JTextField(10);	
		add(new JLabel("Name: "), SOUTH);
		add(nameField, SOUTH);
		nameField.addActionListener(this);
		graphButton = new JButton("Graph");
		add(graphButton, SOUTH);
		clearButton = new JButton("Clear");
		add(clearButton, SOUTH);
		addActionListeners();
		graph = new NameSurferGraph();
		add(graph);

	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		// You fill this in //
		if(e.getSource() == nameField){
			println(nameField.getText());
		}
		if(e.getActionCommand().equals("Graph")){
			println("GRAPHING");
			hashEntry = nameDataBase.findEntry(nameField.getText());
			if(hashEntry != null){
				println(hashEntry.toString());
				graph.addEntry(hashEntry);
			}

		}
		if(e.getActionCommand().equals("Clear")){
			println("CLEARING");
		}
	}
	
	private JTextField nameField;
	private JButton graphButton;	
	private JButton clearButton;
	private NameSurferEntry hashEntry; 
	private NameSurferDataBase nameDataBase;
	private NameSurferGraph graph;
}
