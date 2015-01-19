package memory;

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
	 */
	public abstract void read(int address);

}