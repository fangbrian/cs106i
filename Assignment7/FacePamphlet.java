/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends ConsoleProgram 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		populateNorthPanel();
		populateWestPanel();
		addActionListeners();
    }
	
	private void populateNorthPanel(){
		nameField = new JTextField(TEXT_FIELD_SIZE);	
		add(new JLabel("Name: "), NORTH);
		add(nameField, NORTH);
		nameField.addActionListener(this);
		addButton = new JButton("Add");
		add(addButton, NORTH);
		deleteButton = new JButton("Delete");
		add(deleteButton, NORTH);
		lookupButton = new JButton("Lookup");
		add(lookupButton, NORTH);
	}
	
	private void populateWestPanel(){
		changeStatusField = new JTextField(TEXT_FIELD_SIZE);
		add(changeStatusField, WEST);
		changeStatusButton = new JButton("Change Status");
		add(changeStatusButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		changePictureField = new JTextField(TEXT_FIELD_SIZE);
		add(changePictureField, WEST);
		changePictureButton = new JButton("Change Picture");
		add(changePictureButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		addFriendField = new JTextField(TEXT_FIELD_SIZE);
		add(addFriendField, WEST);
		addFriendButton = new JButton("Add Friend");
		add(addFriendButton, WEST);
	}
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		if(e.getSource() == nameField){
			println(nameField.getText());
		}
		if(e.getActionCommand().equals("Add")){
			println("Add Profile " + nameField.getText());

		}
		if(e.getActionCommand().equals("Delete")){
			println("Deleting " + nameField.getText());
		}
		if(e.getActionCommand().equals("Lookup")){
			println("Lookup " + nameField.getText());
		}
		if(e.getActionCommand().equals("Change Status")){
			println("Change Status " + changeStatusField.getText());
		}
		if(e.getActionCommand().equals("Change Picture")){
			println("Change Picture " + changePictureField.getText());
		}
		if(e.getActionCommand().equals("Add Friend")){
			println("Add Friend " + addFriendField.getText());
		}
	}
    
    private JTextField nameField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton lookupButton;
    private JTextField changeStatusField; 
    private JTextField changePictureField; 
    private JTextField addFriendField; 
    private JButton changeStatusButton; 
    private JButton changePictureButton;
    private JButton addFriendButton;
    

}
