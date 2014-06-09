package net.zhuoweizhang.pocketinveditor;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.entity.Player;
import net.zhuoweizhang.pocketinveditor.entity.PlayerAbilities;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public final class WorldInfoActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, LevelDataLoadListener,
	AdapterView.OnItemSelectedListener {

	public static final int DAY_LENGTH = 19200; //16 minutes

	private static final int DIALOG_CHANGE_GAME_MODE = 1167366;

	private static final int DIALOG_MOVE_PLAYER = 4142;

	private static final String[] GAMEMODES = {"Survival", "Creative"};

	private TextView gameModeText;

	private Button gameModeChangeButton;

	private EditText worldTimeText;

	private Button timeToMorningButton;

	private Button timeToNightButton;

	private Button spawnToPlayerButton;

	private Button warpToSpawnButton;

	private TextView playerXText, playerYText, playerZText;

	private EditText healthText;

	private Button fullHealthButton, infiniteHealthButton;

	private Button sidewaysOnButton, sidewaysOffButton;

	private Button movePlayerButton;

	private TextView worldNameText;

	private TextView worldFolderNameText;

	private CheckBox flyingBox, mayFlyBox, invulnerableBox, instaBuildBox;
	private TextView dayCycleStopTimeText;
	private CheckBox spawnMobsBox;
	private Spinner generatorSpinner;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world_info);
		gameModeText = (TextView) findViewById(R.id.world_info_gamemode);
		gameModeChangeButton = (Button) findViewById(R.id.world_info_gamemode_change);
		gameModeChangeButton.setOnClickListener(this);
		worldTimeText = (EditText) findViewById(R.id.world_info_time_text);
		worldTimeText.setOnFocusChangeListener(this);
		spawnToPlayerButton = (Button) findViewById(R.id.world_info_spawn_to_player_button);
		spawnToPlayerButton.setOnClickListener(this);
		warpToSpawnButton = (Button) findViewById(R.id.world_info_warp_to_spawn_button);
		warpToSpawnButton.setOnClickListener(this);
		timeToMorningButton = (Button) findViewById(R.id.world_info_time_to_morning);
		timeToMorningButton.setOnClickListener(this);
		timeToNightButton = (Button) findViewById(R.id.world_info_time_to_night);
		timeToNightButton.setOnClickListener(this);
		playerXText = (TextView) findViewById(R.id.world_info_player_x);
		playerYText = (TextView) findViewById(R.id.world_info_player_y);
		playerZText = (TextView) findViewById(R.id.world_info_player_z);
		healthText = (EditText) findViewById(R.id.world_info_health);
		fullHealthButton = (Button) findViewById(R.id.world_info_full_health);
		infiniteHealthButton = (Button) findViewById(R.id.world_info_infinite_health);
		healthText.setOnFocusChangeListener(this);
		fullHealthButton.setOnClickListener(this);
		infiniteHealthButton.setOnClickListener(this);
		sidewaysOnButton = (Button) findViewById(R.id.world_info_sideways_on);
		sidewaysOffButton = (Button) findViewById(R.id.world_info_sideways_off);
		sidewaysOnButton.setOnClickListener(this);
		sidewaysOffButton.setOnClickListener(this);
		worldNameText = (TextView) findViewById(R.id.world_info_name);
		worldNameText.setOnFocusChangeListener(this);
		worldFolderNameText = (TextView) findViewById(R.id.world_info_folder_name);
		worldFolderNameText.setOnFocusChangeListener(this);
		movePlayerButton = (Button) findViewById(R.id.world_info_move_player);
		movePlayerButton.setOnClickListener(this);

		flyingBox = (CheckBox) findViewById(R.id.world_info_flying);
		flyingBox.setOnClickListener(this);
		invulnerableBox = (CheckBox) findViewById(R.id.world_info_invulnerable);
		invulnerableBox.setOnClickListener(this);
		instaBuildBox = (CheckBox) findViewById(R.id.world_info_insta_build);
		instaBuildBox.setOnClickListener(this);
		mayFlyBox = (CheckBox) findViewById(R.id.world_info_may_fly);
		mayFlyBox.setOnClickListener(this);

		dayCycleStopTimeText = (TextView) findViewById(R.id.world_info_day_cycle_stop_time_text);
		dayCycleStopTimeText.setOnFocusChangeListener(this);

		spawnMobsBox = (CheckBox) findViewById(R.id.world_info_spawn_mobs);
		spawnMobsBox.setOnClickListener(this);
		generatorSpinner = (Spinner) findViewById(R.id.world_info_generator);
		generatorSpinner.setOnItemSelectedListener(this);

		//if (EditorActivity.level != null) {
		//	onLevelDataLoad();
		//} else {

		//}

	}

	public void onStart() {
		super.onStart();
		EditorActivity.loadLevelData(this, this, this.getIntent().getStringExtra("world"));
	}

	public void onLevelDataLoad() {
		gameModeText.setText(EditorActivity.level.getGameType() == 1 ? R.string.gamemode_creative : R.string.gamemode_survival);
		updateTimeText();
		updatePlayerPositionText();
		updatePlayerHealthText();
		worldNameText.setText(EditorActivity.level.getLevelName());
		worldFolderNameText.setText(EditorActivity.worldFolder.getName());
		updatePlayerAbilitiesCheckBoxes();
		dayCycleStopTimeText.setText(Long.toString(EditorActivity.level.getDayCycleStopTime()));
		spawnMobsBox.setChecked(EditorActivity.level.getSpawnMobs());
		generatorSpinner.setSelection(EditorActivity.level.getGenerator());
	}

	public void updateTimeText() {
		worldTimeText.setText(Long.toString(EditorActivity.level.getTime()));
	}

	public void updatePlayerPositionText() {
		Vector3f loc = EditorActivity.level.getPlayer().getLocation();
		playerXText.setText("X: " + Float.toString(loc.getX()));
		playerYText.setText("Y: " + Float.toString(loc.getY()));
		playerZText.setText("Z: " + Float.toString(loc.getZ()));
	}

	private void setSpawnToPlayerPosition() {
		Level level = EditorActivity.level;
		Vector3f loc = level.getPlayer().getLocation();
		level.setSpawnX((int) loc.getX());
		level.setSpawnY((int) loc.getY());
		level.setSpawnZ((int) loc.getZ());
		Player player = level.getPlayer();
		player.setSpawnX((int) loc.getX());
		player.setSpawnY((int) loc.getY());
		player.setSpawnZ((int) loc.getZ());
		player.setBedPositionX((int) loc.getX());
		player.setBedPositionY((int) loc.getY());
		player.setBedPositionZ((int) loc.getZ());
	}

	private void setTimeToMorning() {
		EditorActivity.level.setTime((EditorActivity.level.getTime() / DAY_LENGTH) * DAY_LENGTH);
		EditorActivity.save(this);
	}

	private void setTimeToNight() {
		EditorActivity.level.setTime(((EditorActivity.level.getTime() / DAY_LENGTH) * DAY_LENGTH) + (DAY_LENGTH / 2));
		EditorActivity.save(this);
	}

	public void updatePlayerHealthText() {
		healthText.setText(Short.toString(EditorActivity.level.getPlayer().getHealth()));
	}

	private void setPlayerHealthToFull() {
		EditorActivity.level.getPlayer().setHealth((short) 20);
		EditorActivity.save(this);
		updatePlayerHealthText();
	}

	private void setPlayerHealthToInfinite() {
		EditorActivity.level.getPlayer().setHealth(Short.MAX_VALUE);
		EditorActivity.save(this);
		updatePlayerHealthText();
	}

	private void warpPlayerToSpawn() {
		Vector3f loc = EditorActivity.level.getPlayer().getLocation();
		Level level = EditorActivity.level;
		loc.setX(level.getSpawnX());
		loc.setY(level.getSpawnY());
		loc.setZ(level.getSpawnZ());
		EditorActivity.save(this);
		updatePlayerPositionText();
	}

        private void playerSideways(boolean doThis) {
                EditorActivity.level.getPlayer().setDeathTime(doThis? Short.MAX_VALUE : 0); 
                EditorActivity.save(this);
        }

	private void updatePlayerAbilitiesCheckBoxes() {
		PlayerAbilities abilities = EditorActivity.level.getPlayer().getAbilities();
		flyingBox.setChecked(abilities.flying);
		invulnerableBox.setChecked(abilities.invulnerable);
		instaBuildBox.setChecked(abilities.instabuild);
		mayFlyBox.setChecked(abilities.mayFly);
	}


	public void onClick(View v) {
		if (v == gameModeChangeButton) {
			showDialog(DIALOG_CHANGE_GAME_MODE);
		} else if (v == spawnToPlayerButton) {
			setSpawnToPlayerPosition();
			EditorActivity.save(this);
		} else if (v == timeToMorningButton) {
			setTimeToMorning();
			updateTimeText();
		} else if (v == timeToNightButton) {
			setTimeToNight();
			updateTimeText();
		} else if (v == fullHealthButton) {
			setPlayerHealthToFull();
		} else if (v == infiniteHealthButton) {
			setPlayerHealthToInfinite();
		} else if (v == warpToSpawnButton) {
			warpPlayerToSpawn();
		} else if (v == sidewaysOnButton) {
			playerSideways(true);
		} else if (v == sidewaysOffButton) {
			playerSideways(false);
		} else if (v == movePlayerButton) {
			showDialog(DIALOG_MOVE_PLAYER);
		} else if (v == flyingBox) {
			Player player = EditorActivity.level.getPlayer();
			player.getAbilities().flying = flyingBox.isChecked();
			EditorActivity.save(this);
		} else if (v == invulnerableBox) {
			Player player = EditorActivity.level.getPlayer();
			player.getAbilities().invulnerable = invulnerableBox.isChecked();
			EditorActivity.save(this);
		} else if (v == instaBuildBox) {
			Player player = EditorActivity.level.getPlayer();
			player.getAbilities().instabuild = instaBuildBox.isChecked();
			EditorActivity.save(this);
		} else if (v == mayFlyBox) {
			Player player = EditorActivity.level.getPlayer();
			player.getAbilities().mayFly = mayFlyBox.isChecked();
			EditorActivity.save(this);
		} else if (v == spawnMobsBox) {
			EditorActivity.level.setSpawnMobs(instaBuildBox.isChecked());
			EditorActivity.save(this);
		}
	}

	public Dialog onCreateDialog(int dialogId) {
		switch (dialogId) {
			case DIALOG_CHANGE_GAME_MODE:
				return createChangeGameModeDialog();
			case DIALOG_MOVE_PLAYER:
				return createMovePlayerDialog();
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
			case DIALOG_MOVE_PLAYER:
				Player player = EditorActivity.level.getPlayer();
				Vector3f playerLoc = player.getLocation();
				((EditText) dialog.findViewById(R.id.entities_spawn_x)).setText(Float.toString(playerLoc.getX()));
				((EditText) dialog.findViewById(R.id.entities_spawn_y)).setText(Float.toString(playerLoc.getY()));
				((EditText) dialog.findViewById(R.id.entities_spawn_z)).setText(Float.toString(playerLoc.getZ()));
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
						EditorActivity.level.getPlayer().getAbilities().initForGameType(item);
						EditorActivity.save(WorldInfoActivity.this);
						updatePlayerAbilitiesCheckBoxes();
						dialog.dismiss();
					}
			}).create();
	}

	protected Dialog createMovePlayerDialog() {
		final View textEntryView = getLayoutInflater().inflate(R.layout.move_player_dialog, null);
		return new AlertDialog.Builder(this)
			.setTitle(R.string.world_info_move_player)
			.setView(textEntryView)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogI, int button) {
					try {
						AlertDialog dialog = (AlertDialog) dialogI;
						float x = Float.parseFloat(((EditText) dialog.findViewById(R.id.entities_spawn_x)).getText().toString());
						float y = Float.parseFloat(((EditText) dialog.findViewById(R.id.entities_spawn_y)).getText().toString());
						float z = Float.parseFloat(((EditText) dialog.findViewById(R.id.entities_spawn_z)).getText().toString());

						EditorActivity.level.getPlayer().setLocation(new Vector3f(x, y, z));
						EditorActivity.save(WorldInfoActivity.this);
						updatePlayerPositionText();
					} catch (NumberFormatException ne) {
						Toast.makeText(WorldInfoActivity.this, R.string.invalid_number, Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (v == worldTimeText) {
			if (!hasFocus) {
				checkTimeInputAfterChange();
			}
		} else if (v == healthText) {
			if (!hasFocus) {
				checkHealthInputAfterChange();
			}
		} else if (v == worldNameText) {
			if (!hasFocus) {
				checkWorldNameAfterChange();
			}
		} else if (v == worldFolderNameText) {
			if (!hasFocus) {
				checkWorldFolderNameAfterChange();
			}
		} else if (v == dayCycleStopTimeText) {
			if (!hasFocus) {
				checkStopTimeInputAfterChange();
			}
		}
	}

	public void onPause() {
		super.onPause();
		checkTimeInputAfterChange();
		checkHealthInputAfterChange();
		checkWorldNameAfterChange();
		checkWorldFolderNameAfterChange();
		checkStopTimeInputAfterChange();
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

	public void checkHealthInputAfterChange() {
		short newHealth = 0;
		try {
			newHealth = Short.parseShort(healthText.getText().toString());
			healthText.setError(null);
			if (newHealth != EditorActivity.level.getPlayer().getHealth()) {
				EditorActivity.level.getPlayer().setHealth(newHealth);
				EditorActivity.save(this);
			}
		} catch (NumberFormatException e) {
			healthText.setError(this.getResources().getText(R.string.invalid_number));
		}
	}

	protected void checkWorldNameAfterChange() {
		String newText = worldNameText.getText().toString();
		if (!newText.equals(EditorActivity.level.getLevelName())) {
			EditorActivity.level.setLevelName(newText);
			EditorActivity.save(this);
		}
	}

	protected void checkWorldFolderNameAfterChange() {
		String newText = worldFolderNameText.getText().toString();
		if (!newText.equals(EditorActivity.worldFolder.getName())) {
			File newLoc = new File(EditorActivity.worldFolder.getParentFile(), newText);
			if (newLoc.exists()) {
				worldFolderNameText.setError(this.getResources().getText(R.string.folder_exists));
				return;
			}
			worldFolderNameText.setError(null);
			boolean success = EditorActivity.worldFolder.renameTo(newLoc);
			if (!success) {
				worldFolderNameText.setError(this.getResources().getText(R.string.folder_rename_failed));
			} else {
				Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
				EditorActivity.worldFolder = newLoc;
			}
		}
	}

	public void checkStopTimeInputAfterChange() {
		long newTime = 0;
		try {
			newTime = Long.parseLong(dayCycleStopTimeText.getText().toString());
			dayCycleStopTimeText.setError(null);
			if (newTime != EditorActivity.level.getDayCycleStopTime()) {
				EditorActivity.level.setDayCycleStopTime(newTime);
				EditorActivity.save(this);
			}
		} catch (NumberFormatException e) {
			dayCycleStopTimeText.setError(this.getResources().getText(R.string.invalid_number));
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent == generatorSpinner) {
			EditorActivity.level.setGenerator(position);
			EditorActivity.save(this);
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}
}
