package shared;

import java.io.IOException;
import java.net.Socket;

import server.ChatServer;
import client.ChatClient;

public class Chat {
	
	
	public Chat(int port, String nick) throws IOException {
		new ChatServer(port);
		createConnection("127.0.0.1", port, nick);
	}
	
	public Chat(String ip, int port, String nick) throws IOException {
		createConnection(ip, port, nick);
	}
	
	private void createConnection(String ip, int port, String nick) throws IOException {
		Socket con = new Socket(ip, port);
		new ChatClient(con, nick);
	}
}
