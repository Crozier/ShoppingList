package net.gautampulla.shoppinglist.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDb extends SQLiteOpenHelper
{
	public MyDb(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public boolean DoesTableExist(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(
			"sqlite_master", // table
			null, // columns
			"type=? AND name=?", // selection
			new String[] { "table", tableName }, // selectionargs
			null,  // groupby
			null, // having
			null); // orderby
		
		int numTables = cursor.getCount();
		assert numTables <= 1 : numTables;
		return numTables > 0;
	}
}
