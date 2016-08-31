package secure.message.transaction.db;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.smbapi.FileData;
import secure.message.transaction.utility.Constant;

public class DBManager {
	
	public static boolean isDataBaseExsit(String dbName){
		File file = new File(dbName);
		if (file.exists()) // here's how to check
		{
			System.out.print("This database name already exists");
			return true;
		}
		return false;
	}
	
	private static Connection instance = null;
	private static Statement instanceStatement  = null;

	public synchronized static Connection getInstance() throws Exception {
		if(instance == null){
			instance = dataDaseConnect(Constant.DB_NAME);
		}
		return instance;
	}
	
	public synchronized static Statement getInstanceStatement() throws Exception {
		if(instanceStatement == null){
			instanceStatement = getInstance().createStatement();
		}
		return instanceStatement;
	}

	public static void deleteAllInstance(){
		if(instance != null){
			try{
				getInstance().createStatement().close();
				getInstance().close();
			} catch(Exception e){
				e.printStackTrace();
			}
			instance = null;
		}
	}
	
	private static Connection dataDaseConnect(String dbName) throws Exception {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			//stmt = c.createStatement();
			return connection;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": "+ e.getMessage());
			return null;
		}
	}

	public static void createTables() {

		try {
			Statement stmt = DBManager.getInstanceStatement();
			String sql = "CREATE TABLE "+Constant.DB_T_UserInfo+" "
					+ "("+UserInfo.ID+" INTEGER PRIMARY KEY   AUTOINCREMENT,"
					+ " "+UserInfo.NAME+"           TEXT    NOT NULL, "
					+ " "+UserInfo.USER_PIN+"           TEXT    NOT NULL, "
					+ " "+UserInfo.KEY_PATH+"           TEXT   NULL, "
					+ " "+UserInfo.CERTIFICATE_PATH+"           TEXT NULL )";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE "+Constant.DB_T_EncryptFileInfo+" "
					+ "("+EncryptFile.ID+"  INTEGER PRIMARY KEY   AUTOINCREMENT,"
					+ " "+EncryptFile.FILE_NAME+"      TEXT    NOT NULL, "
					+ " "+EncryptFile.S_KEY_VALUE+"      TEXT     NOT NULL)";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE "+Constant.DB_T_SMBInfo+" "
					+ "("+SMBServer.ID+" INTEGER PRIMARY KEY   AUTOINCREMENT,"
					+ " "+SMBServer.IP+"           TEXT     NULL, "
					+ " "+SMBServer.USER_NAME+"           TEXT    NULL, "
					+ " "+SMBServer.PASSWORD+"           TEXT    NULL, "
					+ " "+SMBServer.SHARE_FOLDER_NAME+"      TEXT     NULL)";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE "+Constant.DB_T_RecipientInfo+" "
					+ "("+SMTRecipientInfo.ID+" INTEGER PRIMARY KEY   AUTOINCREMENT,"
					+ " "+SMTRecipientInfo.NAME+"           TEXT    NOT NULL, "
					+ " "+SMTRecipientInfo.CERTIFICATE_PATH+"   TEXT    NOT NULL, "
					+ " "+SMTRecipientInfo.EMAIL+"      TEXT     NOT NULL)";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO " + Constant.DB_T_SMBInfo + "  (IP, USER_NAME, USER_PASSWORD, SHARE_FOLDER_NAME) VALUES ('','','','')";
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println("Table created successfully");
	}
	
	
	public static UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_UserInfo+";" );
	      while ( rs.next() ) {
	         int id = rs.getInt(UserInfo.ID);
	         String  name = rs.getString(UserInfo.NAME);
	         String  pin = rs.getString(UserInfo.USER_PIN);
	         String  keyPath = rs.getString(UserInfo.KEY_PATH);
	         String  certPath = rs.getString(UserInfo.CERTIFICATE_PATH);
	         userInfo.setId(id);
	         userInfo.setUserName(name);
	         userInfo.setPIN(pin);
	         userInfo.setKeyPath(keyPath);
	         userInfo.setCertificatePath(certPath);
	         System.out.println();
	         return userInfo;
	      }
		} catch(Exception e){
			userInfo = null;
			e.printStackTrace();
		}
		return null;
	}
	
	public static String addUserInfo(UserInfo userInfo) {

		try {
			Statement stmt = DBManager.getInstanceStatement();

			System.out.println("Opened database successfully");

			String sql = "INSERT INTO " + Constant.DB_T_UserInfo + "  ("+UserInfo.NAME+", "+UserInfo.USER_PIN+") VALUES ('"+userInfo.getUserName()+"' ,'"+userInfo.getPIN()+"')";
			
			System.out.println(sql);
			
			stmt.executeUpdate(sql);		
			return Constant.SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static SMBServer getSMBInfo(){
		SMBServer smbServer = new SMBServer();
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_SMBInfo+";" );
	      while ( rs.next() ) {
	    	 smbServer.setId(rs.getInt(SMBServer.ID));
	    	 smbServer.setIdAddress(rs.getString(SMBServer.IP));
	    	 smbServer.setUserName(rs.getString(SMBServer.USER_NAME));
	    	 smbServer.setUserPassword(rs.getString(SMBServer.PASSWORD));
	    	 smbServer.setShareFolderName(rs.getString(SMBServer.SHARE_FOLDER_NAME));
	      }
		} catch(Exception e){
			smbServer = null;
			e.printStackTrace();
		}
		return smbServer;
	}
	
	public static String updateSMBInfo(SMBServer smbServer){
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 stmt.executeUpdate( "UPDATE  "+Constant.DB_T_SMBInfo+" SET "
				 + SMBServer.USER_NAME +"='"+smbServer.getUserName()+"',"
				 + SMBServer.PASSWORD +"='"+smbServer.getUserPassword()+"',"
				 + SMBServer.IP +"='"+smbServer.getIdAddress()+"',"
				 + SMBServer.SHARE_FOLDER_NAME +"='"+smbServer.getShareFolderName()+"'"
				 + "WHERE " + SMBServer.ID +" = " + smbServer.getId()+";");
		 	
		} catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
		return Constant.SUCCESS;
	}
	
	public static String addKeyInfoIntoUserInfo(UserInfo inputUserInfo){
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 stmt.executeUpdate( "UPDATE  "+Constant.DB_T_UserInfo+" SET "
				 + UserInfo.KEY_PATH +"='"+inputUserInfo.getKeyPath()+"'"
				 + "WHERE " + UserInfo.ID +" = " + inputUserInfo.getId()+";");
		 	
		} catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
		return Constant.SUCCESS;
	}
	
	public static String addCertInfoIntoUserInfo(UserInfo inputUserInfo){
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 stmt.executeUpdate( "UPDATE  "+Constant.DB_T_UserInfo+" SET "
				 + UserInfo.CERTIFICATE_PATH +"='"+inputUserInfo.getCertificatePath()+"'"
				 + "WHERE " + UserInfo.ID +" = " + inputUserInfo.getId()+";");
		 	
		} catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
		return Constant.SUCCESS;
	}
	
	public static String addRecipient(SMTRecipientInfo recipientInfo) {

		try {
			Statement stmt = DBManager.getInstanceStatement();

			System.out.println("Opened database successfully");

			String sql = "INSERT INTO " + Constant.DB_T_RecipientInfo 
					+ "  ("+SMTRecipientInfo.NAME+", "+SMTRecipientInfo.CERTIFICATE_PATH+","+SMTRecipientInfo.EMAIL+") VALUES ('"+recipientInfo.getName()+"' ,'"+recipientInfo.getCertificatePath()+"' ,'"+recipientInfo.getEmail()+"')";
			
			stmt.executeUpdate(sql);		
			return Constant.SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static String deleteRecipient(SMTRecipientInfo recipientInfo) {

		try {
			Statement stmt = DBManager.getInstanceStatement();

			System.out.println("Opened database successfully");

			String sql = "DELETE FROM " + Constant.DB_T_RecipientInfo 
					+ " WHERE "+SMTRecipientInfo.ID +"="+recipientInfo.getId();
			
			stmt.executeUpdate(sql);		
			return Constant.SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static ArrayList<SMTRecipientInfo> getRecipientInfos(){
		ArrayList<SMTRecipientInfo> recipInfoList = new ArrayList<SMTRecipientInfo>();
		
		
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_RecipientInfo+";" );
	      while ( rs.next() ) {
	    	  
	    	 SMTRecipientInfo recipientInfo = new SMTRecipientInfo();
	    	 recipientInfo.setId(rs.getInt(SMTRecipientInfo.ID));
	    	 recipientInfo.setName(rs.getString(SMTRecipientInfo.NAME));
	    	 recipientInfo.setEmail(rs.getString(SMTRecipientInfo.EMAIL));
	    	 recipientInfo.setCertificatePath(rs.getString(SMTRecipientInfo.CERTIFICATE_PATH));
	    	 recipInfoList.add(recipientInfo);
	      }
		} catch(Exception e){
			e.printStackTrace();
		}
		return recipInfoList;
	}
	
	public static String addEncryptedFile(EncryptFile encryptFile) {
		try {
			Statement stmt = DBManager.getInstanceStatement();
			String sql = "INSERT INTO " + Constant.DB_T_EncryptFileInfo 
					+ "  ("+EncryptFile.FILE_NAME+", "+EncryptFile.S_KEY_VALUE+") VALUES ('"+encryptFile.getFileName()+"' ,'"+encryptFile.getKeyValue()+"')";
			stmt.executeUpdate(sql);		
			return Constant.SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static ArrayList<EncryptFile> getEncryptedFiles(){
		ArrayList<EncryptFile> encryptedFileList = new ArrayList<EncryptFile>();
		
		
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_EncryptFileInfo+";" );
	      while ( rs.next() ) {
	    	  
	    	 EncryptFile encryptFile = new EncryptFile();
	    	 encryptFile.setId(rs.getInt(EncryptFile.ID));
	    	 encryptFile.setFileName(rs.getString(EncryptFile.FILE_NAME));
	    	 encryptFile.setKeyValue(rs.getString(EncryptFile.S_KEY_VALUE));
	    	 encryptedFileList.add(encryptFile);
	      }
		} catch(Exception e){
			e.printStackTrace();
		}
		return encryptedFileList;
	}
	
	public static EncryptFile getEncryptedFileByFileName(String fileName){
		ArrayList<EncryptFile> encryptedFileList = new ArrayList<EncryptFile>();
		EncryptFile encryptFile = new EncryptFile();
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_EncryptFileInfo+" WHERE "+EncryptFile.FILE_NAME+" = '"+fileName+"';" );
	      while ( rs.next() ) {
	    	 encryptFile.setId(rs.getInt(EncryptFile.ID));
	    	 encryptFile.setFileName(rs.getString(EncryptFile.FILE_NAME));
	    	 encryptFile.setKeyValue(rs.getString(EncryptFile.S_KEY_VALUE));
	    	 return encryptFile;
	      }
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public static UserInfo getUserInfoLogon(String PIN){
		UserInfo userInfo = new UserInfo();
		try {
		Statement stmt = DBManager.getInstanceStatement();
		 ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Constant.DB_T_UserInfo+" WHERE "+UserInfo.USER_PIN+" = '"+PIN+"';" );
	      while ( rs.next() ) {
	         int id = rs.getInt(UserInfo.ID);
	         String  name = rs.getString(UserInfo.NAME);
	         String  pin = rs.getString(UserInfo.USER_PIN);
	         String  keyPath = rs.getString(UserInfo.KEY_PATH);
	         String  certPath = rs.getString(UserInfo.CERTIFICATE_PATH);
	         userInfo.setId(id);
	         userInfo.setUserName(name);
	         userInfo.setPIN(pin);
	         userInfo.setKeyPath(keyPath);
	         userInfo.setCertificatePath(certPath);
	         System.out.println();
	         return userInfo;
	      }
		} catch(Exception e){
			userInfo = null;
			e.printStackTrace();
		}
		return null;
	}
	

}
