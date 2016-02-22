package com.example.animaldb;


import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AnimalView extends Activity
{
	private static final String ROW_ID = "row_id";
	
	private AnimalDB DB;
	
	private long ANI_ID;
	
	private EditText txtANI_ID_VIEW;
	private EditText txtANI_TYPE_CD_VIEW;
	private EditText txtANI_COUNT_VIEW;
	private EditText txtANI_DTM_VIEW;
	private EditText txtANI_COM_VIEW;
	private Spinner cboANI_CODE;
	
	private ArrayList<AnimalCode> spinnerItems;
	private ArrayAdapter<AnimalCode> dataAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animal_view);
		
		txtANI_ID_VIEW = (EditText)findViewById(R.id.txtANI_ID_VIEW);
		txtANI_TYPE_CD_VIEW = (EditText)findViewById(R.id.txtANI_TYPE_CD_VIEW);
		txtANI_COUNT_VIEW = (EditText)findViewById(R.id.txtANI_COUNT_VIEW);
		txtANI_DTM_VIEW = (EditText)findViewById(R.id.txtANI_DTM_VIEW);
		txtANI_COM_VIEW = (EditText)findViewById(R.id.txtANI_COM_VIEW);
		
		
		cboANI_CODE = (Spinner)findViewById(R.id.cboANI_CODE);
		
		cboANI_CODE.setOnItemSelectedListener(cboANI_CODE_Selected);
		
		
		ANI_ID = getIntent().getExtras().getLong(ROW_ID);
		
		spinnerItems = new ArrayList<AnimalCode>();
		DB = new AnimalDB(this);
		
	}
	
	OnItemSelectedListener cboANI_CODE_Selected = new OnItemSelectedListener()
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

	@Override
	protected void onResume()
	{
		super.onResume();
		new getAnimalCodes().execute((Object[])null);
		new AnimalInquireTask().execute((Object[])null);

	}
	
	
	private class AnimalInquireTask extends AsyncTask<Object, Object, Cursor>
	{

		@Override
		protected Cursor doInBackground(Object... params) 
		{
			return DB.Inquire(ANI_ID);
		}

		protected void onPostExecute(Cursor result)
		{
			// Move data from the cursor to the screen
			result.moveToFirst();
			float count = result.getFloat(result.getColumnIndex("count_no"));
			String name = result.getString(result.getColumnIndex("animal_type_cd"));
			String dtm = result.getString(result.getColumnIndex("seenon_dtm"));
			String comment = result.getString(result.getColumnIndex("comments_txt"));
			String cd = result.getString(result.getColumnIndex("animal_cd"));
		    
			int i = spinnerItems.indexOf(cd);
		    
			
			// Move data to the screen
			txtANI_ID_VIEW.setText(String.valueOf(ANI_ID));
			txtANI_TYPE_CD_VIEW.setText(name);
			txtANI_COUNT_VIEW.setText(String.valueOf(count));
			txtANI_DTM_VIEW.setText(dtm);
			txtANI_COM_VIEW.setText(comment);
			if (!(i == -1))
			cboANI_CODE.setSelection(i);
		}
		
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
			
			cboANI_CODE.setAdapter(dataAdapter);
			cboANI_CODE.setSelection(0);
			
		}
	}
	
	private class AnimalUpdateTask extends AsyncTask<Object, Object, Object>
	{
		@Override
		protected Object doInBackground(Object... params) 
		{
			String name = params[0].toString();
			String count = params[0].toString();
			String dtm = params[2].toString();
			String com = params[3].toString();
			String cd = params[4].toString();
			
			return DB.Update(Long.parseLong(params[0].toString()), name, Integer.parseInt(count), dtm, com, cd);
		}
//		protected Long doInBackground(Object... params) 
//		{
//			String name = params[0].toString(); 
//			String count = params[1].toString();
//			String dtm = params[2].toString();
//			String com = params[3].toString();
//			String cd = params[4].toString();
//
//			return DB.Add(name, Integer.parseInt(count), dtm, com, cd);
//		}
		protected void onPostExecute(Object result)
		{
			finish();
		}
	}
	
	private class AnimalDeleteTask extends AsyncTask<Object, Object, Object>
	{
		@Override
		protected Object doInBackground(Object... params) 
		{
			return DB.Delete(ANI_ID);
		}

		protected void onPostExecute(Object result)
		{
			finish();
		}
	}
	
	private void updateAnimal()
	{
		String id = txtANI_ID_VIEW.getText().toString();
		String name = txtANI_TYPE_CD_VIEW.getText().toString();
		String count = txtANI_COUNT_VIEW.getText().toString();
		String dtm = txtANI_DTM_VIEW.getText().toString();
		String com = txtANI_COM_VIEW.getText().toString();
		String cd = cboANI_CODE.getSelectedItem().toString();
		
		new AnimalUpdateTask().execute(id, name, count, dtm, com, cd);
	}
	
	private void deleteAnimal()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(AnimalView.this);
		
		dialog.setTitle(R.string.delete_dialog_title);
		dialog.setMessage(R.string.delete_dialog_message);
		
		dialog.setPositiveButton(R.string.dialog_btnYes, 
				new DialogInterface.OnClickListener() 
			{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			
				new AnimalDeleteTask().execute((Object[])null);
				
			}
		});
		
		dialog.setNegativeButton(R.string.dialog_btnNo, null);
		dialog.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animal_view, menu);
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
		if (id == R.id.action_update)
		{
			updateAnimal();
		}

		if (id == R.id.action_delete)
		{
			deleteAnimal();
		}
		return super.onOptionsItemSelected(item);
	}
}
