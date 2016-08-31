package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.GenerateCertificate;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.utility.CertificateUtility;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.FileUtility;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.utility.Utility;
import secure.message.transaction.view.RecipientAddView;
import secure.message.transaction.view.RecipientListView;

public class Recipient implements ActionListener, PropertyChangeListener {

	int operation = 0;

	public Recipient(int operationType) {
		this.operation = operationType;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		MainWindowView.cleanContent();
		switch (operation) {
		case 1:
		{
			RecipientAddView recipientAddView = new RecipientAddView();
			String[] result = recipientAddView.getRecipientAddData();
			
			if(result == null){
				UIMessage.showWarningMessage("Invalied input");
			}
			
			try {
				X509Certificate cert = CertificateUtility.getCertificate(result[2]);
				if (cert == null) {
					UIMessage.showErrorMessage("Invalied certificate");
					return;
				}
				
				String recipientCertName = BigInteger.valueOf(System.currentTimeMillis())+FileUtility.filePathTofileName(result[2])+"";
				
				GenerateCertificate generateCertificate = new GenerateCertificate();
				generateCertificate.exportCertificateIntoFile(cert, Constant.RECIPIENT_CERT_PATH+Utility.getFileSeparator()+recipientCertName);
				
				SMTRecipientInfo recipientInfo = new SMTRecipientInfo();
				recipientInfo.setName(result[0]);
				recipientInfo.setEmail(result[1]);
				recipientInfo.setCertificatePath(recipientCertName);
				
				String dbResult = DBManager.addRecipient(recipientInfo);
				if(dbResult.equals(Constant.SUCCESS)){
					UIMessage.showSuccessMessage("Recipient add success");
				} else {
					UIMessage.showErrorMessage(dbResult);
				}
				return;
				
			} catch(Exception e){
				
			}
		}
			break;
		case 2:
		{
			ArrayList<SMTRecipientInfo> recipInfoList = DBManager.getRecipientInfos();
			RecipientListView recipientListView = new RecipientListView();
			MainWindowView.updateContent(recipientListView.getRecipientListData(recipInfoList));
		}
			
			break;
		default:
			break;
		}

	}

}
