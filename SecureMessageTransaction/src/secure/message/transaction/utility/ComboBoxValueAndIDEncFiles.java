package secure.message.transaction.utility;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;

public class ComboBoxValueAndIDEncFiles {

	public EncryptFile id;
	public String label;
	
	public ComboBoxValueAndIDEncFiles(){
		
	}
	public ComboBoxValueAndIDEncFiles(EncryptFile recInfo, String label){
		setId(recInfo);
		setLabel(label);
	}
	
	public EncryptFile getId() {
		return id;
	}
	public void setId(EncryptFile id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString(){
		return getLabel();
	}
}
