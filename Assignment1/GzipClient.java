
/*
 * GzipClient Class
 * 
 * Assignment 1
 * Name: Navroop Chahal
 * Date: Feb 13 2021
 * 
 */


import java.util.logging.*;
import java.io.*;
import java.net.Socket;

public class GzipClient {

	private static final Logger logger = Logger.getLogger("GzipClient"); // global logger
	public int MAX_BUFF_SIZE = 1000; // Default value of MAX_BUFF_SIZE
	public String serverName = "localhost"; // Inputing localhost as the name of serverName
	public int serverPort = 2025; // The default value of serverPort 2025

	/**
	 * Constructor to initialize the class.
	 * 
	 * @param serverName	remote server name
	 * @param serverPort	remote server port number
	 * @param bufferSize	buffer size used for read/write
	 */
	public GzipClient(String serverName, int serverPort, int bufferSize) {
	//Initializing the arguments with with conditions as explained on the README.txt
		this.serverName = serverName;

		if(serverPort < 64000 && serverPort > 1024) {
			this.serverPort = serverPort;
		}
		if(bufferSize > 0) {
			this.MAX_BUFF_SIZE = bufferSize;
		}

	}
	
	/**
	 * Compress the specified file via the remote server.
	 * 
	 * @param inName		name of the input file to be compressed
	 * @param outName		name of the output compressed file
	 */
	public void gzip(String inName, String outName) {
		if(true) {
			try {

				Socket socket = new Socket(serverName, serverPort); // Making a socket to Establishes a TCP connection with the server

				BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream()); // Output stream to read the data from the server for the Gzip file

				BufferedInputStream input = new BufferedInputStream(socket.getInputStream()); // Input Stream to receive data from the server for the Gzip file

				Thread read = new Thread(new Read(socket, inName, MAX_BUFF_SIZE, output));  // Implementing threads to avoid deadlocks

				Thread write = new Thread(new Write(socket, outName, MAX_BUFF_SIZE, input)); //Implementing threads to avoid deadlocks

				read.start(); // Starts the thread for Read and executes the reading portion of the data 

				write.start(); // Starts the thread for write and executes the write portion of the data

			
				try {
					//Waits for Read and Write thread to end
					read.join(); 

					write.join();

				} catch(InterruptedException e){
					System.out.println("Server Interrupted!");
					System.out.println("Error: " + e.getMessage());
					
				}
				//Closing the socket streams
				output.close(); 
				input.close();
				socket.close();
			
			}
			catch(IOException e) { // Error check for IO error
				System.out.println("IO error!");
				System.out.println("Error: " + e.getMessage());
			
			}
			catch (Exception e) { // Error checking
				System.out.println("Error: " + e.getMessage());
			}		
		}		
	}

}

/**
 * IdleTcp class
 * CPSC 441
 *
 * To open multiple TCP connections
 * to a given server at a given port.
 * Can be used to check a server's 
 * multi-threading functionality
 *
 *
 * @author: Majid Ghaderi
 */