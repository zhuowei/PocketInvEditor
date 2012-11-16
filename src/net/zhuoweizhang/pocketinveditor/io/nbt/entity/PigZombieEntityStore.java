package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.PigZombie;

public class PigZombieEntityStore<T extends PigZombie> extends LivingEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Anger")) {
			entity.setAnger(((ShortTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ShortTag("Anger", entity.getAnger()));
		return tags;
	}
}
