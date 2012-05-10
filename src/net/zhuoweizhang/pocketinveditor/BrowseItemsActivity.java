package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import net.zhuoweizhang.pocketinveditor.material.Material;

import net.zhuoweizhang.pocketinveditor.io.xml.MaterialLoader;

public final class BrowseItemsActivity extends ListActivity implements OnItemClickListener, TextWatcher {

	private ArrayAdapter<Material> itemsListAdapter;

	private EditText filterInputText;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setResult(RESULT_CANCELED);
		if (Material.materials == null) {
			//Load the materials on the main thread - may cause slowdowns
			new MaterialLoader(getResources().getXml(R.xml.item_data)).run();
		}
		setContentView(R.layout.item_id_browse);
		itemsListAdapter = new ArrayAdapter<Material>(this, R.layout.item_id_list_item, Material.materials);
		filterInputText = (EditText) findViewById(R.id.item_id_browse_filter_text);
		filterInputText.addTextChangedListener(this);
		setListAdapter(itemsListAdapter);
		getListView().setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent returnIntent = new Intent();
		Material material = (Material) parent.getItemAtPosition(position);
		returnIntent.putExtra("TypeId", material.getId());
		returnIntent.putExtra("HasSubtypes", material.hasSubtypes());
		returnIntent.putExtra("Damage", material.getDamage());
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void afterTextChanged(Editable e) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		itemsListAdapter.getFilter().filter(s);
	}

}
