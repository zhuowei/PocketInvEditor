package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public final class WorldInfoActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

	private static final int DIALOG_CHANGE_GAME_MODE = 1167366;

	private static final String[] GAMEMODES = {"Survival", "Creative"};

	private TextView gameModeText;

	private Button gameModeChangeButton;

	private EditText worldTimeText;

	private Button spawnToPlayerButton;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world_info);
		gameModeText = (TextView) findViewById(R.id.world_info_gamemode);
		gameModeChangeButton = (Button) findViewById(R.id.world_info_gamemode_change);
		gameModeChangeButton.setOnClickListener(this);
		gameModeText.setText(EditorActivity.level.getGameType() == 1 ? R.string.gamemode_creative : R.string.gamemode_survival);
		worldTimeText = (EditText) findViewById(R.id.world_info_time_text);
		updateTimeText();
		worldTimeText.setOnFocusChangeListener(this);
		spawnToPlayerButton = (Button) findViewById(R.id.world_info_spawn_to_player_button);
		spawnToPlayerButton.setOnClickListener(this);

	}

	public void updateTimeText() {
		worldTimeText.setText(Long.toString(EditorActivity.level.getTime()));
	}

	private void setSpawnToPlayerPosition() {
		Level level = EditorActivity.level;
		Vector loc = level.getPlayer().getLocation();
		level.setSpawnX((int) loc.getX());
		level.setSpawnY((int) loc.getY());
		level.setSpawnZ((int) loc.getZ());
	}

	public void onClick(View v) {
		if (v == gameModeChangeButton) {
			showDialog(DIALOG_CHANGE_GAME_MODE);
		} else if (v == spawnToPlayerButton) {
			setSpawnToPlayerPosition();
			EditorActivity.save(this);
		}
	}

	public Dialog onCreateDialog(int dialogId) {
		switch (dialogId) {
			case DIALOG_CHANGE_GAME_MODE:
				return createChangeGameModeDialog();
			default:
				return super.onCreateDialog(dialogId);
		}
	}

	public void onPrepareDialog(int dialogId, Dialog dialog) {
		switch (dialogId) {
			case DIALOG_CHANGE_GAME_MODE:
				int levelType = EditorActivity.level.getGameType() == 1 ? 1 : 0;
				((AlertDialog) dialog).getListView().setSelection(levelType);
				break;
			default:
				super.onPrepareDialog(dialogId, dialog);
		}
	}

	protected AlertDialog createChangeGameModeDialog() {
		return new AlertDialog.Builder(this).setTitle(R.string.gamemode).
			setSingleChoiceItems(GAMEMODES, -1, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						EditorActivity.level.setGameType(item);
						gameModeText.setText(EditorActivity.level.getGameType() == 1 ? 
							R.string.gamemode_creative : R.string.gamemode_survival);
						EditorActivity.save(WorldInfoActivity.this);
						dialog.dismiss();
					}
			}).create();
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (v == worldTimeText) {
			if (!hasFocus) {
				checkTimeInputAfterChange();
			}
		}
	}

	public void onPause() {
		super.onPause();
		checkTimeInputAfterChange();
	}

	public void checkTimeInputAfterChange() {
		long newTime = 0;
		try {
			newTime = Long.parseLong(worldTimeText.getText().toString());
			worldTimeText.setError(null);
			if (newTime != EditorActivity.level.getTime()) {
				EditorActivity.level.setTime(newTime);
				EditorActivity.save(this);
			}
		} catch (NumberFormatException e) {
			worldTimeText.setError(this.getResources().getText(R.string.invalid_number));
		}
	}
}
