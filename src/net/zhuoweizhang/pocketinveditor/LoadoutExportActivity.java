package net.zhuoweizhang.pocketinveditor;

import java.io.File;
import java.io.FileOutputStream;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.spout.nbt.*;
import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.tileentity.ContainerTileEntity;

import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;

public class LoadoutExportActivity extends Activity implements View.OnClickListener {

	public static final String LOADOUT_EXTENSION = ".peinv";

	private TextView name;

	private Button exportButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadout_export);
		name = (TextView) findViewById(R.id.loadout_name_input);
		exportButton = (Button) findViewById(R.id.loadout_export_button);
		exportButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == exportButton) {
			startExport();
		}
	}

	public void startExport() {
		File file = new File(getLoadoutFolder(this), name.getText().toString() + LOADOUT_EXTENSION);
		if (file.exists()) {
			name.setError("Loadout with same name exists");
			return;
		}
		CompoundTag inventoryTag = NBTConverter.writeLoadout(getInventoryToExport());
		new Thread(new ExportLoadoutTask(file, inventoryTag)).start();
	}

	public List<InventorySlot> getInventoryToExport() {
		if (this.getIntent().getBooleanExtra("IsTileEntity", false)) {
			int tileEntityIndex = this.getIntent().getIntExtra("Index", -1);
			ContainerTileEntity container = (ContainerTileEntity) EditorActivity.level.getTileEntities().get(tileEntityIndex);
			return container.getItems();
		}
		return EditorActivity.level.getPlayer().getInventory();
	}

	private class ExportLoadoutTask implements Runnable {
		public File file;
		public CompoundTag tag;
		public ExportLoadoutTask(File file, CompoundTag tag) {
			this.file = file;
			this.tag = tag;
		}
		public void run() {
			boolean success = true;
			FileOutputStream fos = null;
			NBTOutputStream nos = null;
			try {
				fos = new FileOutputStream(file);
				nos = new NBTOutputStream(fos, false, true);
				nos.writeTag(tag);
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			} finally {
				try {
					if (nos != null) nos.close();
					if (fos != null) fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			}
			final boolean showSuccessMsg = success;
			LoadoutExportActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(LoadoutExportActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
					LoadoutExportActivity.this.finish();
				}
			});
		}
	}

	public static File getLoadoutFolder(Activity activity) {
		File outDir = new File(Environment.getExternalStorageDirectory(), "Android/data/net.zhuoweizhang.pocketinveditor/loadouts");
		outDir.mkdirs();
		return outDir;
	}
		
}
