package net.gautampulla.shoppinglist;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ItemsListActivity extends ListActivity {
	List<String> _values;
	
	public ItemsListActivity(List<String> values) {
		_values = values;
	}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, _values);
		setListAdapter(adapter);
	}
}
