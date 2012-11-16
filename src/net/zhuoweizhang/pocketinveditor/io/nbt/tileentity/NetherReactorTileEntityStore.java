package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.spout.nbt.*;

import net.zhuoweizhang.pocketinveditor.tileentity.NetherReactorTileEntity;
import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;


public class NetherReactorTileEntityStore<T extends NetherReactorTileEntity> extends TileEntityStore<T> {

	@Override
	@SuppressWarnings("unchecked")
	public void loadTag(T entity, Tag tag) {
		if (tag.getName().equals("HasFinished")) {
			entity.setFinished(((ByteTag) tag).getValue() != 0);
		} else if (tag.getName().equals("IsInitialized")) {
			entity.setInitialized(((ByteTag) tag).getValue() != 0);
		} else if (tag.getName().equals("Progress")) {
			entity.setProgress(((ShortTag) tag).getValue());
		} else {
			super.loadTag(entity, tag);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tag> save(T entity) {
		List<Tag> tags = super.save(entity);
		tags.add(new ByteTag("HasFinished", entity.hasFinished()? (byte) 1 : (byte) 0));
		tags.add(new ByteTag("IsInitialized", entity.isInitialized()? (byte) 1 : (byte) 0));
		tags.add(new ShortTag("Progress", entity.getProgress()));
		return tags;
	}
}
