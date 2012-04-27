package net.zhuoweizhang.pocketinveditor.io.zip;

import java.io.*;
import java.util.zip.*;

public class ZipFileWriter {
	public static void write(File file, ZipOutputStream outstream) throws IOException {
		ZipEntry currentEntry = new ZipEntry(file.getName());
		outstream.putNextEntry(currentEntry);
		BufferedInputStream instream = null;
		try {
			instream = new BufferedInputStream(new FileInputStream(file));
			while (true) {
				int inval = instream.read();
				if (inval == -1) {
					break;
				}
				outstream.write(inval);
			}
		} finally {
			if (instream != null) {
				instream.close();
			}
		}
	}

	public static void write(File[] files, File outfile) throws IOException {
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream outputStream = null;
		ZipOutputStream zipstream = null;
		try {
			fileOutputStream = new FileOutputStream(outfile);
			outputStream = new BufferedOutputStream(fileOutputStream);
			zipstream = new ZipOutputStream(outputStream);
			for (File file : files) {
				write(file, zipstream);
			}
		} finally {
			try {
				if (zipstream != null) zipstream.close();
				if (outputStream != null) outputStream.close();
				if (fileOutputStream != null) fileOutputStream.close();
			} catch (Exception e) {
			}
		}
	}
}

