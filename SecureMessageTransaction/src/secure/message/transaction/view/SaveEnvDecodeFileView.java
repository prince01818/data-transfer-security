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
import secure.message.transaction.utility.FileUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.utility.ViewUtil;



public class SaveEnvDecodeFileView{

	JTextField tx_certificatePah = new JTextField();
	JButton btOpenCertificate = new JButton("Save");
	String _fileName = "";
	/**
	 * @wbp.parser.entryPoint
	 */
	public String saveEnvFile(String fileName){
		
		try {
			_fileName = FileUtility.removeExtention(fileName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel certPath = new JLabel("Save To:");

		//tx_label.setText(certificateLabel);
		
		JPanel certLabel = new JPanel(new BorderLayout());
		
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
		
		String title = "Download and decrypt enveloped data";
		int response = JOptionPane.showConfirmDialog(null, panelExportCertificate, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		String result = "";
		while (response == 0){
			try {
				// Reset all data of CertificateInputData 
				String message = selfSignCertificateValidator();
				if(message == null){
					result = tx_certificatePah.getText();
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
		if(tx_certificatePah.getText().length() == 0){
			message = "Select destination path";
		}
		return message;
	}
	
	class SaveCertificate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File(_fileName));

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
