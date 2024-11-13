//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;


public class GridBlock {
	
	private int isOccupied; 
	
	private final boolean isStart;  //is this a starting block?
	private int [] coords; // the coordinate of the block.
	
	GridBlock(boolean startBlock) throws InterruptedException {
		isStart=startBlock;
		isOccupied= -1;
	}
	
	GridBlock(int x, int y, boolean startBlock) throws InterruptedException {
		this(startBlock);
		coords = new int [] {x,y};
	}
	
	public   int getX() {return coords[0];}  
	
	public   int getY() {return coords[1];}
	
	
	
	
	/*
	 * THE GET BLOCK:
	 * This method allows a swimmer to acquire a block, 2 threads can try to acquire
	 * the same block at the same time. This method is synchronized to ensure 
	 * Mutual exclusion: at any point in time only one thread can access this method for 
	 * a specific grid block. Meaning, no two threads can occupy the same block at
	 * the same time
	 */
	public synchronized boolean get(int threadID) throws InterruptedException {
		if (isOccupied==threadID) return true; //thread Already in this block
		if (isOccupied>=0) return false; //space is occupied
		isOccupied= threadID;  //set ID to thread that had block
		return true;
	}
		
	
	/*
	 * THE RELEASE BLOCK
	 * This method allows a swimmer to release a block, a swimmer might be trying to release
	 * a block and another swimmer occupying it. This is synchronized to ensure that one swimmer 
	 * releases a block and resets the state correctly, allowing another swimmer to acquire it afterwards
	 */
	public synchronized void release() {
		isOccupied= -1;
	}
	

	/*
	 * THE OCCUPIED BLOCK
	 * tHIS method checks if a block is currently occupied by any swimmer. It is synchronized
	 * to ensure no thread reads a stale value, since the state of this method changes when
	 * we call get or release.
	 */
	public synchronized boolean occupied() {
		if(isOccupied==-1) return false;
		return true;
	}
	
	
	//is a start block
	public  boolean isStart() {
		return isStart;	
	}

}
