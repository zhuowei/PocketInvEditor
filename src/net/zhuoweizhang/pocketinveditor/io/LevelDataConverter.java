package net.zhuoweizhang.pocketinveditor.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.DataOutputStream;

import org.spout.nbt.*;
import org.spout.nbt.stream.*;

import net.zhuoweizhang.pocketinveditor.Level;

import net.zhuoweizhang.pocketinveditor.io.nbt.NBTConverter;

public final class LevelDataConverter {
	public static final byte[] header = {0x04, 0x00, 0x00, 0x00};

	public static Level read(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream is = new BufferedInputStream(fis);
		is.skip(8);
		Level level = NBTConverter.readLevel((CompoundTag) new NBTInputStream(is, false, true).readTag());
		is.close();
		return level;
	}

	public static void write(Level level, File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new NBTOutputStream(bos, false, true).writeTag(NBTConverter.writeLevel(level));
		FileOutputStream os = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
		int length = bos.size();
		dos.write(header);
		dos.writeInt(Integer.reverseBytes(length));
		bos.writeTo(dos);
		dos.close();
	}	

	public static void main(String[] args) throws Exception {
		Level level = read(new File(args[0]));
		System.out.println(level);
		write(level, new File(args[1]));
	}

}
