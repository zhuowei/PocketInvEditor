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
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Chunk chunk = mgr.getChunk(x, z);
					if (chunk.dirtyTableIsReallyGross()) {
						System.out.print("*");
					} else {
						System.out.print("_");
					}
				}
				System.out.println("|");
			}
			//System.out.println("Adding lava from 100 to 150x and 50 to 100 z");
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 128; y++) {
					for (int z = 0; z < 16; z++) {
						mgr.getChunk(2, 12).setBlockTypeId(x, y, z, 10);
					}
				}
			}
			
			System.out.println("Saving chunks...");
			System.out.println(mgr.saveAll() + " chunks saved");
			mgr.unloadChunks(false);
			mgr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
