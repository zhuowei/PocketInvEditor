package net.zhuoweizhang.pocketinveditor;

import java.io.File;

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

public class PocketInvEditorActivity extends ListActivity
{

	private static final int DIALOG_NO_WORLDS_FOUND = 200;
	private static final int DIALOG_NEEDS_BETA = 202;

	private FindWorldsThread findWorldsThread;

	private ListView worldsList;

	private List<WorldListItem> worlds;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		loadContentView();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				File worldFile = worlds.get(position).folder;
				if (new File(worldFile, "db").exists()) {
					showDialog(DIALOG_NEEDS_BETA);
					return;
				}
				openWorld(worldFile);
			}
		});

		if (Material.materials == null) {
			loadMaterials();
		}

	}

	protected void loadContentView() {
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadWorlds();
	}

	private void loadWorlds() {
		findWorldsThread = new FindWorldsThread(this);
		new Thread(findWorldsThread).start();
	}

	private void receiveWorldFolders(List<WorldListItem> worlds) {
		this.worlds = worlds;
		ArrayAdapter<WorldListItem> adapter = new ArrayAdapter<WorldListItem>(this, R.layout.world_list_item, worlds);
		adapter.sort(new WorldListDateComparator());
		setListAdapter(adapter);
		if (worlds.size() == 0) {
			displayNoWorldsWarning();
		}
	}

	protected void openWorld(File worldFile) {
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
		if (item.getItemId() == R.id.worldselect_about) {
			displayAboutActivity();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public void displayAboutActivity() {
		Intent intent = new Intent(this, AboutAppActivity.class);
		startActivity(intent);
	}

	private void displayNoWorldsWarning() {
		this.showDialog(DIALOG_NO_WORLDS_FOUND);
	}

	public Dialog onCreateDialog(int dialogId) {
		switch (dialogId) {
			case DIALOG_NO_WORLDS_FOUND:
				return createNoWorldsFoundDialog();
			case DIALOG_NEEDS_BETA:
				return createNeedsBetaDialog();
			default:
				return super.onCreateDialog(dialogId);
		}
	}

	protected AlertDialog createNoWorldsFoundDialog() {
		return new AlertDialog.Builder(this).setTitle(R.string.noworldsfound_title).
			setMessage(R.string.noworldsfound_text).create();
	}

	protected AlertDialog createNeedsBetaDialog() {
		return new AlertDialog.Builder(this).setTitle(R.string.world_list_needs_beta_title).
			setMessage(R.string.world_list_needs_beta_message).
			setPositiveButton(R.string.world_list_needs_beta_learn_more, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogI, int button) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(AboutAppActivity.FORUMS_PAGE_URL));
					startActivity(intent);
				}
			}).
			setNegativeButton(android.R.string.cancel, null).
			create();
	}

	private final class FindWorldsThread implements Runnable {

		private final PocketInvEditorActivity activity;

		public FindWorldsThread(PocketInvEditorActivity activity) {
			this.activity = activity;
		}

		public void run() {
			File worldsFolder = new File(Environment.getExternalStorageDirectory(), "games/com.mojang/minecraftWorlds");
			System.err.println(worldsFolder);

			final List<WorldListItem> worldFolders = new ArrayList<WorldListItem>();

			if (!worldsFolder.exists()) {
				System.err.println("no storage folder");
			} else {
				for (File worldFolder : worldsFolder.listFiles()) {
					if (!worldFolder.isDirectory()) continue;
					//check if it has level.dat
					if (!new File(worldFolder, "level.dat").exists())
						continue;
					worldFolders.add(new WorldListItem(worldFolder));
				}
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
		public final File levelDat;
		public WorldListItem(File file) {
			this.folder = file;
			this.levelDat = new File(folder, "level.dat");
		}

		public String toString() {
			return folder.getName();
		}
	}

	private final class WorldListDateComparator implements Comparator<WorldListItem> {
		public int compare(WorldListItem a, WorldListItem b) {
			//System.out.println(a.toString() + ":" + b.toString() + (a.levelDat.lastModified() - b.levelDat.lastModified()));
			long result = a.levelDat.lastModified() - b.levelDat.lastModified();
			if (result < 0) {
				return 1;
			} else if (result > 0) {
				return -1;
			} else {
				return 0;
			}
		}
		public boolean equals(WorldListItem a, WorldListItem b) {
			return a.levelDat.lastModified() == b.levelDat.lastModified();
		}
	}

}
