package memory;

import java.io.BufferedWriter;

import memoryAlgorithms.Algorithm;
import memoryAlgorithms.FIFO;
import memoryAlgorithms.LRU;

/**
 * Information that is used through out the memory system
 * 
 * @author Tom Maxwell
 *
 */
public class Global {


	/** These masks are used for extracting the 
	 *  page and offset information
	 *  from the address
	 */
	static final int offsetbitmask = 0x000000ff;
	static final int pagebitmask = 0x0000ff00;
	
	/**
	 * The sizes of the components.
	 */
	static int TLB_SIZE;
	static int NUMBER_OF_FRAMES;
	
	static algorithm TLB_ALGO;
	static algorithm PAGE_ALGO;
	
	
	static BufferedWriter log;
	
	/**
	 * The types of algorithms available
	 * 
	 * @author Tom Maxwell
	 *
	 */
	public enum algorithm {
		
		LRU{
			@Override
			public String toString() {
				
				return "LRU";
			}
		}, FIFO{
			@Override
			public String toString() {

				return "FIFO";
			}
		}, RANDOM{
			@Override
			public String toString() {

				return "Random";
			}
		}
		
	}
	
	/**
	 * Initialise the data
	 * 
	 * @param TLBSize The number of TLB entries
	 * @param frameSize The Size of a frame
	 * @param NoFrames The number of frames
	 * @param TLBalgo The algorithm to use for the TLB
	 * @param pageAlgo The algorithm to use for the pageTable
	 */
	Global(int TLBSize, int NoFrames, algorithm TLBalgo, algorithm pageAlgo) {
		
		TLB_SIZE = TLBSize;
		NUMBER_OF_FRAMES = NoFrames;
		TLB_ALGO = TLBalgo;
		PAGE_ALGO = pageAlgo;
	}
	
	/**
	 * Created and returns a algorithm to use.
	 * 
	 * @param algo The algorithm to create
	 * @return the algorithm
	 */
	static Algorithm getAlgorithm(algorithm algo) {
		
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
