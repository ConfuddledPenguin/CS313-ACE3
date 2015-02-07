package textcontrol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

import memory.Global.algorithm;
import memory.MMU;
import memory.MemorySubSystem;
import memory.StatsInterface;

/**
 * The starting point for the text control version
 * 
 * @author Tom Maxwell
 *
 */
public class Driver {

	private StatsInterface stats;
	
	/**
	 * The constructor
	 * 
	 * @param filepath The file to use and input
	 */
	public Driver(String filepath) {

		runMemory(16, 128, algorithm.FIFO, algorithm.FIFO, filepath);
	}
	
	/**
	 * A terrible method name but hey
	 * 
	 * Runs the memory system
	 * 
	 * @param TLBSize The number of entries in the TLB
	 * @param NoFrames The number of frames in RAM
	 * @param TLBalgo The algorithm to use in the TLB
	 * @param pageAlgo The algorithm to use in the PageTable
	 * @param filepath The file to use as input
	 */
	private void runMemory(int TLBSize, int NoFrames, algorithm TLBalgo, algorithm pageAlgo, String filepath){
		
		//get memory system
		MemorySubSystem mmu = null;
		try {
			mmu = new MMU(TLBSize, NoFrames, TLBalgo, pageAlgo);
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		//get stats
		stats = mmu.getStats();

		//get input
		File input = new File(filepath);
		LineNumberReader reader = null;
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		//run through input
		try {
			String line;
			while((line = reader.readLine()) != null){
				
				int address = Integer.parseInt(line);
				
				/*
				 * Since we are dealing with two IOExceptions 
				 * we need to catch the inner one and if so
				 * display a sensible message
				 */
				int value =-1;
				try{
					value = mmu.read(address);
				}catch(IOException e){
					throw new IOException("Error reading from disk");
				}
				//Uncomment below for a printout of line numbers to value
//				System.out.print("Line number " +reader.getLineNumber() + " \t");
//				System.out.println("Value: " + value);
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		//print stats
		System.out.println("\n\nFile processed. There were:\n");
		System.out.println("total reads " + stats.getTotalReads());
		System.out.println("total TLB misses " + stats.getTLBMisses());
		System.out.println("total page faults " + stats.getPageFaults());
		System.out.println("total bytes read from disk " + stats.getBytesRead());
		
		System.out.println("\ntotal TLB hits " + stats.getTLBHits());
		System.out.println("total page hits " + stats.getPageHits());
		
		System.out.println("\ntotal TLB hit rate " + stats.getTLBHitPercentage() + "%");
		System.out.println("total page hit rate " + stats.getPageHitPercentage() + "%");
		
		System.out.println("\nThe most loaded page was loaded " + stats.getMaxTimesAPageWasLoaded() + " times");
		System.out.println(stats.getNumberOfPagesLoadedMaxTimes() + " pages where loaded this many times");
		System.out.println("There addresses where:");
		List<Integer> pages = stats.getMostLoadedPages();
		for(int page: pages){
			System.out.print(page + ", ");
		}
	}
	
	/**
	 * Prints a helper message for incorrect usage of
	 * the program, it then exists
	 */
	private static void printHelp() {
		
		System.out.println("Program usage:");
		System.out.println("java -jar ACE3.jar <filepath>");
		System.exit(0);

	}
	
	/**
	 * Where is starts.
	 * 
	 * We check the args to see what is there
	 * 
	 * If empty run default file.
	 * If args:
	 * 	Check if the user wishes for help, if so print help
	 * 	Check if the args are misused, if so print help
	 *  Else assume arg is the filepath
	 * 
	 * The filepath can be relative to the launch folder
	 * 
	 * @param args The filepath
	 */
	public static void main(String[] args) {
		
		int noArgs = args.length;
		if(noArgs >= 1){
			if(noArgs == 1){
				
				if(args[0].equals(("help").toUpperCase()))
					printHelp();
				
				new Driver(args[0]);
			}else{
				System.out.println("Incorrect usage");
				printHelp();
			}
		}else{
			//Try InputFile_1 or InputFile_2 for
			//exciting times
			new Driver("files/InputFile.txt");
		}
		
	}
}
