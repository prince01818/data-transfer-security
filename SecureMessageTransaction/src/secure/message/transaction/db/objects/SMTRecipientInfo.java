package secure.message.transaction.db.objects;

public class SMTRecipientInfo {
	
	public final static String ID = "ID"; 
	public final static String NAME = "NAME";
	public final static String EMAIL = "EMAIL_ID";
	public final static String CERTIFICATE_PATH = "CERTIFICATE_FILE_NAME";
	
	public int id;
	public String name;
	public String email;
	public String certificatePath;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCertificatePath() {
		return certificatePath;
	}
	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}
	
}
