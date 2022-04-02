/*
 * Name: Navroop Chahal
 * Date: Feb 13 2021
 */
import java.io.*;
import java.net.Socket;
import java.util.*;


public class Write extends Thread { // Implementing thread to avoid deadlocks
	public int MAX_BUFF_SIZE;
	private String outName;
	private Socket socket;
	private BufferedInputStream input; 

	/**
	 * Constructor to initialize the class.
	 * 
	 * @param socket	
	 * @param outName	
	 * @param Buffersize
	 * @param output
	 */
	public Write ( Socket socket, String outName, int Buffersize, BufferedInputStream input) {
		this.outName = outName;
		this.socket = socket;
		this.MAX_BUFF_SIZE = Buffersize;
		this.input = input;
	}	
	
	public void run() {
		
		try {
			
			FileOutputStream output = new FileOutputStream(outName);
		
			byte [] buff = new byte [MAX_BUFF_SIZE];
			
			int readBytes = 0;
			//Reads the file from the input and writes it to the socket
			while ( (readBytes = input.read(buff)) != -1) {
			
				output.write(buff, 0, readBytes);

				System.out.println("W " + readBytes);
				output.flush(); // Not needed but good practice
		}
			
			output.close();	 // Closes the socket output stream
		 
	 }
		catch (IOException e) { // Error checking for IO 
		 	System.out.println("Input/Output Exception error");
			e.printStackTrace();
		}
	}


	

}
