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

import generatedsources.sqlLexer;
import generatedsources.sqlParser;
import sun.security.util.Length;


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
	private DataBase current = new DataBase();
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
	public void saveTable(String name, Table tab, String db_name){
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
	public DataBase getCurrent() {
		return current;
	}

	public void setCurrent(DataBase current) {
		this.current = current;
	}
	
	public boolean PrimaryKey(ArrayList<String> fila, Integer indice)
	{
		ArrayList<Constraint> key = this.table.getPrimaryKeys();
		
		ArrayList<Integer> primary = new ArrayList<Integer>();
		
		if (key.size() != 0)
		{
			Constraint llave = key.get(0);
			for (String id : llave.getIDS_local())	
			{
				Attribute atr = this.table.getID(id);
				primary.add(this.table.getattributes().indexOf(atr));
			}
			
			int i=0;
			for (ArrayList<String> newfila:this.table.getData())
			{
				int cont=0;
				for (int index : primary)
				{
					if (newfila.get(index).equals(fila.get(index)))
					{
						cont++;
					}
				}
				if (cont == primary.size())
				{
					if (indice!=-1)
					{
						if (indice!= i)
						{
							return false;
						}
					}
					else
						return false;
				}
				i++;
			}
		}
		
		return true;
	}
	
	
	//devuelve si la llave foreana existe en la/las otras tablas
	public boolean ForeignKey(ArrayList<String> fila, Integer indice)
	{
		ArrayList<Constraint> key = this.table.getForeignKey();
		
		int exist = 0;
		for (Constraint llave : key)
		{
			//tengo que ir a traer la tabla a la que hacen referencia
			//recorrer la tabla y ver si el valor en la fila en el indice del id local existe en la tabla
			//aumento un contador
			//si el contador al final es igla al total de constraints devuelve true 
			Table ref_tabla = this.current.getTable(llave.getId_ref());
			if (ref_tabla != null)
			{
				int index = 0;
				int cont = 0;
				for (String id : llave.getIDS_refs()) 
				{
					//Traemos el attribute de la tabla a la que hacemos referencia
					//Y el indice de esta en la tabla
					Attribute atr = ref_tabla.getID(id);
					int index_ref = ref_tabla.getattributes().indexOf(atr);
					
					//Traems el attribute de la tabla local
					//Y el indice de este en la tabla
					String local = llave.getIDS_local().get(index);
					Attribute atr2 = this.table.getID(local);
					int index_loc = this.table.getattributes().indexOf(atr2);
					
					for (ArrayList<String> row:ref_tabla.getData())
					{
						if(row.get(index_ref).equals(fila.get(index_loc)))
						{
							cont++;
						}
					}
					
					index++;
				}
				if (cont != llave.getIDS_refs().size())
					return false;
				else
					exist++;
			}
			else
				return false;
		}
		
		if (exist == key.size())
			return true;
		else
			return false;
	}

	/**
	 * CARTESIAN JOIN TO USE IN FROM STATEMENT
	 * 
	 * @param tb1
	 * @param tb2
	 * @return
	 */
	public Table CartesianCross(Table tb1, Table tb2){
		//tb1.setNameByTable(); ya estan mezclados
		tb2.setNamesByTable();
		Table nTb = new Table();
		
		nTb.setName("Select");
		
		//agregamos todos los atributos
		ArrayList<Attribute> at = new ArrayList();
		at.addAll(tb1.getattributes());
		at.addAll(tb2.getattributes());
		nTb.setattributes(at);
		
		//agregamos nuevos nombres tabla.atributo
		ArrayList<String> otN = new ArrayList();
		otN.addAll(tb1.getOthersIds());
		otN.addAll(tb2.getOthersIds());
		nTb.setOthersIds(otN);
		
		//Cross Join of Data
		ArrayList<ArrayList<String>> data = new ArrayList();
		for (ArrayList<String> tupla1: tb1.getData()){
			for (ArrayList<String> tupla2: tb2.getData()){
				ArrayList<String> tupla = new ArrayList();
				tupla.addAll(tupla1);
				tupla.addAll(tupla2);
				data.add(tupla);
			}
		}
		nTb.setData(data);
		return nTb;
	}
	
	

/**
 * 
 * 
 * 
 * 
 * 
 * VISITOR LOGIC
 *
 *
 *
 *
 *
 *
 */
	public Object visitSql2003Parser (sqlParser.Sql2003ParserContext ctx){
		Object obj = visitChildren(ctx);
		if(this.updated_rows != 0) this.messages.add("Updated "+updated_rows+" succesfully");
		if(this.inserted_rows != 0) this.messages.add("Inserted "+inserted_rows+" succesfully);");
		return obj;
	}
	
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitUse_schema_statement(generatedsources.sqlParser.Use_schema_statementContext)
	 *
	 *
	 *
	 *
	 *
	 *
	 * 
	 * VISITOR LOGIC : DATA DEFINITION LANGUAGE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
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
	 *
	 *
	 *
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
	 * (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitDrop_schema_statement(generatedsources.sqlParser.Drop_schema_statementContext)
	 */
	@Override
	public Object visitDrop_schema_statement(sqlParser.Drop_schema_statementContext ctx){
		String ID = ctx.ID().getText();
		File dir = new File(this.path+ID);
		if (dir.exists())
		{
			//Creates new database array list that doesn't include the deleted one
			ArrayList<DataBase> new_schema = new ArrayList<DataBase>();
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
							current = stack.lastElement().listFiles();
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
	/**
	 * VISITOR LOGIC: ALTER DATABASE STATEMENT
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitAlter_database_statement(generatedsources.sqlParser.Alter_database_statementContext)
	 */
	@Override
	public Object visitAlter_database_statement(sqlParser.Alter_database_statementContext ctx){
		String ID = ctx.ID(0).getText();
		String NewID = ctx.ID(1).getText();
		File dir = new File(this.path + ID);
		if (!ID.equals(NewID)){
			boolean exists = false;
			for(DataBase db: this.schema.getSchema())
				if (db.getName().equals(ID)){
					db.setName(NewID);
					exists = true;
					break;
				}
			if (exists){
				//Saves changes in filesystem
				if (this.getCurrent().getName().equals(ID)){
					this.getCurrent().setName(NewID);
				}
				dir.renameTo(new File(this.path + NewID));
				System.out.println("DataBase \""+ ID + "\" renamed to \""+ NewID + "\" succesfully");
				this.messages.add("DataBase \""+ ID + "\" renamed to \""+ NewID + "\" succesfully");
			}
			else{
				String nonexistentdb = "DataBase can't be renamed because \"" + ID +"\" doesn't exist @line: "+ctx.getStop().getLine();
				this.errors.add(nonexistentdb);
			}
		}
		return (T)"";
	}
	/**
	 * VISITOR LOGIC: ALTER TABLE RENAME TO STATEMENT
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitRename_table_statement(generatedsources.sqlParser.Rename_table_statementContext)
	 */
	@Override
	public Object visitRename_table_statement(sqlParser.Rename_table_statementContext ctx){
		String ID = ctx.ID(0).getText();
		String NewID = ctx.ID(1).getText();
		
		if (!ID.equals(NewID))
		{
			if (this.getCurrent().getName().isEmpty())
			{
				String noDB = "No database in use @line: " + ctx.getStop().getLine();
				this.errors.add(noDB);
			}
			else
			{
				//Verifies table with ID exists
				if (this.getCurrent().existTable(ID))
				{	
					//Verifies that a table with that name already exists
					if(!this.getCurrent().existTable(NewID))
					{
						//Rename references
						if (!this.getCurrent().existRef(ID))
						{
							for (Table t: this.getCurrent().getTables())
								t.renameRefIdFK(ID, NewID);
						}
						if (!this.getCurrent().getConstraints_refs().isEmpty())
						{
							this.getCurrent().renameRef(ID, NewID);
						}	
						System.out.println("Table \"" + ID + "\" renamed successfully to \"" + NewID + "\"");
						this.messages.add("Table \"" + ID + "\" renamed successfully to \"" + NewID + "\"");
						Table tab = this.getCurrent().getTable(ID);
						tab.setName(NewID);
						File dir = new File(this.path+"\\"+this.getCurrent().getName()+"\\"+ ID + ".bin");
						dir.renameTo(new File(this.path +"\\"+ this.getCurrent().getName()+"\\"+ NewID +".bin"));
					}
					else
					{
						String tablealreadyexists = "A table with the same name already exists in the DataBase \""+ this.getCurrent().getName()+"\" @line: "+ ctx.getStop().getLine();
						this.errors.add(tablealreadyexists);
					}
				}
				else
				{
					String table_not_found = "The table \"" + ID + "\" does not exist in the DataBase \"" + this.getCurrent().getName() + "\" @line: " + ctx.getStop().getLine();
					this.errors.add(table_not_found);
				}
			}
		}
		return(T)"";
	}
	/**
	 * VISITOR LOGIC: CREATE TABLE STATEMENT
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitTable_definition(generatedsources.sqlParser.Table_definitionContext)
	 */
	@Override
	public Object visitTable_definition(sqlParser.Table_definitionContext ctx){
		String name = ctx.ID().getText();
		ArrayList<Attribute> atr = new ArrayList<Attribute>();
		ArrayList<Constraint> pks = new ArrayList<Constraint>();
		ArrayList<Constraint> fks = new ArrayList<Constraint>();
		ArrayList<Constraint> checks = new ArrayList<Constraint>();
		ArrayList<String> ids = new ArrayList<String>();
		int errores = 0;
		//Verifies the data base exists
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
			errores++;
		}
		else
		{
			//Tables cant have the same name
			if (!this.getCurrent().existTable(name))
			{
				for(int i = 4; i <ctx.getChildCount()-2; i++)
				{
					ParseTree child = ctx.getChild(i);
					String childtxt = child.getText();
					//Ignore comma
					if (!childtxt.equals(","))
					{
						//Attribute
						if(child.getChildCount()==2)
						{
							atr.add((Attribute)this.visit(child));
						}
						//Constraint
						else
						{
							Constraint constr = (Constraint)this.visit(child);
							ids.add(constr.getId());
							switch (constr.gettype())
							{
							case "Primary Key":
								if (pks.isEmpty())
									pks.add(constr);
								else
								{
									String multiplePK = "A table can't have more than one Primary Key @line: " + ctx.getStop().getLine();
									this.errors.add(multiplePK);
								}
								break;
							
							case "Foreign Key":
								fks.add(constr);
								break;
							case "Check":
								checks.add(constr);
								break;
							}
						}
					}
				}
				//Validations
				if(errores == 0)
				{
					//No attribute can have the same name
					ArrayList<String> attribute_names = new ArrayList<String>();
					boolean error1 = false;
					int cont = 0;
					for (Attribute a: atr)
					{
						String name_a = a.getId();
						attribute_names.add(name_a);
						error1 = false;
						for (Attribute j: atr.subList(cont+1, atr.size()))
						{
							if (name_a.equals(j.getId()))
							{
								error1= true;
								errores++;
								break;
							}
						}
						if (error1 == true)
						{
							String attr_declared = "The attribute \"" + name_a + "\" is declared more than once @line: " + ctx.getStop().getLine();
							this.errors.add(attr_declared);
						}
						cont++;
					}
					// No constraint can have the same name
					error1 = false;
					cont = 0;
					for (String i: ids)
					{
						error1 = false;
						for (String j: ids.subList(cont, ids.size()))
						{
							if (i.equals(j))
							{
								error1 = true;
								errores++;
								break;
							}
						}
						if (error1== true)
						{
							String const_declared = "The constraint \""+ i +"\" is declared more than once @line: " +ctx.getStop().getLine();
							this.errors.add(const_declared);
						}
						cont++;
					}
					//Local IDs belong to table
					if (errores == 0)
					{
						if (!pks.isEmpty())
						{
							Constraint pk = pks.get(0);
							ArrayList<String> idPk = pk.getIDS_local();
							for (String i: ids)
							{
								if (!attribute_names.contains(i))
								{
									String localIDnotfound = "The attribute \""+ i+ "\" from the Primary Key \"" + pk.getId() + "\" is not declared in the table \"" + name + "\" @line: " + ctx.getStop().getLine();
									this.errors.add(localIDnotfound);
									errores++;
								} 	
							}
						}
						//Foreign Keys
						for (Constraint i:fks)
						{
							//Local Ids
							for (String j: i.getIDS_local())
							{	
								if (! attribute_names.contains(j))
								{
									String localIDnotfound = "The attribute \""+ j + "\" from the Foreign Key \"" + i.getId()+ "\" is not declared in the table \"" + name + "\" @line: " + ctx.getStop().getLine();
									this.errors.add(localIDnotfound);
									errores++;
								}
							}
							//Ref IDS
							if (!this.getCurrent().existTable(i.getId_ref()))
							{
								String table_not_found = "The table \""+ i.getId_ref() +"\" that references the Foreign Key \"" +i.getId() + "\" is not declared in the database \"" + this.getCurrent().getName()+"\" @line: " + ctx.getStop().getLine();
								this.errors.add(table_not_found);
								errores++;
							}
							else
							{
								Table table_ref = this.getCurrent().getTable(i.getId_ref());
								// Verify that RefIDS belong to the table
								for (String j: i.getIDS_refs())
								{
									if(!table_ref.hasAttribute(j))
									{
										String ref_id_not_found = "The attribute \"" + j + "\" is not declared in the table \"" + i.getId_ref() + "\" that references the Foreign Key \"" + i.getId() + "\" @line: "+ ctx.getStop().getLine();
										this.errors.add(ref_id_not_found);
										errores++;
									}
								}
							}
						}
						//Check
						table = new Table();
						table.setattributes(atr);
						
						for (Constraint i: checks)
						{
						//Local IDs
							//Analyzes the checks by creating an input stream
							ANTLRInputStream input = new ANTLRInputStream(i.getCondition());
							sqlLexer lexer = new sqlLexer(input);
							CommonTokenStream tokens = new CommonTokenStream(lexer);
							sqlParser parser = new sqlParser(tokens);
							ParseTree tree = parser.condition();
							Object obj = (Object) visit(tree);
							
							if (obj == null)
							{
								String chk = "Check: " + i.getId() + "not correctly defined @line: " + ctx.getStop().getLine();
								this.errors.add(chk);
								errores++;
							}
						}
						// Creates table when thorougly validated
						if (errores == 0)
						{
							
							Table newTab = new Table(name,atr,pks,fks,checks);
							for(Constraint i: fks)
							{
								this.getCurrent().addRef(i.getId_ref());
							}
							this.getCurrent().addTable(newTab);
							System.out.println("Table \"" +name+"\" successfully aded to the DataBase \"" + this.getCurrent().getName()+ "\"");
							this.messages.add("Table \"" +name+"\" successfully aded to the DataBase \"" + this.getCurrent().getName()+ "\"");
							
							saveTable(this.getCurrent().getName(), newTab, name);					
						}
					}
				}
			}
			else
			{
				String table_existent = "A table with the same name already exists in the DataBase \"" +this.getCurrent().getName() +"\" @line: " +ctx.getStop().getLine();
				this.errors.add(table_existent);
			}
		}	
		return(T)"";
	}	
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitColumn_literal(generatedsources.sqlParser.Column_literalContext)
	 */
	@Override
	public Object visitColumn_literal(sqlParser.Column_literalContext ctx){
		Attribute attr = (Attribute) this.visit(ctx.tipo_literal());
		attr.setId(ctx.ID().getText());
		
		return (T)attr;
	}
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitColumn_constraint(generatedsources.sqlParser.Column_constraintContext)
	 */
	@Override
	public Object visitColumn_constraint(sqlParser.Column_constraintContext ctx){
		Constraint constr = (Constraint) this.visit(ctx.constraint());
		return (T)constr;
	}
	
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitConstraint(generatedsources.sqlParser.ConstraintContext)
	 */
	@Override
	public Object visitConstraint(sqlParser.ConstraintContext ctx){
		Constraint constr = (Constraint) this.visit(ctx.constraintType());
		return (T)constr;
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitLocalIDS(sqlParser.LocalIDSContext)
	 */
	@Override
	public Object visitLocalIDS(sqlParser.LocalIDSContext ctx) {
		// TODO Auto-generated method stub
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(ctx.ID().getText());
		if (ctx.getChildCount() != 1)
		{
			ids.addAll((ArrayList<String>) this.visit(ctx.localIDS()));
		}
		return (T)ids;
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitRefIDS(sqlParser.RefIDSContext)
	 */
	@Override
	public Object visitRefIDS(sqlParser.RefIDSContext ctx) {
		// TODO Auto-generated method stub
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(ctx.ID().getText());
		if (ctx.getChildCount() != 1)
		{
			ids.addAll((ArrayList<String>) this.visit(ctx.refIDS()));
		}
		return (T)ids;
		//return super.visitRefIDS(ctx);
	}

	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitIdRef(sqlParser.IdRefContext)
	 */
	@Override
	public Object visitIdRef(sqlParser.IdRefContext ctx) {
		// TODO Auto-generated method stub
		return (T)ctx.ID().getText();
		//return super.visitIdRef(ctx);
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitConstraintTypeForeignKey(sqlParser.ConstraintTypeForeignKeyContext)
	 */
	@Override
	public Object visitConstraintTypeForeignKey(sqlParser.ConstraintTypeForeignKeyContext ctx) {
		// TODO Auto-generated method stub
		Constraint const_pk = new Constraint(ctx.getChild(0).getText(), "Foreign Key");
		const_pk.setIDS_local((ArrayList<String>)this.visit(ctx.localIDS()));
		const_pk.setIDS_refs((ArrayList<String>)this.visit(ctx.refIDS()));
		const_pk.setId_ref((String)this.visit(ctx.idRef()));
		return (T)const_pk;
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitConstraintTypePrimaryKey(sqlParser.ConstraintTypePrimaryKeyContext)
	 */
	@Override
	public Object visitConstraintTypePrimaryKey(sqlParser.ConstraintTypePrimaryKeyContext ctx) {
		// TODO Auto-generated method stub
		Constraint const_pk = new Constraint(ctx.getChild(0).getText(), "Primary Key");
		const_pk.setIDS_local((ArrayList<String>)this.visit(ctx.localIDS()));
		return (T)const_pk;
	}
	
	
	
	
	/**
	 * VISITOR LOGIC: CHECKS
	 */
	public Object visitCompCheck(sqlParser.CompContext ctx){
		if (ctx instanceof sqlParser.CompIdContext){//tiene los dos ids
			ArrayList<String> ids = new ArrayList();
			ids.add((String)visit(ctx.getChild(0)));
			if (ctx.getChild(2) instanceof sqlParser.NIDContext){
				ids.add((String)visit(ctx.getChild(2)));
			}
			return ids;
		}else if (ctx instanceof sqlParser.CompLitIdContext){
			ArrayList<String> ids = new ArrayList();
			ids.add((String)visit(ctx.getChild(2)));
			return ids;
		}
		return new ArrayList<String>();
	}

	public Object visitConditionCheck(sqlParser.ConditionContext ctx){
        
		if (ctx instanceof sqlParser.ConditionCondContext){
			//System.out.println("Es conditionCond");
			ArrayList<String> ids = new ArrayList();
			ids.addAll((ArrayList<String>)visitConditionCheck((sqlParser.ConditionContext)ctx.getChild(1)));
			if (ctx.getChildCount() > 3){
				ids.addAll((ArrayList<String>)visitConditionCheck((sqlParser.ConditionContext)ctx.getChild(4)));
			}
			return ids;
		}else if (ctx instanceof sqlParser.ConditionCompContext){
			//System.out.println("Es conditioncomp");
			ArrayList<String> ids = new ArrayList();
			ids.addAll((ArrayList<String>)visitCompCheck((sqlParser.CompContext)ctx.getChild(0)));
			if (ctx.getChildCount() > 1){
				ids.addAll((ArrayList<String>)visitConditionCheck((sqlParser.ConditionContext)ctx.getChild(2)));
			}
			return ids;
		}
		
		return visitConditionCheck((sqlParser.ConditionContext)ctx.getChild(1));// es not_logic condition
	}
	
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitConstraintTypeCheck(sqlParser.ConstraintTypeCheckContext)
	 */
	@Override
	public Object visitConstraintTypeCheck(sqlParser.ConstraintTypeCheckContext ctx) {
		// TODO Auto-generated method stub
		Constraint const_check = new Constraint(ctx.getChild(0).getText(), "Check");
		const_check.setCondition(ctx.condition().getText());
		ArrayList<String> ids = (ArrayList<String>)visitConditionCheck(ctx.condition());
		LinkedHashSet<String> ids_noRepetidos = new LinkedHashSet(ids);
		ids = new ArrayList(ids_noRepetidos);
		const_check.setIDS_local(ids);
		
		return (T)const_check;
		//return super.visitConstraintTypeCheck(ctx);
	}
	
	///////////////////////////////////////////////
	///////////////////////////////////////////////
	/////////////END OF CHECKS
	///////////////////////////////////////////////
	//////////////////////////////////////////////
	/**
	 * VISITOR LOGIC: LOGICAL STATEMENTS
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitExp_logic(generatedsources.sqlParser.Exp_logicContext)
	 */
	@Override
	public Object visitExp_logic(sqlParser.Exp_logicContext ctx) {
		// TODO Auto-generated method stub
		return (T)this.visit(ctx.logic());
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitLogic_and(sqlParser.Logic_andContext)
	 */
	@Override
	public Object visitLogic_and(sqlParser.Logic_andContext ctx) {
		// TODO Auto-generated method stub
		return (T)"AND";
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitLogic_not(sqlParser.Logic_notContext)
	 */
	@Override
	public Object visitLogic_not(sqlParser.Logic_notContext ctx) {
		// TODO Auto-generated method stub
		return (T)"NOT";
	}
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitLogic_or(sqlParser.Logic_orContext)
	 */
	@Override
	public Object visitLogic_or(sqlParser.Logic_orContext ctx) {
		// TODO Auto-generated method stub
		return (T)"OR";
	}
	/**
	 * VISITOR LOGIC: ATTRIBUTE TYPE
	 */
	
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitTipo_lit_date(sqlParser.Tipo_lit_dateContext)
	 */
	@Override
	public Object visitTipo_lit_date(sqlParser.Tipo_lit_dateContext ctx) {
		// TODO Auto-generated method stub
		Attribute date_attr = new Attribute("", "date");
		return (T) date_attr;
	}
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitTipo_lit_char(sqlParser.Tipo_lit_charContext)
	 */
	@Override
	public Object visitTipo_lit_char(sqlParser.Tipo_lit_charContext ctx) {
		// TODO Auto-generated method stub		
		Attribute char_attr = new Attribute("", "char", Integer.valueOf(ctx.INT().getText()));
		return (T) char_attr;
	}
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitTipo_lit_float(sqlParser.Tipo_lit_floatContext)
	 */
	@Override
	public Object visitTipo_lit_float(sqlParser.Tipo_lit_floatContext ctx) {
		// TODO Auto-generated method stub
		Attribute float_atr = new Attribute("", "float");
		return (T) float_atr;
		//return super.visitTipo_lit_float(ctx);
	}

	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitTipo_lit_int(sqlParser.Tipo_lit_intContext)
	 */
	@Override
	public Object visitTipo_lit_int(sqlParser.Tipo_lit_intContext ctx) {
		// TODO Auto-generated method stub
		Attribute int_atr = new Attribute("", "int");
		return (T) int_atr;
		//return super.visitTipo_lit_int(ctx);
	}	
	/**
	 * VISITOR LOGIC: ALTER TABLE ADD COLUMN
	 */

	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitAlterAddColumn(generatedsources.sqlParser.AlterAddColumnContext)
	 */
	@Override
	public Object visitAlterAddColumn(sqlParser.AlterAddColumnContext ctx){
		String tabID = (String) this.visit(ctx.idTable());
		String colID = (String) this.visit(ctx.idColumn());
		
		// Verifies used database
		if(this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
			//Verifies table exists
			if (this.getCurrent().existTable(tabID))
			{
				Table mod = this.getCurrent().getTable(tabID);
				ArrayList<String> attr_names = mod.getattributesNames();
				Attribute attr = (Attribute) this.visit(ctx.tipo_literal());
				attr.setId(colID);
				boolean insertAttr = mod.canAddAttribute(attr);
				//if constraint aint null
				if (ctx.constraint() != null)
				{
					//gets the constraint
					Constraint constr = (Constraint) this.visit(ctx.constraint());
					boolean  insertConstr = mod.canAddConstraint(constr);
					
					//Verifies both 
					if (insertAttr && insertConstr)
					{
						//Validation
						int errores = 0;
						//Local IDS belong to the table
						ArrayList<String> ids = constr.getIDS_local();
						for (String i: ids)
						{
							if(!attr_names.contains(i))
							{
								if (! i.equals(colID))
								{
									String local_id_not_found = "The attribute \""+ i+ "\" from the " + constr.gettype() + " \""+ constr.getId() + "\" is not declared in table \"" + mod.getName() + "\" @line: " + ctx.getStop().getLine();
									this.errors.add(local_id_not_found);
									errores++;
								}
							}
						}
						switch (constr.gettype())
						{
						case "Primary Key":
							if(! mod.getPrimaryKeys().isEmpty())
							{
								String multiplePk = "A table cant have more than one Primary Key @line "+ ctx.getStop().getLine();
								this.errors.add(multiplePk);
								errores++;
							}
							break;
						case "Foreign Key":
							if (!this.getCurrent().existTable(constr.getId_ref()))
							{
								String table_not_found = "The table \"" + constr.getId_ref() + "\" that references the Foreign Key \"" +constr.getId() + "\" is not declared @line" + ctx.getStop().getLine();
								this.errors.add(table_not_found);
								errores++;
							}
							else
							{
								Table table_ref = this.getCurrent().getTable(constr.getId_ref());
								for (String j: constr.getIDS_refs())
								{
									if (! table_ref.hasAttribute(j))
									{
										String ref_if_not_found = "The attribute \"" + j + "\" is not declared in Table \"" + constr.getId_ref() + "\" that references Foreign Key \"" + constr.getId() + "\" @line: " + ctx.getStop().getLine();
										this.errors.add(ref_if_not_found);
										errores++;
									}
								}
							}
							break;
						case "Check":
							table = new Table(mod);
							table.setData(new ArrayList<ArrayList<String>>());
							
							ANTLRInputStream input = new ANTLRInputStream(constr.getCondition());
							sqlLexer lexer = new sqlLexer(input);
							CommonTokenStream tokens = new CommonTokenStream(lexer);
							sqlParser parser = new sqlParser(tokens);
							ParseTree tree = parser.condition();
							
							Object obj = (Object) visit(tree);
							if (obj == null)
							{
								String check_ = "Check: " + constr.getId()+ " not defined correctly @line: " + ctx.getStop().getLine();
								this.errors.add(check_);
								errores++;
							}
							break;
						}
						if (errores == 0)
						{
							mod.addConstraint(constr);
							if (constr.gettype().equals("Foreign Key"))
							{
								this.getCurrent().addRef(constr.getId_ref());			
							}
							System.out.println("Constraint \"" + constr.getId() + "\" added succesfully to the table \"" + mod.getName()+"\"");
							this.messages.add("Constraint \"" + constr.getId() + "\" added succesfully to the table \"" + mod.getName() + "\"");
						}
					}
					else
					{
						//Report errors
						if (!insertAttr)
						{
							String column_repeated ="Column can't be added \"" + colID+ "\" because one with the same name already exists @line: "+ctx.getStop().getLine();
							this.errors.add(column_repeated);
						}
						if (! insertConstr)
						{
							String constraint_repeated = "Constraint can't be added \"" + constr.getId()+ "\" because one with the same name already exists @line: "+ctx.getStop().getLine();
							this.errors.add(constraint_repeated);
							
						}
					}
				}
				else
				{
					if (insertAttr)
					{
						mod.addAttribute(attr);
						System.out.println("Column \"" + colID + "\" added succesfully to the table \"" + mod.getName()+"\"");
						this.messages.add("Column \"" + colID + "\" added succesfully to the table \"" + mod.getName() + "\"");
					}
					else
					{
						String column_repeated ="Column can't be added \"" + colID+ "\" because one with the same name already exists @line: "+ctx.getStop().getLine();
						this.errors.add(column_repeated);
					}
				}
			}
			else
			{			
				String table_not_found = "The table \"" + tabID + "\" does not exist in DataBase \""+ this.getCurrent().getName() + "\" @Line: "+ctx.getStop().getLine();
				this.errors.add(table_not_found);
					
			}
		}
		return (T)"";
	}
	/**
	 * VISITOR LOGIC: ALTER TABLE ADD CONSTRAINT
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitAlterAddConstraint(generatedsources.sqlParser.AlterAddConstraintContext)
	 */
	@Override
	public Object visitAlterAddConstraint(sqlParser.AlterAddConstraintContext ctx){
		String tabID =(String) this.visit(ctx.idTable());
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
			//Check if table exists
			if (this.getCurrent().existTable(tabID))
			{
				Table mod = this.getCurrent().getTable(tabID);
				ArrayList<String> attr_names = mod.getattributesNames();
				
				//Get constraint
				Constraint con = (Constraint) this.visit(ctx.constraint());
				boolean insertConstraint = mod.canAddConstraint(con);
				//Check if constraint can be added 
				if (insertConstraint)
				{
					//Check for constraint errors
					int errores = 0;
					
					//Local Ids belong to table
					ArrayList<String> ids = con.getIDS_local();
					for (String i: ids)
						if(!attr_names.contains(i))
						{
							String local_id_not_found = "The attribute \""+ i+ "\" from the " +con.gettype() + "\""+con.getId() + "\" is not declared in the table \"" + mod.getName() + "\" @line: " + ctx.getStop().getLine();
							this.errors.add(local_id_not_found);
							errores++;
						}
					switch (con.gettype())
					{
					case "Primary Key":
						if (! mod.getPrimaryKeys().isEmpty())
						{
							String multiplePk = "A table cant have more than one Primary Key @line "+ ctx.getStop().getLine();
							this.errors.add(multiplePk);
							errores++;
						}
						if (errores == 0)
						{
							for (String i: con.getIDS_local())
							{
								int nullCount = 0;
								int index_data_attr = mod.getattributesNames().indexOf(i);
								for (String data_i: mod.dataColumnI(index_data_attr))
									if (data_i.toLowerCase().equals("null"))
										nullCount++;
								if (nullCount > 0)
								{
									String pkNotNull = "Primary Key \""+ con.getId() +"\" can't be added because the Attribute \"" + i + "\" has " + nullCount+ " null values @line: " +ctx.getStop().getLine();
									this.errors.add(pkNotNull);
								}
							}
						}
						break;
					case "Foreign Key":
						//Referenced IDS
						if (!this.getCurrent().existTable(con.getId_ref()))
						{
							String tabNotFound = "The table \"" + con.getId_ref() + "\" referenced by the Foreign Key \"" + con.getId() + "\" is not declared in the DataBase \"" + this.getCurrent().getName() + "\" @line: " + ctx.getStop().getLine();
							this.errors.add(tabNotFound);
							errores++;
						}
						else 
						{
							Table refTab = this.getCurrent().getTable(con.getId_ref());
							for (String j: con.getIDS_refs())
								if(!refTab.hasAttribute(j))
								{
									String refID_notfound = "The attribute \"" + j + "\" is not daclared in the table \"" +con.getId_ref()+ "\" that references the Foreign Key \"" + con.getId() + "\" @line: " + ctx.getStop().getLine();
									this.errors.add(refID_notfound);
									errores++;
								}
						}
						break;
					case "Check":
						table = new Table(mod);
						table.setData(new ArrayList<ArrayList<String>>());
						
						//Configure the antlr Input Stream and tokens
						ANTLRInputStream input = new ANTLRInputStream(con.getCondition());
						sqlLexer lexer = new sqlLexer(input);
						CommonTokenStream tokens = new CommonTokenStream(lexer);
						sqlParser parser = new sqlParser(tokens);
						ParseTree tree = parser.condition();
						
						Object obj = (Object) visit(tree);
						if (obj == null)
						{
							String check_ = "Check: " + con.getId() +"Not defined correctly @line: " + ctx.getStop().getLine();
							this.errors.add(check_);
							errores++;
						}
						break;
					}
					
					if (errores == 0)
					{
						//Adds constraint
						mod.addConstraint(con);
						if (con.gettype().equals("Foreign Key"))
							this.getCurrent().addRef(con.getId_ref());
						System.out.println("Constraint \"" + con.getId() + "\" added succesfully to the table \"" + mod.getName()+"\"");
						this.messages.add("Constraint \"" + con.getId() + "\" added succesfully to the table \"" + mod.getName() + "\"");
					}
				}
				else
				{
					if (!insertConstraint)
					{
						String constraint_repeated = "Can't add constraint \"" + con.getId() + "\" because one with the same name already exists @line: " +ctx.getStop().getLine();
						this.errors.add(constraint_repeated);
					}
				}
			}
			else
			{
				String table_not_found = "The table \"" + tabID + "\" does not exist in DataBase \""+ this.getCurrent().getName() + "\" @Line: "+ctx.getStop().getLine();
				this.errors.add(table_not_found);
			}
		}	
		return (T)"";
	}
	/**
	 * VISITOR LOGIC: ALTER TABLE DROP COLUMN
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitAlterDropColumn(generatedsources.sqlParser.AlterDropColumnContext)
	 */
	@Override
	public Object visitAlterDropColumn(sqlParser.AlterDropColumnContext ctx){
		String tabID = (String) this.visit(ctx.idTable());
		String colID = (String) this.visit(ctx.idColumn());
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
			//Verfies if table exists
			if (this.getCurrent().existTable(tabID))
			{
				Table mod = this.getCurrent().getTable(tabID);
				ArrayList<String> attr_names = mod.getattributesNames();
				if (attr_names.contains(colID))
				{
					int errores = 0;
					ArrayList<Constraint> pks = mod.getPrimaryKeys();
					if (!pks.isEmpty())
					{
						Constraint pk = pks.get(0);
						ArrayList<String> locals = pk.getIDS_local();
						if (locals.contains(colID))
						{
							int index = locals.indexOf(colID);
							locals.remove(index);
							this.messages.add("The attribute \"" + colID +"\" has been successfully deleted from the Primary key \"" + pk.getId() +"\"");
						}
					}
					// Checks
					ArrayList<Constraint> cks = mod.getChecks();
					if (cks.isEmpty())
					{
						for (Constraint i: cks)
						{
							ArrayList<String> locals = i.getIDS_local();
							if (locals.contains(colID))
							{
								String delete_check_first = "The Check \"" + i.getId() +"\" contains the attribute \"" + colID + "\". DROP the CONSTRAINT first before deleting the attribute @line: " +ctx.getStop().getLine();
								this.errors.add(delete_check_first);
								errores++;
							}
						}
					}
					// Referencing FK from other tables
					if (this.getCurrent().existRef(tabID)){
						for (Table t: this.getCurrent().getTables())
							if (! t.getName().equals(tabID))
								for (Constraint c: t.getForeignKey())
								{
									ArrayList<String> ref = c.getIDS_refs();
									if (ref.contains(colID))
									{
										String delete_fk_first = "The foreing key \""+ c.getId() + "\" from the Table \"" + t.getName() + "\" contains the Attribute \"" + colID + "\" that you are trying to delete. Perform the necesary DROP CONSTRAINT before proceeding. @line: " + ctx.getStop().getLine();
										this.errors.add(delete_fk_first);
										errores++;
									}
								}
					}
					if (errores == 0)
					{
						mod.deleteAttribute(colID);
						System.out.println("Attribute \"" + colID + "\" deleted successfully from Table \"" + tabID +"\"");
						this.messages.add("Attribute \"" + colID + "\" deleted successfully from Table \"" + tabID +"\"");
					}
				}
				else
				{
					String attr_not_found = "Attribute: \"" + colID + "\" does not exist in Table \"" + mod.getName() + "\" @line: " +ctx.getStop().getLine();
					this.errors.add(attr_not_found);
				}
			}
			else
			{
				String table_not_found = "The table \"" + tabID + "\" does not exist in DataBase \""+ this.getCurrent().getName() + "\" @Line: "+ctx.getStop().getLine();
				this.errors.add(table_not_found);
			}
				
		}
		return (T)"";
	}
	/**
	 * VISITOR LOGIC: ALTER TABLE DROP CONSTRAINT
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitAlterDropConstraint(generatedsources.sqlParser.AlterDropConstraintContext)
	 */
	@Override
	public Object visitAlterDropConstraint(sqlParser.AlterDropConstraintContext ctx){
		String tabID = (String) this.visit(ctx.idTable());
		String conID = (String) this.visit(ctx.idConstraint());
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
			if (this.getCurrent().existTable(tabID))
			{
				Table mod = this.getCurrent().getTable(tabID);
				if (mod.existeConstraint(conID))
				{
					Constraint drop = mod.getConstraint(conID);
					if(drop.gettype().equals("Foreign Key"))
					{
						int cont = 0;
						for (Table t: this.getCurrent().getTables())
							if (!t.getName().equals(tabID))
							{
								for (Constraint c: t.getForeignKey())
									if(c.getId_ref().equals(drop.getId_ref()))
									{
										cont++;
										break;
									}
							}
						if (cont == 0)
							this.getCurrent().deleteRef(drop.getId_ref());
						
					}
					mod.deleteConstraint(drop);
					System.out.println("Constraint \"" + conID + "\" deleted successfully from Table \"" + tabID +"\"");
					this.messages.add("Constraint \"" + conID + "\" deleted successfully from Table \"" + tabID +"\"");
				}
				else
				{
					String con_not_found = "Consraint: \"" + conID + "\" does not exist in Table \"" + mod.getName() + "\" @line: " +ctx.getStop().getLine();
					this.errors.add(con_not_found);
				}
			}
			else
			{
				String table_not_found = "The table \"" + tabID + "\" does not exist in DataBase \""+ this.getCurrent().getName() + "\" @Line: "+ctx.getStop().getLine();
				this.errors.add(table_not_found);
			}
		}
		return (T)"";	
	}
	
	/**
	 * VISITOR LOGIC: ID TABLE 
	 */
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitIdTable(sqlParser.IdTableContext)
	 */
	@Override
	public Object visitIdTable(sqlParser.IdTableContext ctx) {
		// TODO Auto-generated method stub
		return (T)ctx.ID().getText();
		
	}
	/**
	 * VISITOR LOGIC: ID COLUMN
	 */
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitIdColumn(sqlParser.IdColumnContext)
	 */
	@Override
	public Object visitIdColumn(sqlParser.IdColumnContext ctx) {
		// TODO Auto-generated method stub
		return (T)ctx.ID().getText();
		
	}
	/**
	 * VISITOR LOGIC: ID CONSTRAINT
	 */
	/* (non-Javadoc)
	 * @see sqlBaseVisitor#visitIdConstraint(sqlParser.IdConstraintContext)
	 */
	@Override
	public Object visitIdConstraint(sqlParser.IdConstraintContext ctx) {
		// TODO Auto-generated method stub
		return (T)ctx.ID().getText();
	}
	/**
	 * VISITOR LOGIC: SHOW COLUMN FROM
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitShow_column_statement(generatedsources.sqlParser.Show_column_statementContext)
	 */
	@Override
	public T visitShow_column_statement(sqlParser.Show_column_statementContext ctx){
		//SHOW COLUMNS FROM ID (comprobar use database, id contenido en database)
		
		if (this.getCurrent().getName().isEmpty()){
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}else{
			String ID = ctx.getChild(3).getText();
			Table tb = getCurrent().getTable(ID);
			if (tb == null){
				String no_database_in_use = "There is no Table " +ID+" in the database " +this.getCurrent().getName()+" @line: " + ctx.getStop().getLine();
	        	this.errors.add(no_database_in_use);
	        	//System.out.println("error de tabla");
			}else{
				return (T)tb;
			}
		}
		
		return (T)new String();
	}
	/**
	 * VISITOR LOGIC: SHOW SCHEMA
	 */
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitShow_schema_statement(generatedsources.sqlParser.Show_schema_statementContext)
	 */
	@Override
	public T visitShow_schema_statement(sqlParser.Show_schema_statementContext ctx){
		return (T)schema;
	}
	/**
	 * VISITOR LOGIC: SHOW TABLES
	 */
	@Override
	public T visitShow_table_statement(sqlParser.Show_table_statementContext ctx){
		//SHOW TABLES (comprobar use database)
		if (this.getCurrent().getName().isEmpty()){
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}else{
			
			ArrayList<Attribute> atr = new ArrayList();
			atr.add(new Attribute("Tables"));
			
			ArrayList<ArrayList<String>> values = new ArrayList();
			for (Table tb: getCurrent().getTables()){
				ArrayList<String> val = new ArrayList();
				val.add(tb.getName());
				values.add(val);
			}
			Table tb1 = new Table(getCurrent().getName());
			tb1.setattributes(atr);
			tb1.setData(values);
			return (T)tb1;
		}
		return (T)new String();
	}
	/* (non-Javadoc)
	 * @see generatedsources.sqlBaseVisitor#visitNID(generatedsources.sqlParser.NIDContext)
	 */
	@Override
	public Object visitNID( sqlParser.NIDContext ctx){
		if (ctx.getChildCount() <= 1) return ctx.getChild(0).getText();
	
		return ctx.getChild(0).getText()+"."+ctx.getChild(2).getText();
	}
	
	public Object visitNlocalIDS (sqlParser.NlocalIDSContext ctx){
		if (ctx.getChildCount() <= 1){
			ArrayList<String> ar = new ArrayList();
			ar.add((String)visitChildren(ctx));
			return ar;
		}
		
		ArrayList<String> ar = new ArrayList();
		ar.add((String) visit(ctx.getChild(0)));
		ar.addAll((ArrayList<String>)visit(ctx.getChild(2)));
		return ar;		
	}
	
	public Object checkDuplicates (ArrayList<String> tables){
		LinkedHashSet<String> ntables = new LinkedHashSet(tables);
		LinkedHashSet<String> dup = new LinkedHashSet();
		for (String st: ntables){
			if (dup.contains(st)){
				return st;
			}
			dup.add(st);
		}
		return "";
	}
	
	@Override
	public Object visitLiteral(sqlParser.LiteralContext ctx) {
		
		if (ctx.getChild(0).getText().toUpperCase().equals("NULL"))
			return "NULL";
		else
			return this.visit(ctx.getChild(0));
		
	}

	/****************************
	 * Recibimos un numero
	 * Si este contiene un punto
	 * quitamos todo lo que este despues del punto
	 ****************************/
	@Override 
	public T visitInt_literal(@NotNull sqlParser.Int_literalContext ctx) 
	{ 
		String num = ctx.INT().getText();
		
		if (num.contains("."))
		{
			int index = num.indexOf('.');
			num = num.substring(0, index);
		}
		
		return (T)"int"; 
	}
	
	
	/****************************
	 * Recibimos un numero
	 * Si este no contiene punto
	 * le agregamos .0
	 ****************************/
	@Override 
	public T visitFloat_literal(@NotNull sqlParser.Float_literalContext ctx) 
	{ 
		String num = ctx.INT(0).getText();
		
		if (!num.contains("."))
		{
			num += ".0";
		}
		
		return (T)"float"; 
	}
	
	
	/******************************************
	 * -MONTH-DAY
	 * 1<=MONTH<=12
	 * Validamos el dia segun el mes y el año
	 *******************************************/
	@Override 
	public T visitDate_literal(@NotNull sqlParser.Date_literalContext ctx) 
	{ 
		String fecha = ctx.DATE().getText(); 
		
		fecha = fecha.replace("'", "");
		
		String[] date = fecha.split("-");
		
		int year = Integer.parseInt(date[0]);
		int mes = Integer.parseInt(date[1]);
		int dia = Integer.parseInt(date[2]);
		
		String tipo = "Error";
		
		if (1 <= mes && mes<= 12 && dia>=1)
		{
			if (leap(year))
			{
				if (mes == 2)
				{
					if (dia<=29)
					{
						tipo = "date";
					}
				}
				else
				{
					if (dia<=maxday(mes))
					{
						tipo = "date";
					}
				}
			}
			else
			{
				if (mes == 2)
				{
					if (dia<=28)
					{
						tipo = "date";
					}
				}
				else
				{
					if (dia<=maxday(mes))
					{
						tipo = "date";
					}
				}
			}
		}
		
		return (T)tipo; 
	}
	
	
	/****************************
	 * Recibimos un texto
	 * Debemos revisar el tamaño de este texto
	 ****************************/
	@Override 
	public T visitChar_literal(@NotNull sqlParser.Char_literalContext ctx) 
	{ 
		String text = ctx.CHAR().getText();
		
		text = text.substring(1, text.length() - 1);
		
		int length = text.length();
		
		return (T)"char";
	}
	
	
	/******************
	 * @param year
	 * @return
	 */
	public boolean leap(int year)
	{
		return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
	}
	
	
	/************************

	 * @param mes
	 * @return
	 */
	public int maxday(int mes)
	{
		int dia = 0;
		
		if (mes == 1 || dia == 3 || dia == 5 || dia == 7 || dia == 8 || dia == 10 || dia == 12)
			dia = 31;
		else
			dia = 30;
		
		return dia;
	}
	
	public String compareDate(String date1, String date2)
	{
		date1.replaceAll("'", "");
		date2.replaceAll("'", "");
		
		String valor1[] = date1.split("-");
		String valor2[] = date2.split("-");
		
		if (Integer.parseInt(valor1[0])<Integer.parseInt(valor2[0]))
		{
			return "lower than";
		}
		else
		{
			if (Integer.parseInt(valor1[0])>Integer.parseInt(valor2[0]))
			{
				return "bigger than";
			}
			else
			{
				if (Integer.parseInt(valor1[1])<Integer.parseInt(valor2[1]))
				{
					return "lower than";
				}
				else
				{
					if (Integer.parseInt(valor1[1])>Integer.parseInt(valor2[1]))
					{
						return "bigger than";
					}
					else
					{
						if (Integer.parseInt(valor1[2])<Integer.parseInt(valor2[2]))
						{
							return "lower than";
						}
						else
						{
							if (Integer.parseInt(valor1[2])>Integer.parseInt(valor2[2]))
							{
								return "bigger than";
							}
							else
							{
								return "equal";
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkDate(String date)
	{
		if (!date.contains("-"))
		{
			return false;
		}
		else
		{
			String fecha[] = date.split("-");
			
			int size = fecha.length;
			if (size!=3)
				return false;
			else
			{
				fecha[0].replaceAll("'", "");
				fecha[2].replaceAll("'", "");
				if(fecha[0].length() >4 && fecha[1].length() > 2 && fecha[2].length()>2)
				{
					return false;
				}
				else
				{
					//revisar que sean fechas validas
					return true;
				}
			}	
		}	
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * VISITOR LOGIC: DATA MODELING LANGUAGE
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**************************
	 * INSERT
	 **************************/
	@Override
	public Object visitInsert_value(sqlParser.Insert_valueContext ctx){
		String ID = ctx.ID().getText();
		
		//Checks current db in use
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
			if (this.current.existTable(ID))
			{
				table = this.getCurrent().getTable(ID);
				ArrayList<String> row = new ArrayList();
				int values;
				int columns;
				if (ctx.getChildCount()==7)
				{
					values = ctx.getChild(5).getChildCount();
					columns = ctx.getChild(3).getChildCount();
				}
				else
				{
					values = ctx.getChild(4).getChildCount();
					columns = table.getattributes().size()-1;
				}
				//Checks for the amount of errors BEFORE doing the INSERT statement
				int errCont = this.errors.size();
				
				//Fills row with null
				int colNum = table.getattributes().size();
				for (int i=0; i<colNum;i++)
				{
					row.add("NULL");
				}
				if (table != null)
				{
					if(columns <= values || (columns+2 <= values && ctx.getChild(3).getText().contains("("))|| (columns <= values+2 && ctx.getChild(5).getText().contains("(")))
					{
						ArrayList<Attribute> cols;
						ArrayList<Value> vals;
						
						if(ctx.getChildCount()==7)
						{
							cols = (ArrayList<Attribute>) this.visit(ctx.getChild(3));
							vals = (ArrayList<Value>) this.visit(ctx.getChild(5));
						}
						else
						{
							cols = table.getattributes();
							vals = (ArrayList<Value>)this.visit(ctx.getChild(4));
						}
						{
							for (int cont=0; cont<cols.size() && cont<vals.size();cont++)
							{
								Attribute attr = cols.get(cont);
								Value val = vals.get(cont);
								if (attr.gettype().equals(val.getType())|| val.getValue().toUpperCase().equals("NULL"))
								{
									if (attr.gettype().equals("char"))
									{
										if (attr.getSize()>= val.getSize()-2)
										{
											int index = this.table.getattributes().indexOf(attr);
											row.set(index, val.getValue());
										}
										else
										{
											String rule_s = "Size of value: \"" + val.getValue() + "\" is bigger than the size declared for attribute: \"" + attr.getId() + "\" @line: "+ ctx.getStop().getLine();
											this.errors.add(rule_s);
										}
									}
									else
									{
										int index = this.table.getattributes().indexOf(attr);
										row.set(index, val.getValue());
									}
								}
								else
								{
									//Checks if values can be casted into int or float
									if (attr.gettype().equals("int")&& val.getType().equals("float"))
									{
										String num = val.getValue();
										int index = num.indexOf('.');
										num = num.substring(0, index);
										val.setValue(num);
										val.setType("int");
										
										int index2 = this.table.getattributes().indexOf(attr);
										row.set(index2,  val.getValue());
									}
									else
									{
										if (val.getType().equals("int") && attr.gettype().equals("float"))
										{
											String num = val.getValue();
											num += ".0";
											val.setValue(num);
											val.setType("float");
											
											int index = this.table.getattributes().indexOf(attr);
											row.set(index, val.getValue());
										}
										else
											if(attr.gettype().equals("char") && val.getType().equals("date"))
											{
												if(attr.getSize() >= val.getValue().length()-2)
												{
													int index = this.table.getattributes().indexOf(attr);
													row.set(index, val.getValue());
												}
												else
												{
													String rule_s = "Size of value: \"" + val.getValue() + "\" is bigger than the size declared for attribute: \"" + attr.getId() + "\" @line: "+ ctx.getStop().getLine();
													this.errors.add(rule_s);
												}
											}
											else
											{
												if (attr.gettype().equals("date") && val.getType().equals("char"))
												{
													if(checkDate(val.getValue()))
													{
														int index = this.table.getattributes().indexOf(attr);
														row.set(index,val.getValue());
													}
													else
													{
														String rule_c = "The value \""+ val.getValue() + "\", can't be casted to \"" + attr.gettype() +"\" @line: " + ctx.getStop().getLine();
														this.errors.add(rule_c);
													}	
												}
												else
												{
													String rule_c = "The type of value \""+ val.getValue() + "\", can't be casted to \"" + attr.gettype() +"\" @line: " + ctx.getStop().getLine();
													this.errors.add(rule_c);
												}
												
											}
									}
								}	
							}			
							//Checks for nulls in PK		
							{
								if (this.table.getPrimaryKeys().size() > 0)
								{
									Constraint key = this.table.getPrimaryKeys().get(0);
									for (String idk : key.getIDS_local())
									{
										Attribute atrk = this.table.getID(idk);
										int indexk = this.table.getattributes().indexOf(atrk);
										if (row.get(indexk).toUpperCase().equals("NULL"))
										{
											String rule_5 = "Nulls can't be inserted in Primary Key @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);	
											break;
										}
									}
								}
							}
							if (errCont == this.errors.size())
							{
								if (PrimaryKey(row, -1))
								{
									
									
									//revisar check
									ArrayList<Constraint> check = table.getChecks();//obtenemos checks
									Table temp = table;//guardo la tabla temporal
									table = new Table();
									table.setattributes(temp.getattributes());//seteamos atributos
									table.addData(row);//agregamos fila para evaluar check
									boolean set = true;
									for (Constraint ct: check){
										ANTLRInputStream input = new ANTLRInputStream(ct.getCondition());
										sqlLexer lexer = new sqlLexer(input);
										CommonTokenStream tokens = new CommonTokenStream(lexer);
										sqlParser parser = new sqlParser(tokens);
										ParseTree tree = parser.condition();
										System.out.println(tree.getText());
										Object obj = (Object)visit(tree);
										
										if (obj == null){
											String rule_5 = "Unexpected error evaluating Check "+ct.getId()+" @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											set = false;
										}else{
											LinkedHashSet<Integer> lhk = (LinkedHashSet<Integer>) obj;
											if (lhk.size() == 0){
												String rule_5 = "Inserted values don't meet check evalation "+ct.getId()+" "+ct.getCondition()+" @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												set = false;
											}
										}
										
									}
									if (set){
										if (ForeignKey(row,-1)){
											table = temp;

											this.table.addData(row);
											this.inserted_rows++;
										}else{
											String rule_5 = "Value does not exist in referenced table @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
										}
										
									}
								}
								else
								{
									String rule_5 = "Can't have duplicated Primary Keys @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
								}
							}
						}
					}
					else
					{	
						String rule_5 = "Cant INSERT more columns than values @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
					}
				}
			}
			else
			{
				String rule_5 = "The table " + ID + " does not exist in database " + this.getCurrent().getName() + " @line: " + ctx.getStop().getLine();
				this.errors.add(rule_5);
			}
		}		
		
		
		
		// TODO Auto-generated method stub
		return new String();
	}
	/**************************
	 * LIST
	 **************************/
	@Override
	public Object visitList(sqlParser.ListContext ctx) {
		
		ArrayList<Value> values = new ArrayList();
		
		int cont = 0;
		for (int i = 0; i < ctx.getChildCount(); i++)
			if (!ctx.getChild(i).getText().equals("(") && !ctx.getChild(i).getText().equals(")") && !ctx.getChild(i).getText().equals(","))
			{
				Value valor = new Value();
				String text = ctx.literal(cont).getText();
				String tipo = (String)this.visit(ctx.getChild(i));
				cont++;
				if (tipo.equals("char"))
				{
					valor = new Value(text,tipo, text.length()-2);
				}
				else
				{
					if (tipo.equals("Error"))
					{
						String rule_5 = "Date " + text + " isn't valid @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
						valor = new Value(text,"date");
					}
					else
					{
						valor = new Value(text,tipo);
					}
				}
				values.add(valor);
			}
		
		// TODO Auto-generated method stub
		return values;
	}
	/*****************************
	 * COLUMNS
	 *****************************/
	@Override
	public Object visitColumns(sqlParser.ColumnsContext ctx) {
		
		ArrayList<Attribute> columnas = new ArrayList();
		
		for (int i = 0; i < ctx.getChildCount(); i++)
			if (!ctx.getChild(i).getText().equals("(") && !ctx.getChild(i).getText().equals(")") && !ctx.getChild(i).getText().equals(","))
			{
				
				String columna = ctx.getChild(i).getText(); 
				if (this.table.hasAttribute(columna))
				{
					Attribute id = this.table.getID(columna);
					columnas.add(id);
				}
				else
				{
					String rule_5 = "The table " + this.table.getName() + " does not contain the column: " + columna + " @line: " + ctx.getStop().getLine();
					this.errors.add(rule_5);
				}
			}
		
		// TODO Auto-generated method stub
		return columnas;
	}
	/*************************
	 * UPDATE
	 *************************/
	@Override
	public Object visitUpdate_value(sqlParser.Update_valueContext ctx) {
		
		String id = ctx.getChild(1).getText();
		int contErrores = this.errors.size();
		
		// Verificar que haya un DB en uso
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{
		
			if (this.current.existTable(id))
			{
				this.table = this.current.getTable(id);
				int size = this.table.getData().size();
				
				if (ctx.getChildCount() == 5)
				{
					ArrayList<String> newfila = (ArrayList<String>)this.visit(ctx.getChild(3));
					
					for (int i = 0; i<size && contErrores==this.errors.size();i++)
					{
						this.table.getData().set(i, newfila);
					}
				}
				else
				{
					Object obj = (Object) visit(ctx.getChild(5));
					if (obj == null){
						String rule_5 = "Error en condiciones definidas @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
						return null;
					}
					
					if (!(obj instanceof LinkedHashSet)){
						String rule_5 = "Error en condiciones definidas @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
						return null;
					}
					
					LinkedHashSet<Integer> indices = (LinkedHashSet<Integer>)obj;
					ArrayList<Integer> index= new ArrayList(indices);
					ArrayList<String> newfila = (ArrayList<String>)this.visit(ctx.getChild(3));
					ArrayList<String> fila = new ArrayList<String>();
					ArrayList<String> fin = new ArrayList<String>();
					
					for (int j=0;j<newfila.size();j++)
						fin.add("");
					
					boolean flag = true;
					if (contErrores == this.errors.size())
					{
						for (int i: index){
							fila = table.getData().get(i);
							for (String col : newfila)
							{
								int index2 = newfila.indexOf(col);
								if (col.equals("UPDATE"))
								{
									fin.set(i, fila.get(index2));
								}
								else
									fin.set(i, newfila.get(index2));
							}
							
							//Revisamos que no venga un NULL en una PrimaryKey
							{
								if (this.table.getPrimaryKeys().size() > 0)
								{
									Constraint key = this.table.getPrimaryKeys().get(0);
									for (String idk : key.getIDS_local())
									{
										Attribute atrk = this.table.getID(idk);
										int indexk = this.table.getattributes().indexOf(atrk);
										if (fila.get(indexk).toUpperCase().equals("NULL"))
										{
											flag = false;
											String rule_5 = "NULL can't be inserted in a Primary Key @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);	
											break;
										}
									}
								}
							}
							
							if (PrimaryKey(fin,i))
							{
								//En update solo lo podemos cambiar si el foreign key no existe
								if (!ForeignKey(fin,i))
								{
									//revisar check
									ArrayList<Constraint> check = table.getChecks();//obtenemos checks
									Table temp = table;//guardo la tabla temporal
									table = new Table();
									table.setattributes(temp.getattributes());//seteamos atributos
									table.addData(fin);//agregamos fila para evaluar check
									
									for (Constraint ct: check){
										ANTLRInputStream input = new ANTLRInputStream(ct.getCondition());
										sqlLexer lexer = new sqlLexer(input);
										CommonTokenStream tokens = new CommonTokenStream(lexer);
										sqlParser parser = new sqlParser(tokens);
										ParseTree tree = parser.condition();
										
										Object obj1 = (Object)visit(tree);
										if (obj1 == null){
											String rule_5 = "Unexpected error evaluating a check "+ct.getId()+" @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											flag = false;
										}else{
											LinkedHashSet<Integer> lhk = (LinkedHashSet<Integer>) obj1;
											if (lhk.size() == 0){
												String rule_5 = "Inserted values don't meet check requirements "+ct.getId()+" "+ct.getCondition()+" @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												flag = false;
											}
										}
										
									}
									
								}
								else
								{
									flag=false;
									String rule_5 = "The Value trying to be updated references another Table  @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									break;
								}
							}
							else
							{
								flag=false;
								String rule_5 = "Duplicated Primary Key  @line: " + ctx.getStop().getLine();
								this.errors.add(rule_5);
								break;
							}
						}
						
						//Si todos cumplieron con las validaciones los actualizamos
						if (flag)
						{
							for (int i: index){
								this.table.getData().set(i, fin);
								this.updated_rows++;
							}
						}
					}
				}
			}
			else
			{
				String rule_5 = "La tabla " + id + " no existe en la base de datos " + this.getCurrent().getName() + " @line: " + ctx.getStop().getLine();
				this.errors.add(rule_5);
			}
		}
		
		// TODO Auto-generated method stub
		return super.visitUpdate_value(ctx);
	}
	
	/**************************
	 * Asignacion
	 **************************/
	@Override
	public Object visitAsignacion(sqlParser.AsignacionContext ctx) {
		
		ArrayList<String> newfila = new ArrayList<String>();
		
		int numCols = this.table.getattributes().size();
		for (int i=0;i<numCols;i++)
			newfila.add("UPDATE");
		
		int index = 0;
		Attribute atr = new Attribute();
		for (int j=0;j<ctx.getChildCount();j++)
		{
			String text = ctx.getChild(j).getText();
			if (!text.equals("=") && !text.equals(","))
			{
				if (j==0 || j%4==0)
				{
					if (this.table.hasAttribute(text))
					{
						atr = this.table.getID(text);
						index = this.table.getattributes().indexOf(atr);
					}
					else
					{
						String rule_5 = "La tabla " + this.table.getName() + " no contiene la columna " + text + " @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
					}
				}
				else
				{
					String tipo = (String)this.visit(ctx.getChild(j));
					//buscamos si el tipo puede ser casteado
					if (atr.gettype().equals(tipo) || text.toUpperCase().equals("NULL"))
					{
						if (tipo.equals("char"))
						{
							if (text.length()-2 <= atr.getSize())
							{
								newfila.set(index, text);
							}
							else
							{
								String rule_5 = "The size of '" + text + "' es bigger than the size reserved for character: '"+ atr.getId() +" @line: " + ctx.getStop().getLine();
								this.errors.add(rule_5);
							}
						}
						else
							newfila.set(index, text);
					}
					else
					{
						if (atr.gettype().equals("int") && tipo.equals("float"))
						{
							int k = text.indexOf('.');
							newfila.set(index, text.substring(0, k));
						}
						else
						{
							if (atr.gettype().equals("float") && tipo.equals("int"))
							{
								newfila.set(index, text+".0");
							}
							else
							{
								if (atr.gettype().equals("char") && tipo.equals("date"))
								{
									if (text.length()-2 == atr.getSize())
									{
										newfila.set(index, text);
									}
									else
									{
										String rule_5 = "The size of '" + text + "' is bigger than the size reserved for '"+ atr.getId() +" @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
									}
								}
								else
								{
									if (atr.gettype().equals("date") && tipo.equals("char"))
									{
										if (this.visit(ctx.getChild(j)).equals("date"))
										{
											newfila.set(index, text);
										}
										else
										{
											String rule_5 = "Value: '" + text + "', can't be casted to '"+ atr.gettype() +" @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
										}
									}
									else
									{
										String rule_5 = "The type: '" + tipo + "', can't be casted to '"+ atr.gettype() +" @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
									}
								}
							}
						}
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		return newfila;
	}
	
	
	/**************************
	 * DELETE 
	 * debo revisar que si exista la tabla en la database actual
	 * si no existe condicion borro toda la tabla
	 * si existe reviso que filas cumplen con la condicion
	 */
	@Override
	public Object visitDelete_value(sqlParser.Delete_valueContext ctx) {
		String id = ctx.ID().getText();
		// Verificar que haya un DB en uso
		if (this.getCurrent().getName().isEmpty())
		{
			String noDB = "No database in use @line: " + ctx.getStop().getLine();
			this.errors.add(noDB);
		}
		else
		{		
			this.table = this.getCurrent().getTable(id);
			
			if (table == null){
				String rule_5 = "The table " + id + " does not exist in DataBase: " + this.getCurrent().getName() + " @line: " + ctx.getStop().getLine();
				this.errors.add(rule_5);
				return null;
			}
			
			int errores = 0;
			
			// Sin WHERE condition -> borra toda la data
			if (ctx.getChildCount() <= 4)
			{
				// Revisar FKS
				// Verificar que alguna fk haga referencia a esta tabla
				if (this.current.existRef(id))
				{
					for (Table i: this.current.getTables())
						for (Constraint f: i.getForeignKey())
							if (f.getId_ref().equals(id))
							{							
								ArrayList<Integer> index_table_with_fk = new ArrayList<Integer>();
								ArrayList<Integer> index_table = new ArrayList<Integer>();
								for (String s: f.getIDS_local())
									index_table_with_fk.add(i.getattributesNames().indexOf(s));
								for (String s: f.getIDS_refs())
									index_table.add(this.table.getattributesNames().indexOf(s));							
								// Verificar que sean del mismo tamaño sino no se puede hacer la comapracion
								if (index_table_with_fk.size() == index_table.size())
								{
									int cont = 0;
									int index_fk = 0;
									for (Integer index_i: index_table)
									{
										index_fk = index_table_with_fk.get(cont);
										for (String use_data_i: this.table.dataColumnI(index_i))
											for (String ref_data_i: i.dataColumnI(index_fk))
											{
												if (use_data_i.equals(ref_data_i))
												{
													String data_to_delete_in_ref = "Can't delete the value \"" + use_data_i + "\" because it belongs to table:  \"" + i.getName() + "\", esta vinculacion la hace la Foreign Key \"" + f.getId() + "\" @line: " + ctx.getStop().getLine();
													this.errors.add(data_to_delete_in_ref);
													errores++;
												}
											}
										cont++;
									}
								}
								else
								{
									String compare_failed = "Conflict detected with Foreign Key \"" + f.getId() + "\" @line: " + ctx.getStop().getLine();
									this.errors.add(compare_failed);
									errores++;
								}
							}
				}
				// Borrar toda la data si no hay errores
				if (errores == 0)
				{
					this.messages.add("Deleted " + this.table.getData().size() + " rows from the table \"" + this.table.getName() + "\"");
					table.setData(new ArrayList<ArrayList<String>>());
				}
				return table;				
			}
			
			
			Object obj = (Object) visit(ctx.getChild(4));
			if (obj == null){
				String rule_5 = "Error in the defined conditions @line: " + ctx.getStop().getLine();
				this.errors.add(rule_5);
				return null;
			}
			
			if (!(obj instanceof LinkedHashSet)){
				String rule_5 = "Error in the defined conditions @line: " + ctx.getStop().getLine();
				this.errors.add(rule_5);
				return null;
			}
			
			LinkedHashSet<Integer> indices = (LinkedHashSet<Integer>)obj;
			ArrayList<Integer> index = new ArrayList(indices);
			ArrayList<Integer> indexesToDelete = new ArrayList<Integer>();
			Collections.reverse(index);
			
			// Revisar FKS
			// Verificar que alguna fk haga referencia a esta tabla
			if (this.current.existRef(id))
			{
				for (Table i: this.current.getTables())
					for (Constraint f: i.getForeignKey())
						if (f.getId_ref().equals(id))
						{							
							ArrayList<Integer> index_table_with_fk = new ArrayList<Integer>();
							ArrayList<Integer> index_table = new ArrayList<Integer>();
							for (String s: f.getIDS_local())
								index_table_with_fk.add(i.getattributesNames().indexOf(s));
							for (String s: f.getIDS_refs())
								index_table.add(this.table.getattributesNames().indexOf(s));							
							// Verificar que sean del mismo tamaño sino no se puede hacer la comapracion
							if (index_table_with_fk.size() == index_table.size())
							{
								int cont = 0;
								int index_fk = 0;
								for (Integer index_i: index_table)
								{
									index_fk = index_table_with_fk.get(cont);
									int cont_2 = 0;
									int pos = 0;
									for (String use_data_i: this.table.dataColumnIWithIndexs(index_i, index))
									{
										int match = 0;
										pos = index.get(cont_2);										
										for (String ref_data_i: i.dataColumnI(index_fk))
										{
											if (use_data_i.equals(ref_data_i))
											{
												String data_to_delete_in_ref = "Value \"" + use_data_i + "\" can't be deleted, included in table: \"" + i.getName() + "\", via Foreign Key: \"" + f.getId() + "\" @line: " + ctx.getStop().getLine();
												this.errors.add(data_to_delete_in_ref);
												errores++;
												match++;
											}											
										}
										cont_2++;
										if (match == 0)
											indexesToDelete.add(pos);
									}
									cont++;
								}
							}
							else
							{
								String compare_failed = "Can't DELETE, conflict with Foreign Key: \"" + f.getId() + "\" @line: " + ctx.getStop().getLine();
								this.errors.add(compare_failed);
								errores++;
							}							
						}
			}
			// Indices que cumplen con el WHERE CONDITION y no tengan restriccion de CONSTRAINT
			for (int i: indexesToDelete){
				table.getData().remove(i);
			}
			if (! indexesToDelete.isEmpty())
				this.messages.add("Deleted " + indexesToDelete.size() + " rows from the table: \"" + this.table.getName() + "\"");
		}
		return (T) table;
	}
	/**************************
	 * Condition
	 */
	@Override 
	public T visitConditionNot(sqlParser.ConditionNotContext ctx) 
	{ 
		Object obj = visit(ctx.getChild(1));
		if (obj == null){
			return null;
		}
		
		if (!(obj instanceof LinkedHashSet)){
			return null;
		}
		
		LinkedHashSet<Integer> indices = (LinkedHashSet<Integer>) obj;
		
		int size = table.getData().size();
		
		LinkedHashSet<Integer> nIndices = new LinkedHashSet();
		
		for (int i = 0; i < size; i++){
			if (!indices.contains(i))
				nIndices.add(i);
		}
		
		return (T)nIndices;
	}

	public T visitConditionComp (sqlParser.ConditionCompContext ctx){
		if (ctx.getChildCount() <= 1) return (T)visitChildren(ctx);
		
		Object objComp = visit(ctx.getChild(0));
		if (objComp == null) return null;
		
		if (!(objComp instanceof LinkedHashSet)) return null;
		
		LinkedHashSet<Integer> compIndex = (LinkedHashSet<Integer>) objComp;
		
		String logic = (String) visit(ctx.getChild(1));
		
		Object objCond = visit(ctx.getChild(2));
		if (objCond == null) return null;
		
		if (!(objCond instanceof LinkedHashSet)) return null;
		
		LinkedHashSet<Integer> condIndex = (LinkedHashSet<Integer>) objCond;
		LinkedHashSet<Integer> result = null;
		
		switch (logic){
			case "OR":
				
				compIndex.addAll(condIndex);
				result = new LinkedHashSet<Integer>();
				result.addAll(compIndex);
				
				break;
			case "AND":
				result = new LinkedHashSet<Integer>();
				for (int i: compIndex){
					if (condIndex.contains(i))
						result.add(i);
				}
				break;
		}
		
		
		return (T)result;
	}

	public T visitConditionCond (sqlParser.ConditionCondContext ctx){
		if (ctx.getChildCount() <= 3) return (T) visit(ctx.getChild(1));
		
		Object obj1 = visit(ctx.getChild(1));
		if (obj1 == null) return null;
		
		if (!(obj1 instanceof LinkedHashSet)) return null;
		
		LinkedHashSet<Integer> cond1Index = (LinkedHashSet<Integer>) obj1;
		
		Object obj2 = visit(ctx.getChild(4));
		if (obj2 == null) return null;
		
		if (!(obj2 instanceof LinkedHashSet)) return null;
		
		LinkedHashSet<Integer> cond2Index = (LinkedHashSet<Integer>) obj2;
		
		String logic = (String)visit(ctx.getChild(3));
		
		LinkedHashSet<Integer> result = null;
		
		switch (logic){
			case "OR":
				cond1Index.addAll(cond2Index);
				result = new LinkedHashSet<Integer>();
				result.addAll(cond1Index);
				break;
			case "AND":
				result = new LinkedHashSet<Integer>();
				for (int i: cond1Index){
					if (cond2Index.contains(i))
						result.add(i);
				}
				break;
		}
		
		
		return (T)result;
	}
	/***************************
	 * CompId
	 * Revisamos comp que puede tener
	 * ID relational ID | literal
	 */
	@Override 
	public T visitCompId(@NotNull sqlParser.CompIdContext ctx) 
	{
		
		LinkedHashSet<Integer> list = new LinkedHashSet();
		String op = ctx.getChild(1).getText(); //agarro el relational
		
		
		String id = (String) visit(ctx.getChild(0)); //agarro el primer id
		
		if (this.table.hasAttribute(id)) //reviso si existe en la tabla
		{
			
			if (table.isAmbiguous(id)){
				String error_ = "La llamada <<"+id+">> es ambigua @line: " + ctx.getStop().getLine();
				errors.add(error_);
				return null;
			}
			//tomo el atributo de la tabla y el indice de este
			
			Attribute atr = this.table.getID(id);
			
			Attribute id2 = new Attribute();
			int index = this.table.getattributes().indexOf(atr);
			
			
			//visito el ultimo hijo 
			String tipo = (String)this.visit(ctx.getChild(2)); //si es literal, voy a recibir el tipo
			String value = ctx.getChild(2).getText();
			
			//creo una bandera para ver si voy a poder castear los tipos
			boolean flag = false;
			
			//Si es un literal
			if (tipo.equals("int") || tipo.equals("float") || tipo.equals("date") || tipo.equals("char") || tipo.equals("NULL"))
			{
				if (value.toUpperCase().equals("NULL"))
					flag=true;
				else
				{	
					if (atr.gettype().equals("int") && (tipo.equals("int") || tipo.equals("float")))
					{
						flag = true;
					}
					else
					{
						if (atr.gettype().equals("float") && (tipo.equals("int") || tipo.equals("float")))
						{
							flag = true;
						}
						else
						{
							if (atr.gettype().equals("char") && (tipo.equals("char") || tipo.equals("date")))
							{
								flag = true;
							}
							else
							{
								if (atr.gettype().equals("date") && (tipo.equals("char") || tipo.equals("date")))
								{
									flag = true;
								}
								else
								{
									String rule_5 = "Can't compare type:  " + atr.getId() + " with type:  " + tipo + " @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
							}
								
						}
					}
				}
			}
			else
				if (tipo.equals("Error"))
				{
					// no acepto la fecha
					String rule_5 = "The date " + value + " isn't valid @line: " + ctx.getStop().getLine();
					this.errors.add(rule_5);
					return null;
				}
				else
				{
					//Si es una columna de la tabla
					String columna = (String)visit(ctx.getChild(2)); 
					if (this.table.hasAttribute(columna))
					{
						if (table.isAmbiguous(columna)){
							String error_ = "La llamada <<"+columna+">> es ambigua @line: " + ctx.getStop().getLine();
							errors.add(error_);
							return null;
						}
						id2 = this.table.getID(columna);
						
						value = "'";
						
						if (atr.gettype().equals("int") && (id2.gettype().equals("int") || id2.gettype().equals("float")))
						{
							flag = true;
						}
						else
						{
							if (atr.gettype().equals("float") && (id2.gettype().equals("int") || id2.gettype().equals("float")))
							{
								flag = true;
							}
							else
							{
								if (atr.gettype().equals("char") && (id2.gettype().equals("char") || id2.gettype().equals("date")))
								{
									flag = true;
								}
								else
								{
									if (atr.gettype().equals("date") && (id2.gettype().equals("char") || id2.gettype().equals("date")))
									{
										flag = true;
									}
									else
									{
										String rule_5 = "The type " + atr.getId() + " can't be compared with  a " + id2.gettype() + " @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
								}
									
							}
						}
					}
					else
					{
						String rule_5 = "The table " + this.table.getName() + " doesn't have the column:  " + columna + " @line: " + ctx.getStop().getLine();
						this.errors.add(rule_5);
						return null;
					}
				}
			
			if (flag)
			{
				/*
				 * CompareTo devuelve 0 si son iguales
				 * mas de 0 si el primero es mayor al segundo
				 * y menos de 0 si el primero es menor al tercero
				 */
				if (value.toUpperCase().equals("NULL"))
				{
					if (op.equals("=") || op.equals(">=") || op.equals("<="))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (value.toUpperCase().equals(fila.get(index).toUpperCase()))
								list.add(cont);
							cont++;
						}
					}
					if (op.equals("<>"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (!value.toUpperCase().equals(fila.get(index).toUpperCase()))
								list.add(cont);
							cont++;
						}
					}
							
				}
				else
				switch (op)
				{
					//Igual
					case "=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare a NULL with a value. @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(s);
									if (valor.compareTo(comp)==0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(s);
										Double comp = Double.parseDouble(s1);
										if (valor.compareTo(comp)==0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (compareDate(comp, value).equals("equal"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare value with NULL @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (compareDate(comp, valor).equals("equal"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare with a NULL @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (value.compareTo(comp)==0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare NULL with a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (valor.compareTo(comp)==0)
														list.add(cont);
													cont++;
												}
											}
										}
									}
								}
							}	
						}
						
						break;
					//Distinto
					case "<>":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare Null with a Value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(s);
									if (valor.compareTo(comp)!=0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare Null with a Value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(s);
										Double comp = Double.parseDouble(s1);
										if (valor.compareTo(comp)!=0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{	
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare Null to value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (!compareDate(comp, value).equals("equal"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (!compareDate(comp, valor).equals("equal"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (value.compareTo(comp)!=0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (valor.compareTo(comp)!=0)
														list.add(cont);
													cont++;
												}
											}
										}
									}
								}
							}
						}
						break;
					//Menor
					case "<":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (comp.compareTo(valor)<0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(s);
										Double comp = Double.parseDouble(s1);
										if (comp.compareTo(valor)<0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (compareDate(comp, value).equals("lower than"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (compareDate(comp, valor).equals("lower than"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(value)<0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(valor)<0)
														list.add(cont);
													cont++;
												}
											}
										}
									}
								}
							}
						}
						break;
					//Mayor
					case ">":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(s);
									if (comp.compareTo(valor)>0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(fila.get(index2));
										Double comp = Double.parseDouble(fila.get(index));
										if (comp.compareTo(valor)>0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (compareDate(comp, value).equals("bigger than"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (compareDate(comp, valor).equals("bigger than"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(value)>0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(valor)>0)
														list.add(cont);
													cont++;
												}
											}
										}
									}
								}
							}
						}
						break;
					//Menor Igual
					case "<=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (comp.compareTo(valor)<0 || valor.compareTo(comp)==0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(fila.get(index2));
										Double comp = Double.parseDouble(fila.get(index));
										if (comp.compareTo(valor)<0 || valor.compareTo(comp)==0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (compareDate(comp, value).equals("equal") || compareDate(comp, value).equals("lower than"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (compareDate(comp, valor).equals("equal") || compareDate(comp, valor).equals("lower than"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(value)<0 || comp.compareTo(value)==0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(valor)<0 || comp.compareTo(valor)==0)
														list.add(cont);
													cont++;	
												}
											}
										}
									}
								}
							}
						}
						break;
					//Mayor Igual	
					case ">=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (comp.compareTo(valor)>0 || valor.compareTo(comp)==0)
										list.add(cont);
									cont++;
								}
							}
						}
						else //si es una columna de tipo int o float
						{
							if (value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
							{
								int index2 = this.table.getattributes().indexOf(id2);
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									String s = fila.get(index2);
									String s1 = fila.get(index);
									if (s.toLowerCase().equals("null") || s1.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										Double valor = Double.parseDouble(fila.get(index2));
										Double comp = Double.parseDouble(fila.get(index));
										if (comp.compareTo(valor)>0 || valor.compareTo(comp)==0)
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son date y es con literal
								if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (compareDate(comp, value).equals("equal") || compareDate(comp, value).equals("bigger than"))
												list.add(cont);
											cont++;
										}
									}
								}
								else
								{
									//Si ambos son date  y es con columna
									if (value.equals("'") && atr.gettype().equals("date") && id2.gettype().equals("date"))
									{
										int index2 = this.table.getattributes().indexOf(id2);
										int cont = 0;
										for (ArrayList<String> fila : this.table.getData())
										{
											
											String comp = fila.get(index);
											String valor = fila.get(index2);
											if (comp.toLowerCase().equals("null") || valor.toLowerCase().equals("null"))
											{
												String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
												this.errors.add(rule_5);
												return null;
											}
											else
											{
												if (compareDate(comp, valor).equals("equal") || compareDate(comp, valor).equals("bigger than"))
													list.add(cont);
												cont++;
											}
										}
									}
									else
									{
										//Si ambos son char o mezcla
										if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
										{
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String comp = fila.get(index);
												if (comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(value)>0 || comp.compareTo(value)==0)
														list.add(cont);
													cont++;
												}
											}
										}
										else
										{
											int index2 = this.table.getattributes().indexOf(id2);
											int cont = 0;
											for (ArrayList<String> fila : this.table.getData())
											{
												String valor = fila.get(index2);
												String comp = fila.get(index);
												if (valor.toLowerCase().equals("null") || comp.toLowerCase().equals("null"))
												{
													String rule_5 = "Can't compare Null to a value @line: " + ctx.getStop().getLine();
													this.errors.add(rule_5);
													return null;
												}
												else
												{
													if (comp.compareTo(valor)<0 || comp.compareTo(valor)==0)
														list.add(cont);
													cont++;	
												}
											}
										}
									}
								}
							}
						}
						break;
				}
			}
			//System.out.println("llego a list: "+list);
			return (T)list;
		}
		else
		{
			String rule_5 = "The tab " + this.table.getName() + " doesn't have the column " + id + " @line: " + ctx.getStop().getLine();
			this.errors.add(rule_5);
			return null;
		}
	}
	/*********************************
	 * CompLit
	 */
	@Override
	public Object visitCompLit(sqlParser.CompLitContext ctx) {
		LinkedHashSet<Integer> list = new LinkedHashSet();
		String op = ctx.getChild(1).getText(); //agarro el relational
		
		//visito el primer hijo 
		String tipo = (String)this.visit(ctx.getChild(0)); //voy a recibir el tipo
		String value = ctx.getChild(0).getText();
		
		//visito el ultimo hijo 
		String tipo2 = (String)this.visit(ctx.getChild(2)); //voy a recibir el tipo
		String value2 = (String)visit(ctx.getChild(2));
		
		boolean flag = false;
		
		if (!tipo.equals("Error") && !tipo2.equals("Error"))
			flag = true;
		
		if (flag)
		{
			switch(op)
			{
				case "=":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (compareDate(value,value2).equals("equal"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)==0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)==0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
				case "<>":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (!compareDate(value,value2).equals("equal"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)!=0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)!=0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
				case "<":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (compareDate(value,value2).equals("lower than"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)<0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)<0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
				case ">":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (compareDate(value,value2).equals("bigger than"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)>0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)>0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
				case "<=":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (compareDate(value,value2).equals("lower than") || compareDate(value,value2).equals("equal"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)<0 || num.compareTo(num2)==0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)<0 || value.compareTo(value2)==0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
				case ">=":
					if (tipo.equals("date") && tipo2.equals("date"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (compareDate(value,value2).equals("bigger than") || compareDate(value,value2).equals("equal"))
								list.add(cont);
							cont++;
						}
					}
					else
					{
						if ((tipo.equals("int") || tipo.equals("float")) && (tipo2.equals("int") || tipo2.equals("float")))
						{
							int cont = 0;
							Double num = Double.parseDouble(value);
							Double num2 = Double.parseDouble(value2);
							for (ArrayList<String> fila : this.table.getData())
							{
								if (num.compareTo(num2)>0 || num.compareTo(num2)==0)
									list.add(cont);
								cont++;
							}
						}
						else
						{
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								if (value.compareTo(value2)>0 || value.compareTo(value2)==0)
									list.add(cont);
								cont++;
							}
						}
					}
					break;
			}
			return list;
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	
	/*********************************
	 * CompLitId
	 */
	@Override
	public Object visitCompLitId(sqlParser.CompLitIdContext ctx) {
		
		LinkedHashSet<Integer> list = new LinkedHashSet();
		String op = ctx.getChild(1).getText(); //agarro el relational
		
		
		String id = (String)visit(ctx.getChild(2)); //agarro el primer id
		if (this.table.hasAttribute(id)) //reviso si existe en la tabla
		{
			if (table.isAmbiguous(id)){
				String error_ = "The call <<"+id+">> is ambiguous @line: " + ctx.getStop().getLine();
				errors.add(error_);
				return null;
			}
			//tomo el atributo de la tabla y el indice de este
			Attribute atr = this.table.getID(id);
			int index = this.table.getattributes().indexOf(atr);
			
			//visito el ultimo hijo 
			String tipo = (String)this.visit(ctx.getChild(0)); //si es literal, voy a recibir el tipo
			String value = ctx.getChild(0).getText();
			
			//creo una bandera para ver si voy a poder castear los tipos
			boolean flag = false;
			
			//Si es un literal
			if (tipo.equals("int") || tipo.equals("float") || tipo.equals("date") || tipo.equals("char"))
			{
				if (value.toUpperCase().equals("NULL"))
					flag=true;
				else
				{
					if (atr.gettype().equals("int") && (tipo.equals("int") || tipo.equals("float")))
					{
						flag = true;
					}
					else
					{
						if (atr.gettype().equals("float") && (tipo.equals("int") || tipo.equals("float")))
						{
							flag = true;
						}
						else
						{
							if (atr.gettype().equals("char") && (tipo.equals("char") || tipo.equals("date")))
							{
								flag = true;
							}
							else
							{
								if (atr.gettype().equals("date") && (tipo.equals("char") || tipo.equals("date")))
								{
									flag = true;
								}
								else
								{
									String rule_5 = "The type of " + atr.getId() + " can't be compared to " + tipo + " @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
								}
							}
								
						}
					}
				}
			}
			else
				if (tipo.equals("Error"))
				{
					// no acepto la fecha
					String rule_5 = "The date " + value + " is not valid @line: " + ctx.getStop().getLine();
					this.errors.add(rule_5);
					return null;
				}
				
			
			if (flag)
			{
				/*
				 * CompareTo devuelve 0 si son iguales
				 * mas de 0 si el primero es mayor al segundo
				 * y menos de 0 si el primero es menor al tercero
				 */
				if (value.toUpperCase().equals("NULL"))
				{
					if (op.equals("=") || op.equals(">=") || op.equals("<="))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (value.toUpperCase().equals(fila.get(index).toUpperCase()))
								list.add(cont);
							cont++;
						}
					}
					if (op.equals("<>"))
					{
						int cont = 0;
						for (ArrayList<String> fila : this.table.getData())
						{
							if (!value.toUpperCase().equals(fila.get(index).toUpperCase()))
								list.add(cont);
							cont++;
						}
					}
							
				}
				else
				switch (op)
				{
					//Igual
					case "=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare null to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)==0)
										list.add(cont);
									cont++;
								}
							}
						}
						else
						{
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare null to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (compareDate(comp, value).equals("equal"))
											list.add(cont);
										cont++;
									}	
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)==0)
												list.add(cont);
											cont++;
										}
									}
								}						
							}
						}
						break;
					//Distinto
					case "<>":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)!=0)
										list.add(cont);
									cont++;
								}
							}
						}					
						else
						{	
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (!compareDate(comp, value).equals("equal"))
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)!=0)
												list.add(cont);
											cont++;
										}
									}
								}
							}
						}
						break;
					//Menor
					case "<":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)<0)
										list.add(cont);
									cont++;
								}
							}
						}
						else
						{
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (compareDate(comp, value).equals("lower than"))
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)<0)
												list.add(cont);
											cont++;
										}
									}
								}
							}
						}
						break;
					//Mayor
					case ">":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)>0)
										list.add(cont);
									cont++;
								}
							}
						}
						else
						{
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (compareDate(comp, value).equals("bigger than"))
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)>0)
												list.add(cont);
											cont++;
										}
									}
								}
							}
						}
						break;
					//Menor Igual
					case "<=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)==0 || valor.compareTo(comp)<0)
										list.add(cont);
									cont++;
								}
							}
						}
						else
						{
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (compareDate(comp, value).equals("equal") || compareDate(comp, value).equals("lower than"))
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)==0 || value.compareTo(comp)<0)
												list.add(cont);
											cont++;
										}
									}
								}
							}
						}
						break;
					//Mayor Igual	
					case ">=":
						//si es un literal y de tipo int o float
						if (!value.equals("'") && (atr.gettype().equals("int") || atr.gettype().equals("float")))
						{
							Double valor = Double.parseDouble(value);
							int cont = 0;
							for (ArrayList<String> fila : this.table.getData())
							{
								String s = fila.get(index);
								if (s.toLowerCase().equals("null"))
								{
									String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
									this.errors.add(rule_5);
									return null;
									
								}
								else
								{
									Double comp = Double.parseDouble(fila.get(index));
									if (valor.compareTo(comp)==0 || valor.compareTo(comp)>0)
										list.add(cont);
									cont++;
								}
							}
						}
						else
						{
							//Si ambos son date y es con literal
							if (!value.equals("'") && atr.gettype().equals("date") && tipo.equals("date"))
							{
								int cont = 0;
								for (ArrayList<String> fila : this.table.getData())
								{
									
									String comp = fila.get(index);
									if (comp.toLowerCase().equals("null"))
									{
										String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
										this.errors.add(rule_5);
										return null;
									}
									else
									{
										if (compareDate(comp, value).equals("equal") || compareDate(comp, value).equals("bigger than"))
											list.add(cont);
										cont++;
									}
								}
							}
							else
							{
								//Si ambos son char o mezcla
								if (!value.equals("'") && (atr.gettype().equals("char") || atr.gettype().equals("date")))
								{
									int cont = 0;
									for (ArrayList<String> fila : this.table.getData())
									{
										String comp = fila.get(index);
										if (comp.toLowerCase().equals("null"))
										{
											String rule_5 = "Can't compare NULL to a value @line: " + ctx.getStop().getLine();
											this.errors.add(rule_5);
											return null;
										}
										else
										{
											if (value.compareTo(comp)==0 || value.compareTo(comp)>0)
												list.add(cont);
											cont++;
										}
									}
								}
							}
						}
						break;
				}
			}
			return (T)list;
		}
		else
		{
			String rule_5 = "La tabla " + this.table.getName() + " no contiene la columna " + id + " @line: " + ctx.getStop().getLine();
			this.errors.add(rule_5);
			return null;
		}
		
	}
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
