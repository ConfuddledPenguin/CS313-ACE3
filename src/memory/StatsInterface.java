package memory;

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

}