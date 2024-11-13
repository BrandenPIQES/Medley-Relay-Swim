//M. M. Kuttel 2024 mkuttel@gmail.com
//Class representing the grid for the simulation, made up of grid blocks.

package medleySimulation;

//This class represents the club as a grid of GridBlocks
public class StadiumGrid {
	private GridBlock [][] Blocks;
	private final int x; //maximum x value
	private final int y; //maximum y value
	public  static int start_y; // where the starting blocks are 
	
	private GridBlock entrance; //hard coded entrance
	
	private GridBlock startingBlocks[]; //hard coded starting blocks
	private final static int minX =5;//minimum x dimension
	private final static int minY =5;//minimum y dimension
	
	
	StadiumGrid(int x, int y, int nTeams ,FinishCounter c) throws InterruptedException {
		if (x<minX) x=minX; //minimum x
		if (y<minY) y=minY; //minimum x
		this.x=x;
		this.y=y;
		start_y=y-20; //start row hard-coded
		Blocks = new GridBlock[x][y]; //set up the array grid
		startingBlocks= new GridBlock[nTeams];
		this.initGrid();
		entrance=Blocks[0][y-5];
		}
	
	//initialise the grid, creating all the GridBlocks, marking the starting blocks
	private  void initGrid() throws InterruptedException {
		int startBIndex=0;
		for (int i=0;i<x;i++) {
			for (int j=0;j<y;j++) {
				boolean start_block=false;
				if ((i%5==1)&&(j==start_y)) {
					start_block=true;
				} 		
				Blocks[i][j]=new GridBlock(i,j,start_block);
				if (start_block) {
					this.startingBlocks[startBIndex] = Blocks[i][j];
					startBIndex++;
				}
			}
		}
	}
	
	public  int getMaxX() { return x;}
	
	public int getMaxY() { return y;}

	public GridBlock whereEntrance() {  return entrance; }

	//is this a valid grid reference?
	public  boolean inGrid(int i, int j) {
		if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
			return false;
		return true;
	}
	
	//is this a valid grid reference?
	public  boolean inStadiumArea(int i, int j) {
		return inGrid(i,j);
	}
	
	





	/*
	 * person enters the stadium
	 * This method handles when a swimmer enters a stadium. The swimmers enter through as 
	 * specific enterance block. 
	 * 
	 * Multiple swimmers may try to enter the stadium at once(occupy the entrance block at once)
	 * 
	 * Synchronizing that particular block ensures that only one swimmer can occupy it at a time, meaning only one swimmer can enter the stadium at a time
	 */

	//a person enters the stadium
	public GridBlock enterStadium(PeopleLocation myLocation) throws InterruptedException  {
		       synchronized(entrance){
				while((!entrance.get(myLocation.getID()))) {entrance.wait();} 
				myLocation.setLocation(entrance);
				myLocation.setInStadium(true);
				return entrance;
			   }
			
	}
	
	//returns starting block for a team (the lane)
	public GridBlock returnStartingBlock(int team) {
			return startingBlocks[team];
	}




	

	/*
	 * Make a one block move in a direction
	 * Each swimmer thread can only move to a block if it is unoccupied. 
	 * 
	 * Synchronizing on the new block (newBlock) ensures that only one swimmer can occupy 
	 * the block at any given time. When a swimmer moves towards a new block, no other swimmer 
	 * should be able to occupy that block until the current swimmer has successfully claimed it.
	 * After successfully moving to the new block, the swimmer must release their previous block (currentBlock) 
	 * so other swimmers can move into it.
	 * 
	 */

	public GridBlock moveTowards(GridBlock currentBlock,int xDir, int yDir,PeopleLocation myLocation) throws InterruptedException {  //try to move in 
		
		int c_x= currentBlock.getX();
		int c_y= currentBlock.getY();
		
		int add_x= Integer.signum(xDir-c_x);//-1,0 or 1
		int add_y= Integer.signum(yDir-c_y);//-1,0 or 1
		
		if ((add_x==0)&&(add_y==0)) {//not actually moving
			return currentBlock;
		}
		//restrict i and j to grid
		if (!inStadiumArea(add_x+c_x,add_y+c_y)) {
			System.out.println("Invalid move");
			//Invalid move to outside  - ignore
			return currentBlock;
		}

		GridBlock newBlock;
		if(add_x!=0){
			newBlock = whichBlock(add_x+c_x,c_y); //try moving x only first
		}
		else {
			newBlock= whichBlock(add_x+c_x,add_y+c_y);//try diagonal or y
		}
		
		

		synchronized(newBlock){
			while((!newBlock.get(myLocation.getID()))) {newBlock.wait();}

			myLocation.setLocation(newBlock);
			
			// need to synchronize this as well coz, a swimmer might be occupying
			// the block that is being released
			synchronized(currentBlock){
				currentBlock.release();
				currentBlock.notifyAll();
			}
		}

		return newBlock;
	} 
	





	/*
	 * levitate to a specific block 
	 * 
	 * Multiple swimmers may try to jump to the same block simultaneously. Synchronization ensures 
	 * that only one swimmer can occupy the block, and the others must wait until it becomes available.
	 * 
	 * Similar to moveTowards, the method ensures that the swimmer can only jump to the 
	 * target block if it is unoccupied. And the swimmer must release the block it previously occupied, 
	 * allowing other swimmers to claim it.
	 */
	
	public GridBlock jumpTo(GridBlock currentBlock,int x, int y,PeopleLocation myLocation) throws InterruptedException {  
		//restrict i and j to grid
		if (!inStadiumArea(x,y)) {
			System.out.println("Invalid move");
			//Invalid move to outside  - ignore
			return currentBlock;
		}

		GridBlock newBlock= whichBlock(x,y);//try diagonal or y
		
	
		
		synchronized(newBlock){
			while((!newBlock.get(myLocation.getID()))) {newBlock.wait();}

			myLocation.setLocation(newBlock);

			synchronized(currentBlock){
				currentBlock.release();
				currentBlock.notify();
			}
		}

		return newBlock;
		
		
	} 
	
//x and y actually correspond to the grid pos, but this is for generality.
	public GridBlock whichBlock(int xPos, int yPos) {
		if (inGrid(xPos,yPos)) {
			return Blocks[xPos][yPos];
		}
		System.out.println("block " + xPos + " " +yPos + "  not found");
		return null;
	}
}


	

	

