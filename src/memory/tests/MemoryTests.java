package memory.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import memory.MMU;
import memory.MemorySubSystem;

import org.junit.Test;

/**
 * These tests where hard to do due to just
 * about every part of the memory system being 
 * packaged protected in the memory package.
 * While the above is good practise since 
 * we want to prevent access to the components,
 * it does make testing a headache.
 * 
 * This could be solved by throwing the testing
 * in the same package, but mixing testing and
 * the code is generally a bad idea. Thus an over
 * all testing approach is used.
 *  
 * @author Tom Maxwell
 *
 */
public class MemoryTests {

	/**
	 * A basic test that tests that for the given
	 * input file (minus the 515 at the top since
	 * it is not present in the correct.txt file)
	 * the values that are returned from the memory
	 * system match.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCorrectForGivenInput() throws IOException {
		
		/*
		 * Testing with:
		 * 
		 * TLB - 16
		 * Frame size - 256
		 * Frames - 256
		 * TLB Algo - FIFO
		 * Page Algo - FIFO
		 */
		MemorySubSystem MMU = new MMU(16, 256, memory.Global.algorithm.FIFO, memory.Global.algorithm.FIFO);
		
		/*
		 * Using the standard input file since we have the correct values for it.
		 */
		File input = new File("files/InputFile.txt");
		LineNumberReader reader = null;
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		/*
		 * Get the data back
		 */
		ArrayList<Integer[]> results = new ArrayList<Integer[]>();
		String line;
		while(( line = reader.readLine()) != null){
			
			int address = Integer.parseInt(line);
			
			try{
				int[] value = MMU.readVerbose(address);
				
				//get Integer[] to add to arrayList
				Integer[] returned = {0,0,0};
				returned[0] = Integer.valueOf(value[0]); //address read
				returned[1] = Integer.valueOf(value[1]); //physical address
				returned[2] = Integer.valueOf(value[2]); //value
				
				results.add(returned);
				
			}catch(IOException e){
				fail();
				throw new IOException("Error reading from disk");
			}
		}
		
		
		/*
		 * Load a modified version of the correct file we where given
		 * 
		 * This file has been modified so that it no longer contains any text
		 * to make parsing easy.
		 */
		input = new File("files/correct.txt");
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
			fail();
		}
		
		/*
		 * parse the correct file checking the values against the data we
		 * got back from the system
		 */
		StringTokenizer st;
		int i = 0; // an index to enter the data 
		while(( line = reader.readLine()) != null){
			st = new StringTokenizer(line);
			
			Integer[] expected = results.get(i);
			
			int address = Integer.parseInt(st.nextToken());
			assertEquals("Address must match. Error on line " + reader.getLineNumber(),
					(int) address,(int) expected[0]);
			
			int physicalAddress = Integer.parseInt(st.nextToken());
			assertEquals("Physical addresses must match. Error on line " + reader.getLineNumber(),
					(int) physicalAddress, (int) expected[1]);
			
			int value = Integer.parseInt(st.nextToken());
			assertEquals("Values must match. Error on line " + reader.getLineNumber(),
					(int) value, (int) expected[2]);
			
			i++;
		}	
	}
	
	/**
	 * A test that tests that for the given
	 * input file (minus the 515 at the top since
	 * it is not present in the correct.txt file)
	 * the values that are returned from the memory
	 * system match. This test uses a smaller pageTable
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCorrectForGivenInputTinyPageTable() throws IOException {
		
		/*
		 * Testing with:
		 * 
		 * TLB - 16
		 * Frames - 128
		 * TLB Algo - FIFO
		 * Page Algo - FIFO
		 */
		MemorySubSystem MMU = new MMU(16, 128, memory.Global.algorithm.FIFO, memory.Global.algorithm.FIFO);
		
		/*
		 * Using the standard input file since we have the correct values for it.
		 */
		File input = new File("files/InputFile.txt");
		LineNumberReader reader = null;
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
			fail();
		}
		
		/*
		 * Get the data back
		 */
		ArrayList<Integer[]> results = new ArrayList<Integer[]>();
		String line;
		while(( line = reader.readLine()) != null){
			
			int address = Integer.parseInt(line);
			
			try{
				int[] value = MMU.readVerbose(address);
				
				//get Integer[] to add to arrayList
				Integer[] returned = {0,0,0};
				returned[0] = Integer.valueOf(value[0]); //address read
				returned[1] = Integer.valueOf(value[1]); //physical address
				returned[2] = Integer.valueOf(value[2]); //value
				
				results.add(returned);
				
			}catch(IOException e){
				throw new IOException("Error reading from disk");
			}
		}
		
		
		/*
		 * Load a modified version of the correct file we where given
		 * 
		 * This file has been modified so that it no longer contains any text
		 * to make parsing easy.
		 */
		input = new File("files/correct.txt");
		try {
			reader  = new LineNumberReader(new FileReader(input));
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
			fail();
		}
		
		/*
		 * parse the correct file checking the values against the data we
		 * got back from the system
		 */
		StringTokenizer st;
		int i = 0; // an index to enter the data 
		while(( line = reader.readLine()) != null){
			st = new StringTokenizer(line);
			
			Integer[] expected = results.get(i);
			
			int address = Integer.parseInt(st.nextToken());
			assertEquals("Address must match. Error on line " + reader.getLineNumber(),
					(int) expected[0], (int) address);
			
			/*
			 * Note:
			 * 
			 * Since the physical memory is smaller than the virtual
			 * memory it is likely that you will run out of frames
			 * to store data in. As such they will be replaced.
			 * This means that the physical addresses proved are no
			 * longer correct once we have started to replace frames
			 * as such it can not be tested.
			 * 
			 * We still need to deal with the token tho
			 */
			st.nextToken();
			
			int value = Integer.parseInt(st.nextToken());
			assertEquals("Values must match. Error on line " + reader.getLineNumber(),
					(int) expected[2], (int) value);
			
			i++;
		}	
	}
}
