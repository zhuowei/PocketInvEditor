package net.zhuoweizhang.pocketinveditor.io.region;

import java.io.File;

import net.zhuoweizhang.pocketinveditor.geo.*;

public class RegionTest {
	public static void main(String[] args) {
		try {
			/*RegionFile a = new RegionFile(new File(args[0]));
			byte[] data = a.getChunkData(5, 5);
			Chunk chunk = new Chunk(5, 5);
			System.out.println(data);
			chunk.loadFromByteArray(data);

			a.write(5, 5, data, data.length);
			a.close();*/
			ChunkManager mgr = new ChunkManager(new File(args[0]));
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Chunk chunk = mgr.getChunk(x, z);
					System.out.println("Chunk " + x  + "," + z);
					System.out.println("DIAMONDS: " + chunk.countDiamonds());
					if (chunk.dirtyTableIsReallyGross()) {
						System.out.println("Chunk " + x + "," + z + " has been modified.");
					}
				}
			}
			mgr.unloadChunks();
			mgr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
