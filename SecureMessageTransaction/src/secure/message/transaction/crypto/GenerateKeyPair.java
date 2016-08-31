package secure.message.transaction.crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import secure.message.transaction.utility.Constant;

public class GenerateKeyPair {


	public boolean generateKey(int keySize) {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constant.ALGORITHM);
			
			// keyGen.initialize(1024);
			keyGen.initialize(keySize);
			final KeyPair key = keyGen.generateKeyPair();

			File privateKeyFile = new File(Constant.PRIVATE_KEY_FILE);
			File publicKeyFile = new File(Constant.PUBLIC_KEY_FILE);

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the Public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * The method checks if the pair of public and private key has been
	 * generated.
	 * 
	 * @return flag indicating if the pair of keys were generated.
	 */
	public static boolean areKeysPresent() {

		File privateKey = new File(Constant.PRIVATE_KEY_FILE);
		File publicKey = new File(Constant.PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}
}
