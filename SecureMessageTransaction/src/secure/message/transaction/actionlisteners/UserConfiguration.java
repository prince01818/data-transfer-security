package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.UIController;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.UserConfigureView;

public class UserConfiguration implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainWindowView.cleanContent();
		UserConfigureView userConfigureView  = new UserConfigureView();
		UserInfo userInfo2 = DBManager.getUserInfo();
		if(userInfo2 != null ){
			UIMessage.showWarningMessage("User already exsits \n\n User Name: " + userInfo2.getUserName());
			return;
		}
		UserInfo userInfo = userConfigureView.getUserInfo();
		if(userInfo != null){
			String result = DBManager.addUserInfo(userInfo);
			if(result.equals(Constant.SUCCESS)){
				UIController.hasUser = true;
				UIController.hasLogout = true;
				UIController.hasLogin = false;
				MainWindowView.refreshMenuEnabel();
				UIMessage.showSuccessMessage("User Configure Success");
			} else {
				UIMessage.showErrorMessage(result);
			}
			return;
		}
		
	}
}
