package net.zhuoweizhang.pocketinveditor.tileentity;

public class ChestTileEntity extends ContainerTileEntity {

	private int pairx = -0xffff;
	private int pairz = -0xffff;

	public void setPairX(int pairx) {
		this.pairx = pairx;
	}

	public int getPairX() {
		return pairx;
	}

	public void setPairZ(int pairz) {
		this.pairz = pairz;
	}

	public int getPairZ() {
		return pairz;
	}
}
