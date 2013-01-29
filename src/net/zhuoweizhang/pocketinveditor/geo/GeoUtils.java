package net.zhuoweizhang.pocketinveditor.geo;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.material.MaterialKey;
import net.zhuoweizhang.pocketinveditor.material.MaterialCount;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

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

	public static int makeSphere(AreaBlockAccess mgr, Vector3f center, int radius, MaterialKey to, boolean hollow) {
		return makeSphere(mgr, center.getBlockX(), center.getBlockY(), center.getBlockZ(), radius, to, hollow);
	}

	public static int makeSphere(AreaBlockAccess mgr, int centerX, int centerY, int centerZ, int radius, MaterialKey to, boolean hollow) {
		int replacedCount = 0;

		int radiusSquared = radius * radius;
		int hollowRadiusSquared = (radius - 1) * (radius - 1);
		
		for (int x = -radius; x < radius; ++x) {
			for (int z = -radius; z < radius; ++z) {
				for (int y = -radius; y < radius; ++y) {
					double distFromCenter = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
					if (distFromCenter <= radiusSquared && (!hollow || distFromCenter >= hollowRadiusSquared)) {
						mgr.setBlockTypeId(centerX + x, centerY + y, centerZ + z, to.typeId);
						mgr.setBlockData(centerX + x, centerY + y, centerZ + z, to.damage);
						++replacedCount;
					}
				}
			}
		}
		return replacedCount;

	}

	public static int makeDome(AreaBlockAccess mgr, Vector3f center, int radius, MaterialKey to, boolean hollow) {
		return makeDome(mgr, center.getBlockX(), center.getBlockY(), center.getBlockZ(), radius, to, hollow);
	}

	public static int makeDome(AreaBlockAccess mgr, int centerX, int centerY, int centerZ, int radius, MaterialKey to, boolean hollow) {
		int replacedCount = 0;

		int radiusSquared = radius * radius;
		int hollowRadiusSquared = (radius - 1) * (radius - 1);
		
		for (int x = -radius; x < radius; ++x) {
			for (int z = -radius; z < radius; ++z) {
				for (int y = 0; y < radius; ++y) {
					double distFromCenter = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
					if (distFromCenter <= radiusSquared && (!hollow || distFromCenter >= hollowRadiusSquared)) {
						mgr.setBlockTypeId(centerX + x, centerY + y, centerZ + z, to.typeId);
						mgr.setBlockData(centerX + x, centerY + y, centerZ + z, to.damage);
						++replacedCount;
					}
				}
			}
		}
		return replacedCount;

	}
}
