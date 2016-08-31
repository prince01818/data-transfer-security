package secure.message.transaction.smbapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.FileUtility;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SmbFileManager{
	SMBServer smbServer;
	public SmbFileManager(SMBServer smbServerObj) {
		smbServer = smbServerObj;
	}
	
	public final static String TAG = "SmbFileManager";
	static boolean isConnected = false;

	static String rootPath = null;
	static NtlmPasswordAuthentication auth = null;
	static String defaultDestinationPath = rootPath;

	public String getPath(String path) {
		return rootPath + path + Constant.FILE_SEPARATOR;
	}

	

	
	public boolean downloadFile(String sourcePath, String destinationPath) throws Exception {

		if (rootPath == null || auth == null) {
			//throw new ErrorException(ErrorException.E_SMB_CONFIGURE);
			System.out.println("Error SM Configure");
			return false;
		}

		System.out.println("Source Path: " + sourcePath);
		sourcePath = rootPath +"/"+ sourcePath;
		System.out.println("real source Path: " + sourcePath);
		System.out.println("Destination Path: " + destinationPath);

		if (destinationPath.length() == 0)
			return false;

		try {

			SmbFile smbSourceFile = new SmbFile(sourcePath, auth);
			if (smbSourceFile.exists()) {
				SmbFileInputStream mFStream = new SmbFileInputStream(smbSourceFile);
				FileOutputStream mFileOutputStream = new FileOutputStream(new File(destinationPath));
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = mFStream.read(buffer)) > 0) {
					mFileOutputStream.write(buffer, 0, len);
				}
				mFileOutputStream.close();
				mFStream.close();
			} else {
				return false;
			}
		} catch (SocketException se){
			System.out.println("E_CONNECTION_TIME_OUT");
		} catch (SmbException smbe){
			System.out.println("E_CONNECTION_TIME_OUT");
		} catch (Exception e) {
			System.out.println("File download failed => " + e.getMessage());
			e.printStackTrace();
		}

		return true;

	}

	public String[] shareFile(String filePath, String destinationPath) throws Exception {
		System.out.println("Enter shareFile");
		String result[] = new String[2];
		result[0] = Constant.FAILED;

		if (rootPath == null || auth == null) {
			//throw new ErrorException(ErrorException.E_SMB_CONFIGURE);
			System.out.println("Error SM Configure");
			return null;
		}

		try {

			String fileName = FileUtility.filePathTofileName(filePath);
			//if (Constant.DEFAULT_DESTINATION) {
			destinationPath = defaultDestinationPath + Constant.FILE_SEPARATOR + fileName;
//			} else {
//				destinationPath = rootPath + destinationPath + Constant.FILE_SEPARATOR + fileName;
//			}
			System.out.println("destinationPath : " + destinationPath);

			SmbFile dir = new SmbFile(destinationPath, auth);
			SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(dir);
			FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			byte[] buf = new byte[1024];
			int len;
			while ((len = fileInputStream.read(buf)) > 0) {
				smbFileOutputStream.write(buf, 0, len);
			}
			fileInputStream.close();
			smbFileOutputStream.close();
			result[0] = Constant.SUCCESS;
			result[1] = fileName;

		} catch (SocketException se){
			System.out.println("E_CONNECTION_TIME_OUT");
		} catch (SmbException smbe){
			System.out.println("E_CONNECTION_TIME_OUT");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Share file. Error: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Exit shareFile");
		return result;

	}

	public String truncate(String dirName) {
		return dirName.substring(0, dirName.length() - 1);
	}

	public ArrayList<FileData> getFileName(String path) throws Exception {

		System.out.println("Enter get file name");

		ArrayList<FileData> fileName = new ArrayList<FileData>();

		if (rootPath == null || auth == null) {
			//throw new ErrorException(ErrorException.E_SMB_CONFIGURE);
			System.out.println("Error SM Configure");
			return null;
		}

		try {

			System.out.println("path: " + path);
			System.out.println("getPath path: " + getPath(path));

			SmbFile smbFile = new SmbFile(getPath(path), auth);
			if (smbFile.exists()) {
				if ((smbFile != null) && smbFile.isDirectory()) {
					fileName.clear();
					SmbFile[] listOfFiles = smbFile.listFiles();
					if (listOfFiles.length > 0) {
						for (int i = 0; i < listOfFiles.length; i++) {
							if (listOfFiles[i].isFile()) {
								if (Constant.ENVELOPED_FILE_EXTENSION.equals(FileUtility
										.getFileExtension(listOfFiles[i].getName()))) {
									fileName.add(new FileData(listOfFiles[i].getName(), FileData.TYPE_FILE));
								}
							} else {
								fileName.add(new FileData(truncate(listOfFiles[i].getName()), FileData.TYPE_DIR));
							}
						}
					}
				}
			}

		} catch (SmbException smbe){
			smbe.printStackTrace();
			System.out.println("E_CONNECTION_TIME_OUT");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage() + "\n\n" + "E_SMB_CONF_PROPERTY");
		}
		System.out.println("Exit get file name");
		return fileName;
	}

	public boolean isReadyCommunicationPath() {
		System.out.println("isReadyCommunicationPath => " + isConnected);
		return isConnected;
	}

	public String establishCommunicationPath() throws Exception {
		
		
		SmbConfiguration smbConfiguration =new SmbConfiguration(smbServer);
		if (smbConfiguration.getIP().length() == 0) {
			System.out.println("Confiuger SMB. Press memu -> Configure");
			return "SMB not Confiugered";
		}
		rootPath = smbConfiguration.getRootPath();
		defaultDestinationPath = rootPath;
		if (auth == null) {
			auth = new NtlmPasswordAuthentication(null, smbConfiguration.getUserName(),
					smbConfiguration.getUserPassword());
		}

		
		if (rootPath == null || auth == null) {
			isConnected = false;
			return "SMB not configured path not found";
		} else {

			try {
				SmbFile smbFile = new SmbFile(rootPath, auth);
				if (smbFile.exists()) {
					System.out.println("Connected isConnected => true");
					isConnected = true;
					return Constant.SUCCESS;
				} else {
					System.out.println("Connected isConnected => false");
					isConnected = false;
				}

			} catch (Exception e) {
				System.out.println("Connected mesage => " + e.getMessage());
				isConnected = false;
				return e.getMessage() + "\n\n" + "E_SMB_CONF_PROPERTY";
			}
		}

		return "Connection failed";
	}

	
	public void removeCommunicationPath() {
		auth = null;

	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

}
