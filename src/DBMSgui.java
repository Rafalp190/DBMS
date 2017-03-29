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
	JTextField status, dataBaseUse;

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
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		miOpen = new JMenuItem("Open");
		KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		miOpen.setAccelerator(keyStrokeToOpen);
		miOpen.addActionListener(this);
		menuFile.add(miOpen);
		
		miSave = new JMenuItem("Save");
		KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		miSave.setAccelerator(keyStrokeToSave);
		miSave.addActionListener(this);
		menuFile.add(miSave);
		
		miSaveAs = new JMenuItem("Save As");
		miSaveAs.addActionListener(this);
		menuFile.add(miSaveAs);
		
		JMenu menuEdit = new JMenu("Edit");
		menuBar.add(menuEdit);
		
		miUndo = new JMenuItem("Undo");
		KeyStroke keyStrokeToUndo = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
		miUndo.setAccelerator(keyStrokeToUndo);
		miUndo.addActionListener(this);
		menuEdit.add(miUndo);
		
		miRedo = new JMenuItem("Redo");
		KeyStroke keyStrokeToRedo = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
		miRedo.setAccelerator(keyStrokeToRedo);
		miRedo.addActionListener(this);
		menuEdit.add(miRedo);
		
		 //miRun, miComment, miPrueba
		
		
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
        		
        		
        		scanner.close();
        		
        	} catch (Exception e){
     
        		System.out.println("Error opening file");
        	}
        	
        
        	
        } else {
        	JOptionPane.showMessageDialog(null,"\nNo se ha encontrado el archivo","ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
        }
	}


}
