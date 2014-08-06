/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;

import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		message = new GLabel("");
		profileName = new GLabel("");
		profileStatus = new GLabel("");
		profilePicture = null;
		noImage = new GRect(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT);
		friendString = new GLabel("Friends: ");
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		message.setLabel(msg);
		message.setLocation((APPLICATION_WIDTH - message.getWidth())/2,APPLICATION_HEIGHT - BOTTOM_MESSAGE_MARGIN);
		message.setFont(MESSAGE_FONT);
		add(message);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if(profile != null){
			profileName.setLabel(profile.getName());
			profileStatus.setLabel("Status: " + profile.getStatus());
			profilePicture = profile.getImage();
			displayLabels();
			displayImage();
			displayFriends(profile);
		}
		
	}
	
	private void displayLabels(){
		profileName.setFont(PROFILE_NAME_FONT);
		profileName.setLocation(LEFT_MARGIN, TOP_MARGIN);
		profileName.setColor(Color.BLUE);
		add(profileName);
		profileStatus.setFont(PROFILE_STATUS_FONT);
		profileStatus.setLocation(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN + IMAGE_HEIGHT+ STATUS_MARGIN);
		add(profileStatus);
	}
	
	private void displayImage(){
			double imageHeight = profilePicture.getHeight();
			double imageWidth = profilePicture.getWidth();
			if(imageHeight > IMAGE_HEIGHT) profilePicture.scale(IMAGE_HEIGHT/imageHeight);
			if(imageWidth > IMAGE_WIDTH) profilePicture.scale(IMAGE_WIDTH/imageWidth);
			centerImage();

			add(profilePicture);
		
	}
	private void centerImage(){
		double imageHeight = profilePicture.getHeight();
		if(imageHeight == IMAGE_HEIGHT) profilePicture.setLocation(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN);
		if(imageHeight < IMAGE_HEIGHT) profilePicture.setLocation(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN + ((IMAGE_HEIGHT - imageHeight)/2));
	}
	
	private void displayFriends(FacePamphletProfile profile){
		friendString.setFont(PROFILE_FRIEND_LABEL_FONT);
		friendString.setLocation(APPLICATION_WIDTH/2, TOP_MARGIN);
		add(friendString);

		int i = 2;
    	Iterator<String> friendsIter = profile.getFriends();
    	while(friendsIter.hasNext()){
    		GLabel friendName = new GLabel(friendsIter.next(), APPLICATION_WIDTH/2, i*TOP_MARGIN);
    		add(friendName);
    		i++;
    	}
	}

	private GLabel message;
	private GLabel profileName; 
	private GLabel friendString;
	private GLabel profileStatus; 
	private GImage profilePicture;
	private GRect noImage;
	
}
