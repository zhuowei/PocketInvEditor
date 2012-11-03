package net.zhuoweizhang.pocketinveditor.geo;

import net.zhuoweizhang.pocketinveditor.util.Vector;

public class Schematic implements AreaBlockAccess, SizeLimitedArea {


	protected int width, height, length;

	protected byte[] blocks, metaData;

	public Schematic(Vector size, byte[] blocks, byte[] data) {
		this.width = size.getBlockX();
		this.height = size.getBlockY();
		this.length = size.getBlockZ();
		this.blocks = blocks;
		this.metaData = data;
	}

	public Schematic(Vector size) {
		this(size, new byte[size.getBlockX() * size.getBlockY() * size.getBlockZ()], new byte[size.getBlockX() * size.getBlockY() * size.getBlockZ()]);
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

	public void place(AreaBlockAccess world, Vector startPoint) {
		int beginX = startPoint.getBlockX();
		int beginY = startPoint.getBlockY();
		int beginZ = startPoint.getBlockZ();

		for (int x = 0; x < width; ++x) {
			for (int z = 0; z < length; ++z) {
				for (int y = 0; y < height; ++y) {
					int blockId = getBlockTypeId(x, y, z);
					int data = getBlockData(x, y, z);
					world.setBlockTypeId(beginX + x, beginY + y, beginZ + z, blockId);
					world.setBlockData(beginX + x, beginY + y, beginZ + z, data);
				}
			}
		}
	}
}
