package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

	private boolean running = true;
	private Thread serverThread;
	private int port;
	private ServerSocket server;
	private ConnectionContainer connectionContainer;
	
	public ChatServer(int port) throws IOException {
		this.port = port;
		connectionContainer = ConnectionContainer.instance();
		server = new ServerSocket(port);
		start();
	}
	@Override
	public void run() {
		while(running) {
			try {
				Socket connection = server.accept();
				new Connection(connection, connectionContainer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void start() {
		serverThread = new Thread(this);
		serverThread.start();
	}
	
	public void kill() {
		running = false;
		serverThread.interrupt();
	}
	
	
}
