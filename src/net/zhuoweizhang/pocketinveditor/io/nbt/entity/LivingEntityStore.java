package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Entity;
import net.zhuoweizhang.pocketinveditor.entity.LivingEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;

public class LivingEntityStore<T extends LivingEntity> extends EntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("AttackTime")) {
			entity.setAttackTime(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("DeathTime")) {
			entity.setDeathTime(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("Health")) {
			entity.setHealth(((ShortTag) tag).getValue());
		} else if (tag.getName().equals("HurtTime")) {
			entity.setHurtTime(((ShortTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}
}
