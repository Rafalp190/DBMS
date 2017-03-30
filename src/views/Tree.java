package views;

import java.io.File;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class Tree implements TreeModel {

	private File root;
	private Vector listeners = new Vector();
	
	public Tree(File rootDirectory){
		root = rootDirectory;
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	@Override
	public Object getChild(Object parent, int index) {
		// TODO Auto-generated method stub
		File directory = (File) parent;
		String[] children = directory.list();
		return new TreeFile(directory, children[index]);
	}

	@Override
	public int getChildCount(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public boolean isLeaf(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	private class TreeFile extends File {
	    public TreeFile(File parent, String child) {
	      super(parent, child);
	    }
	 
	    public String toString() {
	      return getName().replace(".bin","");
	    }
	  }
}
