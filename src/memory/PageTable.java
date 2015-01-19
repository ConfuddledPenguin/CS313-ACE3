package memory;

import java.util.HashMap;

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
	
	/**
	 * The constructor for the PageTable
	 */
	public PageTable() {
		table = new HashMap<Integer, Integer>();
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
		
		table.put(page, frame);
	}

}
