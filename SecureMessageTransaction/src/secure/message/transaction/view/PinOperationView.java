package secure.message.transaction.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PinOperationView {

	/**
	 * @wbp.parser.entryPoint
	 */
	public static String getLoginData(){
		
		String responseUserPIN = null;
		
		//setUIFont(new FontUIResource(new Font("Calibri", Font.PLAIN, 13)));
	
		
		JPasswordField userPINTextField = new JPasswordField();
		
		JLabel profileLabel = new JLabel("PIN");
		
		JPanel panelCreateProfile = new JPanel(new GridLayout(0, 1));

		panelCreateProfile.add(profileLabel);
		panelCreateProfile.add(userPINTextField);
		
		
		String userPin = null;
		
		String title = "Login";
		int response = JOptionPane.showConfirmDialog(null, panelCreateProfile, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		while(response == 0) {
			
			userPin = String.valueOf(userPINTextField.getPassword());
			
			if(userPin != null && userPin.length() >= 4 && userPin.length() <= 8 && userPin != "") {
				responseUserPIN = userPin;
				response = 1;
			} else {	
				response = JOptionPane.showConfirmDialog(null, panelCreateProfile, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		}
	
		return responseUserPIN;
	}
}
