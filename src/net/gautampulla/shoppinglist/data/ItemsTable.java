package net.gautampulla.shoppinglist.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Represents the table of items in the database and provides
// methods to get, add, remove items from the database table.
public class ItemsTable {
	private final String TABLE_NAME = "Items";
	private final String COLUMN_Name = "Name";
	private final String COLUMN_Notes = "Notes";
	private final String COLUMN_Checked = "Checked";
	
	private final String[] COLUMNS_ALL = new String[] {
		COLUMN_Name,
		COLUMN_Notes,
		COLUMN_Checked
	};
	private MyDb _db;

	public ItemsTable(MyDb db) {
		_db = db;
		this.create();
	}

	// Creates the database table for Items if it does not already exist
	public void create() {
		if (_db.DoesTableExist(TABLE_NAME)) {
			return;
		}

		SQLiteDatabase dbW = _db.getWritableDatabase();
		try {
			dbW.execSQL(
				"CREATE TABLE " + TABLE_NAME + " (" +
					COLUMN_Name + " VARCHAR(64) PRIMARY KEY, " +
					COLUMN_Notes + " VARCHAR(64), " +
					COLUMN_Checked + " INTEGER" + 
				")");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbW.close();
		}
	}

	// Drops the Items table and recreates it
	public void recreate() {
		SQLiteDatabase dbW = _db.getWritableDatabase();
		try {
			dbW.execSQL("DROP TABLE " + TABLE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.create();
	}
	
	// Adds a new item to the datbase table 
	public void addItem(String name, String notes) throws Exception {

		SQLiteDatabase dbR = _db.getReadableDatabase();
		Cursor cursor = null;

		try {
			cursor = dbR.query(TABLE_NAME, // table
					COLUMNS_ALL, // columns
					COLUMN_Name + "=?", // selection
					new String[] { name }, // selectionargs
					null, // groupby
					null, // having
					null); // orderby

			if (cursor.moveToFirst()) {
				throw new Exception("Item already exists");
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			
			dbR.close();
		}

		SQLiteDatabase dbW = _db.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_Name, name);
			values.put(COLUMN_Notes, notes);
			dbW.insert(TABLE_NAME, null, values);
		} finally {
			dbW.close();
		}
	}

	// Removes an item from the database table
	public void deleteItem(String name) throws Exception {
		SQLiteDatabase dbW = _db.getWritableDatabase();
		try {
			String where = COLUMN_Name + "=?";
			dbW.delete(TABLE_NAME, where, new String[] { name });
		} finally {
			dbW.close();
		}
	}
	
	
	// Updates an item that is assumed to be already existing
	// It the item is renamed, then there should not be an already
	// existing item of that new name, otherwise a primary-key error
	// will occur and an exception thrown
	// TODO: All fields of the item are updated, rather than just
	// changed ones. Future optimization is 'dirty-property-tracking'
	public void updateItem(String originalName, Item item) throws Exception {
		SQLiteDatabase dbW = _db.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_Name, item.get_name());
			cv.put(COLUMN_Notes, item.get_notes());
			cv.put(COLUMN_Checked, item.get_ischecked());
			String whereClause = COLUMN_Name + "=?";
			dbW.update(TABLE_NAME, cv, whereClause, new String[] { originalName });
		}
		finally {
			dbW.close();
		}
	}
	
	public Item getItem(String name) throws Exception {
		Item[] items = getItems(name);
		if (items.length == 0) {
			throw new Exception("No such item");
		}
		
		return items[0];
	}
	
	public Item[] getItems() throws Exception {
		return getItems(null);
	}
	
	private Item[] getItems(String selectName) throws Exception {
		SQLiteDatabase dbR = _db.getReadableDatabase();
		Cursor cursor = null;

		try {
			String selection = null; 
			String[] selectionArgs = null;
			if (selectName != null) {
				selection = COLUMN_Name + "=?";
				selectionArgs = new String[] { selectName };
			}
			
			cursor = dbR.query(
					TABLE_NAME,
					COLUMNS_ALL, // columns
					selection, // selection
					selectionArgs, // selectionargs
					null, // groupby
					null, // having
					null); // orderby

			assert COLUMNS_ALL.length == 3;
			
			int count = cursor.getCount();
			Item[] items = new Item[count];

			for (int i = 0; i < items.length; i++) {
				if (!cursor.moveToNext()) {
					throw new Exception("unexpected");
				}

				String name = cursor.getString(0);
				String notes = cursor.getString(1);
				int checkedFlag = cursor.getInt(2);
				items[i] = new Item(name, notes, checkedFlag != 0);
			}

			return items;

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			
			dbR.close();
		}
	}

	public void removeItem(String itemName) throws Exception {

		SQLiteDatabase dbW = _db.getWritableDatabase();

		try {
			int rowsAffected = dbW.delete(TABLE_NAME, COLUMN_Name + "=?",
					new String[] { itemName });
			assert rowsAffected == 1 : rowsAffected;

		} finally {
			dbW.close();
		}
	}
}
