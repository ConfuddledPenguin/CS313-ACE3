package memoryAlgorithms;

import java.util.Collections;
import java.util.LinkedList;

/**
 * This algorithm randomly selects the next address to be replaced.
 * 
 * @author Tom Maxwell
 *
 */
public class Random implements Algorithm{

	private LinkedList<Integer> used = new LinkedList<Integer>();
	
	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#used(int)
	 */
	@Override
	public void used(int address) {
		
		used.add(address);		
	}

	/* (non-Javadoc)
	 * @see memoryAlgorithms.Algorithm#next()
	 */
	@Override
	public int next() {
		
		/*
		 * Using shuffle is a quick way to
		 * randomise the list of used frames
		 * can then pluck the first one off as
		 * next to use
		 */
		Collections.shuffle(used);
		
		return used.removeFirst();
	
	}

}
