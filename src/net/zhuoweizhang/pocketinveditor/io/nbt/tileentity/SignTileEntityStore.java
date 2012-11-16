package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.tileentity.SignTileEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class SignTileEntityStore<T extends SignTileEntity> extends TileEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Text1")) {
			entity.setLine(0, ((StringTag) tag).getValue());
		} else if (tag.getName().equals("Text2")) {
			entity.setLine(1, ((StringTag) tag).getValue());
		} else if (tag.getName().equals("Text3")) {
			entity.setLine(2, ((StringTag) tag).getValue());
		} else if (tag.getName().equals("Text4")) {
			entity.setLine(3, ((StringTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		String[] lines = entity.getLines();
		for (int i = 0; i < lines.length; i++) {
			tags.add(new StringTag("Text" + (i + 1), lines[i]));
		}
		return tags;
	}
}
