package net.zhuoweizhang.pocketinveditor.io.nbt.schematic;

import java.io.*;

import net.zhuoweizhang.pocketinveditor.geo.*;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

import static java.lang.Integer.parseInt;

public final class SchematicTester {

	/** shoves a schematic into a level based on command line prompts */
	public static void main(String[] args) {
		try {
			ChunkManager mgr = new ChunkManager(new File(args[0]));
			CuboidClipboard schematic = SchematicReader.read(new File(args[1]));
			Vector3f beginVector = new Vector3f(parseInt(args[2]), parseInt(args[3]), parseInt(args[4]));
			schematic.place(mgr, beginVector, false);
					
			System.out.println("Saving chunks...");
			System.out.println(mgr.saveAll() + " chunks saved");
			mgr.unloadChunks(false);
			mgr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
