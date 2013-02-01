package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.FallingBlock;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class FallingBlockEntityStore<T extends FallingBlock> extends EntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		String name = tag.getName();
		if (name.equals("Tile")) {
			entity.setBlockId(((ByteTag) tag).getValue() & 0xff);
		} else if (name.equals("Data")) {
			entity.setBlockData(((ByteTag) tag).getValue());
		} else if (name.equals("Time")) {
			entity.setTime(((ByteTag) tag).getValue() & 0xff);
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ByteTag("Data", entity.getBlockData()));
		tags.add(new ByteTag("Tile", (byte) entity.getBlockId()));
		tags.add(new ByteTag("Time", (byte) entity.getTime()));
		return tags;
	}
}

