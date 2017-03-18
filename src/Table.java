import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable {
	
	private String name;
	private ArrayList<Attribute> attributes;
	private ArrayList<Constraint> PrimaryKeys;
	private ArrayList<Constraint> ForeignKey;
	private ArrayList<Constraint> Checks;
	private ArrayList<ArrayList<String>> data;
	private ArrayList<String> othersIds;
	
	public Table()
	{
		this.name = "";
		this.attributes = new ArrayList<Attribute>();
		this.PrimaryKeys = new ArrayList<Constraint>();
		this.ForeignKey = new ArrayList<Constraint>();
		this.Checks = new ArrayList<Constraint>();
		this.data = new ArrayList<ArrayList<String>>();
		this.setOthersIds(new ArrayList<String>()); 
	}

	public Table(String name) {		
		this.name = name;
		this.attributes = new ArrayList<Attribute>();
		this.PrimaryKeys = new ArrayList<Constraint>();
		this.ForeignKey = new ArrayList<Constraint>();
		this.Checks = new ArrayList<Constraint>();
		this.data = new ArrayList<ArrayList<String>>();
		this.setOthersIds(new ArrayList<String>());
	}	

	public Table(String name, ArrayList<Attribute> attributes, ArrayList<Constraint> primaryKeys, ArrayList<Constraint> foreignKey, ArrayList<Constraint> checks) {
		this.name = name;
		this.attributes = attributes;
		PrimaryKeys = primaryKeys;
		ForeignKey = foreignKey;
		this.Checks = checks;
		this.data = new ArrayList<ArrayList<String>>();
		this.setOthersIds(new ArrayList<String>());
		for (Attribute atr : this.attributes)
		{
			String col = name+"."+atr.getId();
			this.getOthersIds().add(col);
		}
	}
	
	public Table(Table table){
		name = table.getName();
		attributes = table.getattributes();
		PrimaryKeys = table.getPrimaryKeys();
		ForeignKey = table.getForeignKey();
		Checks = table.getChecks();
		data = table.getData();
		othersIds = table.getOthersIds();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the primaryKeys
	 */
	public ArrayList<Constraint> getPrimaryKeys() {
		return PrimaryKeys;
	}

	/**
	 * @return the foreignKey
	 */
	public ArrayList<Constraint> getForeignKey() {
		return ForeignKey;
	}

	/**
	 * @return the attributes
	 */
	public ArrayList<Attribute> getattributes() {
		return attributes;
	}
	
	/**
	 * @return the name of the attributes
	 */
	public ArrayList<String> getattributesNames() {
		ArrayList<String> result = new ArrayList<String>();
		for (Attribute i: this.attributes)
			result.add(i.getId());
		
		for (String name : this.getOthersIds())
			result.add(name);
		return result;
	}

	/**
	 * @return the checks
	 */
	public ArrayList<Constraint> getChecks() {
		return Checks;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setattributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @param checks the checks to set
	 */
	public void setChecks(ArrayList<Constraint> checks) {
		Checks = checks;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param primaryKeys the primaryKeys to set
	 */
	public void setPrimaryKeys(ArrayList<Constraint> primaryKeys) {
		PrimaryKeys = primaryKeys;
	}

	/**
	 * @param foreignKey the foreignKey to set
	 */
	public void setForeignKey(ArrayList<Constraint> foreignKey) {
		ForeignKey = foreignKey;
	}
	
	public void renameRefIdFK(String oldName, String new_name)
	{
		for (Constraint i: this.ForeignKey)
			if (i.getId_ref().equals(oldName))
				i.setId_ref(new_name);
	}

	public boolean hasAttribute(String id)
	{
		boolean flag = false;
		
		for (Attribute i : attributes)
			if (i.getId().equals(id))
			{
				flag = true;
				break;
			}
		
		if (!flag)
			for (String name : this.getOthersIds())
				if (id.equals(name))
				{
					flag=true;
					break;
				}
		
		return flag;
	}
	
	public Attribute getID(String id)
	{
		Attribute atr = null;
		
		for (Attribute i : attributes)
		{
			if (i.getId().equals(id))
			{
				atr = i;
			}
		}
		
		if (atr ==null)
		{
			for (String name : this.getOthersIds())
			{
				//System.out.println(name);
				if (name.equals(id))
				{
					//System.out.println("Table: si lo encountro");
					int index = this.getOthersIds().indexOf(name);
					atr = this.attributes.get(index);
				}
			}
		}
		
		return atr;
	}
	
	public Constraint getConstraint(String id)
	{
		Constraint res = new Constraint();
		int count= 0;
		
		for (Constraint i: this.PrimaryKeys)
			if (i.getId().equals(id))
			{
				res = i;
				count++;
				break;
			}
		
		if (count== 0)
		{
			for (Constraint i: this.ForeignKey)
				if (i.getId().equals(id))
				{
					res = i;
					count++;
					break;
				}
			
			if (count== 0)
			{
				for (Constraint i: this.Checks)
					if (i.getId().equals(id))
					{
						res = i;
						count++;
						break;
					}				
			}
		}	
		
		return res;
	}
	
	/*
	 * return false si ya existe un Attribute con el mismo nombre 
	 */
	public boolean canAddAttribute(Attribute a)
	{		
		int count= 0;
		
		for(Attribute i: this.attributes)
			if (i.getId().equals(a.getId()))
			{
				count++;
				break;
			}
		
		return (count== 0);
	}
	
	public void addAttribute(Attribute a)
	{
		this.attributes.add(a);
		for (ArrayList<String> tupla: data){
			if (tupla.size()==attributes.size()-1){
				tupla.add("null");
			}
		}
	}
	
	public void deleteAttribute(String id)
	{
		int index = -1;
		int count= 0;
		
		for (Attribute i: this.attributes)
		{
			if (i.getId().equals(id))
			{
				index = count;
				break;
			}
			count++;
		}
		
		if (index != -1)
			for (ArrayList<String> tupla: data){
				if (tupla.size()>index){
					tupla.remove(index);
				}
			}
			this.attributes.remove(index);
	}
	
	public void deleteConstraint(Constraint c)
	{
		switch (c.gettype())
		{
			case "Primary Key":
				this.PrimaryKeys.remove(0);
				break;
			case "Foreign Key":
				int index = -1;
				int count= 0;
				for (Constraint i: this.ForeignKey)
				{
					if (i.getId().equals(c.getId()))
					{
						index = count;
						break;
					}
					count++;
				}
				if (index != -1)
					this.ForeignKey.remove(index);
						
				break;
			case "Check":
				index = -1;
				count= 0;
				for (Constraint i: this.Checks)
				{
					if (i.getId().equals(c.getId()))
					{
						index = count;
						break;
					}
					count++;
				}
				if (index != -1)
					this.Checks.remove(index);
						
				break;
		}
	}
	
	/*
	 * return false si ya existe una constraint con el mismo nombre 
	 */
	public boolean canAddConstraint(Constraint c)
	{
		int count= 0;
		
		for (Constraint i: this.PrimaryKeys)
			if (i.getId().equals(c.getId()))
			{
				count++;
				break;
			}
		
		if (count== 0)
		{
			for (Constraint i: this.ForeignKey)
				if (i.getId().equals(c.getId()))
				{
					count++;
					break;
				}
			
			if (count== 0)
			{
				for (Constraint i: this.Checks)
					if (i.getId().equals(c.getId()))
					{
						count++;
						break;
					}				
			}
		}	
		
		return (count== 0);
	}
	
	public void addConstraint(Constraint c)
	{
		switch (c.gettype())
		{
			case "Primary Key":
				this.PrimaryKeys.add(c);
				break;
			case "Foreign Key":
				this.ForeignKey.add(c);
				break;
			case "Check":
				this.Checks.add(c);
				break;
		}
	}
	
	/*
	 * return true if exist the Constraint
	 */
	public boolean existeConstraint(String c)
	{
		int count= 0;
		
		for (Constraint i: this.PrimaryKeys)
			if (i.getId().equals(c))
			{
				count++;
				break;
			}
		
		if (count== 0)
		{
			for (Constraint i: this.ForeignKey)
				if (i.getId().equals(c))
				{
					count++;
					break;
				}
			
			if (count== 0)
			{
				for (Constraint i: this.Checks)
					if (i.getId().equals(c))
					{
						count++;
						break;
					}				
			}
		}	
		
		return (count> 0);
	}
	
	public void addData(ArrayList<String> data){
		this.data.add(data);
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return data;
	}

	public void setData(ArrayList<ArrayList<String>> data) {
		this.data = data;
	}

	public String toString()
	{
		String result = "Table " + this.name + "\n";
		int count= 0;
		result += "\tattributes: ";
		for (Attribute i: this.attributes)
		{
			if (count< this.attributes.size() - 1)
				result += i.toString() + ", ";
			else
				result += i.toString();
			count++;
		}
		result += "\n";
		count= 0;
		result += "\tPrimary Keys: ";
		for (Constraint i: this.PrimaryKeys)
		{
			if (count< this.PrimaryKeys.size() - 1)
				result += i.toString() + ", ";
			else
				result += i.toString();
			count++;
		}
		result += "\n";
		count= 0;
		result += "\tForeign Keys: ";
		for (Constraint i: this.ForeignKey)
		{
			if (count< this.ForeignKey.size() - 1)
				result += i.toString() + ", ";
			else
				result += i.toString();
			count++;
		}
		result += "\n";
		count= 0;
		result += "\tCheck: ";
		for (Constraint i: this.Checks)
		{
			if (count< this.Checks.size() - 1)
				result += i.toString() + ", ";
			else
				result += i.toString();
			count++;
		}
		return result;
	}
	
	public boolean isAmbiguous(String id){
		int counter = 0;
		for (String st: getOthersIds()){
			if (st.equals(id))
				counter ++;
		}
		if (counter>1) return true;
		
		for (Attribute at: attributes){
			if (at.getId().equals(id))
				counter ++;
		}
		
		if (counter > 1) return true;
		
		return false;
		
	}
	
	/**
	 * agrega a othersIds los nombres de tal forma que sea:
	 * nombresultabla.nombreAttribute al momento de hacer el select
	 */
	public void setNamesByTable(){
		ArrayList<String> st = new ArrayList();
		for (Attribute at: attributes)
			st.add(getName()+"."+at.getId());
		this.setOthersIds(st);
	}

	public ArrayList<String> getOthersIds() {
		return othersIds;
	}

	public void setOthersIds(ArrayList<String> othersIds) {
		this.othersIds = othersIds;
	}
	
	public String IDtoString(String id)
	{
		String result = this.getID(id).toString() + "\n"; // id y type
		// Verificar si esta en alguna constraint
		// PK
		if (! this.PrimaryKeys.isEmpty())
			for (String i: this.PrimaryKeys.get(0).getIDS_local())
				if (i.equals(id))
					result += PrimaryKeys.toString() + "\n";
		// FK
		for (Constraint i: this.ForeignKey)
			for (String j: i.getIDS_local())
				if (j.equals(id))
					result += i.toString() + "\n";
		// CHK
		for (Constraint i: this.Checks)
			for (String j: i.getIDS_local())
				if (j.equals(id))
					result += i.toString() + "\n";
		//System.out.println("esto es result "+result);
		return result;
	}
	
	public ArrayList<String> dataColumnI(int pos)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		if (! this.data.isEmpty())
			if (pos < this.data.get(0).size())
			{
				for (int i = 0; i < this.data.size(); i++)
					result.add(this.data.get(i).get(pos));
			}
		
		return result;
	}
	
	public ArrayList<String> dataColumnIWithIndexs(int pos, ArrayList<Integer> indexes)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		if (! this.data.isEmpty())
			if (pos < this.data.get(0).size())
			{
				for (Integer i: indexes)
					result.add(this.data.get(i).get(pos));
			}
		
		return result;
	}

}

