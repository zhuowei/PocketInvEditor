package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.zhuoweizhang.pocketinveditor.util.ROFLCopter;

public final class AboutAppActivity extends Activity implements View.OnLongClickListener, View.OnClickListener {

	public static final String FORUMS_PAGE_URL = 
		"http://www.minecraftforum.net/topic/1167366-wip-pocketinveditor-a-minecraft-pe-inventory-editor-for-android/";

	public static final int SLEEP_INTERVAL = 120;

	public TextView roflField;

	public TextView appNameText;

	public Button gotoForumsButton;

	public int frame;

	public boolean flipRunning = false;

	public boolean flipActivated = false;

	public FlipTask flipTask = new FlipTask();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		roflField = (TextView) findViewById(R.id.rofl_text);
		appNameText = (TextView) findViewById(R.id.about_appnametext);
		appNameText.setOnLongClickListener(this);
		gotoForumsButton = (Button) findViewById(R.id.about_go_to_forums_button);
		gotoForumsButton.setOnClickListener(this);
		if (savedInstanceState != null) {
			flipActivated = savedInstanceState.getBoolean("flipActivated", false);
		}
	}

	protected void onPause() {
		super.onPause();
		flipRunning = false;
	}

	protected void onStart() {
		super.onStart();
		if (flipActivated && !flipRunning) {
			startRofl();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
		icicle.putBoolean("flipActivated", flipActivated);
	}


	public void startRofl() {
		roflField.setVisibility(View.VISIBLE);
		flipRunning = true;
		flipActivated = true;
		new Thread(flipTask).start();
	}

	public boolean onLongClick(View v) {
		if (v == appNameText && !flipRunning) {
			startRofl();
			return true;
		}
		return false;
	}

	public void onClick(View v) {
		if (v == gotoForumsButton) {
			openForumsPage();
		}
	}

	protected void openForumsPage() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(FORUMS_PAGE_URL));
		startActivity(intent);
	}

	public void flipFrame() {
		frame = (frame + 1) % ROFLCopter.r.length;
		roflField.setText(ROFLCopter.r[frame]);
	}

	public class FlipTask implements Runnable {
		public void run() {
			while(flipRunning) {
				AboutAppActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						flipFrame();
					}
				});
				try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (Exception e) {
				}
			}
		}
	}
}
