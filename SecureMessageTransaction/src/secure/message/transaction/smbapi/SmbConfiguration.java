package secure.message.transaction.smbapi;

import secure.message.transaction.db.objects.SMBServer;

public class SmbConfiguration {
	
	SMBServer smbServer;
	
	public SmbConfiguration(SMBServer smbServerData) {
		smbServer = smbServerData;
	}
	
	public void configuration(String userName, String userPassword, String ip, String folderName){
//		sharedPreferencesManager.setValue(USER_NAME, userName);
//		sharedPreferencesManager.setValue(USER_PASSOWRD, userPassword);
//		sharedPreferencesManager.setValue(CLIENT_IP, ip);
//		sharedPreferencesManager.setValue(SHARE_FOLDER_NAME, folderName);
	}
	
	public String getUserName(){
		System.out.println("USER_NAME: " + smbServer.getUserName());
		return smbServer.getUserName();
	}
	
	public String getUserPassword(){
		System.out.println("PASSWORD: " + smbServer.getUserPassword());
		return smbServer.getUserPassword();
	}
	
	public String getIP(){
		System.out.println("IP: " + smbServer.getIdAddress());
		return smbServer.getIdAddress();
	}
	
	public String getShareFolderName(){
		System.out.println("SHARE_FOLDER_NAME" + smbServer.getShareFolderName());
		return smbServer.getShareFolderName();
	}
	
	public String getRootPath(){
		System.out.println("ROOT PATH: " + "smb://" + getIP() + "/"+ getShareFolderName());
		return "smb://" + getIP() + "/"+ getShareFolderName();
	}

}
