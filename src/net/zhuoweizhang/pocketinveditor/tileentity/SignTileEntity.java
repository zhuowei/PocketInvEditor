package net.zhuoweizhang.pocketinveditor.tileentity;

public class SignTileEntity extends TileEntity {

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
}
