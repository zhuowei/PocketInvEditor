package net.zhuoweizhang.pocketinveditor.geo;

public class Chunk {

	public final int x, y;
	public byte[] blocks, blockLight, skyLight, metaData;

	/** width on the X axis */
	public static final int WIDTH = 16;
	/** height on the Y axis */
	public static final int HEIGHT = 128;
	/** length on the Z axis */
	public static final int LENGTH = 16;

	public Chunk(int x, int y) {
		this.x = x;
		this.y = y;
		this.blocks = new byte[WIDTH * HEIGHT * LENGTH];
		this.metaData = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];
		this.blockLight = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];
		this.skyLight = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];

	}

	public void loadFromByteArray(byte[] rawData) {
		int offset = 0;
		System.arraycopy(rawData, 0, blocks, 0, blocks.length);
		offset += blocks.length;
		System.arraycopy(rawData, offset, metaData, 0, metaData.length);
		offset += metaData.length;
		System.arraycopy(rawData, offset, skyLight, 0, skyLight.length);
		offset += skyLight.length;
		System.arraycopy(rawData, offset, blockLight, 0, blockLight.length);
	}

	/** Calculates and returns the number of diamond ore blocks in this chunk */

	public int countDiamonds() {
		int count = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == (byte) 56) {
				count++;
			}
		}
		return count;
	}

}
