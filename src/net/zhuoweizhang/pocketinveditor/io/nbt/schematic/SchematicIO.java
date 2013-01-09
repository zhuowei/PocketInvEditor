package net.zhuoweizhang.pocketinveditor.io.nbt.schematic;

import java.io.*;

import java.util.*;

import org.spout.nbt.*;

import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.geo.*;
import net.zhuoweizhang.pocketinveditor.util.Vector3f;

public class SchematicIO {

	public static CuboidClipboard read(File file) throws IOException {

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
		return new CuboidClipboard(new Vector3f(width, height, length), blocks, data);
	}

	public static void save(CuboidClipboard clipboard, File file) throws IOException {
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new ShortTag("Width", (short) clipboard.getWidth()));
		tags.add(new ShortTag("Height", (short) clipboard.getHeight()));
		tags.add(new ShortTag("Length", (short) clipboard.getLength()));
		tags.add(new StringTag("Materials", "Alpha")); //technically Pocket uses different materails, but this is the closest
		tags.add(new ByteArrayTag("Blocks", clipboard.blocks));
		tags.add(new ByteArrayTag("Data", clipboard.metaData));
		//TODO: entities
		tags.add(new ListTag<CompoundTag>("Entities", CompoundTag.class, (List<CompoundTag>) Collections.EMPTY_LIST));
		tags.add(new ListTag<CompoundTag>("TileEntities", CompoundTag.class, (List<CompoundTag>) Collections.EMPTY_LIST));
		CompoundTag mainTag = new CompoundTag("Schematic", tags);

		NBTOutputStream stream = new NBTOutputStream(new FileOutputStream(file));
		stream.writeTag(mainTag);
		stream.close();
	}


}
