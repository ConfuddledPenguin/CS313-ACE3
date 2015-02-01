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

public class driver {

	private StatsInterface stats;
	
	public driver(String filepath) {

		runMemory(16, 128, algorithm.FIFO, algorithm.FIFO, filepath);
	}
	
	private void runMemory(int TLBSize, int NoFrames, algorithm TLBalgo, algorithm pageAlgo, String filepath){
		
		MemorySubSystem mmu = null;
		try {
			mmu = new MMU(TLBSize, NoFrames, TLBalgo, pageAlgo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error: " + e.getMessage());
		}
		
		stats = mmu.getStats();

		File input = new File(filepath);
		LineNumberReader reader = null;
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
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
				System.out.print("Line number " +reader.getLineNumber() + " \t");
				System.out.println("Value: " + value);
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
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
	
	private static void printHelp() {
		
		System.out.println("Program usage:");
		System.out.println("java -jar ACE3.jar <filepath>");
		System.exit(0);

	}
	
	public static void main(String[] args) {
		
		int noArgs = args.length;
		if(noArgs >= 1){
			if(noArgs == 1){
				
				if(args[0].equals("help"))
					printHelp();
				
				new driver(args[0]);
			}else{
				System.out.println("Incorrect usage");
				printHelp();
			}
		}

		//Try InputFile_1 or InputFile_2 for
		//exciting times
		new driver("files/InputFile.txt");
		
	}
}
