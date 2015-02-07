CS313-ACE3 - Virtual Memory
=========
##
###Description##

Made for the class CS313 at the University of Strathclyde.

This aim of this assignment is to gain an understanding of the memory systems currently being used by operating systems.  

To do this a basic emulation of the hardware was created, to which different paging algorithms can be applied to and statistics can be recorded.  

In addition to the above task I wanted to learn javaFX. Since I had just completed this project, and it was still in my head, I decided to skin it.

###Running the program###

There are two main methods present in the source.

Method one is part of the package text control. This starts the terminal version of the application. This version accepts the input file as an argument at runtime or the arg "help" for help running the program.
For example - 
	java -jar ACE3-text.jar 'filepath'

Method two is part of the package ui. This starts the javaFX thread and is the GUI version of the application. This version allows the user to easily customise the configuration of the memory system, but is limited to only three input files due to time constraints. Further more it is far from the prettiest thin in the world due the time constraints as well. Due to this being javaFX an ant build is required.

It is important that the the required files to run this program are included in a folder called files, in the execution directory. These files are required for the application to run. 

###Author###

Thomas Maxwell		- gvb12182@uni.strath.ac.uk

###License###

Copyright 2014 
This project can not be copied and/or distributed without the express permission of the author.
