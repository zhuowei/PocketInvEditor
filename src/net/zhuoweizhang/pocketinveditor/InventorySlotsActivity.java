package net.zhuoweizhang.pocketinveditor;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public final class InventorySlotsActivity extends ListActivity implements OnItemClickListener {

	private List<InventorySlot> inventory;

	private ArrayAdapter<InventorySlot> inventoryListAdapter;

	public void onStart() {
		super.onStart();
		
		//setListAdapter(inventoryListAdapter);
		//getListView().setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//openInventoryEditScreen(inventory.get(position));
	}

	private void openInventoryEditScreen(InventorySlot slot) {
	}
}
