package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import secure.message.transaction.db.objects.UserInfo;


@SuppressWarnings("serial")
public class UserConfigureView extends JFrame{

	JTextField tx_userName = new JTextField();
	JPasswordField tx_userPIN = new JPasswordField();
	JTextField tx_certificatePah = new JTextField();
	JButton btOpenCertificate = new JButton("Open");
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		JLabel labelUserName = new JLabel("User Name:");
		JLabel labelUserPIN = new JLabel("PIN:");
		
		JPanel recName = new JPanel(new GridLayout(0,1));
		recName.add(labelUserName);
		recName.add(tx_userName);
		
		JPanel recPIN = new JPanel(new GridLayout(0,1));
		recPIN.add(labelUserPIN);
		recPIN.add(tx_userPIN);

		JPanel panelAddRecipient = new JPanel(new GridLayout(0, 1));
		
		panelAddRecipient.add(recName);
		panelAddRecipient.add(recPIN);
		
		String title = "User Configuration";
		int response = JOptionPane.showConfirmDialog(null, panelAddRecipient, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		while(response == 0) {
			userInfo.setUserName(tx_userName.getText());
			userInfo.setPIN(String.valueOf(tx_userPIN.getPassword()));
			return userInfo;
		}
		return null;
	
	}
	
}
