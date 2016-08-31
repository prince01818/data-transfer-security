package secure.message.transaction.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import secure.message.transaction.db.objects.SMTRecipientInfo;
import secure.message.transaction.smbapi.FileData;
import secure.message.transaction.utility.ButtonEditor;
import secure.message.transaction.utility.ButtonRenderer;


public class RecipientListView{

	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel getRecipientListData(ArrayList<SMTRecipientInfo> recipInfoList){
		
		
		JPanel recipeintPanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Recipient List");
		title.setFont (title.getFont ().deriveFont (24.0f));
		
		if(recipInfoList.size() > 0){
			Object rowData[][] = new Object[recipInfoList.size()][];
			
			for(int i = 0; i < recipInfoList.size(); i++){
				SMTRecipientInfo recipInfo = recipInfoList.get(i);
				Object dataObj[] = new Object[3];
				dataObj[0] = i + 1;
				dataObj[1] = recipInfo.getName();
				dataObj[2] = recipInfo.getEmail();
				rowData[i] = dataObj;
			}
			
		    Object columnNames[] = { "SL", "Name",  "Email"};
		    //JTable table = new JTable(rowData, columnNames);
		    
		    
		    
		    DefaultTableModel dm = new DefaultTableModel();
	        dm.setDataVector(rowData, columnNames);
	        
	        
	        JTable table = new JTable(dm);
	        table.getColumn("SL").setMaxWidth(30);
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
