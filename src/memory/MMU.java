package memory;

import java.io.FileNotFoundException;
import java.io.IOException;

import memory.Global.algorithm;

/**
 * The MMU (Memory Management Unit) handles the retrieval
 * of the data from either main memory or the harddisk.
 * 
 * @author Tom Maxwell
 *
 */
public class MMU implements MemorySubSystem {
	
	/** The size of the frames */
	private final int FRAME_SIZE;
	
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
	 * The stats of the systems performance
	 */
	private Stats stats = new Stats();
	
	/**
	 * The constructor for the MMU
	 * 
	 * @param TLBSize The number of entries in the TLB
	 * @param frameSize The size of a frame
	 * @param NoFrames The number of frames
	 * @param TLBalgo The algorithm to use for the TLB
	 * @param pageAlgo The algorithm to use for the pageTable
	 * 
	 * @throws FileNotFoundException Disk not found
	 */
	public MMU(int TLBSize, int frameSize, int NoFrames, algorithm TLBalgo, algorithm pageAlgo) throws FileNotFoundException {
		
		//initialise the data
		new Global(TLBSize, frameSize, NoFrames, TLBalgo, pageAlgo);
		
		tlb = new TLB();
		pageTable = new PageTable();
		ram = new RAM();
		disk = new Disk();
		
		FRAME_SIZE = Global.FRAME_SIZE;
	}
	
	/* (non-Javadoc)
	 * @see memory.MemorySubSystem#read(int)
	 */
	@Override
	public int read(int address) throws IOException {
		
		return readVerbose(address)[2];

	}
	
	/* (non-Javadoc)
	 * @see memory.MemorySubSystem#readVerbose(int)
	 */
	@Override
	public int[] readVerbose(int address) throws IOException{
		
		//The array to return
		int returnValues[] = new int[3];
		returnValues[0] = address;
		
		stats.incrementTotalReads();
		
		int offset = address & offsetbitmask;
		int page = getPage(address);
		
		Integer frame = tlb.getFrame(page);
		
		if(frame == null){	//TLB miss
			
			stats.incrementTLBMisses();
			
			frame = pageTable.getFrame(page);
			
			if(frame == null){ //pagefualt
				
				stats.incrementPageFaults();
				
				handlePagefault(address);	
			}
			
			frame = pageTable.getFrame(page);
			tlb.setFrame(page, frame);
			
		}
				
		int physicalAddress = (frame << 8) | offset;
		returnValues[1] = physicalAddress;
		
		Byte value = ram.read(physicalAddress);
		returnValues[2] = value;
		
		return returnValues;
		
	}
	
	/* (non-Javadoc)
	 * @see memory.MemorySubSystem#getStats()
	 */
	@Override
	public StatsInterface getStats() {
		return stats;
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
