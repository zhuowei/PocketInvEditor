package net.zhuoweizhang.pocketinveditor;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.io.LevelDataConverter;
import net.zhuoweizhang.pocketinveditor.io.zip.ZipFileWriter;

public final class EditorActivity extends Activity {

	public static Level level;

	public static File worldFolder;

	private Thread worldLoadingThread;

	private TextView worldNameView;

	private TextView worldLastPlayedView;

	private TextView worldSeedView;

	private Button startInventoryEditorButton;

	private Button startBackupButton;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		worldNameView = (TextView) findViewById(R.id.main_world_name);
		worldLastPlayedView = (TextView) findViewById(R.id.main_lastplayed);
		worldSeedView = (TextView) findViewById(R.id.main_seed);
		startInventoryEditorButton = (Button) findViewById(R.id.main_edit_inventory);
		startInventoryEditorButton.setEnabled(false);
		startInventoryEditorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startInventoryEditor();
			}
		});
		startBackupButton = (Button) findViewById(R.id.main_backup);
		startBackupButton.setEnabled(false);
		startBackupButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startBackupWorld();
			}
		});
		worldFolder = new File(this.getIntent().getStringExtra("world"));
		loadLevel();
	}

	private void loadLevel() {
		worldLoadingThread = new Thread(new LevelLoadTask());
		worldLoadingThread.start();
	}

	private void levelLoaded(Level level) {
		EditorActivity.level = level;
		updateUiAfterLevelLoad();
	}

	private void updateUiAfterLevelLoad() {
		worldNameView.setText(level.getLevelName());
		worldLastPlayedView.setText(this.getResources().getText(R.string.lastplayed) + ": " + 
			DateFormat.getInstance().format(new Date(level.getLastPlayed() * 1000)));
		worldSeedView.setText(this.getResources().getText(R.string.seed) + ": " + level.getRandomSeed());
		startInventoryEditorButton.setEnabled(true);
		startBackupButton.setEnabled(true);
	}

	private void startInventoryEditor() {
		startActivity(new Intent(this, InventorySlotsActivity.class));
	}

	public static void save(final Activity context) {
		new Thread(new Runnable() {
			public void run() {
				try {
					LevelDataConverter.write(level, new File(worldFolder, "level.dat"));
					if (context != null) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (context != null) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(context, R.string.savefailed, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
		}).start();
	}

	public void startBackupWorld() {
		File backupsFolder = new File(Environment.getExternalStorageDirectory(), "games/com.mojang/minecraftWorlds_backup");
		File backupFolder = new File(backupsFolder, worldFolder.getName());
		backupFolder.mkdirs();

		String currentTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US).format(new Date());
		File backupFile = new File(backupFolder, worldFolder.getName() + currentTime + ".zip");
		int postFix = 1;
		while (backupFile.exists()) {
			postFix++;
			backupFile = new File(backupFolder, worldFolder.getName() + currentTime + "_" + postFix + ".zip");
		}

		new Thread(new BackupTask(worldFolder, backupFile)).start();
	}

	private class LevelLoadTask implements Runnable {
		public void run() {
			try {
				final Level level = LevelDataConverter.read(new File(worldFolder, "level.dat"));
				EditorActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						levelLoaded(level);
					}
				});
			} catch (final Exception e) {
				EditorActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						System.err.println("Failed to load");
						e.printStackTrace();
					}
				});
			}
		}
	}

	private class BackupTask implements Runnable {
		private File worldFolder;
		private File backupFile;
		public BackupTask(File worldFolder, File backupFile) {
			this.worldFolder = worldFolder;
			this.backupFile = backupFile;
		}
		public void run() {
			try {
				ZipFileWriter.write(worldFolder.listFiles(), backupFile);
				EditorActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(EditorActivity.this, EditorActivity.this.getResources().getText(R.string.backupcreated) + 
							backupFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				EditorActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(EditorActivity.this, R.string.backupfailed, Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	}
}
