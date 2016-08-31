package secure.message.transaction.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.utility.Constant;
import secure.message.transaction.utility.UIMessage;
import secure.message.transaction.view.ServerConfigureView;

public class ServerConfiguration implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainWindowView.cleanContent();
		ServerConfigureView  serverConfigureView = new ServerConfigureView();
		SMBServer smbServerInput = DBManager.getSMBInfo();
		
		SMBServer smbSever = serverConfigureView.getSMBServerInputData(smbServerInput);
		if(smbSever != null){
			String result = DBManager.updateSMBInfo(smbSever);
			if(result.equals(Constant.SUCCESS)){
				UIMessage.showSuccessMessage("Configure Success");
			} else {
				UIMessage.showErrorMessage(result);
			}
			return;
		}
		
	}
}
