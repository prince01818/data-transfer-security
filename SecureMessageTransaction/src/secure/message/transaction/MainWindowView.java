package secure.message.transaction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import secure.message.transaction.actionlisteners.CertificateManager;
import secure.message.transaction.actionlisteners.KeyPairManager;
import secure.message.transaction.actionlisteners.Login;
import secure.message.transaction.actionlisteners.MessageDecrypt;
import secure.message.transaction.actionlisteners.MessageEncrypt;
import secure.message.transaction.actionlisteners.MessageShare;
import secure.message.transaction.actionlisteners.Recipient;
import secure.message.transaction.actionlisteners.ServerConfiguration;
import secure.message.transaction.actionlisteners.SharedMessage;
import secure.message.transaction.actionlisteners.UserConfiguration;
import secure.message.transaction.db.DBManager;
import secure.message.transaction.db.objects.UserInfo;
import secure.message.transaction.utility.Constant;


public class MainWindowView {
	private static JFrame frame;
	static JMenuBar menuBar;
	// Menu
	private JMenu mnManager;
	private JMenu mnAuthentication;
	private JMenu mnMessage;
	private JMenu mnAbout;

	// Manager Menu Item
	private JMenu mntmConfiguretion;
	private JMenu mntmRecipient;
	private JMenu mntmKeyPair;
	private JMenu mntmCertificate;
	private JMenuItem mntmExit;
	
	// mntmConfiguretion menu
	private static JMenuItem mntmServerConfigation;
	private static JMenuItem mntmUserConfigation;
	

	// Recipient Menu
	private static JMenuItem mntmRecipientAdd;
	private static JMenuItem mntmRecipientList;
	
	// Message Menu Item
	private static JMenuItem mntmEncrypt;
	private static JMenuItem mntmDecrypt;
	private static JMenuItem mntmShareMessage;
	private static JMenuItem mntmSharedMessage;
	
	
	// Authentication Menu Item
	private static JMenuItem mntmLogin;
	private static JMenuItem mntmLogout;
	private JMenuItem mntmChangeUserPin;
	
	// Key Menu Item
	private static JMenuItem mntmGenerateKeyPair;

	// Certificate Menu Item

	private JMenuItem mntmImportCertificate;
	private JMenuItem mntmShowCertificate;
	private static JMenuItem mntmExportCertificate;
	private JMenuItem mntmCreateCertificateRequest;
	private static JMenuItem mntmCreateSelfSigned;

	
	// About Menu Item
	private JMenuItem mntmAbout;
	private JMenuItem mntmHelp;

	
	
	public MainWindowView() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public class JPanelWithBackground extends JPanel {

		  private Image backgroundImage;

		  // Some code to initialize the background image.
		  // Here, we use the constructor to load the image. This
		  // can vary depending on the use case of the panel.
		  public JPanelWithBackground(String fileName) throws IOException {
		    backgroundImage = ImageIO.read(new File(fileName));
		  }

		  public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    // Draw the background image.
		    g.drawImage(backgroundImage, 0, 0, this);
		  }
		}
	
	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//frame.getContentPane().setBackground(Color.);
//		/frame.getContentPane().add(new JPanelWithBackground("res/bg_image.png"));
		frame.setTitle(Constant.APP_NAME);
		frame.setSize(700, 500);
		
		ImageIcon imageIcon = new ImageIcon("res/logo.png");
		frame.setIconImage(imageIcon.getImage());

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		int x = (screenSize.width - frame.getWidth()) / 2;
		int y = (screenSize.height - frame.getHeight()) / 2;

		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = menuInitialize();
		frame.getContentPane().add(menuBar, BorderLayout.PAGE_START);
		defaultContent();
//		
//		// ------------------- left ------------------- 
//		splitPane.setLeftComponent(leftMenu());
//		// ------------------- right -------------------
//		splitPane.setRightComponent(container());
//
		//frame.getContentPane().add(splitPane, BorderLayout.CENTER);
//	
//		// menu onclick action
		menuAction();
		UserInfo userInfo = DBManager.getUserInfo();
		if(userInfo == null){
			UIController.hasUser = false;
		} else {
			UIController.hasUser = true;
			UIController.hasLogin = false;
			UIController.hasLogout = true;
		}
		menuEnabled();
		frame.revalidate();
	}
	
	public static void defaultContent(){
		JPanel containPanel = new JPanel();
		JLabel title = new JLabel("Welcome to Secured Message Transaction");
		
		try{
			BufferedImage myPicture = ImageIO.read(new File("res/logo.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			containPanel.add(picLabel);
		} catch(Exception e){
			
		}
		title.setFont (title.getFont ().deriveFont (24.0f));
		containPanel.add(title);
		containPanel.setBorder(new EmptyBorder(60, 10, 10, 10));
		frame.getContentPane().add(containPanel, BorderLayout.CENTER);
	}
	
	public static void updateContent(JPanel containPanel){
		frame.getContentPane().removeAll();
		frame.getContentPane().add(menuBar, BorderLayout.PAGE_START);
		containPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(containPanel, BorderLayout.CENTER);
		frame.revalidate();
	}

	public static void cleanContent(){
		frame.getContentPane().removeAll();
		frame.getContentPane().add(menuBar, BorderLayout.PAGE_START);
		frame.getContentPane().add(new JPanel(), BorderLayout.CENTER);
		frame.revalidate();
	}

	public static void refreshMenuEnabel(){
		frame.getContentPane().removeAll();
		frame.getContentPane().add(menuBar, BorderLayout.PAGE_START);
		frame.getContentPane().add(new JPanel(), BorderLayout.CENTER);
		menuEnabled();
		frame.revalidate();
	}
	
	private void menuBarItemsInitialize(){
		mnManager 									= new JMenu("Manager");
		mnAuthentication 								= new JMenu("Authentication");
		mnMessage 									= new JMenu("Message");
		mnAbout 										= new JMenu("About");

		// Manager Menu Item
		mntmConfiguretion 								= new JMenu("Configuration");
		mntmRecipient 								= new JMenu("Recipient");
		mntmKeyPair 									= new JMenu("Key");
		mntmCertificate 								= new JMenu("Certificate");
		mntmExit 									= new JMenuItem("Exit");

		//mntmConfiguretion
		mntmServerConfigation	= new JMenuItem("Server Configuration");
		mntmUserConfigation	= new JMenuItem("User Configuration");
		
		// Recipient Menu
		mntmRecipientAdd 							= new JMenuItem("Recipient Add");
		mntmRecipientList 						= new JMenuItem("Recipient List");
		
		// Message Menu Item
		mntmEncrypt 								= new JMenuItem("Encrypt");
		mntmDecrypt 								= new JMenuItem("Decrypt");
		mntmShareMessage							= new JMenuItem("Share Message");
		mntmSharedMessage 						= new JMenuItem("Shared Message");
		
		
		// Authentication Menu Item
		mntmLogin 								= new JMenuItem("Login");
		mntmLogout 								= new JMenuItem("Logout");
		mntmChangeUserPin 						= new JMenuItem("Change User PIN");
		
		// Key Menu Item
		mntmGenerateKeyPair 						= new JMenuItem("Generate Key Pair");

		// Certificate Menu Item

		mntmImportCertificate 					= new JMenuItem("Import Certificate");
		mntmShowCertificate 						= new JMenuItem("Show Certificate");
		mntmExportCertificate 					= new JMenuItem("Export Certificate");
		mntmCreateCertificateRequest 				= new JMenuItem("Create Certificate Request");
		mntmCreateSelfSigned 						= new JMenuItem("Create Certificate");

		
		// About Menu Item
		mntmAbout 								= new JMenuItem("About");
		mntmHelp 									= new JMenuItem("Help");

		
	}
	
	private JMenuBar menuInitialize() {
		JMenuBar menuBar = new JMenuBar();
		menuBarItemsInitialize();
		// Menu
		menuBar.add(mnManager);
		mnManager.add(mntmConfiguretion);
		mnManager.add(mntmKeyPair);
		mnManager.add(mntmCertificate);
		mnManager.add(mntmRecipient);
		mnManager.add(new JSeparator());
		mnManager.add(mntmExit);

		// Configuration 
		mntmConfiguretion.add(mntmUserConfigation);
		mntmConfiguretion.add(mntmServerConfigation);
		
		// Key Pair Menu
		mntmKeyPair.add(mntmGenerateKeyPair);
				
		// Certificate Menu
		//mntmCertificate.add(mntmImportCertificate);
		//mntmCertificate.add(mntmShowCertificate);
		mntmCertificate.add(mntmExportCertificate);
		//mntmCertificate.add(mntmCreateCertificateRequest);
		mntmCertificate.add(mntmCreateSelfSigned);

		// Recipient Menu
		mntmRecipient.add(mntmRecipientAdd);
		mntmRecipient.add(mntmRecipientList);

		// Authentication Menu

		menuBar.add(mnAuthentication);		
		mnAuthentication.add(mntmLogin);
		mnAuthentication.add(mntmLogout);
		//mnAuthentication.add(new JSeparator());
		//mnAuthentication.add(mntmChangeUserPin);
		
		// Message Menu
		menuBar.add(mnMessage);
		mnMessage.add(mntmEncrypt);
		mnMessage.add(mntmDecrypt);
		mnMessage.add(mntmShareMessage);
		mnMessage.add(mntmSharedMessage);


		// About Menu
		menuBar.add(mnAbout);
		mnAbout.add(mntmAbout);
		//mnAbout.add(mntmHelp);

		return menuBar;
	}
	
	public static void menuEnabled() {
		
		if(UIController.hasUser){
			mntmUserConfigation.setEnabled(false);
			mntmServerConfigation.setEnabled(true);
			
			mntmGenerateKeyPair.setEnabled(true);
			mntmExportCertificate.setEnabled(true);
			mntmCreateSelfSigned.setEnabled(true);
	
			// Recipient Menu
			mntmRecipientAdd.setEnabled(true);
			mntmRecipientList.setEnabled(true);
	
			// Authentication Menu
	
			mntmLogin.setEnabled(true);
			mntmLogout.setEnabled(true);
			
			mntmEncrypt.setEnabled(true);
			mntmDecrypt.setEnabled(true);
			mntmShareMessage.setEnabled(true);
			mntmSharedMessage.setEnabled(true);
		} else {
			mntmUserConfigation.setEnabled(true);
			mntmServerConfigation.setEnabled(false);
			
			mntmGenerateKeyPair.setEnabled(false);
			mntmExportCertificate.setEnabled(false);
			mntmCreateSelfSigned.setEnabled(false);
	
			// Recipient Menu
			mntmRecipientAdd.setEnabled(false);
			mntmRecipientList.setEnabled(false);
	
			// Authentication Menu
	
			mntmLogin.setEnabled(false);
			mntmLogout.setEnabled(false);
			
			mntmEncrypt.setEnabled(false);
			mntmDecrypt.setEnabled(false);
			mntmShareMessage.setEnabled(false);
			mntmSharedMessage.setEnabled(false);
		}
		
		if(UIController.hasLogin){
			mntmUserConfigation.setEnabled(false);
			mntmServerConfigation.setEnabled(true);
			
			mntmGenerateKeyPair.setEnabled(true);
			mntmExportCertificate.setEnabled(true);
			mntmCreateSelfSigned.setEnabled(true);
	
			// Recipient Menu
			mntmRecipientAdd.setEnabled(true);
			mntmRecipientList.setEnabled(true);
	
			// Authentication Menu
	
			mntmLogin.setEnabled(false);
			mntmLogout.setEnabled(true);
			
			mntmEncrypt.setEnabled(true);
			mntmDecrypt.setEnabled(true);
			mntmShareMessage.setEnabled(true);
			mntmSharedMessage.setEnabled(true);
		}

		if(UIController.hasLogout){
			mntmUserConfigation.setEnabled(false);
			mntmServerConfigation.setEnabled(false);
			
			mntmGenerateKeyPair.setEnabled(false);
			mntmExportCertificate.setEnabled(false);
			mntmCreateSelfSigned.setEnabled(false);
	
			// Recipient Menu
			mntmRecipientAdd.setEnabled(false);
			mntmRecipientList.setEnabled(false);
	
			// Authentication Menu
	
			mntmLogin.setEnabled(true);
			mntmLogout.setEnabled(false);
			
			mntmEncrypt.setEnabled(false);
			mntmDecrypt.setEnabled(false);
			mntmShareMessage.setEnabled(false);
			mntmSharedMessage.setEnabled(false);
		}
		
	}
	
	
	private void menuAction(){
		mntmUserConfigation.addActionListener(new UserConfiguration());
		mntmServerConfigation.addActionListener(new ServerConfiguration());
		
		mntmGenerateKeyPair.addActionListener(new KeyPairManager());
		mntmCreateSelfSigned.addActionListener(new CertificateManager(1));
		mntmExportCertificate.addActionListener(new CertificateManager(2));
		
		mntmRecipientAdd.addActionListener(new Recipient(1));
		mntmRecipientList.addActionListener(new Recipient(2));
		
		mntmLogin.addActionListener(new Login());
		mntmLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UIController.hasLogout = true;
				UIController.hasLogin = false;
				refreshMenuEnabel();
				
			}
		});
		mntmChangeUserPin.addActionListener(null);
		
		mntmEncrypt.addActionListener(new MessageEncrypt());
		mntmDecrypt.addActionListener(new MessageDecrypt());
		mntmShareMessage.addActionListener(new MessageShare());
		mntmSharedMessage.addActionListener(new SharedMessage(1));
		
		
		
		mntmExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure", "Exit Application",
						JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent){
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure", "Exit Application",
						JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
		mntmAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());

				
				JLabel lblContent = new JLabel();
				String content = "<html>";
				content += "  <p style='text-align:center;'><h3>"+Constant.APP_NAME+"</h3>";
				content += "  <p style='text-align:center;'>Tasnova Fahana</p>";
				content += "  <p style='text-align:center;'>Bappy</p>";
				content += "  <p style='text-align:center;'>Prince</p>";
				content += "</html>";
				lblContent.setHorizontalAlignment(JLabel.CENTER);
				lblContent.setText(content);
				panel.add(lblContent, BorderLayout.PAGE_START);

				JDialog dialog = new JDialog();
				dialog.setIconImage(new ImageIcon(Constant.APP_ICON).getImage());
				dialog.setTitle("About Us");
				dialog.setSize(380, 400);
				dialog.setContentPane(panel);
				dialog.setLocationRelativeTo(null);
				dialog.setModal(true);
				dialog.setVisible(true);
				
			}
		});

//		mntmGenerateKeyPair.addActionListener(new KeyOperations(Constant.GENERATE_KEY_PAIR));
//		mntmGenerateSecretKey.addActionListener(new KeyOperations(Constant.GENERATE_SECRET_KEY));
//		mntmCreateSecretKey.addActionListener(new KeyOperations(Constant.CREATE_SECRET_KEY));
	}
	
	
}
