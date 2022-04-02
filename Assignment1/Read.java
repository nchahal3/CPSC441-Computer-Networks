/*
 * Name: Navroop Chahal
 * Date: Feb 13 2021
 */
//This class reads the content from the local file and writes it to the buffer
import java.util.logging.*;
import java.io.*;
import java.net.Socket;

public class Read extends Thread{ // Implementing thread to avoid deadlock
	
	public int MAX_BUFF_SIZE;
	private String inName;
	private Socket socket;
	private BufferedOutputStream output; 

	/**
	 * Constructor to initialize the class.
	 * 
	 * @param socket	
	 * @param inName	
	 * @param Buffersize
	 * @param output
	 */
	public Read ( Socket socket, String inName, int Buffersize, BufferedOutputStream output) {
		//Initializing the variables
		this.inName = inName;
		this.socket = socket;
		this.MAX_BUFF_SIZE = Buffersize;
		this.output = output;
	}

		
	public void run() {
		
		
		try {
			FileInputStream input = new FileInputStream(inName);

			byte [] buff = new byte [MAX_BUFF_SIZE];

			int readBytes = 0;
			//Reads the file from the input and writes it to the socket
			while ( (readBytes = input.read(buff)) != -1) {
			
				output.write(buff, 0, readBytes);
				System.out.println("R " + readBytes); // Reads from the input file
				output.flush();// Not needed but good practice
		}
			// Closes the read socket stream
			socket.shutdownOutput();
			input.close();	

	 } 
		//IO Exception handling
		catch (IOException e) {
		 	System.out.println("Input/Output Exception error");
			e.printStackTrace();
		}
	}



}
