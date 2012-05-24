package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditInventorySlotActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener {

	public static final int BROWSE_ITEM_REQUEST = 500;

	public static final int MAX_SLOT_SIZE = 64;

	private EditText idEdit;

	private EditText damageEdit;

	private EditText countEdit;

	private Button browseItemIdButton;

	private Button fillSlotButton;

	private Button maxCountToSlotButton;

	private short originalTypeId, originalDamage;
	private int originalCount;
	private Intent returnIntent = new Intent();

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventoryslot);
		setResult(Activity.RESULT_CANCELED); //in case user cancels
		idEdit = (EditText) findViewById(R.id.slot_identry);
		damageEdit = (EditText) findViewById(R.id.slot_damageentry);
		countEdit = (EditText) findViewById(R.id.slot_countentry);
		browseItemIdButton = (Button) findViewById(R.id.slot_browseitemid);
		browseItemIdButton.setOnClickListener(this);
		fillSlotButton = (Button) findViewById(R.id.slot_fillslot);
		fillSlotButton.setOnClickListener(this);
		maxCountToSlotButton = (Button) findViewById(R.id.slot_maxcount);
		maxCountToSlotButton.setOnClickListener(this);
		Intent intent = getIntent();
		originalTypeId = intent.getShortExtra("TypeId", (short) 0);
		originalDamage = intent.getShortExtra("Damage", (short) 0);
		originalCount = intent.getIntExtra("Count", (byte) 0);
		idEdit.setText(Short.toString(originalTypeId));
		damageEdit.setText(Short.toString(originalDamage));
		countEdit.setText(Integer.toString(originalCount));
		idEdit.setOnFocusChangeListener(this);
		damageEdit.setOnFocusChangeListener(this);
		countEdit.setOnFocusChangeListener(this);
		returnIntent.putExtras(intent);

	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			checkInputAfterChange();
		}
	}

	private boolean checkInputAfterChange() {
		boolean isCorrect = true;
		short newId = originalTypeId;
		short newDamage = originalDamage;
		int newCount = originalCount;
		try {
			newId = Short.parseShort(idEdit.getText().toString());
			idEdit.setText(Short.toString(newId));
			idEdit.setError(null);
		} catch (NumberFormatException e) {
			idEdit.setError("Derp");
			isCorrect = false;
		}
                try {
			newDamage = Short.parseShort(damageEdit.getText().toString());
			damageEdit.setText(Short.toString(newDamage));
                        damageEdit.setError(null);
                } catch (NumberFormatException e) {
                        damageEdit.setError("Derp");
			isCorrect = false;
                }
                try {
			newCount = Integer.parseInt(countEdit.getText().toString());
			if (newCount < 0 || newCount > 255) {
				throw new NumberFormatException("derp");
			}
			countEdit.setText(Integer.toString(newCount));
                        countEdit.setError(null);
                } catch (NumberFormatException e) {
                        countEdit.setError("Derp");
			isCorrect = false;
                }
		if (isCorrect) {
			returnIntent.putExtra("TypeId", newId);
			returnIntent.putExtra("Damage", newDamage);
			returnIntent.putExtra("Count", newCount);
			if (newId != originalTypeId || newDamage != originalDamage || newCount != originalCount) {
				setResult(RESULT_OK, returnIntent);
			} else {
				setResult(RESULT_CANCELED);
			}
		} else {
			setResult(RESULT_CANCELED);
		}
		return isCorrect;
	}

	public void onBackPressed() {
		if (checkInputAfterChange()) {
			super.onBackPressed();
		}
	}

	private boolean checkAllInput() {
		try {
			Short.decode(idEdit.getText().toString());
			Short.decode(damageEdit.getText().toString());
			Short.decode(countEdit.getText().toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void onClick(View v) {
		if (v == browseItemIdButton) {
			showBrowseItemIdActivity();
		} else if (v == fillSlotButton) {
			fillSlotToMax();
		} else if (v == maxCountToSlotButton) {
			fillSlotToMaxByteValue();
		}
	}

	public void showBrowseItemIdActivity() {
		startActivityForResult(new Intent(this, BrowseItemsActivity.class), BROWSE_ITEM_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == BROWSE_ITEM_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				idEdit.setText(Integer.toString(intent.getIntExtra("TypeId", 0)));
				if (intent.getBooleanExtra("HasSubtypes", false)) {
					damageEdit.setText(Short.toString(intent.getShortExtra("Damage", (short) 0)));
				}
				checkInputAfterChange();
			}
		}
	}

	private void fillSlotToMax() {
		int newCount = MAX_SLOT_SIZE;
		returnIntent.putExtra("Count", newCount);
		if (newCount != originalCount) {
			setResult(RESULT_OK, returnIntent);
		}
		countEdit.setText(Integer.toString(newCount));
	}

        private void fillSlotToMaxByteValue() {
                int newCount = 255;
                returnIntent.putExtra("Count", newCount);
                if (newCount != originalCount) {
                        setResult(RESULT_OK, returnIntent);
                }
                countEdit.setText(Integer.toString(newCount));
        }

}
