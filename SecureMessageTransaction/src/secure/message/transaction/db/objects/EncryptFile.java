package secure.message.transaction.db.objects;

public class EncryptFile {
	public final static String ID = "ID"; 
	public final static String FILE_NAME = "NAME";
	public final static String S_KEY_VALUE = "ENCRYPTED_S_KEY";
	
	public int id;
	public String fileName;
	public String keyValue;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
}
