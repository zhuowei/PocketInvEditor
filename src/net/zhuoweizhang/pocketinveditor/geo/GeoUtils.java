package net.zhuoweizhang.pocketinveditor.geo;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.material.MaterialKey;
import net.zhuoweizhang.pocketinveditor.material.MaterialCount;

public final class GeoUtils {

	public static Map<MaterialKey, MaterialCount> countBlocks(AreaBlockAccess mgr, CuboidRegion region) {
		Map<MaterialKey, MaterialCount> counts = new HashMap<MaterialKey, MaterialCount>();
		for (int x = region.x; x < region.x + region.width; ++x) {
			for (int z = region.z; z < region.z + region.length; ++z) {
				for (int y = region.y; y < region.y + region.height; ++y) {
					MaterialKey key = new MaterialKey((short) mgr.getBlockTypeId(x, y, z), (short) mgr.getBlockData(x, y, z));
					MaterialCount oldCount = counts.get(key);
					if (oldCount == null) {
						counts.put(key, new MaterialCount(key, 1));
					} else {
						oldCount.count++;
					}
				}
			}
		}
		return counts;
	}

	public static int replaceBlocks(AreaBlockAccess mgr, CuboidRegion region, MaterialKey from, MaterialKey to) {
		int replacedCount = 0;
		for (int x = region.x; x < region.x + region.width; ++x) {
			for (int z = region.z; z < region.z + region.length; ++z) {
				for (int y = region.y; y < region.y + region.height; ++y) {
					int type = mgr.getBlockTypeId(x, y, z);
					int data = mgr.getBlockData(x, y, z);
					if (type == from.typeId && (data == from.damage || from.damage == -1)) {
						mgr.setBlockTypeId(x, y, z, to.typeId);
						mgr.setBlockData(x, y, z, to.damage);
						replacedCount++;
					}
				}
			}
		}
		return replacedCount;
	}

	public static void setBlocks(AreaBlockAccess mgr, CuboidRegion region, MaterialKey to) {
		int replacedCount = 0;
		for (int x = region.x; x < region.x + region.width; ++x) {
			for (int z = region.z; z < region.z + region.length; ++z) {
				for (int y = region.y; y < region.y + region.height; ++y) {
					mgr.setBlockTypeId(x, y, z, to.typeId);
					mgr.setBlockData(x, y, z, to.damage);
				}
			}
		}
	}
}
