package net.zhuoweizhang.pocketinveditor.io.nbt.tileentity;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.tileentity.*;

public class TileEntityStoreLookupService {

	public static Map<String, TileEntityStore> idMap = new HashMap<String, TileEntityStore>();

	public static Map<String, Class> classMap = new HashMap<String, Class>();

	public static TileEntityStore<TileEntity> defaultStore = new TileEntityStore<TileEntity>();

	/** Binds a TileEntityStore to a tile entity ID and register the preferred class for the store. 
	 *  All IDs are stored as uppercase.
	 */
	public static void addStore(String id, TileEntityStore store, Class clazz) {
		String realId = id.toUpperCase();
		idMap.put(realId, store);
		classMap.put(realId, clazz);
	}

	public static TileEntity createTileEntityById(String id) {
		Class<TileEntity> clazz = (Class<TileEntity>) classMap.get(id.toUpperCase());
		if (clazz == null) return new TileEntity();
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new TileEntity();
		}
	}

	public static TileEntityStore getStoreById(String id) {
		return idMap.get(id.toUpperCase());
	}

	static {
		addStore("Furnace", new FurnaceTileEntityStore<FurnaceTileEntity>(), FurnaceTileEntity.class);
		addStore("Chest", new ChestTileEntityStore<ChestTileEntity>(), ChestTileEntity.class);
		addStore("NetherReactor", new NetherReactorTileEntityStore<NetherReactorTileEntity>(), NetherReactorTileEntity.class);
		addStore("Sign", new SignTileEntityStore<SignTileEntity>(), SignTileEntity.class);
	}
}
