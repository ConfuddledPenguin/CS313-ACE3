package memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * The hard disk for the memory system.
 * 
 * A file represents the hard disk, when data
 * is requested this file is accessed to locate
 * it.
 * 
 * It is not possible to write to the disk.
 * 
 * @author Tom Maxwell
 *
 */
class Disk {

	/**
	 * The disk as a random access file
	 * to allow for quick searching through
	 * the disk.
	 */
	private RandomAccessFile disk;
	
	/**
	 * The constructor
	 * 
	 * @throws FileNotFoundException Disk not found
	 */
	public Disk() throws FileNotFoundException {
		
		File file = new File("files/BACKING_STORE");
		
		disk = new RandomAccessFile(file, "r");
		
	}
	
	/**
	 * Reads data from the disk.
	 * The data is read starting from the address and 
	 * noBytes bytes are read from the disk.
	 * 
	 * @param address The address to read from
	 * @param noBytes The number of bytes to read
	 * @return The bytes
	 * @throws IOException Disk yanked out during operation
	 */
	public Byte[] read(int address, int noBytes) throws IOException {
		
		byte[] readBytes = new byte[noBytes];
		
		disk.seek(address);
		
		disk.read(readBytes);
		
		Byte[] returnBytes = new Byte[noBytes];
		for(int i = 0; i < readBytes.length; i++){
			returnBytes[i] = readBytes[i];
		}
		
		return returnBytes;
	}
	
}
