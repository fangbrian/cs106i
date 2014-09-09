package com.example.fangb.testdatabase;

        import android.app.Activity;
        import android.content.Context;
        import android.database.Cursor;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.util.List;
        import android.os.Handler;

/*
 * Steps to using the DB:
 * 1. [DONE] Instantiate the DB Adapter
 * 2. [DONE] Open the DB
 * 3. [DONE] use get, insert, delete, .. to change data.
 * 4. [DONE]Close the DB
 */

/**
 * Demo application to show how to use the
 * built-in SQL lite database.
 */
public class MyActivity extends Activity {

    DBAdapter myDb;
    private EditText nameButton;
    private EditText phoneButton;
    //private EditText longitudeButton;
   //private EditText latitudeButton;
    public TextView textView;
    private LocationManager gpsManager;
    private LocationListener gpsListener;
    private double longitude;
    private double latitude;
    private Location destination = null;
    private static int DESTINATION_THRESHOLD_METERS = 250;
    private static int LEAVING = 1;
    private static int ARRIVED = 2;
    private Destination currDestination;
    private Context a;
    private Location currLocation = new Location("");
    private ProgressBar progressInterface;
    private double initialDistance;
    private double currentDistance;
    private int progressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        nameButton = (EditText) findViewById(R.id.nameText);
        phoneButton = (EditText) findViewById(R.id.phoneText);
        progressInterface = (ProgressBar) findViewById(R.id.progressBar);
        initialDistance = 0;
        currentDistance = 0;
        //longitudeButton = (EditText) findViewById(R.id.longitudeText);
        //latitudeButton = (EditText) findViewById(R.id.latitudeText);
        currDestination = new Destination("", 0, 0, false);
        currLocation.setLongitude(0);
        currLocation.setLatitude(0);
        a = this;
        latitude = 0;
        longitude = 0;
        openDB();
        initializeLocation();
    }

    public class LocationManagerHelper implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if(loc != null) {
                currLocation = loc;
                double lat = loc.getLatitude();
                double longi = loc.getLongitude();


                if(currDestination.valid()){
                    if(initialDistance == 0) {
                        initialDistance = loc.distanceTo(currDestination.getLocation());
                    }

                    currentDistance = loc.distanceTo(currDestination.getLocation());
                    //DEBUG
                    String msg = "Initial Distance: " + initialDistance;
                    displayText(msg);

                    //Check distance and see if we have met threshold
                    if(loc.distanceTo(currDestination.getLocation()) <= DESTINATION_THRESHOLD_METERS ) {
                        //Send Text
                        Cursor cursor = myDb.getAllRows();
                        sendTextToName(cursor, ARRIVED);
                        displayText("THRESHOLD MET. YOU'RE CLOSE");
                        //Clear Current Destination
                        currDestination.clearAll();
                        clearInternalDistances();
                    }



                } else {
                    String msg = "Long: " + lat + " Lat: " + longi;
                    displayText(msg);
                }
            }
        }


        @Override
        public void onProviderDisabled(String provider) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void clearInternalDistances(){
        currentDistance = 0;
        initialDistance = 0;
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void initializeLocation(){
        gpsManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new LocationManagerHelper();
        gpsManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        if(gpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            String msg = "Long: " + longitude + " Lat: " + latitude;
            displayText(msg);
            Log.i("GPS", "DOES THIS WORK");
        } else{
            displayText("GPS DISABLED");
        }
    }

    private void closeDB() {
        myDb.close();
    }

    private void displayText(String message) {
        textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    private void clearDisplayText(){
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText("");
    }

    public void onClick_AddRecord(View v) {
        displayText("Clicked add record!");

        //Test Longitude and Latitude
        //Need Error Checking
        //longitude = Integer.parseInt(longitudeButton.getText().toString());
        //latitude = Integer.parseInt(latitudeButton.getText().toString());

/*
        Geocoder gc = new Geocoder(a.getApplicationContext());
        //if(gc.isPresent()){
            try{
                List<Address> list = gc.getFromLocationName("1600 Amphitheatre Parkway, Mountain View, CA", 1);
                Address address = list.get(0);

                longitude = address.getLatitude();
                latitude = address.getLongitude();
            }catch(IOException e){

            }
        //}
        */

        //long newId = myDb.insertRow("Jenny", "5556");
        if(!nameButton.getText().toString().equals("Name") && !phoneButton.getText().toString().equals("Phone")) {
            long newId = myDb.insertRow(nameButton.getText().toString(), phoneButton.getText().toString(),
                    currLocation.getLongitude(), currLocation.getLatitude());
            // Query for the record we just added.
            // Use the ID:
            Cursor cursor = myDb.getRow(newId);
            displayRecordSet(cursor);
        } else {
            displayText("Invalid Entry");
        }
    }

    public void onClick_ClearAll(View v){
        displayText("Clicked clear all!");
        myDb.deleteAll();
    }

    public void onClick_DisplayRecords(View v){
        displayText("Clicked display record!");
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    public void onClick_SetDestination(View v){
        Cursor cursor = myDb.getAllRows();
        //sendTextToName(cursor);
        setCurrentDestination(cursor);
        Cursor cursor2 = myDb.getAllRows();
        sendTextToName(cursor2, LEAVING);
        StartProgressBar();
    }

    private void StartProgressBar(){
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    if(initialDistance == 0){
                        progressStatus = 0;
                    } else {
                        progressStatus = 1-(((int)currentDistance-DESTINATION_THRESHOLD_METERS)/((int)initialDistance-DESTINATION_THRESHOLD_METERS));
                        //DEBUG
                        String msg = "Progress Status: " + progressStatus;
                        displayText(msg);
                    }



                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            progressInterface.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                String phoneNum = cursor.getString(DBAdapter.COL_PHONE);
                double longitudeCoord = cursor.getDouble(DBAdapter.COL_LONG);
                double latitudeCoord = cursor.getDouble(DBAdapter.COL_LAT);

                // Append data to the message:
                message += "id=" + id
                        +", Name=" + name
                        +", Phone=" + phoneNum
                        +", Long=" + longitudeCoord
                        +", Lat=" + latitudeCoord
                        +"\n";
            } while(cursor.moveToNext());
        }
        cursor.close();
        displayText(message);
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void sendTextToName(Cursor cursor, int action){
        if(!currDestination.getName().equals("") && currDestination.valid()){
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    int id = cursor.getInt(DBAdapter.COL_ROWID);
                    String name = cursor.getString(DBAdapter.COL_NAME);
                    if (name.equals(currDestination.getName())) {
                        String phoneNum = cursor.getString(DBAdapter.COL_PHONE);
                        clearDisplayText();
                        String msg = "Texting " + name;
                        Toast.makeText(MyActivity.this, msg, Toast.LENGTH_LONG).show();
                        String msgText = "";
                        switch(action){
                            case 1:
                                msgText = "I'm leaving now. Be there soon.";
                                break;
                            case 2:
                                msgText = "Hey come outside. I'm here.";
                                break;
                            default:
                                break;
                        }
                        sendSMS(phoneNum, msgText);

                        break;
                    }
                } while (cursor.moveToNext());
        }
            cursor.close();
        }
    }

    private void setCurrentDestination(Cursor cursor){
        if (cursor.moveToFirst()) {
            currDestination.clearAll();
            //DEBUG
            String message = "";
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                if (name.equals(nameButton.getText().toString())) {
                    // Process the data:
                    String phoneNum = cursor.getString(DBAdapter.COL_PHONE);
                    double longitudeCoord = cursor.getDouble(DBAdapter.COL_LONG);
                    double latitudeCoord = cursor.getDouble(DBAdapter.COL_LAT);
                    //DEBUG: memory leak?
                    currDestination.setName(name);
                    currDestination.setLong(longitudeCoord);
                    currDestination.setLat(latitudeCoord);
                    currDestination.setValid(true);
                    //DEBUG
                    message += "Set Destination Name=" + currDestination.getName()
                            + ", Long=" + currDestination.getLong()
                            + ", Lat=" + currDestination.getLat();
                    displayText(message);

                    break;

                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        initialDistance = 0;
    }
}










