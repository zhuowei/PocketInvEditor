package net.zhuoweizhang.pocketinveditor.tileentity;

public class NetherReactorTileEntity extends TileEntity {

	private boolean isInitialized = false;

	private short progress = 0;

	private boolean hasFinished = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean initialized) {
		isInitialized = initialized;
	}

	public short getProgress() {
		return progress;
	}

	public void setProgress(short progress) {
		this.progress = progress;
	}

	public boolean hasFinished() {
		return hasFinished;
	}

	public void setFinished(boolean finished) {
		hasFinished = finished;
	}

}
