package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import net.zhuoweizhang.pocketinveditor.material.MaterialKey;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialIcon;

import net.zhuoweizhang.pocketinveditor.io.xml.MaterialLoader;
import net.zhuoweizhang.pocketinveditor.io.xml.MaterialIconLoader;
import net.zhuoweizhang.pocketinveditor.material.Material;

import net.zhuoweizhang.pocketinveditor.tileentity.*;

public final class ArmorSlotsActivity extends ListActivity implements OnItemLongClickListener, LevelDataLoadListener {

	private List<ItemStack> inventory;

	private ArrayAdapter<ItemStack> inventoryListAdapter;

	public static final int EDIT_SLOT_REQUEST = 534626;

	public static final int DIALOG_SLOT_OPTIONS = 805;

//	public static final int[] SLOT_NAME_RESOURCES = [R.string.armor_helmet, R.string.armor_chestplate, R.string.armor_leggings, R.string.armor_boots];

	/** Currently selected slot, used for determining which slot the long-press menu modifies. */

	private int currentlySelectedSlot = -1;

	/** If there was an Intent for a slot editing request returned from the slot editing activity, 
	 * but the app is not able to immediately handle it, it gets stored here. */

	protected Intent slotActivityResultIntent = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (Material.materials == null) {
			//Load the materials on the main thread - may cause slowdowns
			new MaterialLoader(getResources().getXml(R.xml.item_data)).run();
			new MaterialIconLoader(this).run();

		}

		if (EditorActivity.level != null) {
			onLevelDataLoad();
		} else {
			EditorActivity.loadLevelData(this, this, this.getIntent().getStringExtra("world"));
		}
	}

	public void onLevelDataLoad() {

		inventory = EditorActivity.level.getPlayer().getArmor();

		inventoryListAdapter = new ArrayAdapter<ItemStack>(this, R.layout.slot_list_item, R.id.slot_list_main_text, inventory) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View retval = super.getView(position, convertView, parent);
				ImageView iconView = (ImageView) retval.findViewById(R.id.slot_list_icon);
				ItemStack stack = this.getItem(position);
				MaterialIcon icon = MaterialIcon.icons.get(new MaterialKey(stack.getTypeId(), stack.getDurability()));
				if (icon == null) {
					icon = MaterialIcon.icons.get(new MaterialKey(stack.getTypeId(), (short) 0));
				}
				if (icon != null) {
					BitmapDrawable myDrawable = new BitmapDrawable(icon.bitmap);
					myDrawable.setDither(false);
					myDrawable.setAntiAlias(false);
					myDrawable.setFilterBitmap(false);
					iconView.setImageDrawable(myDrawable);
					iconView.setVisibility(View.VISIBLE);
				} else {
					iconView.setVisibility(View.INVISIBLE);
				}
				return retval;
			}
		};
		setListAdapter(inventoryListAdapter);
		inventoryListAdapter.notifyDataSetChanged();

		if (slotActivityResultIntent != null) {
			onSlotActivityResult(slotActivityResultIntent);
		}
	}

	public void onStart() {
		super.onStart();
		if (EditorActivity.level != null && inventoryListAdapter != null) {
			inventoryListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (!this.getIntent().getBooleanExtra("CanEditArmor", false)) {
			showGetProMessage();
		} else {
			openInventoryEditScreen(position, inventory.get(position));
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (!this.getIntent().getBooleanExtra("CanEditArmor", false)) return false;
		currentlySelectedSlot = position;
		showDialog(DIALOG_SLOT_OPTIONS);
		return true;
	}

	private void openInventoryEditScreen(int position, ItemStack stack) {
		Intent intent = new Intent(this, EditInventorySlotActivity.class);
		intent.putExtra("TypeId", stack.getTypeId());
		intent.putExtra("Damage", stack.getDurability());
		intent.putExtra("Count", stack.getAmount());
		intent.putExtra("Index", position);
		startActivityForResult(intent, EDIT_SLOT_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == EDIT_SLOT_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				if (EditorActivity.level != null && inventory != null) {
					onSlotActivityResult(intent);
				} else {
					slotActivityResultIntent = intent;
				}
			}
		}
	}

	protected void onSlotActivityResult(Intent intent) {
		int slotIndex = intent.getIntExtra("Index", -1);
		if (slotIndex < 0) {
			System.err.println("wrong slot index");
			return;
		}
		ItemStack stack = inventory.get(slotIndex);
		stack.setAmount(intent.getIntExtra("Count", 0));
		stack.setDurability(intent.getShortExtra("Damage", (byte) 0));
		stack.setTypeId(intent.getShortExtra("TypeId", (byte) 0));
		EditorActivity.save(this);
		slotActivityResultIntent = null;
	}

	protected void showGetProMessage() {
		Toast.makeText(this, R.string.armor_get_pro_to_edit, Toast.LENGTH_SHORT).show();
	}
}
