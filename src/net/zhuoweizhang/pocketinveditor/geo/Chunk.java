package net.zhuoweizhang.pocketinveditor.geo;

public class Chunk {

	public final int x, z;
	public byte[] blocks, blockLight, skyLight, metaData, dirtyTable;

	public boolean needsSaving = false;
	private boolean hasFilledDirtyTable = false;

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

	public byte[] saveToByteArray() {
		byte[] retval = new byte[blocks.length + metaData.length + skyLight.length + blockLight.length + dirtyTable.length + 3];
		int offset = 0;
		System.arraycopy(blocks, 0, retval, 0, blocks.length);
		offset += blocks.length;
		System.arraycopy(metaData, 0, retval, offset, metaData.length);
		offset += metaData.length;
		System.arraycopy(skyLight, 0, retval, offset, skyLight.length);
		offset += skyLight.length;
		System.arraycopy(blockLight, 0, retval, offset, blockLight.length);
		offset += blockLight.length;
		System.arraycopy(dirtyTable, 0, retval, offset, dirtyTable.length);
		return retval;
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
		if (x >= WIDTH || y >= HEIGHT || z >= LENGTH || x < 0 || y < 0 || z < 0) {
			return 0;
		}
		int typeId = blocks[getOffset(x, y, z)];
		if (typeId < 0) typeId = 256 + typeId;
		return typeId;
	}

	public int getBlockData(int x, int y, int z) {
		if (x >= WIDTH || y >= HEIGHT || z >= LENGTH || x < 0 || y < 0 || z < 0) {
			return 0;
		}
		int offset = getOffset(x, y, z);
		int dualData = metaData[offset >> 1];
		return offset % 2 == 1 ? (dualData >> 4) & 0xf : dualData & 0xf;
	}

	/** Sets a block type, and also set the corresponding dirty table entry and set the saving flag. */
	public void setBlockTypeId(int x, int y, int z, int type) {
		if (x >= WIDTH || y >= HEIGHT || z >= LENGTH || x < 0 || y < 0 || z < 0) {
			return;
		}
		setBlockTypeIdNoDirty(x, y, z, type);
		setDirtyTable(x, y, z);
		setNeedsSaving(true);
	}

	public void setBlockTypeIdNoDirty(int x, int y, int z, int type) {
		//System.out.println(this.x + ":" + this.z + " setBlockTypeIdNoDirty: " + x + ":" + y + ":" + z);
		blocks[getOffset(x, y, z)] = (byte) type;
	}

	public void setBlockData(int x, int y, int z, int newData) {
		if (x >= WIDTH || y >= HEIGHT || z >= LENGTH || x < 0 || y < 0 || z < 0) {
			return;
		}
		setBlockDataNoDirty(x, y, z, newData);
		setDirtyTable(x, y, z);
		setNeedsSaving(true);
	}

	public void setBlockDataNoDirty(int x, int y, int z, int newData) {
		int offset = getOffset(x, y, z);
		byte oldData = metaData[offset >> 1];
		if (offset % 2 == 1) {
			metaData[offset >> 1] = (byte) ((newData << 4) | (oldData & 0xf));
		} else {
			metaData[offset >> 1] = (byte) ((oldData & 0xf0) | (newData & 0xf));
		}
	}

	public void setDirtyTable(int x, int y, int z) {
		if (hasFilledDirtyTable) return;
		//System.err.println("BRUTE FORCE AND IGNORANCE BEGIN! setDirtytable : fixme");
		for (int i = 0; i < 256; i++) {
			dirtyTable[i] = (byte) 0xff;
		}
		hasFilledDirtyTable = true;
	}

	/** Does this chunk need saving next time the ChunkManager issues a save? */
	public void setNeedsSaving(boolean save) {
		this.needsSaving = save;
	}

	private static int getOffset(int x, int y, int z) {
		return (x * HEIGHT * LENGTH) + (z * HEIGHT) + y;
	}

}
