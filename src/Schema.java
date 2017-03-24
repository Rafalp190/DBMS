import java.io.Serializable;
import java.util.ArrayList;


public class Schema implements Serializable {

	private ArrayList<DataBase> dataBases;

	public Schema() {
		this.dataBases = new ArrayList<DataBase>();
	}

	/**
	 * @return the dataBases
	 */
	public ArrayList<DataBase> getSchema() {
		return dataBases;
	}

	/**
	 * @param dataBases the dataBases to set
	 */
	public void setSchema(ArrayList<DataBase> dataBases) {
		this.dataBases = dataBases;
	}
	
	public void addDataBase(DataBase b)
	{
		this.dataBases.add(b);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "Schema:\n";
		int cont = 1;
		for (DataBase i: this.dataBases)
		{
			res += "\tDB#" + Integer.toString(cont) + " " + i.toString() + "\n";
			cont++;
		}
		return res;
	}	
	
}
