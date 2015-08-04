package shared;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StartFenster extends JFrame implements ActionListener {
	private JLabel oben;
	private JRadioButton serverRadio;
	private JLabel portClient;
	private JLabel portServer;
	private JTextField textPortServer;
	private JTextField textPortClient;
	private JLabel serverIP;
	private JTextField ipTextField;
	private JRadioButton clientRadio;
	private JLabel nickname;
	private JTextField nicknameText;
	private JTextArea fehlerLog;
	private JButton start;
	
	
	public StartFenster() {
		setLayout(new BorderLayout());
		JPanel obenP = new JPanel();
		oben = new JLabel("Wollen Sie als Client oder als Server starten?");
		obenP.add(oben);
		JPanel centerP = new JPanel(new BorderLayout());
		ButtonGroup g = new ButtonGroup();
		serverRadio = new JRadioButton("Server");
		serverRadio.addActionListener(this);
		serverRadio.setSelected(true);
		g.add(serverRadio);
		portClient = new JLabel("port");
		textPortClient = new JTextField(6);
		textPortServer = new JTextField(6);
		clientRadio = new JRadioButton("Client");
		clientRadio.addActionListener(this);
		g.add(clientRadio);
		JPanel serverPanel = new JPanel(new GridLayout(2,1));
		JPanel serverPanel1 = new JPanel();
		JPanel serverPanel2 = new JPanel();
		start = new JButton("Start");
		start.addActionListener(this);
		JPanel westPanel = new JPanel();
		westPanel.add(serverRadio);
		westPanel.add(portClient);
		westPanel.add(textPortServer);
		westPanel.add(clientRadio);
		centerP.add(westPanel, BorderLayout.WEST);
		centerP.add(serverPanel, BorderLayout.CENTER);
		centerP.add(start, BorderLayout.EAST);
		serverIP = new JLabel("Server-IP");
		ipTextField = new JTextField(20);
		portServer = new JLabel("port");
		serverPanel1.add(serverIP);
		serverPanel1.add(ipTextField);
		serverPanel2.add(portServer);
		serverPanel2.add(textPortClient);
		serverPanel.add(serverPanel1);
		serverPanel.add(serverPanel2);
		JPanel nicknameP = new JPanel();
		nickname = new JLabel("Nickname");
		nicknameText = new JTextField(30);
		nicknameP.add(nickname);
		nicknameP.add(nicknameText);
		JPanel oberste = new JPanel(new BorderLayout());
		oberste.add(obenP, BorderLayout.NORTH);
		oberste.add(centerP, BorderLayout.CENTER);
		add(oberste, BorderLayout.NORTH);
		add(nicknameP, BorderLayout.CENTER);
		fehlerLog = new JTextArea(10, 30);
		add(fehlerLog, BorderLayout.SOUTH);
		fehlerLog.setEnabled(false);
		ipTextField.setEnabled(false);
		textPortClient.setEnabled(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")) {
			if(serverRadio.isSelected())
			{
				if(testNickname() && testPort())
					startServer();
			}
			else
			{			
				if(testNickname() && testPort() && ipTest())
					startClient();
			}
		} else if(e.getActionCommand().equals("Server")) {
			ipTextField.setEnabled(false);
			textPortClient.setEnabled(false);
			textPortServer.setEnabled(true);
			
		} else if(e.getActionCommand().equals("Client")) {
			ipTextField.setEnabled(true);
			textPortClient.setEnabled(true);
			textPortServer.setEnabled(false);
		}
		
	}
	
	private boolean ipTest() {
		String ip = ipTextField.getText();
		boolean matched = ip.matches("\\d+.\\d+.\\d+.\\d+");
		if(!matched) {
			new MessageWindow(this, "nicht gültige IP");
			return false;
		}
		
			
		String[] Zahlen = ip.split(".");
		int a;
		try {
			for (int i = 0; i < Zahlen.length; i++) {
				a = Integer.parseInt(Zahlen[i]);
				if(a < 0 || a > 255) {
					new MessageWindow(this, "nicht gültige IP");
					return false;
				}
			}
			
		} catch (Exception e) {
			new MessageWindow(this, "nicht gültige IP");
			return false;
		}
		return true;
		
	}
	
	private boolean testPort() {
		int p;
		String port;
		if(serverRadio.isSelected()) {
			port = textPortServer.getText();
		} else {
			port = textPortClient.getText();
		}
		try {
			p = Integer.parseInt(port);
		} catch (Exception e) {
			String fehler = "ungueltiger Port";
			new MessageWindow(this, fehler);
			fehlerLog.append(fehler + "\n");
			return false;
		}
	
		if(p < 0 && p >= 65535) {
			String fehler = "ungueltiger Port";
			new MessageWindow(this, fehler);
			fehlerLog.append(fehler + "\n");
			return false;
		}
		return true;
	}
	
	private boolean testNickname() {
		String nickname = nicknameText.getText();
		if(nickname.equals("")) {
			String fehler = "Nickname angeben";
			new MessageWindow(this, fehler);
			fehlerLog.append(fehler + "\n");
			return false;
		}
		return true;
	}
	
	public void startServer()  {
		try {
			new Chat(Integer.parseInt(textPortServer.getText()), nicknameText.getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startClient() {
		try {
			new Chat(ipTextField.getText(), Integer.parseInt(textPortServer.getText()), 
					nicknameText.getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
