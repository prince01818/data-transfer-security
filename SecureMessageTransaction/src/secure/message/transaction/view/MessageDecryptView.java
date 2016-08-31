package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import secure.message.transaction.db.objects.EncryptFile;
import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.smbapi.FileData;
import secure.message.transaction.utility.ButtonEditor;
import secure.message.transaction.utility.ButtonEditorForDecrypt;
import secure.message.transaction.utility.ButtonRenderer;


public class MessageDecryptView{

	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel getEncryptedMessageListData(ArrayList<EncryptFile> enFileList){
		
		
		JPanel recipeintPanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Encrypted Message List");
		title.setFont (title.getFont ().deriveFont (24.0f));
		
		if(enFileList.size() > 0){
			Object rowData[][] = new Object[enFileList.size()][];
			
			for(int i = 0; i < enFileList.size(); i++){
				EncryptFile recipInfo = enFileList.get(i);
				Object dataObj[] = new Object[3];
				dataObj[0] = i + 1;
				dataObj[1] = recipInfo.getFileName();
				dataObj[2] = "Decrypt";
				rowData[i] = dataObj;
			}
			
		    Object columnNames[] = { "SL", "File", "Operation"};
		    //JTable table = new JTable(rowData, columnNames);
		    
		    
		    
		    DefaultTableModel dm = new DefaultTableModel();
	        dm.setDataVector(rowData, columnNames);

	        JTable table = new JTable(dm);
	        table.getColumn("Operation").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Operation").setCellEditor(new ButtonEditorForDecrypt(new JCheckBox()));

	        table.getColumn("SL").setMaxWidth(30);
	        table.getColumn("Operation").setMaxWidth(160);
	        
		    JScrollPane scrollPane = new JScrollPane(table);
		    //scrollPane.setBorder(new EmptyBorder(10, 1, 1, 1));
		    
		    recipeintPanel.add(title, BorderLayout.PAGE_START);
		    recipeintPanel.add(scrollPane, BorderLayout.CENTER);
		} else {
			
			title = new JLabel("No record found");
			recipeintPanel.add(title, BorderLayout.PAGE_START);
		}
		
		
		return recipeintPanel;
		
		
	}
	
	
}
