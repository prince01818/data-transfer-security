package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class KeyPairGenerationView extends JFrame{

	JTextField tx_keyLabel = new JTextField();
	JTextField tx_keySize = new JTextField();
	String keySize = "1024";
	/**
	 * @wbp.parser.entryPoint
	 */
	public String[] getKeyPairGenerationInputData(){
		JLabel labelKeyLabel = new JLabel("Label:");
		JLabel labelKeySize = new JLabel("Key Size:");
		
		String keyList[] = {"1024", "2048"};
		final JComboBox keySizeComboBox = new JComboBox(keyList);

		keySizeComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				keySize = String.valueOf(keySizeComboBox.getSelectedItem());
			}
		});
		
		JPanel recName = new JPanel(new GridLayout(0,1));
		recName.add(labelKeyLabel);
		recName.add(tx_keyLabel);
		
		JPanel recPIN = new JPanel(new GridLayout(0,1));
		recPIN.add(labelKeySize);
		recPIN.add(keySizeComboBox);

		JPanel panelAddRecipient = new JPanel(new GridLayout(0, 1));
		
		panelAddRecipient.add(recName);
		panelAddRecipient.add(recPIN);
		
		String title = "Generate Key Pair";
		int response = JOptionPane.showConfirmDialog(null, panelAddRecipient, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		while(response == 0) {
			String[] outputData = new String[2];
			outputData[0] = tx_keyLabel.getText();
			outputData[1] = keySize;
			return outputData;
		}
		return null;
	
	}
	
}
