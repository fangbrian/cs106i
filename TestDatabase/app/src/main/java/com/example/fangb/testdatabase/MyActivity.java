package com.example.fangb.testdatabase;

        import android.app.Activity;
        import android.content.Context;
        import android.database.Cursor;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

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
    private EditText longitudeButton;
    private EditText latitudeButton;
    public TextView textView;
    private LocationManager gpsManager;
    private LocationListener gpsListener;
    private double longitude;
    private double latitude;
    private Location destination = null;
    private static int DESTINATION_THRESHOLD_METERS = 100000;
    private Destination currDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        nameButton = (EditText) findViewById(R.id.nameText);
        phoneButton = (EditText) findViewById(R.id.phoneText);
        longitudeButton = (EditText) findViewById(R.id.longitudeText);
        latitudeButton = (EditText) findViewById(R.id.latitudeText);
        currDestination = new Destination("", 0, 0, false);
        latitude = 0;
        longitude = 0;
        openDB();
        initializeLocation();
    }

    public class LocationManagerHelper implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if(loc != null) {
                double lat = loc.getLatitude();
                double longi = loc.getLongitude();

                String msg = "Long: " + lat + " Lat: " + longi;
                displayText(msg);

                if(loc.distanceTo(currDestination.getLocation()) <= DESTINATION_THRESHOLD_METERS ){
                    if(currDestination.valid()) {
                        //Send Text
                        //Cursor cursor = myDb.getAllRows();
                        //sendTextToName(cursor);
                        displayText("THRESHOLD MET. YOU'RE CLOSE");
                        //Clear Current Destination
                        currDestination.clearAll();
                    }
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
        longitude = Integer.parseInt(longitudeButton.getText().toString());
        latitude = Integer.parseInt(latitudeButton.getText().toString());

        //long newId = myDb.insertRow("Jenny", "5556");
        if(!nameButton.getText().toString().equals("Name") && !phoneButton.getText().toString().equals("Phone")) {
            long newId = myDb.insertRow(nameButton.getText().toString(), phoneButton.getText().toString(), (int)longitude, (int)latitude);
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
                int longitudeCoord = cursor.getInt(DBAdapter.COL_LONG);
                int latitudeCoord = cursor.getInt(DBAdapter.COL_LAT);

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

    private void sendTextToName(Cursor cursor){
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
                        sendSMS(phoneNum, "Hello from Brian's Android Program");

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
                    int longitudeCoord = cursor.getInt(DBAdapter.COL_LONG);
                    int latitudeCoord = cursor.getInt(DBAdapter.COL_LAT);
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
    }
}










