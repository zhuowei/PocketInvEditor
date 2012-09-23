package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import net.zhuoweizhang.pocketinveditor.io.EntityDataConverter;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialIcon;
import net.zhuoweizhang.pocketinveditor.material.icon.MaterialKey;
import net.zhuoweizhang.pocketinveditor.tileentity.*;

public class TileEntityViewActivity extends ListActivity implements LevelDataLoadListener, EntityDataLoadListener {

	protected ArrayAdapter<TileEntity> listAdapter;

	protected List<TileEntity> tileEntities;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (EditorActivity.level != null) {
			onLevelDataLoad();
		} else {
			EditorActivity.loadLevelData(this, this, this.getIntent().getStringExtra("world"));
		}
	}

	public void onLevelDataLoad() {
		loadEntities();
	}

	protected void loadEntities() {
		new Thread(new EntitiesInfoActivity.LoadEntitiesTask(this, this)).start();
	}

	public void onEntitiesLoaded(EntityDataConverter.EntityData entitiesDat) {
		EditorActivity.level.setEntities(entitiesDat.entities);
		EditorActivity.level.setTileEntities(entitiesDat.tileEntities);
		this.tileEntities = entitiesDat.tileEntities;
		listAdapter = new ArrayAdapter<TileEntity>(this, R.layout.slot_list_item, R.id.slot_list_main_text, tileEntities) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View retval = super.getView(position, convertView, parent);
				ImageView iconView = (ImageView) retval.findViewById(R.id.slot_list_icon);
				TileEntity tileEntity = this.getItem(position);
				MaterialKey iconKey =  getIconMaterial(tileEntity.getClass());
				MaterialIcon icon = null;
				if (iconKey != null) icon = MaterialIcon.icons.get(iconKey);
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
		setListAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		TileEntity entity = listAdapter.getItem(position);
		Intent intent = getTileEntityIntent(entity.getClass());
		if (intent == null) return;
		intent.putExtras(this.getIntent());
		intent.putExtra("Index", position);
		startActivity(intent);
	}

	protected Intent getTileEntityIntent(Class<? extends TileEntity> clazz) {
		if (ContainerTileEntity.class.isAssignableFrom(clazz)) {
			return new Intent(this, ChestSlotsActivity.class);
		}
		return null;
	}

	/** Provides a MaterialKey for the material to be displayed as an icon in the selection list. */
	public static MaterialKey getIconMaterial(Class<? extends TileEntity> clazz) {
		if (clazz == FurnaceTileEntity.class) return new MaterialKey((short) 61, (short) 0); //Lit furnace
		return new MaterialKey((short) 54, (short) 0); //Chest
	}
}
