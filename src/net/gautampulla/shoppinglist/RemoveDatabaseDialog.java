package net.gautampulla.shoppinglist;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

@TargetApi(11)
public class RemoveDatabaseDialog extends DialogFragment {
	MainActivity _parentWindow;
	View _removeDatabaseDialogBox;
	public RemoveDatabaseDialog(MainActivity activity) {
		_parentWindow = activity;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		_removeDatabaseDialogBox = inflater.inflate(R.layout.remove_database_dlg, null);
		
		builder.setView(_removeDatabaseDialogBox);
		builder.setPositiveButton(R.string.ok_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MainActivity.Instance.dropDb();
							}
						});
		
		builder.setNegativeButton(
				"Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//dialog.dismiss();
					}
				});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
