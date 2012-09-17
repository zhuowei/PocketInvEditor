package net.zhuoweizhang.pocketinveditor;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import net.zhuoweizhang.pocketinveditor.io.EntityDataConverter;
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
		listAdapter = new ArrayAdapter<TileEntity>(this, R.layout.slot_list_item, R.id.slot_list_main_text, tileEntities);
		setListAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
	}
}
