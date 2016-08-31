package secure.message.transaction.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class FileUtility {

	public static byte[] fileNameToByteArray(String filePath) {
		byte fileData[] = null;
		try {
			File file = new File(filePath);
			System.gc();
			long freeMemory = Runtime.getRuntime().freeMemory();
			
			if(file.length() > freeMemory) return null;
			
			FileInputStream inFile = new FileInputStream(file);
			fileData = new byte[(int) file.length()];
			inFile.read(fileData);
			inFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileData;
	}

	public static String filePathTofileName(String path) throws Exception {
		File file = new File(path);
		return file.getName();
	}

	public static String saveEncryptedFile(byte[] encryptedFileData, String fileName, String destinationPath) throws Exception {

		String updatedFileName = fileName + Constant.ENCRYPTED_FILE_EXTENSION;
		String destination = getValideFileName(destinationPath + Constant.FILE_SEPARATOR + updatedFileName);
		File fileForOutput = new File(destination);
		FileOutputStream outFile = new FileOutputStream(fileForOutput);
		outFile.write(encryptedFileData);
		outFile.close();
		return filePathTofileName(destination);
	}

	public static String removeExtention(String path) throws Exception {
		if(path == null) return null;
		int pos = path.lastIndexOf(".");
		if(pos == -1) return path;
		return path.substring(0,pos);
	}
	
	public static String validateExtention(String fileName) throws Exception {
		if(fileName == null) return null;

		String fileExtention = getFileExtension(fileName);
		System.out.println("fileExtention => " + fileExtention);
		if(fileExtention.contains("(")){
			int pos = fileName.lastIndexOf("(");
			return fileName.substring(0,pos);
		}
		return fileName;
	}
	public static void fileCopy(String sourcePath, String destinationPath){
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
		
			File source = new File(sourcePath);
			File destination = new File(destinationPath);
			
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(destination).getChannel();
			
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public static boolean deleteFile(String filePath){
		boolean isSuccess = false;
		try {
			File file = new File(filePath);
			if(file.delete()){
				isSuccess = true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return isSuccess;
	}

	public static ArrayList<String> getfileListFromFolder(String folderLocatoin){
		ArrayList<String> fileNames = new ArrayList<String>();
		try {
			File folder = new File(folderLocatoin);
			File[] listOfFiles = folder.listFiles();
			
			if(listOfFiles.length > 0) {
				for(int i = 0; i < listOfFiles.length; i++){
					if(listOfFiles[i].isFile()){
						fileNames.add(listOfFiles[i].getName());
					}
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileNames;
	}

	public static String createDirectoryInApp(String dirName){
		try{
			String appDir = Constant.DATA_STORE_PATH;
			System.out.println("Data dir "+appDir);
			File file = new File(appDir + Constant.FILE_SEPARATOR + dirName);
			if(!file.exists()){
				if(file.mkdir())
					return file.getPath();
			}
			else{
				return file.getPath();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return null;
	}

	public static String getAppDirectory(){
		try{
			String appDir =  Constant.DATA_STORE_PATH;
			return appDir;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	public static String getFileExtension(String fileName){
		int position = fileName.lastIndexOf(".");
		if(position> 0){
			System.out.println("File Extention: " + fileName.substring(position));
			return fileName.substring(position);
		}
		return null;
	}

	public static String getValideFileName(String filePath){
		String newFilePath = null;
		File file = new File(filePath);

		if(!file.exists()) return filePath;
		String path = file.getParent();
		String fileName = file.getName();

		try {
			String intermideateFileName = removeExtention(fileName);
			String onlyFileName = removeExtention(intermideateFileName);
			String extention = getFileExtension(intermideateFileName)+getFileExtension(fileName);
			int i = 1;
			File newFile = null;
			do{
				newFilePath = path + Constant.FILE_SEPARATOR + onlyFileName + "("+i+")" + extention;
				newFile = new File(newFilePath);
				System.out.println("getValideFileName => newFile name => " + newFile.getPath());
				i++;
			} while(newFile.exists());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return newFilePath;
	}

}
