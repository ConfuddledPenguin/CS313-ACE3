package memory;

import java.io.IOException;

/**
 * The public interface for the memory subsystem
 * @author Tom
 *
 */
public interface MemorySubSystem {

	/**
	 * Reads the provided virtual address, from
	 * the underlying system.
	 * 
	 * @param address The address to be read
	 * @return The value read
	 * @throws IOException Error reading from disk
	 */
	public int read(int address) throws IOException;
	
	/**
	 * Reads the provided virtual address, from
	 * the underlying system.
	 * 
	 * It returns an array of int that contain three values.
	 * the first being the address to read from,
	 * the second being the physical address in main memory that was read from,
	 * the third being the actual value stored in the address. 
	 * 
	 * @param address The address to read.
	 * @return The array of values. where array[0] is the address read from,
	 * array[1] is the physical address, array[2] is the value read
	 * 
	 * @throws IOException Error reading from disk.
	 */
	public int[] readVerbose(int address) throws IOException;
	
	/**
	 * Get the stats of the systems performance.
	 * 
	 * @return this.stats
	 */
	public StatsInterface getStats();

}