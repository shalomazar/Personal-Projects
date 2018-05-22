Fat32 Utility Reader
by Noah Potash and Shalom Azar

 
Files/directories:

	• fat32Reader.java - Main class that reads the fat32 image. The fat32 utility reader.
	• DirectoryObj.java - Class Object of the entire current directory. A list of all the directory entries/files.
	• DirEntry.java - Class Object that contains all the info for each individual directory entry.
 
Instructions for compiling program:
	• Step1: Once inside of the directory that contains the files of the project.
	type ```javac *.java``` to compile all the files.
	• Step2: To run type ```java -cp . fat32Reader <absolute_path of fat32 Image>```.

Challenges encountered along the way:

	• Not understanding the fat32 spec without reading it over several times.
	• Figuring out where the root directory, the current directory, and the FAT were.
	• Dealing with signed and unsigned ints and converting to little endian in java.
	• Reading across multiple clusters.
	• Writing to a file and the fat table.
	• The time and date of a newly created file.
	

Sources used:
	
	• https://www.pjrc.com/tech/8051/ide/fat32.html
	• Fat32 spec pdf
	• Used a hexeditor to look at the fat32 image
	• http://slideplayer.com/slide/3592883/
