package net.zhuoweizhang.pocketinveditor.io.nbt.entity;

import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.entity.Projectile;

public class ProjectileEntityStore<T extends Projectile> extends EntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		String name = tag.getName();
		if (name.equals("inGround")) {
			entity.setInGround(((ByteTag) tag).getValue() != 0);
		} else if (name.equals("inTile")) {
			entity.setInBlock(((ByteTag) tag).getValue());
		} else if (name.equals("shake")) {
			entity.setShake(((ByteTag) tag).getValue());
		} else if (name.equals("xTile")) {
			entity.setXTile(((ShortTag) tag).getValue());
		} else if (name.equals("yTile")) {
			entity.setYTile(((ShortTag) tag).getValue());
		} else if (name.equals("zTile")) {
			entity.setZTile(((ShortTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ByteTag("inGround", entity.isInGround()? (byte) 1: (byte) 0));
		tags.add(new ByteTag("inTile", entity.getInBlock()));
		tags.add(new ByteTag("shake", entity.getShake()));
		tags.add(new ShortTag("xTile", entity.getXTile()));
		tags.add(new ShortTag("yTile", entity.getYTile()));
		tags.add(new ShortTag("zTile", entity.getZTile()));
		return tags;
	}
}

