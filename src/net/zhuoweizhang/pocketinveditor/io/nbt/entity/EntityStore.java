package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Entity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class EntityStore<T extends Entity> {

	public void load(T entity, CompoundTag tag) {
		List<Tag> tags = tag.getValue();
		for (Tag t: tags) {
			loadTag(entity, t);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Pos")) {
			entity.setLocation(NBTConverter.readVector((ListTag<FloatTag>) tag));
		} else if (tag.getName().equals("Motion")) {
			entity.setVelocity(NBTConverter.readVector((ListTag<FloatTag>) tag));
		} else if (tag.getName().equals("Air")) {
			entity.setAirTicks(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("Fire")) {
			entity.setFireTicks(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("FallDistance")) {
			entity.setFallDistance(((FloatTag) tag).getValue());
		} else if (tag.getName().equals("Rotation")) {
			List<FloatTag> rotationTags = ((ListTag<FloatTag>) tag).getValue();
			entity.setYaw(rotationTags.get(0).getValue());
			entity.setPitch(rotationTags.get(1).getValue());
		} else if (tag.getName().equals("OnGround")) {
			entity.setOnGround(((ByteTag) tag).getValue() > 0 ? true : false);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = new ArrayList<Tag>();

		tags.add(new ShortTag("Air", entity.getAirTicks()));
		tags.add(new FloatTag("FallDistance", entity.getFallDistance()));
		tags.add(new ShortTag("Fire", entity.getFireTicks()));
		tags.add(NBTConverter.writeVector(entity.getVelocity(), "Motion"));
		tags.add(NBTConverter.writeVector(entity.getLocation(), "Pos"));
		List<FloatTag> rotationTags = new ArrayList<FloatTag>(2);
		rotationTags.add(new FloatTag("", entity.getYaw()));
		rotationTags.add(new FloatTag("", entity.getPitch()));
		tags.add(new ListTag<FloatTag>("Rotation", FloatTag.class, rotationTags));
		tags.add(new ByteTag("OnGround", entity.isOnGround() ? (byte) 1: (byte) 0));
		tags.add(new IntTag("id", entity.getEntityTypeId()));

		return tags;
	}
}
