package server;

import shared.Protocol;

import java.util.ArrayList;
import java.util.Iterator;

public class ConnectionContainer implements Iterable<Connection >{

	private static ConnectionContainer unique = null;
	private ArrayList<Connection> connections;
	
	private ConnectionContainer() {
		connections = new ArrayList<Connection>();
	}
	
	public static ConnectionContainer instance() {
		if (unique == null) {
			unique = new ConnectionContainer();
		}
		return unique;
	}
	
	public void add(Connection con) {
		if(!connections.contains(con)) {
			connections.add(con);
		}
	}

	@Override
	public Iterator<Connection> iterator() {
		return connections.iterator();
	}
	
	public void removeConnection(Connection con) {
		if(connections.contains(con)) {
			connections.remove(con);
		}
	}
	
	public void send(String message) {
		Iterator<Connection> iterator = iterator();
		while(iterator.hasNext()) {
			iterator.next().sendMsg(message);
		}
	}
	
	
}
