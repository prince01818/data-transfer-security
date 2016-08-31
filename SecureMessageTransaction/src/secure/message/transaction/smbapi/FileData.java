package secure.message.transaction.smbapi;

public class FileData {
	public FileData(String fileName, int type) {
		this.fileName = fileName;
		this.type = type;
	}
	public static final int TYPE_DIR = 1;
	public static final int TYPE_FILE = 0;
	public String fileName;
	public int type;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
