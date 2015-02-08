package memoryAlgorithms;

import java.util.LinkedList;

/**
 * 
 * This algorithm selects the least recently
 * used address to be the next to replace.
 * 
 * @author Tom Maxwell
 *
 */
public class LRU implements Algorithm {

	private LinkedList<Integer> used = new LinkedList<Integer>();
	
	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#used(int)
	 */
	public void used(int address) {
		
		if(used.contains(address)){
			used.remove( (Integer) address);
		}
		used.add(address);
	}
	
	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#next()
	 */
	public int next() {
		
		return used.removeFirst();
	
	}

}
