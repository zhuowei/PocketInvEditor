package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class WorldInfoActivity extends Activity implements View.OnClickListener {

	private static final int DIALOG_CHANGE_GAME_MODE = 1167366;

	private static final String[] GAMEMODES = {"Survival", "Creative"};

	private TextView gameModeText;

	private Button gameModeChangeButton;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world_info);
		gameModeText = (TextView) findViewById(R.id.world_info_gamemode);
		gameModeChangeButton = (Button) findViewById(R.id.world_info_gamemode_change);
		gameModeChangeButton.setOnClickListener(this);
		gameModeText.setText(EditorActivity.level.getGameType() == 1 ? R.string.gamemode_creative : R.string.gamemode_survival);
	}

	public void onClick(View v) {
		if (v == gameModeChangeButton) {
			showDialog(DIALOG_CHANGE_GAME_MODE);
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
}
