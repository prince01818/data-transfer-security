package secure.message.transaction.db.objects;

public class SMBServer {
	public final static String ID = "ID"; 
	public final static String IP = "IP";
	public final static String USER_NAME = "USER_NAME";
	public final static String PASSWORD = "USER_PASSWORD";
	public final static String SHARE_FOLDER_NAME = "SHARE_FOLDER_NAME";
	
	public int id;
	public String idAddress;
	public String userName;
	public String userPassword;
	public String shareFolderName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdAddress() {
		return idAddress;
	}
	public void setIdAddress(String idAddress) {
		this.idAddress = idAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getShareFolderName() {
		return shareFolderName;
	}
	public void setShareFolderName(String shareFolderName) {
		this.shareFolderName = shareFolderName;
	}
	
	
	
}
