package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.jss.asn1.SET;
import org.jss.pkcs7.RecipientInfo;

import pkcs.secure.file.PKCSEnvelopedData;
import pkcs.secure.file.crypto.BouncyCastleEncryptDecrypt;

//import org.mozilla.jss.asn1.SET;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.Crypto;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.smbapi.SmbFileManager;
import secure.message.transaction.utility.CertificateUtility;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.FileUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.view.MessageShareView;
import secure.message.transaction.view.MessgaeShareInputData;

public class MessageShare implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainWindowView.cleanContent();
		try{
			ArrayList<SMTRecipientInfo> recipInfoList = DBManager.getRecipientInfos();
			ArrayList<EncryptFile> encFileList = DBManager.getEncryptedFiles();
			
			MessageShareView messageShareView = new MessageShareView();
			MessgaeShareInputData messgaeShareInputData = messageShareView.getMessageShareData(recipInfoList, encFileList);
			if(messgaeShareInputData == null){
				//UIMessage.showWarningMessage("Input Invalied");
				return;
			}
			
			ArrayList<SMTRecipientInfo> selectRecipInfoList = messgaeShareInputData.getRecipInfoList();
			EncryptFile selectEncryptFile = messgaeShareInputData.getEncryptFile();
			
			String[] envResult = createEnvelopedData(selectRecipInfoList, selectEncryptFile);
			if(envResult[0] != Constant.SUCCESS){
				UIMessage.showErrorMessage(envResult[0]);
				return;
			}
			
			SMBServer smbServerObj = DBManager.getSMBInfo();
			SmbFileManager smbFileManager = new SmbFileManager(smbServerObj);
			try {
				smbFileManager.establishCommunicationPath();
			} catch (Exception e1) {
				e1.printStackTrace();
				UIMessage.showErrorMessage("Can not connect to server");
				return;
			}
			
			smbFileManager.shareFile(envResult[1], "");
			
			UIMessage.showSuccessMessage("Message share success");
		} catch(Exception e1){
			e1.printStackTrace();
		}
	}

	
	private String[] createEnvelopedData(ArrayList<SMTRecipientInfo> selectedRecipients,  EncryptFile selectEncryptFile) {
		// Get encrypted file data from file
		String[] result = new String[2];
		result[0] = Constant.SUCCESS;
		result[1] = "";
		
		String encryptedFilePath = Constant.ENCRYPTED_FILE_PATH+Constant.FILE_SEPARATOR+selectEncryptFile.getFileName();
		byte encryptedFileData[] = FileUtility.fileNameToByteArray(encryptedFilePath);
		if (encryptedFileData == null){
			result[0] = "Encrypted file not found";
			return result;
		}
		
		//Log.i(TAG,"Encrypted Data: " + Utility.toHexString(encryptedFileData));
//		Log.i(TAG, "Key before decrypt sender private key: " + Utility.toHexString(encryptedKey));
		
		//BouncyCastleEncryptDecrypt bouncyCastleEncryptDecrypt = new BouncyCastleEncryptDecrypt();
		Crypto crypto = new Crypto();
		byte keyByte[];
		try{
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(Constant.PRIVATE_KEY_FILE));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
			
			keyByte = crypto.keyDecrypt(privateKey, Utility.hexToByteArray(selectEncryptFile.getKeyValue()));
			//System.out.println(Utility.toHexString(keyByte));
		} catch(Exception eee){
			result[0] = "Key decryption failed";
			return result;
		}

		
		SET recipientInfos = createRecipientInfo(keyByte, selectedRecipients);
		if(recipientInfos == null) {
			result[0] = "Recipient data failed";
			return result;
		}
		
		
		// Prepare EnvelopedData and Store
		try {
			String orginalFileName = FileUtility.removeExtention(FileUtility.filePathTofileName(selectEncryptFile.getFileName()));
			String envelopedDataStoragePath = Constant.ENVELOPED_FILE_TEMP_PATH + Constant.FILE_SEPARATOR +orginalFileName
					+ Constant.ENVELOPED_FILE_EXTENSION;
			PKCSEnvelopedData pkcsEnvelopedData = new PKCSEnvelopedData();
			pkcsEnvelopedData.getEnvelopedData(envelopedDataStoragePath, encryptedFileData, recipientInfos);
			result[1] = envelopedDataStoragePath;
			// Show Enveloped Data
//			Utility.convertToHex(System.out, new File(envelopedDataStoragePath));

		} catch (Exception e) {
			e.printStackTrace();
			result[0] = "Enveloped data prepare failed";
			return result;
		}

		return result;
	}

	
	private SET createRecipientInfo(byte[] keyByte, ArrayList<SMTRecipientInfo> selectedRecipients){
		SET recipientInfos =  new SET();
		
		
		if(selectedRecipients == null || selectedRecipients.size() < 1) return null;
		
		// encrypt key using recipient public key
		
		for(int i = 0; i < selectedRecipients.size(); i++){
			
			SMTRecipientInfo recipientData = selectedRecipients.get(i); 
		
			CertificateUtility certificateUtility = new CertificateUtility();
			String recipientCertificatePath = Constant.RECIPIENT_CERT_PATH + Constant.FILE_SEPARATOR + recipientData.getCertificatePath();
			
			X509Certificate certificate = certificateUtility.getCertificate(recipientCertificatePath);
			if (certificate == null)
				return null;
			
			PublicKey recipientPublicKey = certificate.getPublicKey(); 
			if (recipientPublicKey == null)
				return null;
	
			String issuerName = certificate.getIssuerDN().toString();
			long serialNumber = certificate.getSerialNumber().longValue();
			
			BouncyCastleEncryptDecrypt bouncyCastleEncryptDecrypt = new BouncyCastleEncryptDecrypt();
			Crypto crypto = new Crypto();
			// key Encryption
			byte[] encryptedSecretKey = crypto.keyEncrypt(keyByte, recipientPublicKey);
			if (encryptedSecretKey == null)
				return null;
			
			
			RecipientInfo recipientInfo =  PKCSEnvelopedData.recipientInfo(encryptedSecretKey, serialNumber, issuerName);
			recipientInfos.addElement(recipientInfo);
		}
		
		return recipientInfos;
	}
	
}
