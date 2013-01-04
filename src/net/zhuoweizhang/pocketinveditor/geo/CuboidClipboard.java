package net.zhuoweizhang.pocketinveditor.geo;

import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class CuboidClipboard implements AreaBlockAccess, SizeLimitedArea {

	public static final int AIR = 0;


	protected int width, height, length;

	protected byte[] blocks, metaData;

	public CuboidClipboard(Vector3f size, byte[] blocks, byte[] data) {
		this.width = (int) size.getX();
		this.height = (int) size.getY();
		this.length = (int) size.getZ();
		this.blocks = blocks;
		this.metaData = data;
	}

	public CuboidClipboard(Vector3f size) {
		this(size, new byte[(int) (size.getX() * size.getY() * size.getZ())], new byte[(int) (size.getX() * size.getY() * size.getZ())]);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getLength() {
		return length;
	}

	public int getBlockTypeId(int x, int y, int z) {
		return blocks[getOffset(x, y, z)];
	}

	public void setBlockTypeId(int x, int y, int z, int type) {
		blocks[getOffset(x, y, z)] = (byte) type;
	}

	public int getBlockData(int x, int y, int z) {
		return metaData[getOffset(x, y, z)];
	}

	public void setBlockData(int x, int y, int z, int type) {
		metaData[getOffset(x, y, z)] = (byte) type;
	}

	public int getOffset(int x, int y, int z) {
		return (y * width * length) + (z * width) + x;
	}

	public void place(AreaBlockAccess world, Vector3f startPoint, boolean noAir) {
		int beginX = (int) startPoint.getX();
		int beginY = (int) startPoint.getY();
		int beginZ = (int) startPoint.getZ();

		for (int x = 0; x < width; ++x) {
			for (int z = 0; z < length; ++z) {
				for (int y = 0; y < height; ++y) {
					int blockId = getBlockTypeId(x, y, z);
					if (noAir && blockId == AIR) continue;
					int data = getBlockData(x, y, z);
					world.setBlockTypeId(beginX + x, beginY + y, beginZ + z, blockId);
					world.setBlockData(beginX + x, beginY + y, beginZ + z, data);
				}
			}
		}
	}

	public void copy(AreaBlockAccess world, Vector3f startPoint) {
		int beginX = startPoint.getBlockX();
		int beginY = startPoint.getBlockY();
		int beginZ = startPoint.getBlockZ();
		for (int x = 0; x < width; ++x) {
			for (int z = 0; z < length; ++z) {
				for (int y = 0; y < height; ++y) {
					int blockId = world.getBlockTypeId(beginX + x, beginY + y, beginZ + z);
					int data = world.getBlockData(beginX + x, beginY + y, beginZ + z);
					this.setBlockTypeId(x, y, z, blockId);
					this.setBlockData(x, y, z, data);
				}
			}
		}
	}
}
