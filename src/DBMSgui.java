import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.undo.UndoManager;

import views.Tree;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class DBMSgui extends JFrame implements ActionListener {

	
	JMenuItem miOpen, miSave, miSaveAs, miUndo, miRedo, miRun, miComment, miCut, miCopy, miPaste;
	JButton btnOpen, btnSave, btnRun, btnUndo, btnRedo, btnDelete;
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
		
		miCut = new JMenuItem("Cut");
		KeyStroke keyStrokeToCut = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);
		miCut.setAccelerator(keyStrokeToCut);
		miCut.addActionListener(this);
		menuEdit.add(miCut);
		
		miCopy = new JMenuItem("Copy");
		KeyStroke keyStrokeToCopy = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
		miCopy.setAccelerator(keyStrokeToCut);
		miCopy.addActionListener(this);
		menuEdit.add(miCopy);
		
		miPaste = new JMenuItem("Paste");
		KeyStroke keyStrokeToPaste = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
		miPaste.setAccelerator(keyStrokeToCut);
		miPaste.addActionListener(this);
		menuEdit.add(miCopy);
		
		miComment = new JMenuItem("Comment");
		miComment.addActionListener(this);
		menuEdit.add(miComment);
		
		JMenu menuRun = new JMenu("Run");
		menuBar.add(menuRun);
		
		miRun = new JMenuItem("Run");
		KeyStroke keyStrokeToRun = KeyStroke.getKeyStroke(KeyEvent.VK_F5,0); 
		miRun.setAccelerator(keyStrokeToRun);
		miRun.addActionListener(this);
		menuRun.add(miRun);
		
		//arreglar desde aqui
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnOpen = new JButton("Open");
		btnOpen.setToolTipText("Open");
		btnOpen.addActionListener(this);
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/open.png"));
			btnOpen.setIcon(new ImageIcon(img));
			btnOpen.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnOpen.setBorder(emptyBorder);
		} 
		catch (Exception e){
			System.out.println("Error in imagenes/open.png");
		}
		toolBar.add(btnOpen);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		btnSave.setToolTipText("Save");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/save.png"));
			btnSave.setIcon(new ImageIcon(img));
			btnSave.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnSave.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/save.png");
		}
		toolBar.add(btnSave);
		
		JButton btnCut = new JButton(new DefaultEditorKit.CutAction());
		btnCut.setText("Cut");
		btnCut.setToolTipText("Cut");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/Cut.png"));
			btnCut.setIcon(new ImageIcon(img));
			btnCut.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnCut.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/Cut.png");
		}
		toolBar.add(btnCut);
		
		JButton btnCopy = new JButton(new DefaultEditorKit.CopyAction());
		btnCopy.setText("Copy");
		btnCopy.setToolTipText("Copy");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/copy.png"));
			btnCopy.setIcon(new ImageIcon(img));
			btnCopy.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnCopy.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/copy.png");
		}
		toolBar.add(btnCopy);
		
		JButton btnPaste = new JButton(new DefaultEditorKit.PasteAction());
		btnPaste.setText("Paste");
		btnPaste.setToolTipText("Paste");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/paste.png"));
			btnPaste.setIcon(new ImageIcon(img));
			btnPaste.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnPaste.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/paste.png");
		}
		toolBar.add(btnPaste);
		
		btnUndo = new JButton("Undo");
		btnUndo.addActionListener(this);
		btnUndo.setToolTipText("Undo");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/undo.png"));
			btnUndo.setIcon(new ImageIcon(img));
			btnUndo.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnUndo.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/undo.png");
		}
		toolBar.add(btnUndo);
		
		btnRedo = new JButton("Redo");
		btnRedo.addActionListener(this);
		btnRedo.setToolTipText("Redo");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/redo.png"));
			btnRedo.setIcon(new ImageIcon(img));
			btnRedo.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnRedo.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/redo.png");
		}
		toolBar.add(btnRedo);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(this);
		btnRun.setToolTipText("Run");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/play.png"));
			btnRun.setIcon(new ImageIcon(img));
			btnRun.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnRun.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/play.png");
		}
		toolBar.add(btnRun);
		
		btnDelete = new JButton("Delete All");
		btnDelete.addActionListener(this);
		btnDelete.setToolTipText("Delete all text from editor");
		try{
			Image img = ImageIO.read(getClass().getClassLoader().getResource("imagenes/erase.png"));
			btnRun.setIcon(new ImageIcon(img));
			btnRun.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnRun.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/play.png");
		}
		toolBar.add(btnDelete);
		
		//clavo aqui
		/*
		JSplitPane splitpane = new JSplitPane();
		splitpane.setRightComponent(splitpane);*/
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == miOpen ||
				e.getSource() == btnOpen){
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
