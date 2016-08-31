package secure.message.transaction.utility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTable;

public class ViewUtil {
	public void setPreferredSize(JPanel windowPanel){
		int width = windowPanel.getPreferredSize().width;
		int height = windowPanel.getPreferredSize().height;
		windowPanel.setPreferredSize(new Dimension(width, height));
	}

	public void setTableStyle(JTable rTable) {
		rTable.getColumnModel().setColumnMargin(10); // Set column margin
		rTable.getTableHeader().setReorderingAllowed(false);
		rTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		rTable.getTableHeader().setVisible(true);
	}

	public void setContainerDefaultValue(JTable rTable){
		rTable.getTableHeader().setVisible(false);
	}

	public void updateRowHeights(JTable table) {
		try {
			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();
				for (int column = 0; column < table.getColumnCount(); column++) {
					Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
				}
				table.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}
}
