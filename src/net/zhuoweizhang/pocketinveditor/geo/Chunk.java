package net.zhuoweizhang.pocketinveditor.geo;

public class Chunk {

	public final int x, z;
	public byte[] blocks, blockLight, skyLight, metaData, dirtyTable;

	/** width on the X axis */
	public static final int WIDTH = 16;
	/** height on the Y axis */
	public static final int HEIGHT = 128;
	/** length on the Z axis */
	public static final int LENGTH = 16;

	public static final class Key {
		private final int x, z;
		public Key(int x, int z) {
			this.x = x;
			this.z = z;
		}

		public int getX() {
			return x;
		}

		public int getZ() {
			return z;
		}

		public boolean equals(Object other) {
			if (this == other) return true;
			if (!(other instanceof Key)) return false;
			Key ok = (Key) other;
			return ok.getX() == this.x && ok.getZ() == this.z;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + z;
			return result;
		}
	}

	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
		this.blocks = new byte[WIDTH * HEIGHT * LENGTH];
		this.metaData = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];
		this.blockLight = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];
		this.skyLight = new byte[(WIDTH * HEIGHT * LENGTH) >> 1];
		this.dirtyTable = new byte[WIDTH * LENGTH];

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
		offset += blockLight.length;
		System.arraycopy(rawData, offset, dirtyTable, 0, dirtyTable.length);
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

	public boolean dirtyTableIsReallyGross() {
		for (int i = 0; i < dirtyTable.length; i++) {
			if (dirtyTable[i] != 0) return true;
		}
		return false;
	}

	public int getBlockTypeId(int x, int y, int z) {
		return blocks[getOffset(x, y, z)];
	}

	public int getBlockData(int x, int y, int z) {
		int offset = getOffset(x, y, z);
		int dualData = metaData[offset >> 1];
		return offset % 1 == 0 ? dualData >> 4 : dualData & 0xf;
	}

	private static int getOffset(int x, int y, int z) {
		return (x * HEIGHT * LENGTH) + (z * HEIGHT) + y;
	}

}
