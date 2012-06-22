package net.zhuoweizhang.pocketinveditor.entity;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {
	CHICKEN(10, Chicken.class),
	COW(11, Cow.class),
	PIG(12, Pig.class),
	SHEEP(13, Sheep.class),
	ZOMBIE(32, Zombie.class),
	ITEM(64, Item.class),
	UNKNOWN(-1, null);

	private static Map<Integer, EntityType> idMap = new HashMap<Integer, EntityType>();

	private int id;

	private Class<? extends Entity> entityClass;

	EntityType(int id, Class<? extends Entity> entityClass) {
		this.id = id;
		this.entityClass = entityClass;
	}

	public int getId() {
		return id;
	}

	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}

	public static EntityType getById(int id) {
		return idMap.get(id);
	}

	static {
		for (EntityType type : EntityType.values()) {
			idMap.put(type.getId(), type);
		}
	}
}
