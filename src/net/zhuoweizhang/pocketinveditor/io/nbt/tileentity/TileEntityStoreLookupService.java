package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.tileentity.*;

public class TileEntityStoreLookupService {

	public static Map<String, TileEntityStore> idMap = new HashMap<String, TileEntityStore>();

	public static Map<String, Class> classMap = new HashMap<String, Class>();

	public static TileEntityStore<TileEntity> defaultStore = new TileEntityStore<TileEntity>();

	/** Binds a TileEntityStore to a tile entity ID and register the preferred class for the store. */
	public static void addStore(String id, TileEntityStore store, Class clazz) {
		idMap.put(id, store);
		classMap.put(id, clazz);
	}

	public static TileEntity createTileEntityById(String id) {
		Class<TileEntity> clazz = (Class<TileEntity>) classMap.get(id);
		if (clazz == null) return new TileEntity();
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new TileEntity();
		}
	}

	static {
		addStore("Furnace", new FurnaceTileEntityStore<FurnaceTileEntity>(), FurnaceTileEntity.class);
		addStore("Chest", new ContainerTileEntityStore<ContainerTileEntity>(), ContainerTileEntity.class);
		addStore("NetherReactor", new NetherReactorTileEntityStore<NetherReactorTileEntity>(), NetherReactorTileEntity.class);
		addStore("Sign", new SignTileEntityStore<SignTileEntity>(), SignTileEntity.class);
	}
}
