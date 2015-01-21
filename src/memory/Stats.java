package memory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A class for tracking the statistics of the memory system
 * 
 * @author Tom Maxwell
 *
 */
class Stats implements StatsInterface {
	
	
	private static int totalReads = 0;
	private static int totalTLBMisses = 0;
	private static int totalPageFaults = 0;
	private static int bytesReadFromDisk = 0;
	
	/**
	 * Increment the total number of reads
	 */
	void incrementTotalReads(){
		totalReads++;
	}
	
	/**
	 * Increment the total number of TLB misses
	 */
	void incrementTLBMisses(){
		totalTLBMisses++;
	}
	
	/**
	 * Increment the total number of page faults
	 */
	void incrementPageFaults(){
		totalPageFaults++;
	}
	
	/**
	 * Updates the number of bytes read from the disk
	 * 
	 * @param bytesRead The number of bytes to update the count by
	 */
	void updateReadFromDisk(int bytesRead){
		bytesReadFromDisk += bytesRead;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getTotalReads()
	 */
	@Override
	public int getTotalReads(){
		return totalReads;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getTLBMisses()
	 */
	@Override
	public int getTLBMisses(){
		return totalTLBMisses;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getPageFaults()
	 */
	@Override
	public int getPageFaults() {
		return totalPageFaults;
	}	
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getBytesRead()
	 */
	@Override
	public int getBytesRead() {
		return bytesReadFromDisk;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getPageHits()
	 */
	@Override
	public int getPageHits(){
		return totalReads - totalPageFaults;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getTLBHits()
	 */
	@Override
	public int getTLBHits(){
		return totalReads - totalTLBMisses;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getTLBHitPercentage()
	 */
	@Override
	public double getTLBHitPercentage(){
		double p = ((double) getTLBHits() / totalReads ) * 100;

		return new BigDecimal(p).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getPageHitPercentage()
	 */
	@Override
	public double getPageHitPercentage(){
		double p = ((double) getPageHits() / totalReads ) * 100;
		
		return new BigDecimal(p).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
