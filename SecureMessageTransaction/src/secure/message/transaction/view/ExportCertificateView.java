package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.utility.ViewUtil;



public class ExportCertificateView{

	JTextField tx_certificatePah = new JTextField();
	JButton btOpenCertificate = new JButton("Save");
	JTextField tx_label = new JTextField();
	/**
	 * @wbp.parser.entryPoint
	 */
	public String[] exportCertificate(){
		
		
		
		JLabel label = new JLabel("Label: ");
		JLabel certPath = new JLabel("Save To:");

		//tx_label.setText(certificateLabel);
		
		JPanel certLabel = new JPanel(new BorderLayout());
		certLabel.add(label, BorderLayout.PAGE_START);
		certLabel.add(tx_label, BorderLayout.CENTER);
		
		JPanel certificatePath = new JPanel(new BorderLayout());
		certificatePath.add(certPath, BorderLayout.PAGE_START);
		certificatePath.add(tx_certificatePah, BorderLayout.CENTER);
		certificatePath.add(btOpenCertificate, BorderLayout.LINE_END);
		
		btOpenCertificate.addActionListener(new SaveCertificate());
		
		
		JPanel panelExportCertificate = new JPanel(new BorderLayout());
		
		panelExportCertificate.add(certLabel, BorderLayout.PAGE_START);
		panelExportCertificate.add(certificatePath, BorderLayout.PAGE_END);
		
		panelExportCertificate.setPreferredSize(new Dimension(panelExportCertificate.getPreferredSize().width, 80));
		
		ViewUtil viewUtil = new ViewUtil();
		viewUtil.setPreferredSize(panelExportCertificate);
		
		String title = "Export Certificate";
		int response = JOptionPane.showConfirmDialog(null, panelExportCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		String [] result = new String[2];
		while (response == 0){
			try {
				// Reset all data of CertificateInputData 
				String message = selfSignCertificateValidator();
				if(message == null){
					result[0] = tx_label.getText();
					result[1] = tx_certificatePah.getText();
					return result;
				} else {
					UIMessage.showWarningMessage(message);
					response = JOptionPane.showConfirmDialog(null, panelExportCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
						return null;
					}
				}
			
			} catch (Exception e){
				response = JOptionPane.showConfirmDialog(null, panelExportCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
					return null;
				}
			}
		}
		
		return null;
		
	}
	
	public String selfSignCertificateValidator(){
		String message = null;
		if(tx_label.getText().length() == 0){
			message = "Enter Label.";
		}else if(tx_certificatePah.getText().length() == 0){
			message = "Select path to export";
		}
		return message;
	}
	
	class SaveCertificate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File(tx_label.getText()+".crt"));

			JFrame frameForIcon = new JFrame();
			ImageIcon imageIcon = new ImageIcon(Constant.APP_ICON);
			frameForIcon.setIconImage(imageIcon.getImage());
			
			int rVal = fileChooser.showSaveDialog(frameForIcon);
			if(rVal == JFileChooser.APPROVE_OPTION){
				tx_certificatePah.setText(fileChooser.getCurrentDirectory().toString()+Utility.getFileSeparator()+fileChooser.getSelectedFile().getName());
			} else if(rVal == JFileChooser.CANCEL_OPTION){
				tx_certificatePah.setText("");
			}
		}
		
	}
}
