package net.zhuoweizhang.pocketinveditor;

import java.io.File;
import java.io.FileInputStream;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import static android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.io.xml.MaterialLoader;
import net.zhuoweizhang.pocketinveditor.io.xml.MaterialIconLoader;
import net.zhuoweizhang.pocketinveditor.material.Material;
import static net.zhuoweizhang.pocketinveditor.LoadoutExportActivity.LOADOUT_EXTENSION;

import org.spout.nbt.*;
import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;

public class LoadoutImportActivity extends ListActivity
{

	public static final int INVENTORY_SIZE = 36;

	private static final int DIALOG_IMPORT_OPTIONS = 1;

	private static final int REPLACE_MODE = 0;
	private static final int COMBINE_MODE = 1;

	private FindLoadoutsThread findLoadoutsThread;

	private ListView loadoutsList;

	private List<LoadoutListItem> loadouts;

	private LoadoutListItem selectedLoadoutItem;
	private int selectedImportMode = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openLoadoutImportWindow(loadouts.get(position));
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		findLoadouts();
	}

	private void findLoadouts() {
		findLoadoutsThread = new FindLoadoutsThread(this);
		new Thread(findLoadoutsThread).start();
	}

	private void receiveLoadouts(List<LoadoutListItem> loadouts) {
		this.loadouts = loadouts;
		ArrayAdapter<LoadoutListItem> adapter = new ArrayAdapter<LoadoutListItem>(this, R.layout.world_list_item, loadouts);
		adapter.sort(new LoadoutListComparator());
		setListAdapter(adapter);
	}

	private void openLoadoutImportWindow(LoadoutListItem item) {
		this.selectedLoadoutItem = item;
		showDialog(DIALOG_IMPORT_OPTIONS);
	}

	private void importLoadout(LoadoutListItem loadoutItem, int mode) {
		selectedImportMode = mode;
		new Thread(new LoadoutLoader(loadoutItem)).start();
	}

	protected void loadoutLoadedCallback(List<InventorySlot> slots) {
		int mode = selectedImportMode;
		if (mode == REPLACE_MODE) {
			EditorActivity.level.getPlayer().setInventory(slots);
		} else if (mode == COMBINE_MODE) {
			List<InventorySlot> currentSlots = EditorActivity.level.getPlayer().getInventory();
			compactSlots(currentSlots);
			for (int i = 0; i < slots.size(); i++) {
				InventorySlot s = slots.get(i);
				ItemStack stack = s.getContents();
				if (s.getSlot() < 9) continue;
				boolean success = addStack(currentSlots, stack);
				if (!success) {
					Toast.makeText(this, R.string.loadout_import_too_many_items, Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
		EditorActivity.save(this);
		finish();
	}

	public static void compactSlots(List<InventorySlot> slots) {
		int a = 9;
		for (int i = 0; i < slots.size(); i++) {
			InventorySlot s = slots.get(i);
			if (s.getSlot() >= 9) {
				s.setSlot((byte) a);
				a++;
			}
		}
	}

	public static boolean addStack(List<InventorySlot> slots, ItemStack stack) {
		if (slots.size() >= (INVENTORY_SIZE + 9)) return false;
		byte newId = (byte) 0;
		for (int i = 0; i < slots.size(); i++) {
			byte a = slots.get(i).getSlot();
			if (a > newId) newId = a;
		}
		InventorySlot newSlot = new InventorySlot((byte) (newId + 1), stack);
		System.out.println(newSlot);
		slots.add(newSlot);
		return true;
	}

	public Dialog onCreateDialog(int dialogId) {
		switch (dialogId) {
			case DIALOG_IMPORT_OPTIONS:
				return createImportOptionsDialog();
			default:
				return super.onCreateDialog(dialogId);
		}
	}

	public void onPrepareDialog(int dialogId, Dialog dialog) {
		switch (dialogId) {
			case DIALOG_IMPORT_OPTIONS:
				AlertDialog aDialog = (AlertDialog) dialog;
				aDialog.setTitle(selectedLoadoutItem.toString());
				break;
			default:
				super.onPrepareDialog(dialogId, dialog);
		}
	}

	protected AlertDialog createImportOptionsDialog() {
		CharSequence[] options = {this.getResources().getText(R.string.loadout_import_replace), this.getResources().getText(R.string.loadout_import_combine)};
		return new AlertDialog.Builder(this).setTitle("Slot name goes here").
			setItems(options, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogI, int button) {
					switch (button) {
						case 0: //slot duplicate
							importLoadout(selectedLoadoutItem, REPLACE_MODE);
							break;
						case 1: //slot delete
							importLoadout(selectedLoadoutItem, COMBINE_MODE);
							break;
					}
				}
			}).create();
	}
		


	private final class FindLoadoutsThread implements Runnable {

		private final LoadoutImportActivity activity;

		public FindLoadoutsThread(LoadoutImportActivity activity) {
			this.activity = activity;
		}

		public void run() {
			File loadoutsFolder = LoadoutExportActivity.getLoadoutFolder(activity);

			final List<LoadoutListItem> loadouts = new ArrayList<LoadoutListItem>();

			if (!loadoutsFolder.exists()) {
				System.err.println("no storage folder");
			} else {
				for (File loadoutFile : loadoutsFolder.listFiles()) {
					if (loadoutFile.getName().indexOf(LOADOUT_EXTENSION) < 0) continue;
					//check if it has level.dat
					loadouts.add(new LoadoutListItem(loadoutFile));
				}
			}
			this.activity.runOnUiThread(new Runnable() {
				public void run() {
					activity.receiveLoadouts(loadouts);
				}
			});
		}

	}

	private final class LoadoutListItem {
		public final File file;
		public final String displayName;
		public LoadoutListItem(File file) {
			this.file = file;
			this.displayName = file.getName().substring(0, file.getName().indexOf(LOADOUT_EXTENSION));
		}

		public String toString() {
			return displayName;
		}
	}

	private final class LoadoutListComparator implements Comparator<LoadoutListItem> {
		public int compare(LoadoutListItem a, LoadoutListItem b) {
			//System.out.println(a.toString() + ":" + b.toString() + (a.levelDat.lastModified() - b.levelDat.lastModified()));
			return a.displayName.toLowerCase().compareTo(b.displayName.toLowerCase());
		}
		public boolean equals(LoadoutListItem a, LoadoutListItem b) {
			return a.displayName.toLowerCase().equals(b.displayName.toLowerCase());
		}
	}

	private final class LoadoutLoader implements Runnable {

		public LoadoutListItem item;
		public List<InventorySlot> slots = null;

		public LoadoutLoader(LoadoutListItem item) {
			this.item = item;
		}

		public void run() {
			FileInputStream fis = null;
			NBTInputStream nis = null;
			try {
				fis = new FileInputStream(item.file);
				nis = new NBTInputStream(fis, false, true);
				CompoundTag tag = (CompoundTag) nis.readTag();
				slots = NBTConverter.readLoadout(tag);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (nis != null) nis.close();
					if (fis != null) fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			LoadoutImportActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					loadoutLoadedCallback(slots);
				}
			});
		}
	}

}
