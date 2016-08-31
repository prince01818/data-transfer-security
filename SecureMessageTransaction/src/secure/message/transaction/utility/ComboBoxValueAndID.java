package secure.message.transaction.utility;

import secure.message.transaction.db.objects.SMTRecipientInfo;

public class ComboBoxValueAndID {

	public SMTRecipientInfo id;
	public String label;
	
	public ComboBoxValueAndID(){
		
	}
	public ComboBoxValueAndID(SMTRecipientInfo recInfo, String label){
		setId(recInfo);
		setLabel(label);
	}
	
	public SMTRecipientInfo getId() {
		return id;
	}
	public void setId(SMTRecipientInfo id) {
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
