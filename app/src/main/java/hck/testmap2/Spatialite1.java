package hck.testmap2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import jsqlite.Database;
import jsqlite.Exception;

public class Spatialite1 extends Activity {
    protected jsqlite.Database db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatialite1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void runTests(View v) throws Exception, IOException {
        StringBuilder sb = new StringBuilder();
        EditText et = (EditText) findViewById(R.id.editText01);
        String appDir1 = null;
        String spatialDbFile = null;

        appDir1 = this.getFilesDir().getPath();
        spatialDbFile = appDir1 + "/" + "myTest.sp3";
        db = new Database();
        db.open(spatialDbFile, jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
        sb.append("Database opened!\n\n");

        // --- Drop existing table (myFirstSpatialTable) --- //
        mySpatialiteOperations.dropTable(db);
        sb.append("Table dropped.\n\n");

        // --- Create table (myFirstSpatialTable) --- //
        mySpatialiteOperations.createTable(db);
        sb.append("Table created.\n\n");

        // --- Insert two records containing point data type --- //
        mySpatialiteOperations.insertRecords(db);
        sb.append("Records inserted!\n\n");

        // --- Find number of records in the table --- //
        int records = mySpatialiteOperations.selectCount(db);
        sb.append("Total records: ").append(records).append("\n\n");

        // --- List all records --- //
        String str1 = mySpatialiteOperations.showRecordAsText(db);
        sb.append("Records:\n").append(str1).append("\n");

        // --- Find the distance between selected point and a fixed point (830000 812000) --- //
        String str2 = mySpatialiteOperations.showDistance(db);
        sb.append("Distance:\n").append(str2);

        // --- Close the database file (myTest.sp3) --- //
        db.close();
        sb.append("\n\nDatabase closed!\n");

        // --- Display text in the EditText --- //
        et.setText(sb.toString());
    }
}
