package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import shared.MessageWindow;
import shared.Protocol;




public class ChatClient extends JFrame implements Runnable, KeyListener, ActionListener {
	private String username;
	
	private JLabel members;
	private JLabel chat;
	private JLabel input;
	
	private JTextArea taMembers;
	private JTextArea taInput;
	private JTextArea taChat;
	
	private JButton send;
	private JButton quit;
	private PrintWriter outgoingMsg;
	private BufferedReader incomingMsg;
	private Socket chatSocket;
	private Thread t1;
	private boolean running = true;
	private ArrayList<String> allMembers;
	
	
	
	public ChatClient(Socket connection, String benutzer) throws IOException {
		chatSocket = connection;
		username = benutzer;
		allMembers = new ArrayList<String>();
		
		buildFrame();
		
		outgoingMsg = new PrintWriter(chatSocket.getOutputStream(), true);
		incomingMsg = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
		outgoingMsg.println((Protocol.CLIENT_JOINED));
		
		
		start();
			
		
	}
	
	private void append(String s) {
		taChat.append(s + "\n");
	}
	
	private void start() {
		t1 = new Thread(this);
		t1.start();
		
	}
	
	private void kill() {
		running = false;
	}

	private void buildFrame() {
		setTitle(username + "\'s Chat");
		setLayout(new BorderLayout());
		members = new JLabel("Members");
		chat = new JLabel("chat");
		input = new JLabel("input");
		
		taMembers = new JTextArea(30, 10);
		taChat = new JTextArea(30,50);
		taInput = new JTextArea(6, 60);
		
		send = new JButton("Send");
		send.addActionListener(this);
		quit = new JButton("Quit Chat");
		quit.addActionListener(this);
	
		
		JPanel oben = new JPanel();
		JPanel left = new JPanel(new BorderLayout());
		JPanel right = new JPanel(new BorderLayout());
		left.add(members, BorderLayout.NORTH);
		left.add(taMembers, BorderLayout.CENTER);
		
		right.add(chat, BorderLayout.NORTH);
		right.add(taChat, BorderLayout.CENTER);
		
		oben.add(left);
		oben.add(right);
		
		JPanel center = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.add(input);
		center.add(inputPanel, BorderLayout.NORTH);
		center.add(taInput, BorderLayout.CENTER);
		
		JPanel unten = new JPanel();
		unten.add(send);
		unten.add(quit);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(oben, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(unten, BorderLayout.SOUTH);
		taInput.addKeyListener(this);
		taMembers.setEnabled(false);
		taChat.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void run() {
		while(running) {
			try {
				String s = incomingMsg.readLine();
				if(s.startsWith(Protocol.SERVER_QUIT)) {
					taChat.append("server has quit");
					kill();
				} else if (s.startsWith(Protocol.SERVER_ALL_NICKNAMES)){
					allMembers.clear();
					outgoingMsg.println(Protocol.NICK_IDENTIFIER + username);
					
				} else if(s.startsWith(Protocol.NICK_IDENTIFIER)) {
					String name = s.substring(Protocol.NICK_IDENTIFIER.length());
					if(!allMembers.contains(name))
						allMembers.add(name);
					aktualisiereMembers();
				} else if(s.startsWith(Protocol.CLIENT_QUIT)) {
					String name = s.substring(Protocol.NICK_IDENTIFIER.length());
					allMembers.remove(name);
					aktualisiereMembers();
				} else if(s.startsWith(Protocol.SERVER_NICKNAME_ALREADY_IN_USE)) {
					new MessageWindow(this, "client terminated, nickname is already in use");
					terminateClient();
				} else {
					taChat.append(s + "\n");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
	private void aktualisiereMembers() {
		taMembers.setText("");
		for(String name: allMembers) {
			taMembers.append(name + "\n");
		}
	}
	private void sendMessage()
	{
		String input = taInput.getText();
		if(input.equals("")) {
			new MessageWindow(this, "input kann nicht leer sein");
			return;
		} else if(input.equals("\n")) {
			new MessageWindow(this, "input kann nicht nur enter sein");
			return;
		} 
		taInput.setText("");
		
		outgoingMsg.println(username + ": " + input);
	}
	
	private void terminateClient() {
		try {
			outgoingMsg.println(Protocol.CLIENT_QUIT + username);
			chatSocket.close();
			kill();
			dispose();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendMessage();
			e.consume();
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Send")) {
			sendMessage();
		} else if(e.getActionCommand().equals("Quit Chat")) {
			terminateClient();
		}
		
	}

}
