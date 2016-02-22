package com.example.animaldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class AnimalDB
{
	private static final String DB_Name = "AnimalDB";
	SQLiteDatabase DB; 
	private DatabaseOpenHelper dbOpenHelper;
	private static final int DB_Version = 3;
	

		
		public AnimalDB(Context context) 
		{
			dbOpenHelper = new DatabaseOpenHelper(context, DB_Name, null, DB_Version);
		}
		
		public SQLiteDatabase getAnimalDB()
		{
			return DB;
		}
		
		public void Open()
		{
			DB = dbOpenHelper.getWritableDatabase();
		}
		
		public void Close()
		{
			if( DB != null )
				DB.close();
		}
		
		public long Add( String name, int count, String Date, String comment, String animalcd, double lat, double lng )
		{
			long id = 0;
			
			ContentValues values = new ContentValues();
			
			values.put("animal_type_cd", name);
			values.put("count_no", count);
			values.put("seenon_dtm", Date);
			values.put("comments_txt", comment);
			values.put("animal_cd", animalcd);
			values.put("loc_lat", lat);
			values.put("loc_long", lng);
			
			Open();
			id = DB.insert("ANIMAL", null, values);
			Close();
			
			return id; 
		}
		
		public long Update(long id, String name, int count, String Date, String comment, String animalcode)
		{
			long counts = 0;

			ContentValues values = new ContentValues();
			
			values.put("animal_type_cd", name);
			values.put("count_no", count);
			values.put("seenon_dtm", Date);	
			values.put("comments_txt", comment);
			values.put("animal_cd", animalcode);

			
			
			Open();
			counts = DB.update("ANIMAL", values, "_id = " + id, null);
			Close();
			
			return counts; 
		}
		
		public long Delete( long id )
		{
			long deleted = 0; 
			
			Open();
			deleted = DB.delete("ANIMAL", "_id = " + id, null);
			Close();
			
			return deleted; 
		}
		
		public Cursor List()
		{
			Cursor c = null; 
			
			Open();
			
			c = DB.query("ANIMAL", 
					new String[] { "_id", "animal_type_cd", "count_no", "seenon_dtm", "comments_txt", "animal_cd" }, 
					null, 
					null, 
					null, 
					null, 
					"animal_type_cd");
			
			return c; 
		}
		public Cursor AnimalCodeList()
		{
			Cursor c = null;
			Open();
			
			c = DB.query("ANIMAL_CODE", 
					new String[] { "animal_cd", "animal_desc" },
					null,
					null, 
					null, 
					null, 
					null);
			return c;
		}
		public Cursor Inquire(long ANI_ID)
		{
			Cursor c = null; 
			
			Open();
			
			c = DB.query("ANIMAL", 
					new String[] { "_id", "animal_type_cd", "count_no", "seenon_dtm", "comments_txt", "animal_cd" }, 
					"_id = " + ANI_ID, 
					null, 
					null, 
					null, 
					"animal_type_cd");
			
			return c; 
		}
		
		
	}
	class DatabaseOpenHelper extends SQLiteOpenHelper
	{
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			String strSQL = "CREATE TABLE ANIMAL (";
			strSQL += "_id       INTEGER          PRIMARY KEY AUTOINCREMENT,";
			strSQL += "animal_type_cd VARCHAR( 2 )    NOT NULL,";
			strSQL += "count_no  INTEGER  NOT NULL,";
			strSQL += "seenon_dtm  VARCHAR( 40 )            NOT NULL,";
			strSQL += "comments_txt VARCHAR( 40 ) NOT NULL,";
			strSQL += "animal_cd CHAR(3) NULL";
			strSQL += "loc_lat REAL NULL";
			strSQL += "loc_long REAL NULL";
			strSQL += ");";
			
			db.execSQL(strSQL);
			
			strSQL = "CREATE TABLE ANIMAL_CODE (";
			strSQL += "animal_cd CHAR(3) PRIMARY KEY,";
			strSQL += "animal_desc VARCHAR(25) NOT NULL";
			strSQL += ");";
			
			db.execSQL(strSQL);
			
			strSQL = "INSERT INTO ANIMAL_CODE VALUES ('OWL', 'OWLIOUS MAX');";
			db.execSQL(strSQL);
			
			strSQL = "INSERT INTO ANIMAL_CODE VALUES ('FOX', 'FOXICUS MAX');";
			db.execSQL(strSQL);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
