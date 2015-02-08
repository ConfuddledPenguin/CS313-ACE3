package memory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

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
	private Map<Integer, Integer> table;
	
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
		
		replacementAlgo.used(page);
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
		
		int pageToRemove = -1;
		for(Map.Entry<Integer, Integer> entry : table.entrySet()){
			if(entry.getValue() == frame){
				pageToRemove = entry.getKey();
			}
		}
		
		table.remove(pageToRemove);
		replacementAlgo.used(page);
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
			try {
				Global.log.write("using Algorithm to pick frame\t\t\t");
			} catch (IOException e) {
				System.err.println("Error writing to log");
			}
			int free = replacementAlgo.next();
			
			System.out.println(free);
			
			for(Entry<Integer, Integer> mapping: table.entrySet()){
				
				if(free == mapping.getKey()){
					System.out.println("remove - " + mapping.getValue());
					return mapping.getValue();
				}
			}
			
			return free;
		}
		
		int free = freeFrames.iterator().next();
		freeFrames.remove(free);
		try {
			Global.log.write("Using frame " + free + "  Free frames left = " + freeFrames.size() + "\t");
		} catch (IOException e) {
			System.err.println("Error writing to log");
		}
		
		return free;
	}

}
