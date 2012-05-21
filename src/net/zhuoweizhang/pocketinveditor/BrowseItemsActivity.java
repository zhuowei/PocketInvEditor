package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import net.zhuoweizhang.pocketinveditor.material.Material;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialIcon;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialKey;

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
		itemsListAdapter = new ArrayAdapter<Material>(this, R.layout.item_id_list_item, R.id.item_id_main_text, Material.materials) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View retval = super.getView(position, convertView, parent);
				ImageView iconView = (ImageView) retval.findViewById(R.id.item_id_icon);
				Material material = this.getItem(position);
				MaterialIcon icon = MaterialIcon.icons.get(new MaterialKey((short) material.getId(), material.getDamage()));
				if (icon == null) {
					icon = MaterialIcon.icons.get(new MaterialKey((short) material.getId(), (short) 0));
				}
				if (icon != null) {
					BitmapDrawable myDrawable = new BitmapDrawable(icon.bitmap);
					myDrawable.setDither(false);
					myDrawable.setAntiAlias(false);
					myDrawable.setFilterBitmap(false);
					iconView.setImageDrawable(myDrawable);
					iconView.setVisibility(View.VISIBLE);
				} else {
					iconView.setVisibility(View.INVISIBLE);
				}
				return retval;
			}
		};
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
