package net.zhuoweizhang.pocketinveditor;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import android.app.ListActivity;
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

public final class PocketInvEditorActivity extends ListActivity
{
	private FindWorldsThread findWorldsThread;

	private ListView worldsList;

	private List<WorldListItem> worlds;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				File worldFile = worlds.get(position).folder;
				openWorld(worldFile);
			}
		});

		loadWorlds();

		if (Material.materials == null) {
			loadMaterials();
		}

	}

	private void loadWorlds() {
		findWorldsThread = new FindWorldsThread(this);
		new Thread(findWorldsThread).start();
	}

	private void receiveWorldFolders(List<WorldListItem> worlds) {
		this.worlds = worlds;
		setListAdapter(new ArrayAdapter<WorldListItem>(this, R.layout.world_list_item, worlds));
	}

	private void openWorld(File worldFile) {
		Intent intent = new Intent(this, EditorActivity.class);
		intent.putExtra("world", worldFile.getAbsolutePath());
		startActivity(intent);
	}

	private void loadMaterials() {
		new Thread(new MaterialLoader(getResources().getXml(R.xml.item_data))).start();
		new Thread(new MaterialIconLoader(this)).start();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.worldselect_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.worldselect_about:
				displayAboutActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void displayAboutActivity() {
		Intent intent = new Intent(this, AboutAppActivity.class);
		startActivity(intent);
	}

	private final class FindWorldsThread implements Runnable {

		private final PocketInvEditorActivity activity;

		public FindWorldsThread(PocketInvEditorActivity activity) {
			this.activity = activity;
		}

		public void run() {
			File worldsFolder = new File(Environment.getExternalStorageDirectory(), "games/com.mojang/minecraftWorlds");
			System.err.println(worldsFolder);
			if (!worldsFolder.exists()) {
				System.err.println("no storage folder");
				return;
			}
			final List<WorldListItem> worldFolders = new ArrayList<WorldListItem>();
			for (File worldFolder : worldsFolder.listFiles()) {
				if (!worldFolder.isDirectory()) continue;
				//check if it has level.dat
				if (!new File(worldFolder, "level.dat").exists())
					continue;
				worldFolders.add(new WorldListItem(worldFolder));
			}
			this.activity.runOnUiThread(new Runnable() {
				public void run() {
					activity.receiveWorldFolders(worldFolders);
				}
			});
		}

	}

	private final class WorldListItem {
		public final File folder;
		public WorldListItem(File file) {
			this.folder = file;
		}

		public String toString() {
			return folder.getName();
		}
	}

}
