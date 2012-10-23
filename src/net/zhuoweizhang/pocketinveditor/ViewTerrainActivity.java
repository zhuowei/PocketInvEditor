package net.zhuoweizhang.pocketinveditor;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.entity.Player;
import net.zhuoweizhang.pocketinveditor.geo.*;

public class ViewTerrainActivity extends Activity implements View.OnClickListener {

	private Button burnBabyBurnButton;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terrain_edit);
		burnBabyBurnButton = (Button) findViewById(R.id.terrain_burnbabyburn);
		burnBabyBurnButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == burnBabyBurnButton) {
			Toast.makeText(this, R.string.terrain_editing, Toast.LENGTH_SHORT).show();
			new Thread(new DiscoInfernoTask()).start();
		}
	}

	public void discoInferno() {
		ChunkManager mgr = new ChunkManager(new File(this.getIntent().getStringExtra("world") + "/chunks.dat"));

		Level level = EditorActivity.level;
		Player player = level.getPlayer();
		int beginX = (int) player.getLocation().getX();
		int beginZ = (int) player.getLocation().getZ();
		int beginY = (int) player.getLocation().getY() + 10;
		if (beginY > 127 || beginY < 0) beginY = 127;
		int hw = 21;
		int hl = 21;
		for (int x = beginX - hw; x < beginX + hw; x++) {
			for (int z = beginZ - hl; z < beginZ + hl; z++) {
				mgr.setBlockTypeId(x, beginY, z, 10);
			}
		}

		try {
			mgr.saveAll();
			mgr.unloadChunks(false);
			mgr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void doneDisco() {
		Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
	}


	private class DiscoInfernoTask implements Runnable {

		public void run() {
			discoInferno();
			ViewTerrainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					doneDisco();
				}
			});
		}
	}
		
}
		
