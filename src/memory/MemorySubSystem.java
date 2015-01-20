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
	 * @throws IOException Error reading from disk
	 */
	public abstract Byte read(int address) throws IOException;

}