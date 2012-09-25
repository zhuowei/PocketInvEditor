package net.zhuoweizhang.pocketinveditor.tileentity;

public class FurnaceTileEntity extends ContainerTileEntity {

	private short burnTime = 0;
	private short cookTime = 0;

	public short getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(short burnTime) {
		this.burnTime = burnTime;
	}

	public short getCookTime() {
		return cookTime;
	}

	public void setCookTime(short cookTime) {
		this.cookTime = cookTime;
	}

	@Override
	public int getContainerSize() {
		return 3; //input, fuel, output
	}
}
