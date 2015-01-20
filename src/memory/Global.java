package memory;

import memoryAlgorithms.Algorithm;
import memoryAlgorithms.FIFO;
import memoryAlgorithms.LRU;

/**
 * Information that is used through out the memory system
 * 
 * @author Tom Maxwell
 *
 */
class Global {


	/** These masks are used for extracting the 
	 *  page and offset information
	 *  from the address
	 */
	public static final int offsetbitmask = 0x000000ff;
	public static final int pagebitmask = 0x0000ff00;
	
	/**
	 * The sizes of the components.
	 */
	public static int TLB_SIZE;
	public static int FRAME_SIZE;
	public static int NUMBER_OF_FRAMES;
	
	public static algorithm TLB_ALGO;
	public static algorithm PAGE_ALGO;
	
	/**
	 * The types of algorithms available
	 * 
	 * @author Tom Maxwell
	 *
	 */
	public enum algorithm {
		
		LRU, FIFO, RANDOM
		
	}
	
	/**
	 * Creating the info
	 */
	public Global() {
		
		TLB_SIZE = 16;
		FRAME_SIZE = 256;
		NUMBER_OF_FRAMES = 128;
		TLB_ALGO = algorithm.RANDOM;
		PAGE_ALGO = algorithm.RANDOM;
	}
	
	/**
	 * Created and returns a algorithm to use.
	 * 
	 * @param algo The algorithm to create
	 * @return the algorithm
	 */
	public static Algorithm getAlgorithm(algorithm algo) {
		
		switch (algo) {
			case LRU:
				return new LRU();
			case FIFO:
				return new FIFO();
			case RANDOM:
			default:
				return new memoryAlgorithms.Random();
		}
	}
}
