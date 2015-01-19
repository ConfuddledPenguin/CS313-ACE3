package control;

import memory.*;

public class driver {

	public static void main(String[] args) {
		
		MemorySubSystem mmu = new MMU();
		mmu.read(27966);
		mmu.read(64243);

	}
}
