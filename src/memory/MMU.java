package memory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private final int FRAME_SIZE = 256;
	
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
	private Stats stats;
	
	/**
	 * The constructor for the MMU
	 * 
	 * @param TLBSize The number of entries in the TLB
	 * @param NoFrames The number of frames
	 * @param TLBalgo The algorithm to use for the TLB
	 * @param pageAlgo The algorithm to use for the pageTable
	 * 
	 * @throws IOException Error writing to log
	 */
	public MMU(int TLBSize, int NoFrames, algorithm TLBalgo, algorithm pageAlgo) throws IOException {
		
		//initialise the data
		new Global(TLBSize, NoFrames, TLBalgo, pageAlgo);
		stats = new Stats();
		stats.reset();
		
		try{
			//set up the log
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			String date = format.format(new Date());
			File dir = new File("log/");
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File("log/" + date + ".log");
			file.createNewFile();
			Global.log = new BufferedWriter(new FileWriter(file));
		}catch(IOException e){
			throw new IOException("Log file exception. Im probably missing write access.");
		}
		
		tlb = new TLB();
		pageTable = new PageTable();
		ram = new RAM();
		try{
			disk = new Disk();
		}catch(IOException e){
			throw new IOException("Disk missing. Is the files folder present?");
		}
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
		
		Global.log.write("Logical Address: " + address + "\t\t");
		
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
		
		Global.log.write("Physical Address: " + physicalAddress + "\t");
		
		Byte value = ram.read(physicalAddress);
		
		Global.log.write("Value: " + value + "\t");
		returnValues[2] = value;
		
		
		Global.log.write("\n");
		Global.log.flush();
		
		stats.addData(address, returnValues);
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
		
		Global.log.write("Handling page fault ");
		
		/*
		 * We need to load from the disk at the start
		 * of the page. So get the page then multiply
		 * it by the FRAME_SIZE to work out the starting 
		 * address
		 */
		int pageStartPos = getPage(address) * FRAME_SIZE;
		Byte[] data = disk.read(pageStartPos, FRAME_SIZE);
		stats.readPage(pageStartPos);
		
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
