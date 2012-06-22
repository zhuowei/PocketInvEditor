package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Sheep;

public class SheepEntityStore<T extends Sheep> extends AnimalEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Color")) {
			entity.setColor(((ByteTag) tag).getValue());
		} else if (tag.getName().equals("Sheared")) {
			entity.setSheared(((ByteTag) tag).getValue() > 0);
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ByteTag("Color", entity.getColor()));
		tags.add(new ByteTag("Sheared", entity.isSheared() ? (byte) 1 : (byte) 0));
		return tags;
	}
}
