package net.zhuoweizhang.pocketinveditor.geo;


/**
 * An interface for classes that provide access to blocks from a region; e.g. from a save file, dimension, or a schematic. 
 * Similar to the purpose of AreaBlockAccess in Spout.
 */

public interface AreaBlockAccess {

	public int getBlockTypeId(int x, int y, int z);

	public int getBlockData(int x, int y, int z);

	public void setBlockTypeId(int x, int y, int z, int type);

	public void setBlockData(int x, int y, int z, int data);

}
