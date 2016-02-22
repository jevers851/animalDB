package com.example.animaldb;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class AnimalList extends ListActivity
{

	private static final String ROW_ID = "row_id";
	private ListView AnimalListView;
	private CursorAdapter AnimalListViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AnimalDB animalDB = new AnimalDB(this);
		
		// Uncomment to add a test record 
		//AnimalDB.Add("Coyote", 3.75f, "01/01/1900");
		
		
		AnimalListView = getListView();
		AnimalListView.setOnItemClickListener(AnimalListListener);
		
		String[] from = new String[] { "animal_type_cd" };
		int[] to = new int[] { R.id.AnimalTextField };

		AnimalListViewAdapter = new SimpleCursorAdapter(AnimalList.this,
											R.layout.activity_animal_list,
											null,
											from,
											to);


		setListAdapter(AnimalListViewAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animal_list, menu);
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
		
		if (id == R.id.action_add)
		{
			Intent animalAdd = 
					new Intent(AnimalList.this, AnimalAdd.class);
			startActivity(animalAdd);
			
		}
		
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onStop()
	{
		Cursor cursor = AnimalListViewAdapter.getCursor();

		if( cursor != null )
			cursor.deactivate();

		AnimalListViewAdapter.changeCursor(null);
		super.onStop();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		new GetContactsTask().execute((Object[]) null);
	}	
	
	private class GetContactsTask extends AsyncTask<Object, Object, Cursor>
	{
		AnimalDB db = new AnimalDB(AnimalList.this);

		@Override
		protected Cursor doInBackground(Object... parms)
		{
			return db.List();
		}

		protected void onPostExecute(Cursor result)
		{
			AnimalListViewAdapter.changeCursor(result);
			db.Close();
		}

	}	
	
	OnItemClickListener AnimalListListener = new OnItemClickListener()
	{
		
		@Override
		public void onItemClick(AdapterView<?> arg0, 
										  View arg1,
										  int arg2,
										  long ANI_ID)
		{
			Intent AnimalView = new Intent(AnimalList.this, AnimalView.class);
			AnimalView.putExtra(ROW_ID, ANI_ID);
			startActivity(AnimalView);
		}
		
	};
	
	
}
