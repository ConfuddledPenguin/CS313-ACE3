package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import memory.MMU;
import memory.MemorySubSystem;
import memory.StatsInterface;
import memory.Global;

public class driver {

	private StatsInterface stats;
	
	public driver() {

		MemorySubSystem mmu = null;
		try {
			mmu = new MMU(16, 256, 128, Global.algorithm.RANDOM, Global.algorithm.RANDOM);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		stats = mmu.getStats();
		
//		mmu.read(27966);
//		mmu.read(64243);

		File input = new File("files/InputFile_3.txt");
		LineNumberReader reader = null;
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String line;
			while((line = reader.readLine()) != null){
				
				int address = Integer.parseInt(line);
//				System.out.print(reader.getLineNumber() + " \t");
				int value = mmu.read(address);
				System.out.println(value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\n\nFile processed. There were:\n");
		System.out.println("total reads " + stats.getTotalReads());
		System.out.println("total TLB misses " + stats.getTLBMisses());
		System.out.println("total page faults " + stats.getPageFaults());
		System.out.println("total bytes read from disk " + stats.getBytesRead());
		
		System.out.println("\ntotal TLB hits " + stats.getTLBHits());
		System.out.println("total page hits " + stats.getPageHits());
		
		System.out.println("\ntotal TLB hits % " + stats.getTLBHitPercentage());
		System.out.println("total page hits % " + stats.getPageHitPercentage());
		
	}
	
	public static void main(String[] args) {
		
		new driver();
		
	}
}
