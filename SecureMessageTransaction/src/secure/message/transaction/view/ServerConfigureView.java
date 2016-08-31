package secure.message.transaction.view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import secure.message.transaction.db.objects.SMBServer;


public class ServerConfigureView{

	JTextField tx_IP = new JTextField();
	JTextField tx_userName = new JTextField();
	JTextField tx_shareFolder = new JTextField();
	JPasswordField tx_userPassword = new JPasswordField();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public SMBServer getSMBServerInputData(SMBServer smbServerInput){
		SMBServer smbServer = smbServerInput;
		JLabel labelUserName = new JLabel("User Name:");
		JLabel labelIPAddress = new JLabel("IP Address:");
		JLabel labelShareFolder = new JLabel("Share Folder:");
		JLabel labelUserPassword = new JLabel("Passwrod:");
		
		tx_userName.setText(smbServer.getUserName()!=null?smbServer.getUserName():"");
		tx_userPassword.setText(smbServer.getUserPassword()!=null?smbServer.getUserPassword():"");
		tx_IP.setText(smbServer.getIdAddress()!=null?smbServer.getIdAddress():"");
		tx_shareFolder.setText(smbServer.getShareFolderName()!=null?smbServer.getShareFolderName():"");
		
		JPanel panelConfigServer = new JPanel(new GridLayout(0, 1));
		panelConfigServer.add(labelUserName);
		panelConfigServer.add(tx_userName);
		
		panelConfigServer.add(labelUserPassword);
		panelConfigServer.add(tx_userPassword);

		panelConfigServer.add(labelIPAddress);
		panelConfigServer.add(tx_IP);
		
		panelConfigServer.add(labelShareFolder);
		panelConfigServer.add(tx_shareFolder);
		
		
		String title = "Server Configuration";
		int response = JOptionPane.showConfirmDialog(null, panelConfigServer, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		while(response == 0) {
			smbServer.setIdAddress(tx_IP.getText());
			smbServer.setUserName(tx_userName.getText());
			smbServer.setUserPassword(String.valueOf(tx_userPassword.getPassword()));
			smbServer.setShareFolderName(tx_shareFolder.getText());
			return smbServer;
		}
		return null;
	
	}
	
}
