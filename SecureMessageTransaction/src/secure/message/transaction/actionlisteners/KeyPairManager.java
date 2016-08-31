package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.crypto.GenerateKeyPair;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.KeyPairGenerationView;
import secure.message.transaction.view.UserConfigureView;

public class KeyPairManager implements ActionListener {

	
	@Override
	public void actionPerformed(ActionEvent e) {
		MainWindowView.cleanContent();
		UserInfo userInfo2 = DBManager.getUserInfo();
		if(userInfo2 != null ){
			String keyPath = userInfo2.getKeyPath();
			if(keyPath != null){
				UIMessage.showWarningMessage("Key already exsits \n\n Label: " + keyPath);
				return;
			}
			
		}
		KeyPairGenerationView keyPairGenerationView = new KeyPairGenerationView();
		String result[] = keyPairGenerationView.getKeyPairGenerationInputData();
		if(result != null){
			GenerateKeyPair generateKeyPair = new GenerateKeyPair();
			boolean genKeyResult = generateKeyPair.generateKey(Integer.parseInt(result[1]));
			if(genKeyResult){
				userInfo2.setKeyPath(result[0]);
				String addKeyInfoResult = DBManager.addKeyInfoIntoUserInfo(userInfo2);
				if(addKeyInfoResult.equals(Constant.SUCCESS)){
					UIMessage.showSuccessMessage("Key pair generation success");
				} else {
					UIMessage.showErrorMessage("Key pair generation failed");
				}
			} else {
				UIMessage.showErrorMessage("Key pair generation failed");
			}
			return;
		}
		
	}
}
