package secure.message.transaction.actionlisteners;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.jss.asn1.INTEGER;
import org.jss.asn1.SET;
import org.jss.pkcs7.EncryptedContentInfo;
import org.jss.pkcs7.EnvelopedData;
import org.jss.pkcs7.IssuerAndSerialNumber;
import org.jss.pkcs7.RecipientInfo;
import org.jss.pkix.primitive.Name;

import pkcs.secure.file.PKCSEnvelopedDataDecode;
import secure.message.transaction.crypto.Crypto;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.smbapi.SmbFileManager;
import secure.message.transaction.utility.CertificateUtility;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.SaveEnvDecodeFileView;

public class EnvelopeFileDownloadAndProcess {
	PKCSEnvelopedDataDecode pkcsEnvelopedDataDecode = new PKCSEnvelopedDataDecode();
	
	public void evvProcess(String fileName){
		System.out.println("File Name:" + fileName);
		
		SMBServer smbServerObj = DBManager.getSMBInfo();
		SmbFileManager smbFileManager = new SmbFileManager(smbServerObj);
		try {
			smbFileManager.establishCommunicationPath();
		} catch (Exception e) {
			e.printStackTrace();
			UIMessage.showErrorMessage("Can not connect to server");
			return;
		}
		
		try {
			String desPath = Constant.ENVELOPED_FILE_TEMP_DOWNLOAD_PATH + Constant.FILE_SEPARATOR + fileName; 
			smbFileManager.downloadFile(fileName, desPath);
			
			int numberOfRecipient = isMyEnvelopedData(desPath);
			if(numberOfRecipient == -1){
				UIMessage.showWarningMessage("You are not valied recipient");
				return;
			}
			
			
			SaveEnvDecodeFileView saveEnvDecodeFileView = new SaveEnvDecodeFileView();
			String storPath = saveEnvDecodeFileView.saveEnvFile(fileName);
			if(storPath == null){
				UIMessage.showWarningMessage("Invalied destination");
				return;
			}
			
			EnvelopedData envelopedDataReceipient = decodeEnvelopedData(desPath);
			String finalResult = storeEnvFile(envelopedDataReceipient, numberOfRecipient, storPath);
			if(finalResult != Constant.SUCCESS){
				UIMessage.showErrorMessage(finalResult);
			} else{
				UIMessage.showSuccessMessage("Enveloped data decrypt success");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	
	private int isMyEnvelopedData(String envelopedDataFilePath) {
		int result = -1;
		EnvelopedData envelopedDataReceipient = decodeEnvelopedData(envelopedDataFilePath);
		if (envelopedDataFilePath == null)
			return result;
		
			try{
				X509Certificate cert = CertificateUtility.getCertificate(Constant.USER_CERT_PATH);
				
				String issuerName = cert.getIssuerDN().toString();
				long serialNumber = cert.getSerialNumber().longValue();
				
				SET recipientInfos = envelopedDataReceipient.getRecipientInfos();
				
				for (int i = 0; i <recipientInfos.size(); i++) {
					RecipientInfo recipientInfo = (RecipientInfo) recipientInfos.elementAt(i);
					IssuerAndSerialNumber issuerAndSerialNumber = recipientInfo.getissuerAndSerialNumber();
					issuerAndSerialNumber.getSerialNumber();
					
					INTEGER r_serialNumber = issuerAndSerialNumber.getSerialNumber();
					
					
					BigInteger biSLNumber = new BigInteger(r_serialNumber.toByteArray());
					long  r_slNumber = biSLNumber.longValue();
					
					Name issuer = issuerAndSerialNumber.getIssuer();
					String r_issuerName = issuer.getRFC1485().substring(3, issuer.getRFC1485().length());
					
					if(r_issuerName.equals(issuerName) && r_slNumber == serialNumber){
						result = i;
						break;
					}
				}

			} catch(Exception e){
				return -1;
			}

		
		return result;
	}

	
	private EnvelopedData decodeEnvelopedData(String fileName) {

		EnvelopedData envelopedDataReceipient = null;
		try {
			InputStream envelopedDataInputStream = new BufferedInputStream(new FileInputStream(new File(fileName)));
			
			envelopedDataReceipient = pkcsEnvelopedDataDecode.decodeEnvelopedData(envelopedDataInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return envelopedDataReceipient;
	}
	
	public String storeEnvFile(EnvelopedData envelopedDataReceipient, int recipientNumber, String envelopedDataFilePath){
		try {
			SET recipientInfos = envelopedDataReceipient.getRecipientInfos();
			RecipientInfo recipientInfo = (RecipientInfo) recipientInfos.elementAt(recipientNumber);
			
			
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(Constant.PRIVATE_KEY_FILE));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
			Crypto crypto = new Crypto();
			byte[] keyByte = crypto.keyDecrypt(privateKey, recipientInfo.getEncryptedKey().toByteArray());
			
			if (keyByte == null)
				return "Key Decryption Failed";
	
			String saveDecryptedFile = envelopedDataFilePath;
			
			// message decryption
			if (!messageDecrypt(envelopedDataReceipient, keyByte, saveDecryptedFile))
				return "Message Decryption Failed";
		} catch(Exception e){
			return "Message Decryption Failed";
		}
		return Constant.SUCCESS;
	}
	
	
	
	private boolean messageDecrypt(EnvelopedData envelopedDataReceipient, byte[] keyByte, String toSavePath) {

		boolean isSuccess = false;

		try {
			EncryptedContentInfo contentInfo = envelopedDataReceipient.getEncryptedContentInfo();
			byte[] cipherText = contentInfo.getEncryptedContent().toByteArray();
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
