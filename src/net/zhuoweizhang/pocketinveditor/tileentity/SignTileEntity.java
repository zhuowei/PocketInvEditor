package net.zhuoweizhang.pocketinveditor.tileentity;

import java.util.Arrays;

public class SignTileEntity extends TileEntity {

	public static final int NUM_LINES = 4;

	private String[] lines = {"", "", "", ""};

	public String getLine(int index) {
		return lines[index];
	}

	public void setLine(int index, String newLine) {
		lines[index] = newLine;
	}

	public String[] getLines() {
		return lines;
	}

	@Override
	public String toString() {
		return super.toString() + Arrays.asList(lines).toString();
	}
}
