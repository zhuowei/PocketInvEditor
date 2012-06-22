package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Item;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;

public class ItemEntityStore<T extends Item> extends EntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Age")) {
			entity.setAge(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("Health")) {
			entity.setHealth(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("Item")) {
			entity.setItemStack(NBTConverter.readItemStack((CompoundTag) tag));
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ShortTag("Age", entity.getAge()));
		tags.add(new ShortTag("Health", entity.getHealth()));
		tags.add(NBTConverter.writeItemStack(entity.getItemStack(), "Item"));
		return tags;
	}
}
