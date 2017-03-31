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


/**
 * @author Rafa
 *
 * @param <T>
 */
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
								errores++
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
						}
					}
				}
			}
		}
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
