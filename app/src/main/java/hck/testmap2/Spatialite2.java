package hck.testmap2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import gs.spatialite.helpers.ActivityHelper;
import gs.spatialite.helpers.AssetHelper;
import jsqlite.Database;
import jsqlite.Exception;

public class Spatialite2 extends Activity {
    protected jsqlite.Database geodb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatialite2);
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

    public void runHongKong(View v) throws Exception, IOException {
        StringBuilder sb = new StringBuilder();
        TextView tv = (TextView) findViewById(R.id.textView01);
        String gdbFile = "hk.sp3";
        String strFileApp = this.getFilesDir() + "/" + gdbFile;
        String strFileSdcard = this.getExternalFilesDir(null) + "/" + gdbFile;
        File appFile = new File(strFileApp);
        File sdcardFile = new File(strFileSdcard);

        //
        // Test if geodb exists or not
        //
        if (!appFile.exists()) {
            // copy from Assets to app location
            AssetHelper.CopyAsset(this, ActivityHelper.getPath(this, false), gdbFile);
        }

        //
        // Open geodb database
        //
        geodb = new Database();
        geodb.open(strFileApp, jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
        sb.append("Database opened!\n\n");

        //
        // Spatial Queries
        //
        sb.append("District code and name\n");
        sb.append(hkOps.geoex_03_101(geodb));

        sb.append("\nFind schools within Eastern District\n");
        sb.append(hkOps.geoex_03_103(geodb));

        sb.append("\nPoint-in-polygon test (Ex 203)\n");
        sb.append(hkOps.geoex_03_203(geodb));

        //
        // Display results
        //
        tv.setText(sb.toString());
    }
}
