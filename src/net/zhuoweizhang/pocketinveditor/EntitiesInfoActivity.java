package net.zhuoweizhang.pocketinveditor;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.zhuoweizhang.pocketinveditor.entity.*;
import net.zhuoweizhang.pocketinveditor.io.EntityDataConverter;
import net.zhuoweizhang.pocketinveditor.tileentity.*;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class EntitiesInfoActivity extends Activity implements View.OnClickListener, LevelDataLoadListener, EntityDataLoadListener {

	private TextView entityCountText;

	private List<Entity> entitiesList;

	public static final int BABY_GROWTH_TICKS = -24000;

	public static final int BREED_TICKS = 9999; //wait until release to find out actual value

	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entities_info);
		entityCountText = (TextView) findViewById(R.id.entities_main_count);

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
		new Thread(new LoadEntitiesTask(this, this)).start();
	}

	public void onEntitiesLoaded(EntityDataConverter.EntityData entitiesDat) {
		EditorActivity.level.setEntities(entitiesDat.entities);
		EditorActivity.level.setTileEntities(entitiesDat.tileEntities);
		this.entitiesList = entitiesDat.entities;
		countEntities();
	}

	public void onClick(View v) {
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
		if (entityCountString.length() == 0) {
			entityCountString = this.getResources().getText(R.string.entities_none).toString();
		}
		entityCountText.setText(entityCountString);
	}

	private String buildEntityCountString(Map<EntityType, Integer> countMap) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<EntityType, Integer> entry: countMap.entrySet()) {
			Integer resId = EntityTypeLocalization.namesMap.get(entry.getKey());
			if (resId == null) resId = new Integer(R.string.entity_unknown);
			builder.append(this.getResources().getText(resId));
			builder.append(':');
			builder.append(entry.getValue());
			builder.append('\n');
		}
		return builder.toString();
	}

	public void apoCowlypse() {
		List<Entity> list = EditorActivity.level.getEntities();
		Vector3f playerLoc = EditorActivity.level.getPlayer().getLocation();
		int beginX = (int) playerLoc.getX() - 16;
		int endX = (int) playerLoc.getX() + 16;
		int beginZ = (int) playerLoc.getZ() - 16;
		int endZ = (int) playerLoc.getZ() + 16;
		for (int x = beginX; x < endX; x += 2) {
			for (int z = beginZ; z < endZ; z += 2) {
				Cow cow = new Cow();
				cow.setLocation(new Vector3f(x, 128, z));
				cow.setEntityTypeId(EntityType.COW.getId());
				cow.setHealth((short) 128);
				list.add(cow);
			}
		}
		save(this);
		countEntities();
	}

	public void cowTipping(EntityType type, short tipNess) {
		List<Entity> list = EditorActivity.level.getEntities();
		for (Entity entity: list) {
			if (entity.getEntityType() == type) {
				((LivingEntity) entity).setDeathTime(tipNess);
			}
		}
		save(this);
	}

	public void spawnMobs(EntityType type, Vector3f loc, int count) throws Exception {
		List<Entity> entities = EditorActivity.level.getEntities();
		for (int i = 0; i < count; i++) {
			Entity e = type.getEntityClass().newInstance();
			e.setEntityTypeId(type.getId());
			e.setLocation(loc);
			if (e instanceof LivingEntity) {
				((LivingEntity) e).setHealth((short) ((LivingEntity) e).getMaxHealth());
			}
			entities.add(e);
		}
	}

	public int removeEntities(EntityType type) {
		int removedCount = 0;
		List<Entity> entities = EditorActivity.level.getEntities();
		for (int i = entities.size() - 1; i >= 0; --i) {
			Entity e = entities.get(i);
			if (e.getEntityType() == type) {
				entities.remove(i);
				removedCount++;
			}
		}
		return removedCount;
	}

	public void setAllAnimalsAge(EntityType type, int ticks) {
		List<Entity> entities = EditorActivity.level.getEntities();
		for (Entity e: entities) {
			if (e.getEntityType() == type) {
				((Animal) e).setAge(ticks);
			}
		}
	}

	public void replaceEntities(EntityType type, EntityType toType) {
		List<Entity> entities = EditorActivity.level.getEntities();
		Class<? extends Entity> toEntityClass = toType.getEntityClass();
		int toEntityId = toType.getId();
		for (int i = 0; i < entities.size(); ++i) {
			Entity e = entities.get(i);
			if (e.getEntityType() == type) {
				Entity newE = null;
				try {
					newE = toEntityClass.newInstance();
				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}
				newE.setEntityTypeId(toEntityId);
				newE.setLocation(e.getLocation());
				newE.setOnGround(e.isOnGround());
				newE.setVelocity(e.getVelocity());
				newE.setPitch(e.getPitch());
				newE.setYaw(e.getYaw());
				newE.setFallDistance(e.getFallDistance());
				entities.set(i, newE);
			}
		}
	}

	public void dyeAllSheep(byte colour) {
		List<Entity> entities = EditorActivity.level.getEntities();
		for (Entity e: entities) {
			if (e instanceof Sheep) {
				((Sheep) e).setColor(colour);
			}
		}
	}

	public void setAllBreeding(EntityType type, int breedTicks) {
		List<Entity> entities = EditorActivity.level.getEntities();
		for (Entity e: entities) {
			if (e.getEntityType() == type) {
				//((Animal) e).setInLove(breedTicks);
			}
		}
	}

	public void spawnOnSigns() {
		List<Entity> entities = EditorActivity.level.getEntities();
		List<TileEntity> tileEntities = EditorActivity.level.getTileEntities();
		String spawnedText = "Spawned!";
		for (TileEntity t: tileEntities) {
			if (t instanceof SignTileEntity) {
				SignTileEntity sign = (SignTileEntity) t;
				if (sign.getLine(2).equals(spawnedText)) {
					continue;
				}
				int count = 1;
				try {
					count = Integer.parseInt(sign.getLine(1));
				} catch (Exception e) {
					continue;
				}
			}
		}
	}

	public static class LoadEntitiesTask implements Runnable {

		private final Activity activity;

		private final EntityDataLoadListener listener;

		public LoadEntitiesTask(Activity activity, EntityDataLoadListener listener) {
			this.activity = activity;
			this.listener = listener;
		}

		public void run() {
			File entitiesFile = new File(EditorActivity.worldFolder, "entities.dat");
			try {
				final EntityDataConverter.EntityData entitiesList = EntityDataConverter.read(entitiesFile);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						listener.onEntitiesLoaded(entitiesList);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				activity.runOnUiThread(new Runnable() {
					public void run() {
						listener.onEntitiesLoaded(new EntityDataConverter.EntityData(
							new ArrayList<Entity>(), new ArrayList<TileEntity>()));
					}
				});
			}
		}
	}

	public static void save(final Activity context) {
		new Thread(new Runnable() {
			public void run() {
				try {
					EntityDataConverter.write(EditorActivity.level.getEntities(), EditorActivity.level.getTileEntities(), 
						new File(EditorActivity.worldFolder, "entities.dat"));
					if (context != null) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (context != null) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(context, R.string.savefailed, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
		}).start();
	}
}
