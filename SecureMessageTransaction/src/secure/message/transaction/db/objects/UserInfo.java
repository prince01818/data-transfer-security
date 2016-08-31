package secure.message.transaction.db.objects;

public class UserInfo {
	
	public final static String ID = "ID"; 
	public final static String NAME = "NAME";
	public final static String USER_PIN = "PIN";
	public final static String KEY_PATH = "KEY_PATH";
	public final static String CERTIFICATE_PATH = "CERTIFICATE_PATH";
	
	public int id;
	public String userName;
	public String PIN;
	public String certificatePath;
	public String keyPath;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPIN() {
		return PIN;
	}
	public void setPIN(String pIN) {
		PIN = pIN;
	}
	public String getCertificatePath() {
		return certificatePath;
	}
	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}
	public String getKeyPath() {
		return keyPath;
	}
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}
	
}
