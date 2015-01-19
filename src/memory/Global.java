package memory;

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
	
}
