package memory;

import java.util.List;

public interface StatsInterface {

	/**
	 * Returns the total number of reads performed by the
	 * system
	 * 
	 * @return This.totalReads
	 */
	public abstract int getTotalReads();

	/**
	 * Returns the total number of TLD misses
	 * 
	 * @return This.TLBMisses
	 */
	public abstract int getTLBMisses();

	/**
	 * Returns the total number of pageFaults
	 * 
	 * @return This.PageFaults
	 */
	public abstract int getPageFaults();

	/**
	 * Returns the total number of bytes read from the disk
	 * 
	 * @return This.bytesReadFromDisk
	 */
	public abstract int getBytesRead();

	/**
	 * Returns the number of page hits
	 * 
	 * @return This.PageHits
	 */
	public abstract int getPageHits();

	/**
	 * Returns the data for that address
	 * 
	 * It returns an array of int that contain three values.
	 * the first being the address to read from,
	 * the second being the physical address in main memory that was read from,
	 * the third being the actual value stored in the address. 
	 * 
	 * @param address The address to read.
	 * @return The array of values. where array[0] is the address read from,
	 * array[1] is the physical address, array[2] is the value read
	 */
	public abstract int[] getData(int address);
	
	/**
	 * Returns the data;
	 * 
	 * It returns an ArrayList of arrays of int that contain three values.
	 * the first being the address to read from,
	 * the second being the physical address in main memory that was read from,
	 * the third being the actual value stored in the address. 
	 * 
	 * @return The array of values. where array[0] is the address read from,
	 * array[1] is the physical address, array[2] is the value read
	 */
	public abstract List<Integer[]> getData();
	
	/**
	 * Returns the number of TLB hits
	 * 
	 * @return this.TLBHits
	 */
	public abstract int getTLBHits();

	/**
	 * Returns the percentage of TLB hits
	 * 
	 * @return This.TLBHitPerc
	 */
	public abstract double getTLBHitPercentage();

	/**
	 * Returns the percentage of page hits
	 * 
	 * @return This.TLBHitPerc
	 */
	public abstract double getPageHitPercentage();
	
	/**
	 * Returns the pages that where loaded the
	 * most often
	 * 
	 * @return The pages loaded most often
	 */
	public List<Integer> getMostLoadedPages();
	
	/**
	 * Returns the max number of times a page was loaded
	 * 
	 * @return The value
	 */
	public int getMaxTimesAPageWasLoaded();
	
	/**
	 * Returns the number of pages loaded the max times
	 * 
	 * @return The value
	 */
	public int getNumberOfPagesLoadedMaxTimes();

}