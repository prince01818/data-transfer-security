package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;


@SuppressWarnings("serial")
public class MessageEncryptView extends JFrame{

	//JTextArea tx_inputMessage = new JTextArea("",5,20);
	JTextField tx_filePath = new JTextField();
	JButton btOpenFile = new JButton("Open");
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public String getMessageEncryptData(){

		
		JLabel filePath = new JLabel("Select File:");

		
		JPanel fileBrowsePanel = new JPanel(new BorderLayout());

		fileBrowsePanel.add(tx_filePath,BorderLayout.CENTER);
		fileBrowsePanel.add(btOpenFile,BorderLayout.LINE_END);
		
		JPanel certificatePath = new JPanel(new BorderLayout());
		certificatePath.add(filePath, BorderLayout.PAGE_START);
		certificatePath.add(fileBrowsePanel, BorderLayout.PAGE_END);
		
		btOpenFile.addActionListener(new OpenCertificate());
		
		
		String title = "Message Encrypt";
		int response = JOptionPane.showConfirmDialog(null, certificatePath, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
			return null;
		}
		while (response == 0){
			if(tx_filePath.getText().length() == 0){
				UIMessage.showWarningMessage("Select file");
				response = JOptionPane.showConfirmDialog(null, certificatePath, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			} else {
				return tx_filePath.getText();
			}
		}
		return null;
	}
	
	class OpenCertificate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int rVal = fileChooser.showOpenDialog(MessageEncryptView.this);
			
			JFrame frameForIcon = new JFrame();
			ImageIcon imageIcon = new ImageIcon(Constant.APP_ICON);
			frameForIcon.setIconImage(imageIcon.getImage());
			
			if(rVal == JFileChooser.APPROVE_OPTION){
				tx_filePath.setText(fileChooser.getCurrentDirectory().toString()+Utility.getFileSeparator()+fileChooser.getSelectedFile().getName());
			} else if(rVal == JFileChooser.CANCEL_OPTION){
				tx_filePath.setText("");
			}
		}
		
	}
}
