package secure.message.transaction;

import secure.message.transaction.db.DBManager;
import secure.message.transaction.utility.Constant;

public class AppLauncher {
	public static void main(String[] args) {
		try {
			
			if(!DBManager.isDataBaseExsit(Constant.DB_NAME)){
				DBManager.createTables();
			}
			@SuppressWarnings("unused")
			MainWindowView mainWindowView = new MainWindowView();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
