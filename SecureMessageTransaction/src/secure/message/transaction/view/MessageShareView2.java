package secure.message.transaction.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.utility.ComboBoxValueAndID;
import secure.message.transaction.utility.ComboBoxValueAndIDEncFiles;
import secure.message.transaction.utility.UIMessage;

public class MessageShareView2 {
	
	SMTRecipientInfo selectRecInfo;
	EncryptFile selectEncryptFile;
	public MessgaeShareInputData getMessageShareData(ArrayList<SMTRecipientInfo> recipInfoList, ArrayList<EncryptFile> encFileList) {
		
		JLabel selectRecipient = new JLabel("Select Recipient:");
		JLabel messageLabel = new JLabel("Select Message:");
		
		
		int recListSize = recipInfoList.size();
		ComboBoxValueAndID recInfoObjectList[] = new ComboBoxValueAndID[recListSize + 1];
		if (recListSize > 0) {
			recInfoObjectList[0] = new ComboBoxValueAndID(null, "Select Recipient");
			for(int i = 0; i < recListSize; i ++){
				SMTRecipientInfo recInfoObj = recipInfoList.get(i);
				recInfoObjectList[i+1] = new ComboBoxValueAndID(recInfoObj, recInfoObj.getName());
			}
		}

		final JComboBox recipientListComboBox = new JComboBox(recInfoObjectList);

		recipientListComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				ComboBoxValueAndID selectedObj = (ComboBoxValueAndID) recipientListComboBox.getSelectedItem();
				selectRecInfo = selectedObj.getId();
			}
		});
		
		
		
		int encListSize = encFileList.size();
		ComboBoxValueAndIDEncFiles encFindInfoObjectList[] = new ComboBoxValueAndIDEncFiles[encListSize + 1];
		if (encListSize > 0) {
			encFindInfoObjectList[0] = new ComboBoxValueAndIDEncFiles(null, "Select Encrypted File");
			for(int i = 0; i < encListSize; i ++){
				EncryptFile encryptFileObj = encFileList.get(i);
				encFindInfoObjectList[i+1] = new ComboBoxValueAndIDEncFiles(encryptFileObj, encryptFileObj.getFileName());
			}
		}

		final JComboBox encryptedMessageList = new JComboBox(encFindInfoObjectList);

		encryptedMessageList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				ComboBoxValueAndIDEncFiles selectedObj = (ComboBoxValueAndIDEncFiles) encryptedMessageList.getSelectedItem();
				selectEncryptFile = selectedObj.getId();
			}
		});

		
		selectRecInfo = null;
		selectEncryptFile = null;
		
		
		JPanel messageSharePanel = new JPanel(new GridLayout(0,1));
		messageSharePanel.add(selectRecipient);
		messageSharePanel.add(recipientListComboBox);
		messageSharePanel.add(messageLabel);
		messageSharePanel.add(encryptedMessageList);
		
		String title = "Message Share";
		int response = JOptionPane.showConfirmDialog(null, messageSharePanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		while (response == 0){
			try {
				MessgaeShareInputData messgaeShareInputData = new MessgaeShareInputData();
				// Reset all data of CertificateInputData 
				String message = inputValidator();
				if(message == null){
					ArrayList<SMTRecipientInfo> inputRecipInfoList = new ArrayList<SMTRecipientInfo>();
					inputRecipInfoList.add(selectRecInfo);
					messgaeShareInputData.setEncryptFile(selectEncryptFile);
					messgaeShareInputData.setRecipInfoList(inputRecipInfoList);
					return messgaeShareInputData;
					
				} else {
					UIMessage.showWarningMessage(message);
					response = JOptionPane.showConfirmDialog(null, messageSharePanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
						return null;
					}
				}
			
			} catch (Exception e){
				response = JOptionPane.showConfirmDialog(null, messageSharePanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION){
					return null;
				}
			}
		}
		
		return null;
	}

	public String inputValidator(){
		String message = null;
		if(selectEncryptFile == null ){
			message = "Select Encrypted File";
		}else if(selectRecInfo == null){
			message = "Select Recipient";
		}
		return message;
	}
	
}
