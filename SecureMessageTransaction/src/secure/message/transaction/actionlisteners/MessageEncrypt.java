package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

import pkcs.secure.file.crypto.BouncyCastleEncryptDecrypt;
import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.Crypto;
import secure.message.transaction.crypto.CryptoUtil;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.FileUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.view.MessageEncryptView;



public class MessageEncrypt implements ActionListener, PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		MainWindowView.cleanContent();
		MessageEncryptView messageEncrypt = new MessageEncryptView();
		String result = messageEncrypt.getMessageEncryptData();
		if(result == null){
			return;
		}
		
		try{
			byte fileData[] = FileUtility.fileNameToByteArray(result);
			if(fileData == null){
				UIMessage.showErrorMessage("File too large");
				return;
			}
			
			//BouncyCastleEncryptDecrypt bouncyCastleEncryptDecrypt = new BouncyCastleEncryptDecrypt();
			Crypto crypto = new Crypto();
			String generateSKey = CryptoUtil.generateRandom(Constant.DES3_KEY_SIZE);
			System.out.println("Sec: " + generateSKey);
			byte[] encryptedFileData = crypto.fileEncrypt(fileData, generateSKey.getBytes());
			
			
			
			//Log.i(TAG,"cipherText Data:" + Utility.toHexString(encryptedFileData) + "File Length: " + encryptedFileData.length);
			
			String encryptedFileNameTemp = BigInteger.valueOf(System.currentTimeMillis())+FileUtility.filePathTofileName(result)+"";
			
			// Encrypt data store into directory
			String encryptedFileName = FileUtility.saveEncryptedFile(encryptedFileData, encryptedFileNameTemp,Constant.ENCRYPTED_FILE_PATH);
			System.out.println("encryptedFileName: " + encryptedFileName);
			
			// Encrypt secret key using public key (Sender)
			
			ObjectInputStream inputStream = null;

			// Encrypt the string using the public key
			inputStream = new ObjectInputStream(new FileInputStream(Constant.PUBLIC_KEY_FILE));
			final PublicKey publicKey = (PublicKey) inputStream.readObject();
			byte encryptedKey[] = crypto.keyEncrypt(generateSKey.getBytes(), publicKey);
			
			EncryptFile encryptFile = new EncryptFile();
			encryptFile.setFileName(encryptedFileName);
			encryptFile.setKeyValue(Utility.toHexString(encryptedKey));
			
			String dbResult = DBManager.addEncryptedFile(encryptFile);
			if(dbResult.equals(Constant.SUCCESS)){
				UIMessage.showSuccessMessage("File Encrypt Success");
			} else {
				UIMessage.showErrorMessage("File Encrypt Failed");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
