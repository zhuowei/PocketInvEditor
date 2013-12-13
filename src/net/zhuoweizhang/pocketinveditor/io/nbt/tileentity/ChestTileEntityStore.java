package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.tileentity.ChestTileEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class ChestTileEntityStore<T extends ChestTileEntity> extends ContainerTileEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		String name = tag.getName();
		if (name.equals("pairx")) {
			entity.setPairX(((IntTag) tag).getValue());
		} else if (name.equals("pairz")) {
			entity.setPairZ(((IntTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		if (entity.getPairX() != -0xffff) {
			tags.add(new IntTag("pairx", entity.getPairX()));
			tags.add(new IntTag("pairz", entity.getPairZ()));
		}
		return tags;
	}
}
