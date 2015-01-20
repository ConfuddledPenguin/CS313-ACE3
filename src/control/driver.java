package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import memory.MMU;
import memory.MemorySubSystem;

public class driver {

	public static void main(String[] args) {
		
		MemorySubSystem mmu = new MMU();
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
				byte value = mmu.read(address);
				System.out.println(value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
