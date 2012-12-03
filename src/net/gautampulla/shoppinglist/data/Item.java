package net.gautampulla.shoppinglist.data;

import net.gautampulla.shoppinglist.MainActivity;

public class Item {
	private String _name;
	private String _notes;
	private Boolean _ischecked;
	
	public Item(String name, String notes, Boolean ischecked) {
		_name = name;
		_notes = notes;
		_ischecked = ischecked;
	}

	public String get_name() {
		return _name;
	}

	public String get_notes() {
		return _notes;
	}
	
	public Boolean get_ischecked() {
		return _ischecked;
	}
	
	public void set_ischecked(Boolean ischecked) {
		_ischecked = ischecked;
	}
	
	// Saves the item, the primary key (Name) must not have changed
	// This is applicable to the list view, from where it is possible to
	// update the 'checked' state of the item, but not the name, so we know
	// the the primary key did not change.
	// TODO: should really refactor this - not a very clean design
	// Maybe the item should keep track of changes and do either a
	// - new if it was a previously non-existent item
	// - update using the old name in the where clause if the name field changes
	// - update using the name in the where clause if name has not changed
	// - delete if the item is being dropped
	// - no-op if the item has not changed
	public void save_primaryKeyUnchanged() throws Exception {
		// TODO: Keep track of which items are dirty and update only those
		MainActivity.Instance.ItemsTable.updateItem(this.get_name(), this);
	}
}
