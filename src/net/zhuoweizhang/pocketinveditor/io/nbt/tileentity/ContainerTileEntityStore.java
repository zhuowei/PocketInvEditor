package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.tileentity.ContainerTileEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class ContainerTileEntityStore<T extends ContainerTileEntity> extends TileEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("Items")) {
			entity.setItems(NBTConverter.readInventory((ListTag<CompoundTag>) tag));
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(NBTConverter.writeInventory(entity.getItems(), "Items"));
		return tags;
	}
}
