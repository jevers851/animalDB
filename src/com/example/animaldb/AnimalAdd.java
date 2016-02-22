package com.example.animaldb;


import java.util.ArrayList;


import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AnimalAdd extends Activity
{


	private EditText txtANI_TYPE_CD_ADD;
	private EditText txtANI_COUNT_ADD;
	private EditText txtANI_DTM_ADD;
	private EditText txtANI_COM_ADD;
	private Button btnAdd;
	private AnimalDB DB;
	
	private Spinner cboAnimalCode;
	private ArrayList<AnimalCode> spinnerItems;
	private ArrayAdapter<AnimalCode> dataAdapter;
	private double loc_lat = 0;
	private double loc_lng = 0;
	
	LocationManager locationManager = null;
	Location currentLocation = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animal_add);
		
		txtANI_TYPE_CD_ADD = (EditText)findViewById(R.id.txtANI_TYPE_CD_ADD);
		txtANI_COUNT_ADD = (EditText)findViewById(R.id.txtANI_COUNT_ADD);
		txtANI_DTM_ADD = (EditText)findViewById(R.id.txtANI_DTM_ADD);
		txtANI_COM_ADD = (EditText)findViewById(R.id.txtANI_COM_ADD);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(btnAdd_Click);
		
		cboAnimalCode = (Spinner)findViewById(R.id.cboANIMAL_CD);
		cboAnimalCode.setOnItemSelectedListener(cboAnimalCode_Selected);
		
		
		DB = new AnimalDB(this);
		
		spinnerItems = new ArrayList<AnimalCode>();
		
		
		LocationListener locationListener = new LocationListener() 
		{
		    public void onLocationChanged(Location location) {

		    	Log.i("edu.davenport.cisp340.studentdatabase", "onLocationChanged: " + location.toString());
		    	loc_lat = location.getLatitude();
		    	loc_lng = location.getLongitude();
		    	
		    	currentLocation = location; 
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) 
		    {
		    	Log.i("edu.davenport.cisp340.studentdatabase", "onStatusChanged: " + String.valueOf(status));
		    }

		    public void onProviderEnabled(String provider) 
		    {
		    	Log.i("edu.davenport.cisp340.studentdatabase","onProviderEnabled");
		    }

		    public void onProviderDisabled(String provider) 
		    {
		    	Log.i("edu.davenport.cisp340.studentdatabase","onProviderDisabled");
		    }
		  };
	
		  
		  
			locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
			
			if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
			{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
				Log.i("edu.davenport.cisp340.studentdatabase", "isProviderEnabled(GPS): TRUE");
			}
			else
				Log.i("edu.davenport.cisp340.studentdatabase", "isProviderEnabled(GPS): FALSE");

			if( locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) )
				Log.i("edu.davenport.cisp340.studentdatabase", "isProviderEnabled(NETWORK): TRUE");
			else
				Log.i("edu.davenport.cisp340.studentdatabase", "isProviderEnabled(NETWORK): FALSE");
		  
		  
		  
		  if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) 
		  {
			  Log.i("edu.davenport.cisp340.studentdatabase","isProviderEnabled");
			  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		    }
		  
		  
	}
	
	
	OnItemSelectedListener cboAnimalCode_Selected = new OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
			
		}
	};	
	
	OnClickListener btnAdd_Click = new OnClickListener()
	{

		@Override
		public void onClick(View v) 
		{
			String name = txtANI_TYPE_CD_ADD.getText().toString();
			String count = txtANI_COUNT_ADD.getText().toString();
			String dtm = txtANI_DTM_ADD.getText().toString();
			String com = txtANI_COM_ADD.getText().toString();
			String cd = cboAnimalCode.getSelectedItem().toString();
			
			
			new AnimalAddTask().execute( name, count, dtm, com, cd, loc_lat, loc_lng );
		}
	};
	
	protected void onResume()
	{
		super.onResume();
		new getAnimalCodes().execute((Object[])null);
	}
	
	private class getAnimalCodes extends AsyncTask<Object, Object, Cursor>
	{

		@Override
		protected Cursor doInBackground(Object... params)
		{
			return DB.AnimalCodeList();
		}
		
		protected void onPostExecute(Cursor result)
		{
		
			if( result.moveToFirst() )
			{
				do
				{
					AnimalCode m = new AnimalCode();
					m.ANIMAL_CD = result.getString(result.getColumnIndex("animal_cd"));
					m.ANIMAL_DESC = result.getString(result.getColumnIndex("animal_desc"));
					spinnerItems.add(m);
				} while( result.moveToNext() );
			}
			
			ArrayAdapter<AnimalCode> dataAdapter = new ArrayAdapter<AnimalCode>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerItems);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			cboAnimalCode.setAdapter(dataAdapter);
			cboAnimalCode.setSelection(0);
		}
	}
	private class AnimalAddTask extends AsyncTask<Object, Object, Long>
	{
		@Override
		protected Long doInBackground(Object... params) 
		{
			String name = params[0].toString(); 
			String count = params[1].toString();
			String dtm = params[2].toString();
			String com = params[3].toString();
			String cd = params[4].toString();
			String lat = params[5].toString();
			String lng = params[6].toString();

			return DB.Add(name, Integer.parseInt(count), dtm, com, cd, Float.parseFloat(lat), Float.parseFloat(lng));
		}
		protected void onPostExecute(Long result)
		{
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animal_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
