package memory;

import java.util.HashMap;
import java.util.HashSet;

import memoryAlgorithms.Algorithm;

/**
 * The PageTable keep track of which virtual
 * memory addresses map to which physical addresses
 * in physical memory
 * 
 * @author Tom Maxwell
 *
 */
class PageTable {
	
	/**
	 * The table
	 */
	private HashMap<Integer, Integer> table;
	
	private Algorithm replacementAlgo;
	private final int NUMBER_OF_FRAMES;
	
	/**
	 * Tracking information
	 */
	private HashSet<Integer> freeFrames = new HashSet<Integer>();
	
	/**
	 * The constructor for the PageTable
	 */
	public PageTable() {
		table = new HashMap<Integer, Integer>();
		replacementAlgo = Global.getAlgorithm(Global.PAGE_ALGO);
		NUMBER_OF_FRAMES = Global.NUMBER_OF_FRAMES;
		
		for(int i = 0; i < NUMBER_OF_FRAMES; i++){
			freeFrames.add(i);
		}
	}
	
	/**
	 * Returns the frame address for the page address
	 * 
	 * @param page The address of the page requested
	 * @return The frame address
	 */
	public Integer getFrame(int page){
		
		return table.get(page);
	}
	
	/**
	 * Sets the address of the frame for a given
	 * page.
	 * 
	 * @param page The page address to set for
	 * @param frame The frame address
	 */
	public void setFrame(int page, int frame){
		
		if(table.containsKey(page)){ //already set
			return;
		}
		
		
		replacementAlgo.used(frame);
		table.put(page, frame);
	}
	
	/**
	 * Returns the nextFrame
	 * 
	 * @return an integer value representing the 
	 * next frame to use
	 */
	public int nextFrame(){
		
		if(freeFrames.size() == 0){
			return replacementAlgo.next();
		}
		
		int free = freeFrames.iterator().next();
		freeFrames.remove(free);
		
		return free;
	}

}
