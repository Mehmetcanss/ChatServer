package shared;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ChatClient;


public class StartWindow extends JFrame implements ActionListener {
	
	private JButton button;
	
	public StartWindow() {
		super("StartWindow");
		button = new JButton("click");
		button.addActionListener(this);
		add(button);
		pack();
		setVisible(true);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("click")) {
			new MessageWindow(this, "Warning Message");		}
		
	}
	
	public static void main(String args[]) {

		try {
			new StartFenster();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
