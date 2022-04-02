
/**
 * HttpClient Class
 * 
 * Assignment 2
 * Date: Feb 26th 2021
 */


import java.util.logging.*;
import java.io.*;
import java.net.Socket;

public class HttpClient {
	//Variables
	 public String host;
	 public int port;
	 public String filepath;
	 public int MAX_BUFF_SIZE = 16*1024;
	 public InputStream inputStream;
	 public String str_header = " ";
	 public OutputStream outputStream;
	 public String header = null;

	private static final Logger logger = Logger.getLogger("HttpClient"); // global logger

    /**
     * Default no-arg constructor
     */
	public HttpClient() {

		// nothing to do!
	}
	
    /**
     * Downloads the object specified by the parameter url.
	 *
     * @param url	URL of the object to be downloaded. It is a fully qualified URL.
     */
	@SuppressWarnings("deprecation")
	public void get(String url) {
		
	//This right here parses the input url 
		String [] parts = url.split("/", 2);
		
		// If the url has a specified port
		if(parts[0].contains(":")){
			 String[] hostandport = parts[0].split(":",2);
			 
			 host = hostandport[0]; // Gets the hostname
			 
			 port = Integer.parseInt(hostandport[1]); // Gets the port number
			 

			 filepath = "/" + parts[1]; // Pathname
		}
		
		else { // if the url does not have a port specified 
			host = parts[0];
			port = Integer.parseInt("80");
			filepath = "/" + parts[1];

		}
		
		try {
			
			Socket socket = new Socket(host, port); // Establishes a TCP connection
			System.out.println("Connected to the Server...");
		
			// Setting up get request line			
			DataOutputStream outputStream = new DataOutputStream (socket.getOutputStream()); // Sends/Write stuff to socket
			
			String Http_Request = "GET "+ filepath +" HTTP/1.1" + "\r\n" + "Host:"+ host + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
			System.out.println("Request line set: " + Http_Request);
			byte[] Bytes = Http_Request.getBytes("US-ASCII");
			
			
			outputStream.write(Bytes);
			outputStream.flush();
			
			
			//Setting up HTTP response header
			DataInputStream inputStream = new DataInputStream (socket.getInputStream());
			while(true) {
				
				str_header = inputStream.readLine();
				header += str_header;
				System.out.println(str_header);
				
				if (str_header.isEmpty()) {
					break;
					}
		        }
			
			
			// Time to download the file if it contained the response 200OK
			if(header.contains("200 OK")) {
				
			
				// Splitting it to get the filename from the url
				String [] parts1 = url.split("/", 4);
				String filename = parts1[3];
				
				try{
					
					System.out.println("Downloading the file " + filename);
					FileOutputStream file = new FileOutputStream(filename); 
					byte [] buff = new byte [MAX_BUFF_SIZE];
					
					int readBytes = 0;
					//Reads the file from the input and writes it to the socket
					while ( (readBytes = inputStream.read(buff)) != -1) {
			
					
						file.write(buff, 0, readBytes);
	
						file.flush();
				}
					
					file.close();	 // Closes the socket output stream
					socket.close();
				} 
				catch (IOException e) { // Error checking for IO 
				 	System.out.println("Input/Output Exception error");
					e.printStackTrace();
				}
			}
			
			else { // If its not 200 OK then there is an issue with the URL 
				System.out.println("URL Error!!");
				
			}
			// Closing the socket and input stream
			socket.close(); 
			inputStream.close();
			

		}
		catch (Exception e) { // Error checking
			System.out.println("Error: " + e.getMessage());
		}
		
	}

}
