package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.tileentity.FurnaceTileEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class FurnaceTileEntityStore<T extends FurnaceTileEntity> extends ContainerTileEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		String name = tag.getName();
		if (name.equals("BurnTime")) {
			entity.setBurnTime(((ShortTag) tag).getValue());
		} else if (name.equals("CookTime")) {
			entity.setCookTime(((ShortTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ShortTag("BurnTime", entity.getBurnTime()));
		tags.add(new ShortTag("CookTime", entity.getCookTime()));
		return tags;
	}
}
