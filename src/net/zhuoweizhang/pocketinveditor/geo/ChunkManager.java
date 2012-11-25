package net.zhuoweizhang.pocketinveditor.geo;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.io.region.*;

public class ChunkManager implements AreaChunkAccess {

	public static final int WORLD_WIDTH = 256;
	public static final int WORLD_HEIGHT = 128;
	public static final int WORLD_LENGTH = 256;

	public static CuboidRegion worldRegion = new CuboidRegion(0, 0, 0, WORLD_WIDTH, WORLD_HEIGHT, WORLD_LENGTH);

	protected Map<Chunk.Key, Chunk> chunks = new HashMap<Chunk.Key, Chunk>();

	protected File chunkFile;

	protected RegionFile region;

	private Chunk.Key lastKey = null;
	private Chunk lastChunk = null;

	public ChunkManager(File chunkFile) {
		this.chunkFile = chunkFile;
		this.region = new RegionFile(chunkFile);
	}


	public Chunk getChunk(int x, int z) {
		if (lastKey != null && lastKey.getX() == x && lastKey.getZ() == z) return lastChunk;
		return getChunk(new Chunk.Key(x, z));
	}

	public Chunk getChunk(Chunk.Key key) {
		if (lastKey != null && lastKey.equals(key)) {
			return lastChunk;
		}
		Chunk chunk = chunks.get(key);
		if (chunk == null)
			chunk = loadChunk(key);
		lastKey = key;
		lastChunk = chunk;
		return chunk;
	}

	public Chunk loadChunk(Chunk.Key key) {
		//System.out.println("Loading chunk: " + key.getX() + ":" + key.getZ());
		Chunk chunk = new Chunk(key.getX(), key.getZ());
		byte[] data = region.getChunkData(key.getX(), key.getZ());
		if (data != null) {
			chunk.loadFromByteArray(data);
		} else {
			System.err.println("WTF:" + key.getX() + ":" + key.getZ());
		}
		chunks.put(key, chunk);
		return chunk;
	}

	public int getBlockTypeId(int x, int y, int z) {
		if (x >= WORLD_WIDTH || y >= WORLD_HEIGHT || z >= WORLD_LENGTH || x < 0 || y < 0 || z < 0) {
			return 0;
		}
		return getChunk(x >> 4, z >> 4).getBlockTypeId(x & 15, y, z & 15);
	}

	public int getBlockData(int x, int y, int z) {
		if (x >= WORLD_WIDTH || y >= WORLD_HEIGHT || z >= WORLD_LENGTH || x < 0 || y < 0 || z < 0) {
			return 0;
		}
		return getChunk(x >> 4, z >> 4).getBlockData(x & 15, y, z & 15);
	}

	public void setBlockTypeId(int x, int y, int z, int type) {
		if (x >= WORLD_WIDTH || y >= WORLD_HEIGHT || z >= WORLD_LENGTH || x < 0 || y < 0 || z < 0) {
			return;
		}
		//System.out.println("setBlockTypeId:" + x + ":" + y + ":" + z);
		getChunk(x >> 4, z >> 4).setBlockTypeId(x & 15, y, z & 15, type);
	}

	public void setBlockData(int x, int y, int z, int data) {
		if (x >= WORLD_WIDTH || y >= WORLD_HEIGHT || z >= WORLD_LENGTH || x < 0 || y < 0 || z < 0) {
			return;
		}
		getChunk(x >> 4, z >> 4).setBlockData(x & 15, y, z & 15, data);
	}

	/** Saves all chunks that needs saving. 
	 * @return The number of chunks saved */

	public int saveAll() {
		int savedCount = 0;
		for (Map.Entry<Chunk.Key, Chunk> entry: chunks.entrySet()) {
			Chunk.Key key = entry.getKey();
			Chunk value = entry.getValue();
			if (key.getX() != value.x || key.getZ() != value.z) {
				throw new AssertionError("WTF: key x = " + key.getX() + " z = " + key.getZ() + " chunk x=" + value.x + " chunk z=" + value.z);
			}
			if (value.needsSaving) {
				saveChunk(value);
				savedCount++;
			}
		}
		return savedCount;
	}

	protected void saveChunk(Chunk chunk) {
		byte[] chunkData = chunk.saveToByteArray();
		region.write(chunk.x, chunk.z, chunkData, chunkData.length);
	}

	public void unloadChunks(boolean saveFirst) {
		if (saveFirst) saveAll();
		chunks.clear();
	}

	public void close() throws IOException {
		region.close();
	}
}
