package hck.testmap2;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GPSLocation extends Activity implements LocationListener {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslocation);

        textView = (TextView) findViewById(R.id.textView);

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                String locate = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
                textView.setText(textView.getText().toString() + "\n" + locate);
            }
            Toast.makeText(this, "GPS started",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "GPS error\n"+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpslocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_LONG).show();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String locate = "Latitude: " + latitude + ", Longitude: " + longitude;

        Log.i("Geo_Location", locate);
        Toast.makeText(this, locate, Toast.LENGTH_LONG).show();
        textView.setText(textView.getText().toString()+"\n"+locate);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
