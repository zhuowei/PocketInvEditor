package net.zhuoweizhang.pocketinveditor.io.nbt.schematic;

import java.io.*;

import java.util.List;

import org.spout.nbt.*;

import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.geo.*;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class SchematicReader {

	public static Schematic read(File file) throws IOException {

		NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
		CompoundTag mainTag = (CompoundTag) stream.readTag();
		stream.close();

		int width = 0, height = 0, length = 0;
		String materials = null;
		byte[] blocks = null;
		byte[] data = null;

		List<Tag> tags = mainTag.getValue();
		for (Tag tag: tags) {
			String tagName = tag.getName();
			if (tagName.equals("Width")) {
				width = ((ShortTag) tag).getValue();
			} else if (tagName.equals("Height")) {
				height = ((ShortTag) tag).getValue();
			} else if (tagName.equals("Length")) {
				length = ((ShortTag) tag).getValue();
			} else if (tagName.equals("Materials")) {
				materials = ((StringTag) tag).getValue();
			} else if (tagName.equals("Blocks")) {
				blocks = ((ByteArrayTag) tag).getValue();
			} else if (tagName.equals("Data")) {
				data = ((ByteArrayTag) tag).getValue();
			} else if (tagName.equals("Entities")) {
				//Do nothing
			} else if (tagName.equals("TileEntities")) {
				//Do nothing
			} else {
				System.err.println("WTF: invalid tag name: " + tagName);
			}
		}
		return new Schematic(new Vector3f(width, height, length), blocks, data);
	}


}
