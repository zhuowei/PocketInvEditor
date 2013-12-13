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
		addStore(33, new LivingEntityStore<Creeper>());
		addStore(34, new LivingEntityStore<Skeleton>());
		addStore(35, new LivingEntityStore<Spider>());
		addStore(36, new PigZombieEntityStore<PigZombie>());
		addStore(64, new ItemEntityStore<Item>());
		addStore(65, new TNTPrimedEntityStore<TNTPrimed>());
		addStore(66, new FallingBlockEntityStore<FallingBlock>());
		addStore(80, new ArrowEntityStore<Arrow>());
		addStore(81, new ProjectileEntityStore<Snowball>());
		addStore(82, new ProjectileEntityStore<Egg>());
		addStore(83, new PaintingEntityStore<Painting>());
		addStore(84, new MinecartEntityStore<Minecart>());
	}
}
