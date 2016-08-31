package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import secure.message.transaction.utility.RequestFocusListener;
import secure.message.transaction.utility.UIMessage;


public class SelfSingCertificateView {
	
	
	int publicKeyobject;
	
	JTextField tx_label = new JTextField();
	JTextField tx_firstName = new JTextField();
	JTextField tx_surName = new JTextField();
	JTextField tx_emailAddress = new JTextField();
	JTextField tx_department = new JTextField();
	JTextField tx_organization = new JTextField();
	JTextField tx_city = new JTextField();
	JTextField tx_state = new JTextField();
	JTextField tx_country = new JTextField();
	

	/**
	 * @wbp.parser.entryPoint
	 */
	public String[] createSelfSignCertificate(){
				
		JLabel label = new JLabel("* Label:");
		JLabel firstName = new JLabel("* First Name:");
		JLabel surName = new JLabel("* Surname:");
		JLabel emailAddress = new JLabel("* Email Address:");
		JLabel department = new JLabel("  Department:");
		JLabel organization = new JLabel("  Organization:");
		JLabel city = new JLabel("  City:");
		JLabel state = new JLabel("  State:");
		JLabel country = new JLabel("* Country:");

		// Set default value
		tx_label.addAncestorListener(new RequestFocusListener());
		
		
		JPanel subjectInfo = new JPanel(new GridLayout(0,1));
		subjectInfo.add(label); 		subjectInfo.add(tx_label);
		
		subjectInfo.add(firstName); 	subjectInfo.add(tx_firstName);
		subjectInfo.add(surName); 		subjectInfo.add(tx_surName);
		
		subjectInfo.add(emailAddress);	subjectInfo.add(tx_emailAddress);
		subjectInfo.add(department); 	subjectInfo.add(tx_department);
		
		subjectInfo.add(organization); subjectInfo.add(tx_organization);
		subjectInfo.add(state); 		subjectInfo.add(tx_state);
		
		subjectInfo.add(city); 		subjectInfo.add(tx_city);
		subjectInfo.add(country); 		subjectInfo.add(tx_country);
		
		JPanel subjectAndObjectInfoPanel = new JPanel(new BorderLayout());
		subjectAndObjectInfoPanel.setBorder(BorderFactory.createTitledBorder("Basic Infromation"));
		subjectAndObjectInfoPanel.add(subjectInfo, BorderLayout.PAGE_START);
		
		JPanel panelSelfSignCertificate = new JPanel(new BorderLayout());
		
		panelSelfSignCertificate.add(subjectAndObjectInfoPanel, BorderLayout.PAGE_START);
		
		String title = "Self-Signed Certificate";
		int response = JOptionPane.showConfirmDialog(null, panelSelfSignCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		String result[] = new String[2];
		String x509String = "";
		String certLabel = null;
		while (response == 0){

			try {
			
				
				
				String message = selfSignCertificateValidator();
				if(message == null){
					x509String = (tx_firstName.getText().length() > 0 ? "CN=" + tx_firstName.getText() : "" ) +  
							(tx_emailAddress.getText().length() > 0 ? ", EmailAddress=" + tx_emailAddress.getText() : "") + 
							(tx_department.getText().length() > 0 ? ", OU=" + tx_department.getText() : "") + 
							(tx_organization.getText().length() > 0 ? ", O=" + tx_organization.getText() : "") +
							(tx_city.getText().length() > 0 ? ", L=" + tx_city.getText() : "") +
							(tx_country.getText().length() > 0 ? ", C=" + tx_country.getText() : "");
					
					certLabel = tx_label.getText().toString();
					result[0] = certLabel;
					result[1] = x509String;
					return result;
				} else {
					UIMessage.showWarningMessage(message);
					response = JOptionPane.showConfirmDialog(null, panelSelfSignCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
						return null;
					}
				}
			
			} catch (Exception e){
				response = JOptionPane.showConfirmDialog(null, panelSelfSignCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
					return null;
				}
			}
		}
		
		return null;
		
	}
	
	public String selfSignCertificateValidator(){
		String message = null;
		if(tx_firstName.getText().length() == 0){
			message = "Enter frist name.";
		} else if (tx_surName.getText().length() == 0){
			message = "Enter sur name.";
		} else if (tx_emailAddress.getText().length() == 0){
			message = "Enter email address.";
		} else if (tx_country.getText().length() == 0){
			message = "Enter country.";
		} 
		return message;
	}
	
	
	
}


