package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import javafx.scene.paint.Color;

public class TreePanel extends JPanel {

	private JTree tree;
	private Tree treeFile;
	
	public TreePanel() {
		treeFile = new Tree(new File(System.getProperty("user.dir")+"/data"));
		tree = new JTree(treeFile);
		tree.setEditable(false);//to edit names in tree
		setLayout(new BorderLayout());
		add(new JScrollPane((JTree)tree),"Center");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
	}
	
	public Dimension getPreferredSize(){
	    return new Dimension(50, 30);
	    }
	
	public void revalidate(){
  		removeAll();
  		treeFile = new Tree(new File(System.getProperty("user.dir")+"/data"));
		tree = new JTree(treeFile);
		tree.setEditable(false);//to edit names in tree
		setLayout(new BorderLayout());
		add(new JScrollPane((JTree)tree),"Center");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		super.revalidate();
  	}
  	
  	public JTree getTree(){
  		return tree;
  	}
	
}
