import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import javax.swing.undo.UndoManager;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


import views.TextLineNumber;
import views.Tree;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DBMSgui extends JFrame implements ActionListener {

	
	JMenuItem miOpen, miSave, miSaveAs, miUndo, miRedo, miRun, miComment, miCut, miCopy, miPaste;
	JButton btnOpen, btnSave, btnRun, btnUndo, btnRedo, btnDelete;
	JTextField status, dataBaseUse;
	JCheckBox verboseBox;
	JFileChooser fileChooser;
	UndoManager undoManager;
	File file;
	JTextPane textArea;
	JTextArea OutputArea, verboseArea;
	JTabbedPane tabbedPane;
	JSplitPane izqder,arribajo;
	
	String comment = "//", text = "";
	boolean verboseTrue = false;
	ArrayList<String> verbose = new ArrayList<String>();
	int caretLine = 1, caretColumn = 1;
	
	
	
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
	private void initialize() {// falta tree
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
			btnDelete.setIcon(new ImageIcon(img));
			btnDelete.setText("");
			Border emptyBorder = BorderFactory.createEmptyBorder();
			btnDelete.setBorder(emptyBorder);
		} catch (Exception e){
			System.out.println("Error in imagenes/erase.png");
		}
		toolBar.add(btnDelete);
		
		verboseBox = new JCheckBox("Verbose");
		verboseBox.addActionListener(this);
		toolBar.add(verboseBox);
		
		/*izqder = new JSplitPane();
		izqder.setResizeWeight(0.5);
		izqder.setContinuousLayout(true);
		izqder.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.getContentPane().add(izqder, BorderLayout.CENTER);
		*/
		arribajo = new JSplitPane();
		arribajo.setResizeWeight(0.5);
		arribajo.setContinuousLayout(true);
		arribajo.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.getContentPane().add(arribajo, BorderLayout.CENTER);
		//izqder.setRightComponent(arribajo);
		
		/*textArea = new JTextArea(20,120);
		textArea.setFont(new Font("Monoespaced",Font.PLAIN,12));*/
		textArea = new JTextPane();
		textArea.getDocument().addUndoableEditListener(undoManager);
		setCaretListener(textArea);
		JScrollPane scroll = new JScrollPane (textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		TextLineNumber tln = new TextLineNumber(textArea);
		scroll.setRowHeaderView(tln);
		arribajo.setLeftComponent(scroll);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		arribajo.setRightComponent(tabbedPane);
		
		JScrollPane scroll1 = new JScrollPane();
		tabbedPane.addTab("Data Output", null, scroll1, null);
		OutputArea = new JTextArea();
		OutputArea.setEditable(false);
		scroll1.setViewportView(OutputArea);
		
		JScrollPane scroll2 = new JScrollPane();
		tabbedPane.addTab("Verbose", null, scroll2, null);
		verboseArea = new JTextArea();
		verboseArea.setEditable(false);
		scroll2.setViewportView(verboseArea);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == miOpen ||
				e.getSource() == btnOpen){
			open();
		}else if (e.getSource() == miSave ||
				e.getSource() == btnSave){
			save();
		}else if (e.getSource() == miSaveAs){
			saveAs();
			//explorer.revalidate();
			//explorer.repaint();
			//addTreeSelection(explorer.getTree());
		}else if (e.getSource() == miUndo ||
				e.getSource() == btnUndo){
			undo();
		}else if (e.getSource() == miRedo ||
				e.getSource() == btnRedo){
			redo();
		}else if (e.getSource() == miRun ||
				e.getSource() == btnRun){
			run();
			//explorer.revalidate();
			//explorer.repaint();
			//addTreeSelection(explorer.getTree());
		}else if (e.getSource() == miComment){
			comment();
		}else if (e.getSource() == btnDelete){
			textArea.setText("");
		}else if (e.getSource() == verboseBox){
			if(!verboseTrue){
				verboseBox.setToolTipText("Disable Verbose");
				verboseTrue = true;
			}
			else{
				verboseBox.setToolTipText("Enable Verbose");
				verboseTrue = false;
				verboseArea.setText("");
			}
		}
			
	}
		
	private void redo() {
		// TODO Auto-generated method stub
		try{
			undoManager.redo();
		}catch (Exception e){
			//to do
		}
	}

	private void comment() {
		// TODO Auto-generated method stub
		String text = textArea.getText(), newTxt = "";
		try{
			int caretpos = textArea.getCaretPosition();
			
			
			int lineCount = 1;
			int i = 0;
			boolean condition = true;
			while (condition){
				if (lineCount == caretLine){//if we reach the same line as the caret
					
					if (text.length()>0){
						if (text.length() >= i+comment.length()){//if there is a chance of '//' presence
							String subst = text.substring(i,i+comment.length());
							if (subst.equals(comment)){
								newTxt = text.substring(0,i) + text.substring(i+comment.length());
								
							}else{
								newTxt = text.substring(0,i)+ comment + text.substring(i);
							}
						}else{
							newTxt = text.substring(0,i)+ comment + text.substring(i);
						}
						
					}else{
						newTxt = comment + text;
					}
					i = text.length()-1;//to evaluate text.charAt even if we dont need it
				}
				if (text.length()>0)
				if (text.charAt(i) == '\n'){//if it's a new line
					lineCount++;
				}
				i++;
				condition = i < text.length();
			}
			textArea.setText(newTxt);
			textArea.setCaretPosition(caretpos);
			
			
		} catch (Exception e){
			textArea.setText(text);
			//System.out.println(e);
		}
	}

	private void run() {
		try{
			
			OutputArea.setText("...");
			text = textArea.getSelectedText();
			if (text == null)
				text = textArea.getText();//
			
			// Create DataBase
			long startTime = System.nanoTime();
	    	ANTLRInputStream input = new ANTLRInputStream(text);
	    	
	    	/*
    	    create database antros;
    	    use database antros;
			create table baronRojo (nombre int, dpi char(10), edad char(4), constraint pk primary KEY (nombre, dpi));
			create table baronRojoCayala (nombre int, dpi char(10), CONSTRAINT pk PRIMARY KEY(nombre, dpi), CONSTRAINT fk FOREIGN KEY(nombre) REFERENCES baronRojo (nombre, dpi), CONSTRAINT fk2 FOREIGN KEY(dpi) REFERENCES baronRojo (edad), CONSTRAINT ch CHECK(nombre > dpi) );
			create table baronRojoXela (id int, constraint fk foreign key(id) references baronRojoCayala (nombre) );
			alter table baronRojo add column fecha date constraint fk foreign key (nombre) references baronRojoXela (id);
	    	 */
    	
	    	// Create Table
	    	//ANTLRInputStream input = new ANTLRInputStream("use database prueba; create table baronRojo (nombre int, dpi char(10), edad char(4), constraint pk primary KEY (nombre, dpi));");
	    	
	    	// Create Table con Constraints
	        //ANTLRInputStream input = new ANTLRInputStream("use database prueba; create table baronRojoCayala (nombre int, dpi char(10), CONSTRAINT pk PRIMARY KEY(nombre, dpi), CONSTRAINT fk FOREIGN KEY(nombre) REFERENCES baronRojo (nombre, dpi), CONSTRAINT fk2 FOREIGN KEY(dpi) REFERENCES baronRojo (edad), CONSTRAINT ch CHECK(nombre > dpi) );");  	
	   	
	    	// Rename Table
	    	//ANTLRInputStream input = new ANTLRInputStream("use database prueba; alter table baronRojo rename to baronAzul;");
    	
	        sqlLexer lexer = new sqlLexer(input);
	        
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	
	        sqlParser parser = new sqlParser(tokens);
	        
	        //errores sintacticos
	        parser.removeErrorListeners();
	        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
	        
	        ParseTree tree = parser.sql2003Parser(); // begin parsing at rule 'sql2003Parser'
	        
	        if (!DescriptiveErrorListener.errors.isEmpty()){
	        	OutputArea.setText(DescriptiveErrorListener.errors);
	        	DescriptiveErrorListener.errors = "";
	        	return ;
	        }
	        
	        
	        Object obj = (Object)semantic_checker.visit(tree);
	        semantic_checker.guardarDBs();
	        
	        if (semantic_checker.getActual().getName().isEmpty()){
	        	dataBaseUse.setText("Database: ");
	        }else{
	        	dataBaseUse.setText("Database: "+semantic_checker.getActual().getName());
	        }

	        long estimatedTime = System.nanoTime()-startTime;
	        if (obj instanceof DataBases){
	        	DataBases dbs = (DataBases) obj;
	        	addNewTab(dbs);
	        }else if (obj instanceof Table){
	        	Table tb = (Table) obj;
	        	addNewTab(tb);
	        }
	        
	        // Generar verbose
	        if (verboseTrue){
	        	this.verbose = new ArrayList<String>();
	        	this.recursiveRoot(tree);
	        	verboseArea.setText(toStringVerbose());
	        }
	        //System.out.println(this.toStringVerbose());
	        
	        if (!semantic_checker.erroresToString().isEmpty())
	        	OutputArea.setText(semantic_checker.erroresToString()+"\n"+calculateTime(estimatedTime));
	        else
	        	OutputArea.setText(semantic_checker.toStringMessages() + "\n" + "Terminado"+"\n"+calculateTime(estimatedTime));
	        dataReadArea.setText(text);
	        semantic_checker.resetValues();
	        //splitPane1.setLeftComponent(new SimpleTree());
		} catch (Exception e){
			dataReadArea.setText("Unexpected error: " + e.getStackTrace().toString());
		}
		
	}

	private void undo() {
		// TODO Auto-generated method stub
		try{
			undoManager.undo();
		}catch (Exception e){
			//to do
			System.out.println("no sirve");
		}
	}

	private void saveAs() {
		// TODO Auto-generated method stub
		
	}

	private void save() {
		// TODO Auto-generated method stub
		
	}
 //arreglar open
	public void open(){
		int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION){
        	file = fileChooser.getSelectedFile();
        	miSave.setEnabled(true);
        	btnSave.setEnabled(true);
        	try{
        		
        		FileReader readFile = new FileReader(file.getAbsolutePath());
        		BufferedReader br = new BufferedReader(readFile);
        		textArea.setText("");
        		String newTxt = "";
        		String currentLine;
        		while ((currentLine = br.readLine()) != null){
        			//textArea.append(currentLine+"\n");
        			newTxt += currentLine+"\n";
        			}
        	} catch (Exception e){
     
        		System.out.println("Error opening file");
        	}
        	
        	
        } else {
        	JOptionPane.showMessageDialog(null,"\nNo se ha encontrado el archivo","ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
        }
	}

	private void setCaretListener(JTextPane textArea2){
		 // Add a caretListener to the editor. This is an anonymous class because it is inline and has no specific name.
       textArea2.addCaretListener(new CaretListener() {
           // Each time the caret is moved, it will trigger the listener and its method caretUpdate.
           // It will then pass the event to the update method including the source of the event (which is our textarea control)
           public void caretUpdate(CaretEvent e) {
               //JTextArea editArea = (JTextArea)e.getSource();
           	//JTextPane editArea = (JTextPane)e.getSource();

               // Lets start with some default values for the line and column.
               

               // We create a try catch to catch any exceptions. We will simply ignore such an error for our demonstration.
               try {
                   // First we find the position of the caret. This is the number of where the caret is in relation to the start of the JTextArea
                   // in the upper left corner. We use this position to find offset values (eg what line we are on for the given position as well as
                   // what position that line starts on.
                   //int caretpos = e.getCaretPosition();
                   
                   //(caretpos == 0) ? 1:0;
                   
                   
                   caretLine = getRow(e.getDot(),(JTextComponent)e.getSource());
                   //caretLine = editArea.getLineOfOffset(caretpos);

                   // We subtract the offset of where our line starts from the overall caret position.
                   // So lets say that we are on line 5 and that line starts at caret position 100, if our caret position is currently 106
                   // we know that we must be on column 6 of line 5.
                   caretColumn = getColumn(e.getDot(), (JTextComponent)e.getSource())-1;
                   //caretColumn = caretpos - editArea.getLineStartOffset(caretLine);

                   // We have to add one here because line numbers start at 0 for getLineOfOffset and we want it to start at 1 for display.
                   //caretLine += 1;
               }
               catch(Exception ex) { }

               // Once we know the position of the line and the column, pass it to a helper function for updating the status bar.
               
           }
       });
			
	}
	public int getRow(int pos, JTextComponent editor) {
        int rn = (pos==0) ? 1 : 0;
        try {
            int offs=pos;
            while( offs>0) {
                offs=Utilities.getRowStart(editor, offs)-1;
                rn++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
            
        }
        return rn;
	}

    public int getColumn(int pos, JTextComponent editor) {
        try {
            return pos-Utilities.getRowStart(editor, pos)+1;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public String toStringVerbose()
	{
		String ret = "";
		for (String i: this.verbose)
			ret += i + "\n";
		return ret;
	}
}
