package memoryAlgorithms;

import java.util.LinkedList;

/**
 * This algorithm selects the first address
 * to be used as the next to be replaced.
 * 
 * @author Tom Maxwell
 *
 */
public class FIFO implements Algorithm {

	private LinkedList<Integer> used = new LinkedList<Integer>();
	
	
	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#used(int)
	 */
	@Override
	public void used(int address) {
		
		if(used.contains(address)){
			return;
		}
		used.add(address);
		
	}
	
	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#next()
	 */
	@Override
	public int next() {
		
		return used.removeFirst();
	
	}
}
