package net.zhuoweizhang.pocketinveditor;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.zhuoweizhang.pocketinveditor.entity.*;
import net.zhuoweizhang.pocketinveditor.io.EntityDataConverter;

public final class EntitiesInfoActivity extends Activity {

	private TextView entityCountText;

	private List<Entity> entitiesList;

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entities_info);
		entityCountText = (TextView) findViewById(R.id.entities_main_count);
		loadEntities();
	}

	protected void loadEntities() {
		new Thread(new LoadEntitiesTask()).start();
	}

	protected void onEntitiesLoaded(List<Entity> entitiesList) {
		EditorActivity.level.setEntities(entitiesList);
		this.entitiesList = entitiesList;
		countEntities();
	}

	protected void countEntities() {
		Map<EntityType, Integer> countMap = new EnumMap<EntityType, Integer>(EntityType.class);
		for (Entity e: entitiesList) {
			EntityType entityType = e.getEntityType();
			int newCount = 1;
			Integer oldCount = countMap.get(entityType);
			if (oldCount != null) {
				newCount += oldCount;
			}
			countMap.put(entityType, newCount);
		}

		String entityCountString = buildEntityCountString(countMap);
		entityCountText.setText(entityCountString);
	}

	private String buildEntityCountString(Map<EntityType, Integer> countMap) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<EntityType, Integer> entry: countMap.entrySet()) {
			builder.append(this.getResources().getText(EntityTypeLocalization.namesMap.get(entry.getKey())));
			builder.append(':');
			builder.append(entry.getValue());
			builder.append('\n');
		}
		return builder.toString();
	}

	private class LoadEntitiesTask implements Runnable {
		public void run() {
			File entitiesFile = new File(EditorActivity.worldFolder, "entities.dat");
			try {
				final List<Entity> entitiesList = EntityDataConverter.read(entitiesFile);
				EntitiesInfoActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						EntitiesInfoActivity.this.onEntitiesLoaded(entitiesList);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
