package net.zhuoweizhang.pocketinveditor;

import java.util.EnumMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;

import net.zhuoweizhang.pocketinveditor.entity.EntityType;

public class EntityTypeLocalization {

	public static Map<EntityType, Integer> namesMap = new EnumMap<EntityType, Integer>(EntityType.class);

	private EntityTypeLocalization() {}

	static {
		namesMap.put(EntityType.CHICKEN, R.string.entity_chicken);
		namesMap.put(EntityType.COW, R.string.entity_cow);
		namesMap.put(EntityType.PIG, R.string.entity_pig);
		namesMap.put(EntityType.SHEEP, R.string.entity_sheep);
		namesMap.put(EntityType.ZOMBIE, R.string.entity_zombie);
		namesMap.put(EntityType.CREEPER, R.string.entity_creeper);
		namesMap.put(EntityType.SKELETON, R.string.entity_skeleton);
		namesMap.put(EntityType.SPIDER, R.string.entity_spider);
		namesMap.put(EntityType.ITEM, R.string.entity_item);
		namesMap.put(EntityType.PRIMED_TNT, R.string.entity_primedtnt);
		namesMap.put(EntityType.ARROW, R.string.entity_arrow);
		namesMap.put(EntityType.SNOWBALL, R.string.entity_snowball);
		namesMap.put(EntityType.EGG, R.string.entity_thrownegg);
		namesMap.put(EntityType.UNKNOWN, R.string.entity_unknown);
	}

	public static EntityType lookupFromString(CharSequence name, Context ctx) {
		Resources res = ctx.getResources();
		for (Map.Entry<EntityType, Integer> entry: namesMap.entrySet()) {
			EntityType type = entry.getKey();
			int stringId = entry.getValue();
			CharSequence localizedName = res.getText(stringId);
			if (localizedName.equals(name)) {
				return type;
			}
		}
		return EntityType.UNKNOWN;
	}
}
