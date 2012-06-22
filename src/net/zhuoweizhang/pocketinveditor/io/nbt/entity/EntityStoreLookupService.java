package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.entity.Entity;

public class EntityStoreLookupService {

	public static Map<Integer, EntityStore> idMap = new HashMap<Integer, EntityStore>();

	public static EntityStore<Entity> defaultStore = new EntityStore<Entity>();
}
