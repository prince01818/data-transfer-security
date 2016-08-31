package secure.message.transaction.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import secure.message.transaction.utility.Utility;


public class Crypto {
	
	

	public Cipher cipher;

	/**
	 * @param plaintext
	 * @param puk
	 * @return encrypted
	 */
	public byte[] encryptLargeDataByPublicKey(byte[] plaintext, PublicKey puk)
			throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, puk);
		return cipher.doFinal(plaintext);

		// return blockCipher(plaintext, Cipher.ENCRYPT_MODE);
	}

	public byte[] decryptLargeDataByPrivateKey(byte[] encrypted, PrivateKey prk)
			throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, prk);
		return cipher.doFinal(encrypted);

		// return blockCipher(encrypted, Cipher.DECRYPT_MODE);
	}

	/**
	 * @param bytes
	 * @param mode
	 * @return scrambled
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private byte[] blockCipher(byte[] bytes, int mode)
			throws IllegalBlockSizeException, BadPaddingException {
		byte[] scrambled = new byte[0];

		byte[] toReturn = new byte[0];
		int length = (mode == Cipher.ENCRYPT_MODE) ? 117 : 128;

		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++) {

			if ((i > 0) && (i % length == 0)) {
				scrambled = cipher.doFinal(buffer);
				toReturn = Utility.append(toReturn, scrambled);
				int newlength = length;

				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				buffer = new byte[newlength];
			}
			buffer[i % length] = bytes[i];
		}

		scrambled = cipher.doFinal(buffer);

		toReturn = Utility.append(toReturn, scrambled);

		return toReturn;
	}




	/**
	 * @param crypto
	 * @param privateKey
	 * @param reEncryptedKey
	 * @return
	 */
	public byte[] keyDecrypt(PrivateKey privateKey,
			byte[] reEncryptedKey) {
		byte[] keyValue = null;
		try {
			//this.cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
			Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher1 = Cipher.getInstance("RSA");
			cipher1.init(Cipher.DECRYPT_MODE, privateKey);
			keyValue = cipher1.doFinal(reEncryptedKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyValue;
	}



	/**
	 * @param crypto
	 * @param secretKey
	 * @param publicKey
	 * @return
	 */
	public byte[] keyEncrypt(byte[] secretKey,
			PublicKey publicKey) {
		byte[] encryptedKeyValue = null;
		try {
			Cipher cipher1 = Cipher.getInstance("RSA");  
			//this.cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
			cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			
			cipher1.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedKeyValue = cipher1.doFinal(secretKey);
		} catch (Exception e){
			e.printStackTrace();
		}
		return encryptedKeyValue;
	}


	public byte[] fileEncrypt(byte[] plainText, byte[] keyByte) throws Exception {
		SecretKey sKey = new SecretKeySpec(keyByte, "DESede");
		try {
			System.gc();
			long freeMemory = Runtime.getRuntime().freeMemory();
			if(plainText.length > freeMemory){
				throw new Exception("Large size file");
			}
			
			IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, sKey, iv);
			
			byte cipherText[] = cipher.doFinal(plainText);
			
			return cipherText;
		} catch (Exception e){
			throw e;
		}
	}

	public byte[] fileDecrypt(byte[] cipherText, byte[] keyByte) throws Exception {
		byte plainText[] = null;
		try {
			
			System.gc();
			long freeMemory = Runtime.getRuntime().freeMemory();
			if(cipherText.length > freeMemory){
				throw new Exception("Large size file");
			}
			
			SecretKey sKey = new SecretKeySpec(keyByte, "DESede");
			IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sKey, iv);
			plainText = cipher.doFinal(cipherText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plainText;
	}



}
