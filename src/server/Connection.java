package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import shared.Protocol;

public class Connection extends Thread {


		private Socket connection;
		private PrintWriter outgoing;
		private BufferedReader incoming;
		private ConnectionContainer container;
		private static ArrayList<String> names = new ArrayList<String>();
		private boolean running = true;

		public Connection(Socket socket, ConnectionContainer container) throws IOException {
			connection = socket;
			this.container = container;
			container.add(this);
			outgoing = new PrintWriter(socket.getOutputStream(), true);
			incoming = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			start();
	

		}
		
		public void kill() {
			running = false;
			interrupt();
		}
		
		private boolean isNicknameAlreadyInUse(String nick) {
			for(String name : names) {
				if(nick.equals(name)) return true;
			}
			return false;
		}
			

		public String receive() throws IOException {
			
				return incoming.readLine();
			

		}

		public void sendMsg(String s) {
			outgoing.println(s);
		}

		public boolean isConnected() {
			return connection.isConnected() && !connection.isClosed();
		}

		public void stopConnection() {
			try {
				if (isConnected()) {
					connection.close();
					kill();
					
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		@Override
		public void run() {
			while(running) {
				try {
					String s = incoming.readLine();
					if(s.startsWith(Protocol.CLIENT_JOINED)) {
						sendMsg(Protocol.SERVER_ALL_NICKNAMES);
					} else if (s.startsWith(Protocol.CLIENT_QUIT)) {
						container.send(s);
						container.send(Protocol.SERVER_ALL_NICKNAMES);
						stopConnection();
						container.removeConnection(this);
					} else if(s.startsWith(Protocol.NICK_IDENTIFIER)) {
						String name = s.substring(Protocol.NICK_IDENTIFIER.length());
						if(isNicknameAlreadyInUse(name)) {
							sendMsg(Protocol.SERVER_NICKNAME_ALREADY_IN_USE);
							stopConnection();
						} else {
							names.add(name);
							for(String n: names) container.send(Protocol.NICK_IDENTIFIER + n);
						}
							
					}
					
					else {
						container.send(s);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

