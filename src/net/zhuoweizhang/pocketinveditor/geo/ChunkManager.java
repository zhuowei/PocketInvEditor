package net.zhuoweizhang.pocketinveditor.geo;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import net.zhuoweizhang.pocketinveditor.io.region.*;

public class ChunkManager {

	protected Map<Chunk.Key, Chunk> chunks = new HashMap<Chunk.Key, Chunk>();

	protected File chunkFile;

	protected RegionFile region;

	public ChunkManager(File chunkFile) {
		this.chunkFile = chunkFile;
		this.region = new RegionFile(chunkFile);
	}


	public Chunk getChunk(int x, int z) {
		return getChunk(new Chunk.Key(x, z));
	}

	public Chunk getChunk(Chunk.Key key) {
		Chunk chunk = chunks.get(key);
		if (chunk == null)
			chunk = loadChunk(key);
		return chunk;
	}

	public Chunk loadChunk(Chunk.Key key) {
		Chunk chunk = new Chunk(key.getX(), key.getZ());
		byte[] data = region.getChunkData(key.getX(), key.getZ());
		if (data != null) {
			chunk.loadFromByteArray(data);
		}
		chunks.put(key, chunk);
		return chunk;
	}

	public int getBlockTypeId(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getBlockTypeId(x, y, z);
	}

	public int getBlockData(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getBlockData(x, y, z);
	}

	public void unloadChunks() {
		chunks.clear();
	}

	public void close() throws IOException {
		region.close();
	}
}
