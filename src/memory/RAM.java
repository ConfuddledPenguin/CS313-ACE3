package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * The main memory of the system
 * 
 * @author Tom Maxwell
 *
 */
class RAM {
	
	/** These masks are used for extracting the 
	 *  frame and offset information
	 *  from the address.
	 *  
	 *  The page and frame data is located in the
	 *  same bits hence the same mask can be used.
	 */
	private final int offsetbitmask = Global.offsetbitmask;
	private final int framebitmask = Global.pagebitmask;
	private final int NUMBER_OF_FRAMES = Global.NUMBER_OF_FRAMES;
	
	/**
	 * The memory
	 */
	private Map<Integer, Byte[]> ram;
	
	/**
	 * The constructor for memory
	 */
	public RAM() {
		
		ram = new HashMap<Integer, Byte[]>();
		
	}
	
	/**
	 * Reads the data from the given address in memory
	 * 
	 * @param address Address to read
	 * @return The read Byte
	 */
	public Byte read(int address){
		
		int offset = address & offsetbitmask;
		
		Byte[] data = ram.get(getFrame(address));
		
		return data[offset];
		
	}
	
	/**
	 * Writes data memory to the given address in memory
	 * 
	 * @param frameAddress The address to write to
	 * @param bytes the data to write
	 */
	public void write(int frameAddress, Byte[] bytes ){
		
		if(frameAddress > NUMBER_OF_FRAMES){
			throw new IndexOutOfBoundsException("Invalid frame");
		}
		
		ram.put(frameAddress, bytes );
		
	}
	
	/**
	 * Gets the frame from the address.
	 * 
	 * @param address the address the info is in
	 * @return the frame address
	 */
	private int getFrame(int address){
		return (address & framebitmask) >>> 8;
	}

}
