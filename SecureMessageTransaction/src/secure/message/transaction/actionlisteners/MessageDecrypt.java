package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.util.ArrayList;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.Crypto;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.FileUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.view.MessageDecryptView;
import secure.message.transaction.view.SaveDecryptedFileView;

public class MessageDecrypt implements ActionListener, PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		MainWindowView.cleanContent();
		ArrayList<EncryptFile> enFileList = DBManager.getEncryptedFiles();
		MessageDecryptView messageEncrypt = new MessageDecryptView();
		MainWindowView.updateContent(messageEncrypt.getEncryptedMessageListData(enFileList));
	}

	public void decryptMessage(String fileName) {
		EncryptFile encryptFile = DBManager.getEncryptedFileByFileName(fileName);
		try {
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(Constant.PRIVATE_KEY_FILE));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
			Crypto crypto = new Crypto();
			byte[] keyByte = crypto.keyDecrypt(privateKey, Utility.hexToByteArray(encryptFile.getKeyValue()));
			
			byte[] cipherText = FileUtility.fileNameToByteArray(Constant.ENCRYPTED_FILE_PATH+"/"+encryptFile.getFileName());
			SaveDecryptedFileView saveEnvDecodeFileView = new SaveDecryptedFileView();
			String storFileName = saveEnvDecodeFileView.saveEnvFile(fileName);
			
			if(!messageDecrypt(cipherText, keyByte, storFileName)){
				UIMessage.showErrorMessage("File decryption failed");
				return;
			}
			UIMessage.showSuccessMessage("File decryption success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean messageDecrypt(byte[] cipherText, byte[] keyByte, String toSavePath) {

		boolean isSuccess = false;

		try {
			Crypto crypto = new Crypto();
			byte[] plainText = crypto.fileDecrypt(cipherText, keyByte);
			if (plainText == null)
				return isSuccess;

			File fileForOutput = new File(toSavePath);
			FileOutputStream outFile = new FileOutputStream(fileForOutput);
			outFile.write(plainText);
			outFile.close();
			isSuccess = true;
			//Log.i(TAG, "Decrypt message: " + Utility.toHexString(plainText));
		} catch (Exception e) {
			e.printStackTrace();
			return isSuccess;
		}
		return isSuccess;
	}

}
