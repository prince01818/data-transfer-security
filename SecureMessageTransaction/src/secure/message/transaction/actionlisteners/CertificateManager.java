package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.GenerateCertificate;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.ExportCertificateView;
import secure.message.transaction.view.SelfSingCertificateView;

public class CertificateManager implements ActionListener, PropertyChangeListener {
	int operation = 0;

	public CertificateManager(int operationType) {
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
			try {
				
				MainWindowView.cleanContent();
				UserInfo userInfo2 = DBManager.getUserInfo();
				if(userInfo2 != null ){
					String certPath = userInfo2.getCertificatePath();
					if(certPath != null){
						UIMessage.showWarningMessage("Certificate already exsits \n\n Label: " + certPath);
						return;
					}
				}
				
				ObjectInputStream inputStream = null;

				// Encrypt the string using the public key
				inputStream = new ObjectInputStream(new FileInputStream(Constant.PUBLIC_KEY_FILE));
				final PublicKey publicKey = (PublicKey) inputStream.readObject();

				inputStream = new ObjectInputStream(new FileInputStream(Constant.PRIVATE_KEY_FILE));
				final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
				
				SelfSingCertificateView selfSingCertificateView = new SelfSingCertificateView();
				String result[] = selfSingCertificateView.createSelfSignCertificate();
				if(result == null){
					//UIMessage.showWarningMessage("User Input invalid");
					return;
				}
				
				GenerateCertificate generateCertificate = new GenerateCertificate();
				X509Certificate cert = generateCertificate.generateV3Certificate(result[1], publicKey, privateKey);
				generateCertificate.exportCertificateIntoFile(cert, Constant.USER_CERT_PATH);
				
				userInfo2.setCertificatePath(result[0]);
				String addCertInfoResult = DBManager.addCertInfoIntoUserInfo(userInfo2);
				if(addCertInfoResult.equals(Constant.SUCCESS)){
					UIMessage.showSuccessMessage("Certificate generation success");
				} else {
					UIMessage.showErrorMessage("Certificate generation failed");
				}
				
			} catch (Exception e) {
				UIMessage.showErrorMessage("Certificate generation failed");
			}
		}
			break;
		case 2:
		{
			ExportCertificateView exportCertificateView = new ExportCertificateView();
			String [] result = exportCertificateView.exportCertificate();
			if(result == null){
				//UIMessage.showErrorMessage("Certificate export failed");
				return;
			}
			try{
				File file = new File(Constant.USER_CERT_PATH);
				if(file.length() > 2048){
					UIMessage.showErrorMessage("Certificate export failed");
					return;
				}
				FileInputStream inFile = new FileInputStream(file);
				InputStream inStream = inFile;
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
				
				GenerateCertificate generateCertificate = new GenerateCertificate();
				generateCertificate.exportCertificateIntoFile(cert, result[1]);
				UIMessage.showSuccessMessage("Certificate export success");
			} catch(Exception e){
				UIMessage.showErrorMessage("Certificate export failed");
			}
		}
			break;
		default:
			break;
		}

	}
}
