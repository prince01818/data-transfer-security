package secure.message.transaction.utility;

public class Constant {
//	public final static String APP_NAME = "Secure Message Transaction";
	public final static String APP_ICON = "res/logo.png";
	public final static String APP_NAME = "Secured Message Transaction";
	public final static String DB_NAME = "securedMTDB";
	
	public final static String SUCCESS = "Success";
	public final static String FAILED = "Failed";
	
	public final static String DB_T_UserInfo = "UserInfo"; 
	public final static String DB_T_EncryptFileInfo = "EncryptFileInfo"; 
	public final static String DB_T_SMBInfo = "SMBInfo"; 
	public final static String DB_T_RecipientInfo = "RecipientInfo";
	
	public final static String RECIPIENT_CERT_PATH = "data/recipients/cert";
	public final static String USER_CERT_PATH = "data/user/cert/MyCert.crt";
	public static final String ALGORITHM = "RSA";
	public static final String PRIVATE_KEY_FILE = "data/user/keys/private.key";
	public static final String PUBLIC_KEY_FILE = "data/user/keys/public.key";
	public static final String ENCRYPTED_FILE_PATH = "data/encrypted_file";
	
	public static final int DES3_KEY_SIZE = 24;
	
	public final static String DATA_STORE_PATH = "data";
	public final static String FILE_SEPARATOR = "/";
	public final static String ENCRYPTED_FILE_EXTENSION = ".en";
	public final static String ENVELOPED_FILE_PATH = "SMTEnvelopedData";
	public final static String ENVELOPED_FILE_EXTENSION = ".env";
	public final static String ENVELOPED_FILE_TEMP_PATH = "data/envloped/temp";
	public final static String ENVELOPED_FILE_TEMP_DOWNLOAD_PATH = "data/envloped/tempdownload";
	
}
