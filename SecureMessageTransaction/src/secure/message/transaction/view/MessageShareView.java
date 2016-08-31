package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.utility.ComboBoxValueAndID;
import secure.message.transaction.utility.ComboBoxValueAndIDEncFiles;
import secure.message.transaction.utility.UIMessage;

public class MessageShareView extends JFrame{
	JList places;
	ArrayList<SMTRecipientInfo> inputRecipInfoList2 = new ArrayList<SMTRecipientInfo>();
	SMTRecipientInfo selectRecInfo;
	EncryptFile selectEncryptFile;
	public MessgaeShareInputData getMessageShareData(ArrayList<SMTRecipientInfo> recipInfoList, ArrayList<EncryptFile> encFileList) {
		
		JLabel selectRecipient = new JLabel("Select Recipient:");
		JLabel messageLabel = new JLabel("Select Message:");
		
		
		int recListSize = recipInfoList.size();
		ComboBoxValueAndID recInfoObjectList[] = new ComboBoxValueAndID[recListSize];
		if (recListSize > 0) {
			//recInfoObjectList[0] = new ComboBoxValueAndID(null, "Select Recipient");
			for(int i = 0; i < recListSize; i ++){
				SMTRecipientInfo recInfoObj = recipInfoList.get(i);
				recInfoObjectList[i] = new ComboBoxValueAndID(recInfoObj, recInfoObj.getName());
			}
		}
//
//		final JComboBox recipientListComboBox = new JComboBox(recInfoObjectList);
//
//		recipientListComboBox.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e){
//
//				ComboBoxValueAndID selectedObj = (ComboBoxValueAndID) recipientListComboBox.getSelectedItem();
//				selectRecInfo = selectedObj.getId();
//			}
//		});
		
//		Container c = getContentPane(); 	
//	    c.setLayout(new FlowLayout());
		JPanel rePanel = new JPanel(new GridLayout(0,1));
	    //places = new JList(names) ;                    // creating JList object; pass the array as parameter
	    places = new JList(recInfoObjectList);
	    places.setVisibleRowCount(5); 
			     
	    places.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	    rePanel.add(new JScrollPane(places));

	    places.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object obj[] = places.getSelectedValues();
				inputRecipInfoList2.clear();
				System.out.println("Object size: " + obj.length);
			    for(int i = 0; i < obj.length; i++)
			    {
			    	ComboBoxValueAndID selectedObj = (ComboBoxValueAndID) obj[i];
			    	inputRecipInfoList2.add(selectedObj.id);
			    	System.out.println("Item ["+i+"] "+selectedObj.label);
			    }
				
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
		
		JPanel messageSharePanel1 = new JPanel(new BorderLayout());
		messageSharePanel1.add(messageLabel, BorderLayout.PAGE_START);
		messageSharePanel1.add(encryptedMessageList, BorderLayout.PAGE_END);
		
		
		JPanel messageSharePanel = new JPanel(new BorderLayout());
		messageSharePanel.add(selectRecipient, BorderLayout.PAGE_START);
		messageSharePanel.add(rePanel, BorderLayout.CENTER);
		messageSharePanel.add(messageSharePanel1, BorderLayout.PAGE_END);
		
		String title = "Message Share";
		int response = JOptionPane.showConfirmDialog(null, messageSharePanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		while (response == 0){
			try {
				MessgaeShareInputData messgaeShareInputData = new MessgaeShareInputData();
				// Reset all data of CertificateInputData 
				String message = inputValidator();
				if(message == null){
					//ArrayList<SMTRecipientInfo> inputRecipInfoList = new ArrayList<SMTRecipientInfo>();
					//inputRecipInfoList.add(selectRecInfo);
					messgaeShareInputData.setEncryptFile(selectEncryptFile);
					messgaeShareInputData.setRecipInfoList(inputRecipInfoList2);
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
		if(inputRecipInfoList2.size() <= 0){
			message = "Select Recipient";
		} else if(selectEncryptFile == null ){
			message = "Select Encrypted File";
		}
		return message;
	}
	
}
