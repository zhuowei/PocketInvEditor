package net.zhuoweizhang.pocketinveditor.entity;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {
	CHICKEN(10, Chicken.class),
	COW(11, Cow.class),
	PIG(12, Pig.class),
	SHEEP(13, Sheep.class),
	ZOMBIE(32, Zombie.class),
	CREEPER(33, Creeper.class),
	SKELETON(34, Skeleton.class),
	SPIDER(35, Spider.class),
	PIG_ZOMBIE(36, PigZombie.class),
	ITEM(64, Item.class),
	PRIMED_TNT(65, TNTPrimed.class),
	FALLING_BLOCK(66, FallingBlock.class),
	ARROW(80, Arrow.class),
	SNOWBALL(81, Snowball.class),
	EGG(82, Egg.class),
	PAINTING(83, Painting.class),
	MINECART(84, Minecart.class),
	UNKNOWN(-1, null),
	PLAYER(-2, Player.class);

	private static Map<Integer, EntityType> idMap = new HashMap<Integer, EntityType>();

	private static Map<Class<? extends Entity>, EntityType> classMap = new HashMap<Class<? extends Entity>, EntityType>();

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
		EntityType type = idMap.get(id);
		if (type == null) return EntityType.UNKNOWN;
		return type;
	}

	public static EntityType getByClass(Class<? extends Entity> clazz) {
		EntityType type = classMap.get(clazz);
		if (type == null) return EntityType.UNKNOWN;
		return type;
	}


	static {
		for (EntityType type : EntityType.values()) {
			idMap.put(type.getId(), type);
			if (type.getEntityClass() != null) {
				classMap.put(type.getEntityClass(), type);
			}
		}
	}
}
