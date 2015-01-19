package memory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

/**
 * The MMU (Memory Management Unit) handles the retrieval
 * of the data from either main memory or the harddisk.
 * 
 * @author Tom Maxwell
 *
 */
public class MMU implements MemorySubSystem {
	
	/** The size of the frames */
	private final int FRAME_SIZE = 256;
	/** The total number of  frames */
	private final int TOTAL_NUMBER_FRAMES = 256;
	
	/** These masks are used for extracting the 
	 *  page and offset information
	 *  from the address
	 */
	private final int offsetbitmask = Global.offsetbitmask;
	private final int pagebitmask = Global.pagebitmask;
	
	/**
	 * The components of the memory system
	 */
	private PageTable pageTable = new PageTable();
	private RAM ram = new RAM();
	private Disk disk;
	
	/**
	 * Tracking information
	 */
	private HashSet<Integer> freeFrames = new HashSet<Integer>();
	
	/**
	 * The constructor for the MMU
	 */
	public MMU() {
		
		try {
			disk = new Disk();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < TOTAL_NUMBER_FRAMES; i++){
			freeFrames.add(i);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see memory.MemorySubSystem#read(int)
	 */
	@Override
	public void read(int address) {
		
		int offset = address & offsetbitmask;
		int page = getPage(address);
		
		Integer frame = pageTable.getFrame(page);
		
		if(frame == null){ //pagefualt
			
			handlePagefault(address);	
		}
		
		frame = pageTable.getFrame(page);
		
		int physicalAddress = (frame << 8) | offset;
		
		int value = ram.read(physicalAddress);
		
		System.out.println(value);

	}
	
	/**
	 * Handles a pagefault at the passed in address
	 * This involved getting the data from the disk
	 * and writing it to memory
	 * 
	 * @param address The address the service
	 */
	private void handlePagefault(int address){
		
		try {
			
			/*
			 * We need to load from the disk at the start
			 * of the page. So get the page then multiply
			 * it by the FRAME_SIZE to work out the starting 
			 * address
			 */
			int frameStartPos = getPage(address) * FRAME_SIZE;
			Byte[] data = disk.read(frameStartPos, FRAME_SIZE);
			
			int page = getPage(address);
			int frame = nextFrame();
		
			pageTable.setFrame(page, frame);
			
			ram.write(frame, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * Returns the nextFrame()
	 * 
	 * @return an integer value representing the 
	 * next frame to use
	 */
	private int nextFrame(){
		
		int free = freeFrames.iterator().next();
		freeFrames.remove(free);
		
		return free;
	}
	
	/**
	 * Gets the page section from the address
	 * 
	 * @param address the address the page is from
	 * @return the page value
	 */
	private int getPage(int address){
		return (address & pagebitmask) >>> 8;
	}

}
