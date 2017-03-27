import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Scanner;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.undo.UndoManager;

import views.SqlDocument;


public class DBMSgui extends JFrame implements ActionListener {

	
	JMenuItem miOpen, miSave, miSaveAs, miUndo, miRedo, miRun, miComment, miPrueba;
	JButton btnOpenFile, btnSave, btnRun, btnUndo, btnRedo, btnDelete, btnVerbose;
	JTextPane textArea, dataOutputArea, dataReadArea, dataVerbose;
	JTextField status, dataBaseUse;
	JSplitPane splitPane1;
	JTabbedPane tabbedPane;

	JFileChooser fileChooser;
	UndoManager undoManager;
	File file;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DBMSgui window = new DBMSgui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DBMSgui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.setBounds(100, 100, (width/2), (height/2));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		fileChooser = new JFileChooser();
		undoManager = new UndoManager();
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		miOpen = new JMenuItem("Open");
		KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		miOpen.setAccelerator(keyStrokeToOpen);
		miOpen.addActionListener(this);
		
		mnNewMenu.add(miOpen);
		
		//arreglar todo esto!!
		JScrollPane scrollPane_1 = new JScrollPane();
		
		textArea = new JTextPane();
		textArea.setDocument(new SqlDocument());
		textArea.getDocument().addUndoableEditListener(undoManager);
		scrollPane_1.setViewportView(textArea);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == miOpen ||
				e.getSource() == btnOpenFile){
			open();
	        
		}
		
	}
	
	public void open(){
		int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION){
        	file = fileChooser.getSelectedFile();
        	miSave.setEnabled(true);
        	btnSave.setEnabled(true);
        	try{
        		
        		Scanner scanner = new Scanner(file);
        		String newTxt = "";
        		while (scanner.hasNextLine()){
        			newTxt += scanner.nextLine()+"\n";
        		}
        		SqlDocument doc = new SqlDocument();
        		textArea.setDocument(new DefaultStyledDocument());
        		doc.insertString(0, newTxt, null);
        		
        		textArea.setDocument(doc);
        		
        		scanner.close();
        		
        	} catch (Exception e){
     
        		System.out.println("Error opening file");
        	}
        	
        
        	
        } else {
        	JOptionPane.showMessageDialog(null,"\nNo se ha encontrado el archivo","ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
        }
	}


}
