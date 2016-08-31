package secure.message.transaction.view;

import java.util.ArrayList;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;

public class MessgaeShareInputData {
	ArrayList<SMTRecipientInfo> recipInfoList = new ArrayList<SMTRecipientInfo>();
	EncryptFile encryptFile;
	public ArrayList<SMTRecipientInfo> getRecipInfoList() {
		return recipInfoList;
	}
	public void setRecipInfoList(ArrayList<SMTRecipientInfo> recipInfoList) {
		this.recipInfoList = recipInfoList;
	}
	public EncryptFile getEncryptFile() {
		return encryptFile;
	}
	public void setEncryptFile(EncryptFile encryptFile) {
		this.encryptFile = encryptFile;
	}
	
}
