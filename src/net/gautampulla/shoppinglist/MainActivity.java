package net.gautampulla.shoppinglist;
import net.gautampulla.shoppinglist.data.ItemsTable;
import net.gautampulla.shoppinglist.data.MyDb;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity  {
	public final String ItemsTableExtra = "ItemsTable";
	public static MainActivity Instance;
	
	public static final int ItemDetailActivityCode = 0;
	
	private final int Version = 1;
	private MyDb _db;
	public ItemsTable ItemsTable;
	//private ItemsListActivity _itemsListActivity;
	private ListView _itemsListView;
	private ItemListAdapter _itemsAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Instance = this;
		super.onCreate(savedInstanceState);
		_db = new MyDb(getApplicationContext(), "MyDb", null /* CursorFactory */, Version);
		ItemsTable = new ItemsTable(_db);
		
		setContentView(R.layout.activity_main);
		_itemsListView = (ListView) findViewById(R.id.listView1);
		_itemsAdapter = new ItemListAdapter(this);
		_itemsListView.setAdapter(_itemsAdapter);
		_itemsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				openItemDetail(_itemsAdapter.get(position).get_name());
			}
		});
		
		this.reload();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onDropDb(View view) {
		RemoveDatabaseDialog removeDatabaseDialog = new RemoveDatabaseDialog(this);
		removeDatabaseDialog.show(getFragmentManager(), "RemoveDatabaseDialogTag");
	}
	
	public void dropDb() {
		this.ItemsTable.recreate();
		this.reload();
	}
	
	public void newItem(View view) {
		Intent intent = new Intent(this, ItemDetailActivity.class);
		intent.putExtra(ItemDetailActivity.ActivityModeProperty, ItemDetailActivity.ActivityMode_NewItem);
		startActivityForResult(intent, ItemDetailActivityCode);
		//NewItemDialog newItemDialog = new NewItemDialog(this);
		//newItemDialog.show(getFragmentManager(), "NewItemDialogTag");
	}
	
	public void onHideChecked(View view) {
		_itemsAdapter.toggle_mode();
	}
	
	public void openItemDetail(String name) {
		// TODO: Currently I'm passing the item name and the detail view
		// is loading the item from the database. Future optimization is to
		// pass the item object (which the caller has) and just display that
		// instead of reading from database
		Intent intent = new Intent(this, ItemDetailActivity.class);
		intent.putExtra(ItemDetailActivity.ActivityModeProperty, ItemDetailActivity.ActivityMode_ExistingItem);
		intent.putExtra(ItemDetailActivity.NameProperty, name);
		startActivityForResult(intent, ItemDetailActivityCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ItemDetailActivityCode:
			// Reload the list view whenever an item-detail activity finishes
			// it could have changed the item.
			// TODO: future optimization is to not reload if the item was not
			// modified
			reload();
			break;
		default:
			break;
		}
	}

	private void reload() {
		_itemsAdapter.reload();
	}
}
