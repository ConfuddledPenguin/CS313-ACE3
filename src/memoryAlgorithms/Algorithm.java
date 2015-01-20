package memoryAlgorithms;

/**
 * This interface provides access to the different
 * types of memory management algorithms.
 * 
 * @author Tom MAxwell
 *
 */
public interface Algorithm {

	/**
	 * Sets the given address as used.
	 * 
	 * @param address The address that has been used.
	 */
	public void used(int address);

	/**
	 * Returns the next address to be replaced.
	 * 
	 * @return The address to be replaced
	 */
	public int next();

}