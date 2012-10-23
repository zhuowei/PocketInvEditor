package net.zhuoweizhang.pocketinveditor.geo;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public class CuboidRegion {

	public int x, y, z, width, height, length;

	/**
	 * @param width Width on the X axis
	 * @param height Height on the Y axis
	 * @param length Length on the Z axis
	 */
	public CuboidRegion(int x, int y, int z, int width, int height, int length) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.length = length;
	}

	/** Build a CuboidRegion from two points. Calculates width, height, and length from the difference of the two vectors. */
	public static CuboidRegion fromPoints(Vector pos1, Vector pos2) {
		int minX, maxX, minY, maxY, minZ, maxZ;
		minX = (int) (pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX());
		maxX = (int) (pos1.getX() >= pos2.getX() ? pos1.getX() : pos2.getX());
		minY = (int) (pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY());
		maxY = (int) (pos1.getY() >= pos2.getY() ? pos1.getY() : pos2.getY());
		minZ = (int) (pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ());
		maxZ = (int) (pos1.getZ() >= pos2.getZ() ? pos1.getZ() : pos2.getZ());
		return new CuboidRegion(minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ);
	}
}

