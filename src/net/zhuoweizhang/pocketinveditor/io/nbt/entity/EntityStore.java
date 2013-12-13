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
		String name = tag.getName();
		if (name.equals("Pos")) {
			entity.setLocation(NBTConverter.readVector((ListTag<FloatTag>) tag));
		} else if (name.equals("Motion")) {
			entity.setVelocity(NBTConverter.readVector((ListTag<FloatTag>) tag));
		} else if (name.equals("Air")) {
			entity.setAirTicks(((ShortTag) tag).getValue());
		} else if (name.equals("Fire")) {
			entity.setFireTicks(((ShortTag) tag).getValue());
		} else if (name.equals("FallDistance")) {
			entity.setFallDistance(((FloatTag) tag).getValue());
		} else if (name.equals("Riding")) {
			entity.setRiding(NBTConverter.readSingleEntity((CompoundTag) tag));
		} else if (name.equals("Rotation")) {
			List<FloatTag> rotationTags = ((ListTag<FloatTag>) tag).getValue();
			entity.setYaw(rotationTags.get(0).getValue());
			entity.setPitch(rotationTags.get(1).getValue());
		} else if (name.equals("OnGround")) {
			entity.setOnGround(((ByteTag) tag).getValue() > 0 ? true : false);
		} else if (name.equals("id")) { //Uncomment for debug output when reading unknown tags
			//nothing - handled.
		} else {
			System.err.println("Unknown tag " + name + " for entity " + entity.getClass().getSimpleName() + " : " + tag);
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
		if (entity.getRiding() != null) {
			tags.add(NBTConverter.writeEntity(entity.getRiding(), "Riding"));
		}
		List<FloatTag> rotationTags = new ArrayList<FloatTag>(2);
		rotationTags.add(new FloatTag("", entity.getYaw()));
		rotationTags.add(new FloatTag("", entity.getPitch()));
		tags.add(new ListTag<FloatTag>("Rotation", FloatTag.class, rotationTags));
		tags.add(new ByteTag("OnGround", entity.isOnGround() ? (byte) 1: (byte) 0));
		tags.add(new IntTag("id", entity.getEntityTypeId()));

		return tags;
	}
}
