package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import secure.message.transaction.utility.CertificateUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;


@SuppressWarnings("serial")
public class RecipientAddView extends JFrame{

	JTextField tx_recipientName = new JTextField();
	JTextField tx_recipientEmail = new JTextField();
	JTextField tx_certificatePah = new JTextField();
	JButton btOpenCertificate = new JButton("Open");
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public String[] getRecipientAddData(){

		JLabel label = new JLabel("Recipient Name:");
		JLabel publicKey = new JLabel("Recipient Email:");
		JLabel certPath = new JLabel("Recipient Certificate:");

		tx_recipientName.setEditable(false);
		tx_recipientEmail.setEditable(false);
		
		JPanel recName = new JPanel(new GridLayout(0,1));
		recName.add(label);
		recName.add(tx_recipientName);
		
		JPanel recEmail = new JPanel(new GridLayout(0,1));
		recEmail.add(publicKey);
		recEmail.add(tx_recipientEmail);
		
		JPanel certificatePath = new JPanel(new GridLayout(0,1));
		
		JPanel recipientCertificateBrowse = new JPanel(new BorderLayout());

		recipientCertificateBrowse.add(tx_certificatePah,BorderLayout.CENTER);
		recipientCertificateBrowse.add(btOpenCertificate,BorderLayout.LINE_END);
		certificatePath.add(certPath);
		certificatePath.add(recipientCertificateBrowse);
		
		btOpenCertificate.addActionListener(new OpenCertificate());
		
		
		JPanel panelAddRecipient = new JPanel(new GridLayout(0, 1));
		panelAddRecipient.add(certificatePath);
		panelAddRecipient.add(recName);
		panelAddRecipient.add(recEmail);
		
		
		String title = "Recipient Add";
		int response = JOptionPane.showConfirmDialog(null, panelAddRecipient, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		while(response == 0) {
			if(tx_certificatePah.getText().length() == 0){
				UIMessage.showWarningMessage("Select valied certificate");
				response = JOptionPane.showConfirmDialog(null, panelAddRecipient, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				continue;
			}
			String[] result = new String[3];
			result[0] = tx_recipientName.getText();
			result[1] = tx_recipientEmail.getText();
			result[2] = tx_certificatePah.getText();
			return result;
		}
		return null;
	
	}
	
	class OpenCertificate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("", "crt","cert","cer");
			fileChooser.setFileFilter(filter);
			int rVal = fileChooser.showOpenDialog(RecipientAddView.this);
			if(rVal == JFileChooser.APPROVE_OPTION){
				tx_certificatePah.setText(fileChooser.getCurrentDirectory().toString()+Utility.getFileSeparator()+fileChooser.getSelectedFile().getName());
				try {
					X509Certificate cert = CertificateUtility.getCertificate(tx_certificatePah.getText());
					if (cert == null) {
						UIMessage.showErrorMessage("Invalied certificate");
						return;
					}
					String email = CertificateUtility.getEmailAddressFromCertificate(cert);
					String name = CertificateUtility.getCommonNameFromCertificate(cert);

					tx_recipientName.setText(name);
					tx_recipientEmail.setText(email);
				} catch (Exception e1) {
					tx_certificatePah.setText("");
					tx_recipientName.setText("");
					tx_recipientEmail.setText("");
					//UIMessage.showErrorMessage("Invalied certificate");
				}
				
			} else if(rVal == JFileChooser.CANCEL_OPTION){
				tx_certificatePah.setText("");
			}
		}
		
	}
}
