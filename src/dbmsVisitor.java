import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.Comparator;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import generatedsources.sqlParser;


/**
 * @author Rafa
 *
 * @param <T>
 */
public class dbmsVisitor<T> extends generatedsources.sqlBaseVisitor<Object> {
	
	//Metadata attributes
	private String path;
	private Schema schema = new Schema();
	private DataBase database = new DataBase();
	//Operation attributes
	private Table table = new Table();
	private ArrayList<String> errors = new ArrayList<String>();
	private ArrayList<String> messages = new ArrayList<String>();
	private int inserted_rows = 0;
	private int deleted_rows = 0;
	private int updated_rows = 0;

	/**
	 * Cleans the data values to make new statements
	 */
	public void clearValues(){
		table = new Table();
		errors = new ArrayList<String>();
		messages = new ArrayList<String>();
		inserted_rows = 0;
		deleted_rows = 0;
		updated_rows = 0;
		
	}
	/**
	 * Loads the Database Schema path and previous databases
	 */
	public void dbmsVisitor(){
		Path currentRelativePath = Paths.get("");
		path = currentRelativePath.toAbsolutePath().toString()+ "\\Databases\\";
		loadSchema();
	}
	
	/**
	 * Unserializes the Schema
	 */
	public void loadSchema(){
		try{
			FileInputStream fstream = new FileInputStream(this.path+"schema.bin");
			BufferedReader reader = new BufferedReader(new FileReader(this.path+"schema.bin"));
			if (reader.readLine()!= null){
				ObjectInputStream instream = new ObjectInputStream(fstream);
				this.schema = (Schema)instream.readObject();
			}
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Serializes the schema object
	 */
	public void saveSchema(){
		try {
			FileOutputStream fstream = new FileOutputStream(this.path+"schema.bin");
			ObjectOutputStream outstream = new ObjectOutputStream(fstream);
			outstream.writeObject(this.schema);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Serializes a table 
	 * @param name
	 * @param tab
	 * @param db_name
	 */
	public void SaveTable(String name, Table tab, String db_name){
		try{
			FileOutputStream fstream = new FileOutputStream(this.path+"\\"+ db_name +"\\"+name +".bin");
			ObjectOutputStream outstream = new ObjectOutputStream(fstream);
			outstream.writeObject(tab);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getInserted_rows() {
		return inserted_rows;
	}

	public void setInserted_rows(int inserted_rows) {
		this.inserted_rows = inserted_rows;
	}

	public int getDeleted_rows() {
		return deleted_rows;
	}

	public void setDeleted_rows(int deleted_rows) {
		this.deleted_rows = deleted_rows;
	}

	public int getUpdated_rows() {
		return updated_rows;
	}

	public void setUpdated_rows(int updated_rows) {
		this.updated_rows = updated_rows;
	}

	/**
	 * @return the messages
	 */
	public ArrayList<String> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	/**
	 * @return the errors
	 */
	public ArrayList<String> geterrors() {
		return errors;
	}
	
	/**
	 * @return the errors in toString form
	 */
	public String errorsToString()
	{
		String res = "Errores:\n";
		int cont = 1;
		for (String i: this.errors)
		{
			res += "Error No. " + Integer.toString(cont) + " -> " + i + "\n";
			cont++;
		}
		res += Integer.toString(cont-1) + " ERRORES EN TOTAL";
		if (cont == 1)
			res = "";
		return res;
	}
	/**
	 * @param errors the errors to set
	 */
	public void seterrors(ArrayList<String> errors) {
		this.errors = errors;
	}
	
	public String toStringMessages(){
		String out = "";
		for(String i: this.messages)
			out += i+"\n";
		return out;
	}
	
/**
 * VISITOR LOGIC
 */
	public Object visitSql2003Parser (sqlParser.Sql2003ParserContext ctx){
		Object obj = visitChildren(ctx);
		if(this.updated_rows != 0) this.messages.add("Updated "+updated_rows+" succesfully");
		if(this.inserted_rows != 0) this.messages.add("Inserted "+inserted_rows+" succesfully);");
		return obj;
	}
	
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitUse_schema_statement(generatedsources.sqlParser.Use_schema_statementContext)
	 */
	/**
	 * VISITOR LOGIC: USE STATEMENT
	 */
	@Override
	public T visitUse_schema_statement(@NotNull sqlParser.Use_schema_statementContext ctx){
		String ID = ctx.ID().getText();
		boolean exists = false;
		for (DataBase db: this.schema.getSchema())
			if(db.getName().equals(ID)){
				exists = true;
				this.setCurrent(db);
				break;
			}
		if (exists == false){
			this.setCurrent(new DataBase());
			String rule_1 = "Can't use database\""+ ID + "\" because it doesnt exist @line: "+ ctx.getStop().getLine();
			this.errors.add(rule_1);
			}
		else{
			System.out.println("Database \"" + ID +"\" in USE");
			this.messages.add("Database \"" + ID +"\" in USE");			
		}
		return (T)"";
	}
	
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitSchema_definition(generatedsources.sqlParser.Schema_definitionContext)
	 */
	/**
	 * VISITOR LOGIC: CREATE DATABASE STATEMENT
	 */
	@Override
	public T visitSchema_definition(@NotNull sqlParser.Schema_definitionContext ctx){
		String ID = ctx.ID().getText();
		DataBase db = new DataBase(ID);
		File dir = new File(this.path+ID);
		boolean created = dir.mkdirs();
		if (!created){
			String rule_2 = "Can't create Database " + ID + " because a Database with the same name already exists @line: " + ctx.getStop().getLine();
			this.errors.add(rule_2);
		}
		else{
			System.out.println("Database \""+ ID + " created successfully");
			this.messages.add("Database \""+ ID + " created successfully");
			this.schema.addDataBase(db);
		}
		return (T)"";
	}
	/**
	 * VISITOR LOGIC: DROP DATABASE STATEMENT
	 */
	@Override
	public Object visitDrop_schema_statement(sqlParser.Drop_schema_statementContext ctx){
		String ID = ctx.ID().getText();
		File dir = new File(this.path+ID);
		if (dir.exists())
		{
			//Creates new database array list that doesn't include the deleted one
			ArrayList<DataBase> = new_schema = new ArrayList<DataBase>();
			DataBase delete = new DataBase();
			boolean exists = false;
			for(DataBase db: this.schema.getSchema())
				if (!db.getName().equals(ID))
					new_schema.add(db);
				else{
					exists = true;
					delete = db;
				}
			if (exists)
			{
				int reg = 0;
				for (Table t: delete.getTables())
					reg+= t.getData().size();
				int confirmation = JOptionPane.showConfirmDialog(null, "Delete dabase \""+ID+"\" with "+Integer.toString(reg)+ " registries?", "DROP DATABASE", JOptionPane.YES_NO_OPTION);
				if (confirmation == JOptionPane.YES_OPTION)
				{
					//Saves the new Schema
					this.schema.setSchema(new_schema);
					if (this.getCurrent().getName().equals(ID))
						this.setCurrent(new DataBase());
					File[] current;
					Stack<File> stack = new Stack<File>();
					stack.push(dir);
					while (!stack.isEmpty()){
						if (stack.lastElement().isDirectory()){
							current = stack.lastElement().listFiles()
									if (current != null){
										if (current.length > 0){
											for (File cf: current)
												stack.push(cf);
										}
										else{
											stack.pop().delete();
										}
									}
						}
						else{
							stack.pop().delete();
						}
					}
					System.out.println("Database \"" + ID + "\" deleted succesfully");
					this.messages.add("Database \"" + ID + "\" deleted succesfully");
				}
			}
			else{
				String nonexistent = "Database \""+ID+ "can't be deleted because it doesn't exist @line: "+ ctx.getStop().getLine();
				this.errors.add(nonexistent);
			}
		}
		else{
			String nonexistent = "Database \""+ID+ "can't be deleted because it doesn't exist @line: "+ ctx.getStop().getLine();
			this.errors.add(nonexistent);
		}
		return (T)"";
	}
	
}