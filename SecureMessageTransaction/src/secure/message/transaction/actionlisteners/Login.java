package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.UIController;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.PinOperationView;



public class Login implements ActionListener, PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		MainWindowView.cleanContent();
		
		UserInfo userInfo1 = DBManager.getUserInfo();
		if(userInfo1 == null){
			UIMessage.showWarningMessage("User not exists!\nConfigure user");
			return;
		}
		
		String userPIN = PinOperationView.getLoginData();
		if(userPIN != null) {
			System.out.println("PIN : " + userPIN);
			UserInfo userInfo = DBManager.getUserInfoLogon(userPIN);
			if(userInfo == null){
				UIController.hasLogout = true;
				UIController.hasLogin = false;
				MainWindowView.refreshMenuEnabel();
				UIMessage.showErrorMessage("Login failed");
				return;
			} else {
				UIController.hasLogout = false;
				UIController.hasLogin = true;
				MainWindowView.refreshMenuEnabel();
				UIMessage.showSuccessMessage("Login success");
				return;
			}
			
		}
	}

}
