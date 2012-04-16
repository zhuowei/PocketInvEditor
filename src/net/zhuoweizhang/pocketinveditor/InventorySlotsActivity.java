package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
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

	public static final int EDIT_SLOT_REQUEST = 534626;

	public void onStart() {
		super.onStart();
		inventory = EditorActivity.level.getPlayer().getInventory();
		inventoryListAdapter = new ArrayAdapter<InventorySlot>(this, R.layout.slot_list_item, inventory);
		setListAdapter(inventoryListAdapter);
		getListView().setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		openInventoryEditScreen(position, inventory.get(position));
	}

	private void openInventoryEditScreen(int position, InventorySlot slot) {
		Intent intent = new Intent(this, EditInventorySlotActivity.class);
		ItemStack stack = slot.getContents();
		intent.putExtra("TypeId", stack.getTypeId());
		intent.putExtra("Damage", stack.getDurability());
		intent.putExtra("Count", stack.getAmount());
		intent.putExtra("Slot", slot.getSlot());
		intent.putExtra("Index", position);
		startActivityForResult(intent, EDIT_SLOT_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == EDIT_SLOT_REQUEST) {
			/*if (resultCode == Activity.RESULT_OK) {
				int slotIndex = intent.getIntExtra("Index", -1);
				if (slotIndex < 0) {
					System.err.println("wrong slot index");
					return;
				}
				InventorySlot slot = EditorActivity.level.getPlayer().getInventory().get(slotIndex);
				ItemStack stack = slot.getContents();
				stack.setAmount(intent.getByteExtra("Count", (byte) 0));
				stack.setDurability(intent.getShortExtra("Damage", (byte) 0));
				stack.setTypeId(intent.getShortExtra("TypeId", (byte) 0));
				EditorActivity.save(this);
			}*/
		}
	}
}
