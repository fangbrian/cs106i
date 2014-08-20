package com.example.fangb.testdatabase;

        import android.app.Activity;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.telephony.SmsManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        nameButton = (EditText) findViewById(R.id.nameText);
        phoneButton = (EditText) findViewById(R.id.phoneText);

        openDB();
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
    private void closeDB() {
        myDb.close();
    }

    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    private void clearDisplayText(){
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText("");
    }

    public void onClick_AddRecord(View v) {
        displayText("Clicked add record!");

        //long newId = myDb.insertRow("Jenny", "5556");
        if(nameButton.getText().toString() != null && phoneButton.getText().toString() != null) {
            long newId = myDb.insertRow(nameButton.getText().toString(), phoneButton.getText().toString());
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

    public void onClick_SendText(View v){
        Cursor cursor = myDb.getAllRows();
        sendTextToName(cursor);
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

                // Append data to the message:
                message += "id=" + id
                        +", Name=" + name
                        +", Phone=" + phoneNum
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
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                if(name.equals(nameButton.getText().toString())) {
                    String phoneNum = cursor.getString(DBAdapter.COL_PHONE);
                    clearDisplayText();
                    String msg = "Texting " + nameButton.getText().toString();
                    Toast.makeText(MyActivity.this, msg, Toast.LENGTH_LONG).show();
                    sendSMS(phoneNum, "I'm Outsideeeee!");
                    break;
                }


            } while(cursor.moveToNext());
            cursor.close();
        }
    }
}










