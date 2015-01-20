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
	
	/** These masks are used for extracting the 
	 *  page and offset information
	 *  from the address
	 */
	private final int offsetbitmask = Global.offsetbitmask;
	private final int pagebitmask = Global.pagebitmask;
	
	/**
	 * The components of the memory system
	 */
	private TLB tlb;
	private PageTable pageTable;
	private RAM ram;
	private Disk disk;
	int tlbmiss = 0;
	int pagemiss = 0;
	
	/**
	 * The constructor for the MMU
	 * 
	 * @throws FileNotFoundException Disk cant be found
	 */
	public MMU() throws FileNotFoundException {
		
		//initialise the data
		new Global();
		tlb = new TLB();
		pageTable = new PageTable();
		ram = new RAM();
		
		disk = new Disk();
		
	}
	
	/* (non-Javadoc)
	 * @see memory.MemorySubSystem#read(int)
	 */
	@Override
	public Byte read(int address) throws IOException {
		
		int offset = address & offsetbitmask;
		int page = getPage(address);
		
		Integer frame = tlb.getFrame(page);
		
		if(frame == null){	//TLB miss
			
			frame = pageTable.getFrame(page);
			
			if(frame == null){ //pagefualt
				
				handlePagefault(address);	
			}
			
			frame = pageTable.getFrame(page);
			tlb.setFrame(page, frame);
			
		}
				
		int physicalAddress = (frame << 8) | offset;
		
		Byte value = ram.read(physicalAddress);
		
		return value;

	}
	
	/**
	 * Handles a pagefault at the passed in address
	 * This involved getting the data from the disk
	 * and writing it to memory
	 * 
	 * @param address The address the service
	 * @throws IOException Error reading file
	 */
	private void handlePagefault(int address) throws IOException{
		
		
		/*
		 * We need to load from the disk at the start
		 * of the page. So get the page then multiply
		 * it by the FRAME_SIZE to work out the starting 
		 * address
		 */
		int frameStartPos = getPage(address) * FRAME_SIZE;
		Byte[] data = disk.read(frameStartPos, FRAME_SIZE);
		
		int page = getPage(address);
		int frame = pageTable.nextFrame();
	
		pageTable.setFrame(page, frame);
		
		ram.write(frame, data);	
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
