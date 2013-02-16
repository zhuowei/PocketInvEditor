package net.zhuoweizhang.pocketinveditor.geo;

import net.zhuoweizhang.pocketinveditor.util.Vector3f;

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

	public CuboidRegion(Vector3f pos, Vector3f size) {
		this((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), (int) size.getX(), (int) size.getY(), (int) size.getZ());
	}

	public CuboidRegion(CuboidRegion other) {
		this(other.x, other.y, other.z, other.width, other.height, other.length);
	}

	/** Checks if the other rectangle is fully contained in this rectangle. */
	public boolean contains(CuboidRegion other) {
		return other.x >= this.x && other.y >= this.y && other.z >= this.z &&
			other.x + other.width <= this.x + this.width &&
			other.y + other.height <= this.y + this.height &&
			other.z + other.length <= this.z + this.length;
	}

	/** Create a CuboidRegion from the intersection of two regions i.e. the part that they both share.
	 *  @returns the largest CuboidRegion that can be contained in both regions; if no intersection, returns an invalid CuboidRegion
	 */
	public CuboidRegion createIntersection(CuboidRegion other) {
		int iX = this.x > other.x? this.x : other.x;
		int iY = this.y > other.y? this.y : other.y;
		int iZ = this.z > other.z? this.z : other.z;
		/* This second x */
		int tSX = this.x + this.width;
		int tSY = this.y + this.height;
		int tSZ = this.z + this.length;

		int oSX = other.x + other.width;
		int oSY = other.y + other.height;
		int oSZ = other.z + other.length;

		int iSX = tSX < oSX? tSX : oSX;
		int iSY = tSY < oSY? tSY : oSY;
		int iSZ = tSZ < oSZ? tSZ : oSZ;
		return new CuboidRegion(iX, iY, iZ, iSX - iX, iSY - iY, iSZ - iZ);
	}

	/** Are width, height, and depth positive? */
	public boolean isValid() {
		return width >= 0 && height >= 0 && length >= 0;
	}

	public Vector3f getSize() {
		return new Vector3f(width, height, length);
	}

	public Vector3f getPosition() {
		return new Vector3f(x, y, z);
	}

	public int getBlockCount() {
		return width * height * length;
	}

	/** Build a CuboidRegion from two points (inclusive). Calculates width, height, and length from the difference of the two vectors. */
	public static CuboidRegion fromPoints(Vector3f pos1, Vector3f pos2) {
		int minX, maxX, minY, maxY, minZ, maxZ;
		minX = (int) (pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX());
		maxX = (int) (pos1.getX() >= pos2.getX() ? pos1.getX() : pos2.getX());
		minY = (int) (pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY());
		maxY = (int) (pos1.getY() >= pos2.getY() ? pos1.getY() : pos2.getY());
		minZ = (int) (pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ());
		maxZ = (int) (pos1.getZ() >= pos2.getZ() ? pos1.getZ() : pos2.getZ());
		return new CuboidRegion(minX, minY, minZ, (maxX - minX) + 1, (maxY - minY) + 1, (maxZ - minZ) + 1);
	}
 
}

