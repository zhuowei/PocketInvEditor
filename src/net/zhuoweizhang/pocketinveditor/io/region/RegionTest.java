package net.zhuoweizhang.pocketinveditor.io.region;

import java.io.File;

import net.zhuoweizhang.pocketinveditor.geo.Chunk;

public class RegionTest {
	public static void main(String[] args) {
		try {
			RegionFile a = new RegionFile(new File(args[0]));
			byte[] data = a.getChunkData(5, 5);
			Chunk chunk = new Chunk(5, 5);
			System.out.println(data);
			chunk.loadFromByteArray(data);
			System.out.println("DIAMONDS: " + chunk.countDiamonds());
			a.write(5, 5, data, data.length);
			a.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
