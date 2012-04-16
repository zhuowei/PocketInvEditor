package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class EditInventorySlotActivity extends Activity {

	private EditText idEdit;

	private EditText damageEdit;

	private EditText countEdit;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventoryslot);
		idEdit = (EditText) findViewById(R.id.slot_identry);
		damageEdit = (EditText) findViewById(R.id.slot_damageentry);
		countEdit = (EditText) findViewById(R.id.slot_countentry);
		Intent intent = getIntent();
		idEdit.setText(Short.toString(intent.getShortExtra("TypeId", (short) 0)));
		damageEdit.setText(Short.toString(intent.getShortExtra("Damage", (short) 0)));
		countEdit.setText(Byte.toString(intent.getByteExtra("Count", (byte) 0)));
	}
}
