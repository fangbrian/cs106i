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
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program
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
		faceDatabase = new FacePamphletDatabase();
		hashEntry = null;
		canvasDisplay = new FacePamphletCanvas();
		add(canvasDisplay);
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
			processAdd();
		}
		if(e.getActionCommand().equals("Delete")){
			processDelete();
		}
		if(e.getActionCommand().equals("Lookup")){
			processLookup();
		}
		if(e.getActionCommand().equals("Change Status")){
			processChangeStatus();
		}
		if(e.getActionCommand().equals("Change Picture")){
			processChangePicture();
		}
		if(e.getActionCommand().equals("Add Friend")){
			processAddFriend();
		}
	}
    
    private void deleteProfileFromFriends(){
    	hashEntry = faceDatabase.getProfile(nameField.getText());
    	Iterator<String> friendsIter = hashEntry.getFriends();
    	while(friendsIter.hasNext()){
    		faceDatabase.getProfile(friendsIter.next()).removeFriend(hashEntry.getName());
    	}
    }
    
    private void processAdd(){
		if(faceDatabase.containsProfile(nameField.getText())){
			hashEntry = faceDatabase.getProfile(nameField.getText());
		} else if(!nameField.getText().equals("")) {
			FacePamphletProfile newProfile = new FacePamphletProfile(nameField.getText());
			faceDatabase.addProfile(newProfile);
			hashEntry = faceDatabase.getProfile(nameField.getText());
		}
		canvasDisplay.displayProfile(hashEntry);
    }
    
    private void processDelete(){
		if(faceDatabase.containsProfile(nameField.getText())){
			canvasDisplay.showMessage("Deleting Profile");
			deleteProfileFromFriends();
			faceDatabase.deleteProfile(hashEntry.getName());
		} else {
			canvasDisplay.showMessage("Profile: " + nameField.getText() + " Does Not Exist. Cannot delete.");
		}
		hashEntry = null;
		canvasDisplay.displayProfile(hashEntry);
    }
    
    private void processLookup(){
		if(faceDatabase.containsProfile(nameField.getText())){
			hashEntry = faceDatabase.getProfile(nameField.getText());
		} else {
			hashEntry = null;
		}
		canvasDisplay.displayProfile(hashEntry);
		if(hashEntry == null) canvasDisplay.showMessage("Lookup Profile " + nameField.getText() + " Does Not Exist.");
    }
    
    private void processChangeStatus(){
		if(hashEntry != null){
			hashEntry.setStatus(changeStatusField.getText());
			canvasDisplay.displayProfile(hashEntry);
		} else {
			canvasDisplay.showMessage("Select a Profile first. Cannot Change Status on no profile.");
		}
		
		if(changeStatusField.getText() == ""){
			canvasDisplay.showMessage("Please enter a status.");
		}
    }
    
    private void processChangePicture(){
		GImage image = null;
		try{
			image = new GImage(changePictureField.getText());
		} catch (ErrorException ex) {
			canvasDisplay.showMessage("Invalid Image File!!");
		}
		
		if(image != null && hashEntry != null){
			hashEntry.setImage(image);
			canvasDisplay.displayProfile(hashEntry);
		} else if(hashEntry == null) {
			canvasDisplay.showMessage("Please select Profile to change picture on.");
		} else if(image == null) {
			canvasDisplay.showMessage("Invalid Image File.");
		}
    }
    
    private void processAddFriend(){
		if(hashEntry.getName().equals(addFriendField.getText())){
			canvasDisplay.showMessage("Cannot add yourself as a friend. Go make some real friends.");
		} else if(hashEntry != null && faceDatabase.containsProfile(addFriendField.getText())){
			hashEntry.addFriend(addFriendField.getText());
			FacePamphletProfile friendProfile = faceDatabase.getProfile(addFriendField.getText());
			friendProfile.addFriend(hashEntry.getName());
			canvasDisplay.displayProfile(hashEntry);
		} else if(hashEntry == null) {
			canvasDisplay.showMessage("Cannot Add friend: No current profile chosen.");
		} else if(!faceDatabase.containsProfile(addFriendField.getText())){
			canvasDisplay.showMessage("Friend does not exist. Cannot add " + addFriendField.getText() + ".");
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
    private FacePamphletDatabase faceDatabase;
    private FacePamphletProfile hashEntry;
    private FacePamphletCanvas canvasDisplay;
    

}