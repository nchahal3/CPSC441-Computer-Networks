/**
 * Name: Navroop Chahal
 * Date: March 13 2021
 * UCID: 304045291
 * 
 * Assignment 3
 */
/**
 * WebServer Class
 * 
 * Implements a multi-threaded web server
 * supporting non-persistent connections.
 * 
 * @author 	Majid Ghaderi
 * @version	2021
 *
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class WebServer extends Thread {
	private static final Logger logger = Logger.getLogger("WebServer"); // global logger

	private final int SERVER_PORT;
	private boolean keepListening = true;
	
	private ServerSocket serverSocket;

	private ExecutorService pool;

	private final int poolSize = 20; // the thread size 
    /**
     * Constructor to initialize the web server
     * 
     * @param port 	The server port at which the web server listens > 1024
     * 
     */
	public WebServer(int port) {
		this.SERVER_PORT = port;
	}

	
    /**
	 * Main web server method.
	 * The web server remains in listening mode 
	 * and accepts connection requests from clients 
	 * until the shutdown method is called.
	 *
     */
	public void run() {
        Socket socket = null;

        try {
           serverSocket = new ServerSocket(SERVER_PORT);
           serverSocket.setSoTimeout(5000); //check is shutdown request received after every 5 seconds
           pool = Executors.newFixedThreadPool(poolSize);
	    } catch (IOException e) {
            e.printStackTrace();
	    }
       
        while (keepListening) {
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                	pool.execute(new WorkerThread(socket));
                }
            } catch (IOException e) {
               // logger.severe("I/O error: " + e);
            }            
        }
	}
	

    /**
     * Signals the web server to shutdown.
	 *
     */
	public void shutdown() {
		logger.info("Shutdown request receieved for server");
		pool.shutdown(); // shutting down the threads
		logger.info("No more requests will be entertained");
		try {
			logger.info("Waiting for already processing requests for 60 seconds");
			pool.awaitTermination(60, TimeUnit.SECONDS); // shuts down after 60 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Terminating already in progress requests");
		pool.shutdownNow();
		keepListening = false; // if its shutdown then no need to listen to the request
	}
	
}