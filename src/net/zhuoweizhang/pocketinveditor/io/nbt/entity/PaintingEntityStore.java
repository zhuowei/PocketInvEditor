package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Painting;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class PaintingEntityStore<T extends Painting> extends EntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		String name = tag.getName();
		if (name.equals("Dir")) {
			entity.setDirection(((ByteTag) tag).getValue());
		} else if (name.equals("Motive")) {
			entity.setArt(((StringTag) tag).getValue());
		} else if (name.equals("TileX")) {
			entity.getBlockCoordinates().setX(((IntTag) tag).getValue());
		} else if (name.equals("TileY")) {
			entity.getBlockCoordinates().setY(((IntTag) tag).getValue());
		} else if (name.equals("TileZ")) {
			entity.getBlockCoordinates().setZ(((IntTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ByteTag("Dir", entity.getDirection()));
		tags.add(new StringTag("Motive", entity.getArt()));
		Vector3f bc = entity.getBlockCoordinates();
		tags.add(new IntTag("TileX", bc.getBlockX()));
		tags.add(new IntTag("TileY", bc.getBlockY()));
		tags.add(new IntTag("TileZ", bc.getBlockZ()));
		return tags;
	}
}

