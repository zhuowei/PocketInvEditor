package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.zhuoweizhang.pocketinveditor.util.ROFLCopter;

public final class AboutAppActivity extends Activity implements View.OnLongClickListener {

	public static final int SLEEP_INTERVAL = 120;

	public TextView roflField;

	public TextView appNameText;

	public int frame;

	public boolean flipRunning = false;

	public FlipTask flipTask = new FlipTask();

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		roflField = (TextView) findViewById(R.id.rofl_text);
		appNameText = (TextView) findViewById(R.id.about_appnametext);
		appNameText.setOnLongClickListener(this);
		
	}

	protected void onPause() {
		super.onPause();
		flipRunning = false;
	}

	public void startRofl() {
		roflField.setVisibility(View.VISIBLE);
		flipRunning = true;
		new Thread(flipTask).start();
	}

	public boolean onLongClick(View v) {
		if (v == appNameText && !flipRunning) {
			startRofl();
			return true;
		}
		return false;
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
