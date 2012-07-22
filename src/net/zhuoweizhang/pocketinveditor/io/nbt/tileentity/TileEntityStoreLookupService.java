package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.tileentity.*;

public class TileEntityStoreLookupService {

	public static Map<String, TileEntityStore> idMap = new HashMap<String, TileEntityStore>();

	public static TileEntityStore<TileEntity> defaultStore = new TileEntityStore<TileEntity>();

	public static void addStore(String id, TileEntityStore store) {
		idMap.put(id, store);
	}

	static {
		addStore("Furnace", new FurnaceTileEntityStore<FurnaceTileEntity>());
		addStore("Chest", new ContainerTileEntityStore<ContainerTileEntity>());
	}
}
