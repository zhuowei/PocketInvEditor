package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.entity.*;

public class EntityStoreLookupService {

	public static Map<Integer, EntityStore> idMap = new HashMap<Integer, EntityStore>();

	public static EntityStore<Entity> defaultStore = new EntityStore<Entity>();

	public static void addStore(int id, EntityStore store) {
		idMap.put(id, store);
	}

	static {
		addStore(10, new AnimalEntityStore<Chicken>());
		addStore(11, new AnimalEntityStore<Cow>());
		addStore(12, new AnimalEntityStore<Pig>());
		addStore(13, new SheepEntityStore<Sheep>());
		addStore(32, new LivingEntityStore<Zombie>());
		addStore(64, new ItemEntityStore<Item>());
	}
}
