package net.zhuoweizhang.pocketinveditor.io.zip;

import java.io.*;
import java.util.zip.*;

public class ZipFileWriter {
	public static void write(File file, ZipOutputStream outstream) throws IOException {
		ZipEntry currentEntry = new ZipEntry(file.getName());
		outstream.putNextEntry(currentEntry);
		InputStream instream = null;
		try {
			instream = new FileInputStream(file);
			byte[] buffer = new byte[32768];
			int n;
			while ((n = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, n);
			}
		} finally {
			if (instream != null) {
				instream.close();
			}
		}
		outstream.closeEntry();
	}

	public static void write(File[] files, File outfile) throws IOException {
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream outputStream = null;
		ZipOutputStream zipstream = null;
		try {
			fileOutputStream = new FileOutputStream(outfile);
			outputStream = new BufferedOutputStream(fileOutputStream, 32768);
			zipstream = new ZipOutputStream(outputStream);
			zipstream.setLevel(Deflater.DEFAULT_COMPRESSION);
			zipstream.setMethod(Deflater.DEFLATED);
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

