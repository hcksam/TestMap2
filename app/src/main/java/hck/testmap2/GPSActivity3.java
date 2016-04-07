package hck.testmap2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GPSActivity3 extends Activity implements LocationListener {
	private LocationManager myCurrentLocation = null;
	private Location lastLocation = null;
	private String myBestProvider = "";
	private static final String TAG = "Tracking_006";
	SQLiteDatabase mDatabase;
	String dbFilePublic = "";
	private List<String> enabledProviders;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbFilePublic = Environment.getExternalStoragePublicDirectory("hkpu.lsgi547.app_3").toString() + "/";
		mDatabase = openOrCreateDatabase(dbFilePublic + "LocationTracking_v6.sqlite", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		mDatabase.setVersion(1);
		mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
				"points (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		"time TEXT, " +
				"latitude DOUBLE, " +
				"longitude DOUBLE, " +
				"altitude REAL, " +
        		"accuracy REAL, " +
        		"provider TEXT);");
		
		setContentView(R.layout.gps3);
		final Button start = (Button) findViewById(R.id.start);
		final Button stop = (Button) findViewById(R.id.stop);
		final TextView status = (TextView) findViewById(R.id.status);
		final TextView statusProvider = (TextView) findViewById(R.id.statusProvider);
		myCurrentLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		statusProvider.setText("");
		status.setText("DB file path: " + dbFilePublic);
		//
		
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(GPSActivity3.this, "Starting...", Toast.LENGTH_SHORT).show();
				StringBuffer sb = new StringBuffer();
				enabledProviders = myCurrentLocation.getProviders(true);
				if (enabledProviders.isEmpty()) {
					statusProvider.setText("Enabled Providers: NIL\n");
				} else {
					for (String enabledProvider : enabledProviders) {
						sb.append(enabledProvider).append(" ");
					}
					sb.append("\n");
					statusProvider.setText("Enabled Providers: " + sb);
				}
				Criteria myCriteria = new Criteria();
				myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
				myCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
				myBestProvider = myCurrentLocation.getBestProvider(myCriteria, true);
				try {
					myCurrentLocation.requestLocationUpdates(myBestProvider, 3000, 0, GPSActivity3.this);
				}catch (Exception e){

				}
				status.setText("....Data will go here....");
				
				start.setVisibility(View.GONE);
				stop.setVisibility(View.VISIBLE);
			}
		});
		
		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
				myCurrentLocation.removeUpdates(GPSActivity3.this);
				}catch (Exception e){

				}
				//mDatabase.close();
				stop.setVisibility(View.GONE);
				start.setVisibility(View.VISIBLE);
			}
		});
	}
	
	@Override
	public void onLocationChanged(Location lvLocation) {
		/*
		 * "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
		 * "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
		 * "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
		 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
		 * "yyMMddHHmmssZ"-------------------- 010704120856-0700
		 * "K:mm a, z" ----------------------- 0:08 PM, PDT
		 * "h:mm a" -------------------------- 12:08 PM
		 * "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
		 * 
		 */
/*
		ContentValues cv = new ContentValues();
		cv.put("latitude", lvLocation.getLatitude());
		cv.put("longitude", lvLocation.getLongitude());
		cv.put("altitude", lvLocation.getAltitude());
		cv.put("accuracy", lvLocation.getAccuracy());
		cv.put("time", lvLocation.getTime());
		cv.put("provider", lvLocation.getProvider());
		long locationID = mDatabase.insert("points", null, cv);
		if (locationID == -1) {
			Log.e(TAG, "Unable to save point");
		} else {
			Log.d(TAG, "Received Location -> " + lvLocation);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS Z", Locale.US);
		String currentDT = df.format(Calendar.getInstance().getTime());
		double currentLat = lvLocation.getLatitude();
		double currentLon = lvLocation.getLongitude();
		double currentAltitude = lvLocation.getAltitude();
		double currentAccuracy = lvLocation.getAccuracy();
*/
		
		long currentTime = lvLocation.getTime();
		Date currentDate = new Date(currentTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z", Locale.US);
		String currentTimeString = sdf.format(currentDate);
		double currentLat = lvLocation.getLatitude();
		double currentLon = lvLocation.getLongitude();
		double currentAltitude = lvLocation.getAltitude();
		double currentAccuracy = lvLocation.getAccuracy();
		String currentProvider = lvLocation.getProvider();
		
		ContentValues cv = new ContentValues();
		cv.put("time", currentTimeString);
		cv.put("latitude", currentLat);
		cv.put("longitude", currentLon);
		cv.put("altitude", currentAltitude);
		cv.put("accuracy", currentAccuracy);
		cv.put("provider", currentProvider);
		long locationID = mDatabase.insert("points", null, cv);
		if (locationID == -1) {
			Log.e(TAG, "Unable to save point");
		} else {
			Log.d(TAG, "Received Location -> " + lvLocation);
		}
		
		StringBuilder locInfo = new StringBuilder("Current location:\n");
		locInfo.append("\tDateTime = ").append(currentTimeString).append("\n");
		locInfo.append("\tLat = ").append(currentLat).append("\n");
		locInfo.append("\tLon = ").append(currentLon).append("\n");
		locInfo.append("\tAlt = ").append(currentAltitude).append("\n");
		locInfo.append("\tAcc = ").append(currentAccuracy).append("\n");
		locInfo.append("\tProvider = ").append(currentProvider).append("\n");
		if (lastLocation != null) {
			float distance = lvLocation.distanceTo(lastLocation);
			locInfo.append("\tDistance from last point = ").append(distance).append(" meters\n");
		}
		lastLocation = lvLocation;
		
		TextView status = (TextView) findViewById(R.id.status);
		status.setText(locInfo);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}
}
