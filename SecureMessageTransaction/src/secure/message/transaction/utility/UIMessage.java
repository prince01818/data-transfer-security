package secure.message.transaction.utility;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class UIMessage {
	public static void showErrorMessage(String errorReason, String message) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, errorReason, JOptionPane.ERROR_MESSAGE);
	}
	public static void showErrorMessage(String message) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, "Error Message", JOptionPane.ERROR_MESSAGE);
		
	}
	
	public static void showWarningMessage(String message) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, "Warning Message", JOptionPane.WARNING_MESSAGE);
	}	
	
	public static void showSuccessMessage(String message) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, "Success Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showSuccessMessage(String operationName, String message) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, operationName, JOptionPane.INFORMATION_MESSAGE);
	}
}
