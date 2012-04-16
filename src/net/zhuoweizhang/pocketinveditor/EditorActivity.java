package net.zhuoweizhang.pocketinveditor;

import java.io.File;

import java.text.DateFormat;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.io.LevelDataConverter;

public final class EditorActivity extends Activity {

	public static Level level;

	public static File worldFolder;

	private Thread worldLoadingThread;

	private TextView worldNameView;

	private TextView worldLastPlayedView;

	private TextView worldSeedView;

	private Button startInventoryEditorButton;

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
								Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (context != null) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(context, R.string.savefailed, Toast.LENGTH_SHORT);
							}
						});
					}
				}
			}
		}).start();
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
}
