package memory;

import java.util.HashMap;
import java.util.Map;

import memoryAlgorithms.Algorithm;

/**
 * The TLB ( Transfer lookaside Buffer) provides quick
 * access to the most used pages in memory, where most
 * used is dependent on the algorithm chosen.
 * 
 * @author Tom Maxwell
 *
 */
class TLB {

	private final int TLB_SIZE;
	
	private Map<Integer, Integer> tlb = new HashMap<Integer, Integer>();
	private Algorithm replacementAlgo;
	
	/**
	 * The constructor. Here the size and algorithm are
	 * set based on the values set in {@link memory.Global}.
	 * 
	 * The value of {@link memory.Global#TLB_SIZE} is the size of
	 * the tlb 
	 * 
	 * The value of {@link memory.Global#TLB_ALGO} is the algorithm
	 * used in this TBL
	 */
	public TLB() {
		TLB_SIZE = Global.TLB_SIZE;
		
		replacementAlgo = Global.getAlgorithm(Global.TLB_ALGO);
	}
	
	/**
	 * Returns the frame address for the page address
	 * 
	 * @param page The address of the page requested
	 * @return The frame address
	 */
	public Integer getFrame(int page) {
		
		return tlb.get(page);

	}
	
	/**
	 * Sets the address of the frame for a given
	 * page.
	 * 
	 * @param page The page address to set for
	 * @param frame The frame address
	 */
	public void setFrame(int page, int frame) {
	
		if(tlb.containsKey(page)){ //already set
			return;
		}
		
		if(tlb.size() >= TLB_SIZE ){
	
			int remove = replacementAlgo.next();
			tlb.remove(remove);
			set(page, frame);
			
		}else{
			set(page, frame);
		}
	}
	
	/**
	 * Helper method to set the value in the map
	 * 
	 * @param page The page to map
	 * @param frame the frame to map to
	 */
	private void set(int page, int frame){
		replacementAlgo.used(page);
		tlb.put(page, frame);
	}
}
