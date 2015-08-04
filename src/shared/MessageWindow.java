package shared;
import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MessageWindow extends JDialog implements ActionListener {
	
	private JButton ok;
	private JLabel message;
	
	public MessageWindow(Frame owner, String nachricht) {
		super(owner, true);
		setLayout(new BorderLayout());
		ok = new JButton("OK");
		ok.addActionListener(this);
		message = new JLabel(nachricht);
		this.add(message, BorderLayout.NORTH);
		this.add(ok, BorderLayout.CENTER);
		pack();
		setVisible(true);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK")) {
			dispose();
		}
		
	}

}
