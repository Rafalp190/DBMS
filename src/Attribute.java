import java.io.Serializable;


/**
 * @author Rafa
 *
 */
public class Attribute implements Serializable{
	
	private String id;
	private String type;
	private int size;
	private boolean check;
	
	public Attribute()
	{
		this.id = "";
		this.type = "";
		this.size = -1;
		this.check = false;
	}
	
	public Attribute(String id) {		
		this.id = id;
		this.type = "";
		this.size = -1;
		this.check = false;
	}
	
	public Attribute(String id, String type) {		
		this.id = id;
		this.type = type;
		this.size = -1;
		this.check = false;
	}

	public Attribute(String id, String type, int size) {		
		this.id = id;
		this.type = type;
		this.size = size;
		this.check = false;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public String gettype() {
		return type;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the check
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param type the type to set
	 */
	public void settype(String type) {
		this.type = type;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(boolean check) {
		this.check = check;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = id + " " + type;
		if (this.size >= 0 )
			ret = id + " " + type + "[" + Integer.toString(this.size) + "]";
		return ret;
	}	
	
	

