package memory;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A class for tracking the statistics of the memory system
 * 
 * @author Tom Maxwell
 *
 */
class Stats implements StatsInterface {
	
	private static ArrayList<Integer[]> data = new ArrayList<Integer[]>();
	private static int totalReads = 0;
	private static int totalTLBMisses = 0;
	private static int totalPageFaults = 0;
	private static int bytesReadFromDisk = 0;
	private static Map<Integer, Integer> read = new HashMap<Integer, Integer>();
	
	/*
	 * Ideally these would be local vars to a method, but to avoid computing the
	 * values on every access they are stored here
	 */
	private List<Integer> maxLoadedPages;
	private int maxValue = 0;
	private boolean statsUpdated = true;
	
	/**
	 * This resets the values. Needed because they
	 * are stored statically.
	 */
	void reset() {
		data = new ArrayList<Integer[]>();
		totalReads = 0;
		totalTLBMisses = 0;
		totalPageFaults = 0;
		bytesReadFromDisk = 0;
		read = new HashMap<Integer, Integer>();
	}
	
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
	
	/**
	 * Store the data for a given address
	 * 
	 * @param address The address loaded
	 * @param data The data to be stored
	 */
	void addData(int address, int[] data){
		
		//need to convert int[] to Integer[] for storage
		Integer[] dataInt = new Integer[data.length];
		int i = 0;
		for(int value: data){
			dataInt[i++] = value;
		}
		Stats.data.add(dataInt);
	}
	
	/**
	 * Updates the number of times a page
	 * has been loaded from disk
	 * 
	 * @param page The page loaded
	 */
	void readPage(int page){
		
		if(read.containsKey(page)){
			int val = read.get(page);
			read.put(page, ++val);
		}else{
			read.put(page, 1);
		}
		
		statsUpdated = true;
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
	 * @see memory.StatsInterface#getData(int)
	 */
	@Override
	public int[] getData(int address){
		
		for(Integer[] value: data){
			if(value[0] == address){
				
				int[] returnData = new int[value.length];
				int i = 0;
				for(int x: value){
					returnData[i++] = x;
				}
				
				return returnData;
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getData()
	 */
	@Override
	public List<Integer[]> getData(){
		return Collections.unmodifiableList(data);
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
		return totalTLBMisses - totalPageFaults;
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
		double p = ((double) getPageHits() / totalTLBMisses ) * 100;
		
		return new BigDecimal(p).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getMostLoadedPages()
	 */
	@Override
	public List<Integer> getMostLoadedPages(){
		
		if(statsUpdated || maxLoadedPages == null){
			mostLoadedhelper();
		}
		
		// don't pass back the actual list, or a modifiable one
		return Collections.unmodifiableList(maxLoadedPages);
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getMaxTimesAPageWasLoaded()
	 */
	@Override
	public int getMaxTimesAPageWasLoaded(){
		
		if(statsUpdated || maxLoadedPages == null){
			mostLoadedhelper();
		}
		
		return maxValue;	
		
	}
	
	/* (non-Javadoc)
	 * @see memory.StatsInterface#getNumberOfPagesLoadedMaxTimes()
	 */
	@Override
	public int getNumberOfPagesLoadedMaxTimes(){
		
		if(statsUpdated || maxLoadedPages == null){
			mostLoadedhelper();
		}
		
		return maxLoadedPages.size();
		
	}
	
	/**
	 * Calculated the max number a page has been loaded
	 * and what pages have been loaded this many times;
	 */
	private void mostLoadedhelper(){
	
		maxLoadedPages = new ArrayList<Integer>();
		
		for( Map.Entry<Integer, Integer> entry : read.entrySet()){
			
			int value = entry.getValue();
			
			if(value == maxValue){
				maxLoadedPages.add(entry.getKey());
			}else if(value > maxValue){
				maxLoadedPages = new ArrayList<Integer>();
				maxLoadedPages.add(entry.getKey());
				maxValue = value;
			}
		}
	}
}
