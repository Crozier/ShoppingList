package net.gautampulla.shoppinglist;

import java.util.ArrayList;

import net.gautampulla.shoppinglist.data.Item;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter implements ListAdapter, View.OnClickListener {
	public static final int MODE_HIDE_CHECKED = 0;
	public static final int MODE_GRAY_CHECKED = 1;
	
	private int _mode;
	private ArrayList<Item> _allItems;
	private ArrayList<Item> _uncheckedItems;
	private ArrayList<Item> _currentItemList;
	private LayoutInflater _layoutInflater;

	public ItemListAdapter(Activity a) {
		super();
		_mode = MODE_GRAY_CHECKED;
		_allItems = new ArrayList<Item>();
		_uncheckedItems = new ArrayList<Item>();
		_currentItemList = _allItems;
		_layoutInflater = a.getLayoutInflater();
	}

	// Reloads the adapter from the database table
	// Updates the view
	public void reload() {
		Item[] items = null;
		try {
			items = MainActivity.Instance.ItemsTable.getItems();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		_allItems.clear();
		for (Item item : items) {
			_allItems.add(item);
		}
		buildUncheckedList();
		notifyDataSetChanged();
	}
	
	public Item get(int position) {
		return _currentItemList.get(position);
	}
	
	public int getCount() {
		return _currentItemList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = _layoutInflater.inflate(R.layout.list_item, null);
		}
		
		Item item = _currentItemList.get(position);
		TextView nameTextView = (TextView)(view.findViewById(R.id.item_name_textView));
		nameTextView.setText(item.get_name());
		
		TextView notesTextView = (TextView)(view.findViewById(R.id.item_notes_textView));
		notesTextView.setText(item.get_notes());
		
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.item_select_checkBox);
		checkbox.setChecked(item.get_ischecked());
		checkbox.setTag((Integer)position);
		checkbox.setOnClickListener(this);
		colorize((LinearLayout)view, item.get_ischecked());
		return view;
	}
	
	public void onClick(View arg) {
		CheckBox checkBox = (CheckBox)arg;
		int position = (Integer)checkBox.getTag();
		Boolean ischecked = checkBox.isChecked();
		Item item = _currentItemList.get(position);
		item.set_ischecked(ischecked);
		try {
			item.save_primaryKeyUnchanged();
		} catch (Exception e) {
			// On error: restore the state to the original
			// TODO: Popup an error dialog box
			checkBox.setChecked(!ischecked);
			ischecked = !ischecked;
			e.printStackTrace();
		}
		
		if (_mode == MODE_HIDE_CHECKED) {
			// Assert: We can only check items in 'hide checked' mode
			// ONLY NEED TO HANDLE THE CHECKED CASE, BECAUSE...
			// Not possible to uncheck because an item that is already checked
			// is hidden and un-clickable in this mode
			assert ischecked;
			_uncheckedItems.remove(position);
			notifyDataSetChanged();
			return;
		}
		
		// In gray-checked mode, we need to re-sync the unchecked list
		// because we're not sure in what position the item that was un/checked
		// is in the _unchecked list
		assert _mode == MODE_GRAY_CHECKED;
		buildUncheckedList();
		colorize((LinearLayout)(arg.getParent().getParent()), ischecked);
	}
	
	public void toggle_mode() {
		if (_mode == MODE_GRAY_CHECKED) {
			_mode = MODE_HIDE_CHECKED;
			assert _currentItemList == _allItems;
			_currentItemList = _uncheckedItems;
		} else {
			_mode = MODE_GRAY_CHECKED;
			assert _currentItemList == _uncheckedItems;
			_currentItemList = _allItems;
		}
		
		reload();
	}
	
	// Sets the color appropriately - checked items are grayed out
	// If an item is not visible at all - then it doesn't matter, this method
	// will never get called for it
	private void colorize(LinearLayout itemView, Boolean ischecked) {
		TextView textView = (TextView)itemView.findViewById(R.id.item_name_textView);
		textView.setTextColor(ischecked ? Color.GRAY : Color.BLACK);
		textView = (TextView)itemView.findViewById(R.id.item_notes_textView);
		textView.setTextColor(ischecked ? Color.GRAY : Color.BLACK);
		itemView.setBackgroundColor(ischecked ? Color.LTGRAY : Color.WHITE);
	}
	
	private void buildUncheckedList() {
		_uncheckedItems.clear();
		for (Item item : _allItems) {
			if (!item.get_ischecked()) {
				_uncheckedItems.add(item);
			}
		}
	}
}
