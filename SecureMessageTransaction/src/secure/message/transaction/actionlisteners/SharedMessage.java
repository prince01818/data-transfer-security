package secure.message.transaction.actionlisteners;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import secure.message.transaction.MainWindowView;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.SMBServer;
import secure.message.transaction.smbapi.FileData;
import secure.message.transaction.smbapi.SmbFileManager;
import secure.message.transaction.utility.ButtonEditor;
import secure.message.transaction.utility.ButtonRenderer;
import secure.message.transaction.utility.UIMessage;

public class SharedMessage implements ActionListener, PropertyChangeListener {

	int operation = 0;

	public SharedMessage(int operationType) {
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
		case 1:{
			SMBServer smbServerObj = DBManager.getSMBInfo();
			SmbFileManager smbFileManager = new SmbFileManager(smbServerObj);
			try {
				smbFileManager.establishCommunicationPath();
			} catch (Exception e) {
				e.printStackTrace();
				UIMessage.showErrorMessage("Can not connect to server");
				return;
			}
			
			ArrayList<FileData> envFileList = null;
			try {
				envFileList = smbFileManager.getFileName("");
			} catch (Exception e) {
				e.printStackTrace();
				UIMessage.showErrorMessage("Can not read data from server");
			}
			
						
			JPanel recipeintPanel = new JPanel(new BorderLayout());
			JLabel title = new JLabel("Shared Message List");
			title.setFont (title.getFont ().deriveFont (24.0f));
			
			if(envFileList != null && envFileList.size() > 0){
				Object rowData[][] = new Object[envFileList.size()][];
				
				for(int i = 0; i < envFileList.size(); i++){
					FileData fileData = envFileList.get(i);
					Object dataObj[] = new Object[3];
					dataObj[0] = i + 1;
					dataObj[1] = fileData.getFileName();
					dataObj[2] = "Download";
					rowData[i] = dataObj;
				}
				
			    Object columnNames[] = { "SL", "Enveloped File Name", "Operation"};
			    //JTable table = new JTable(rowData, columnNames);
			    
			    
			    
			    DefaultTableModel dm = new DefaultTableModel();
		        dm.setDataVector(rowData, columnNames);

		        JTable table = new JTable(dm);
		        table.getColumn("Operation").setCellRenderer(new ButtonRenderer());
		        table.getColumn("Operation").setCellEditor(new ButtonEditor(new JCheckBox()));

		        table.getColumn("SL").setMaxWidth(30);
		        table.getColumn("Operation").setMinWidth(100);
		        table.getColumn("Operation").setMaxWidth(160);
			    

			    JScrollPane scrollPane = new JScrollPane(table);
			    //scrollPane.setBorder(new EmptyBorder(10, 1, 1, 1));
			    
			    recipeintPanel.add(title, BorderLayout.PAGE_START);
			    recipeintPanel.add(scrollPane, BorderLayout.CENTER);
			} else {
				title = new JLabel("No record found");
				recipeintPanel.add(title, BorderLayout.PAGE_START);
			}
			
			
		    
		    MainWindowView.updateContent(recipeintPanel);
		}
			break;
		case 2:{
			//RecipientListView recipientListView = new RecipientListView();
			//MainWindowView.updateContent(recipientListView.getRecipientListData());
		}
			break;
		default:
			break;
		}

	}

}
