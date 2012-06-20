package net.zhuoweizhang.pocketinveditor.entity;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {
	CHICKEN(10),
	COW(11),
	PIG(12),
	SHEEP(13),
	ZOMBIE(32),
	ITEM(64),
	UNKNOWN(-1);

	private static Map<Integer, EntityType> idMap = new HashMap<Integer, EntityType>();

	private int id;

	EntityType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
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
