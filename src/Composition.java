/**
 * @author Rafa
 *
 */
public class Composition {
	private Attribute id;
	private String op;
	private String value;
	private String type;
	private boolean withCol;
	private Attribute Col;
	
	
	
	public Composition() {
		id = new Attribute();
		op = new String();
		value = new String();
		type = new String();
		withCol = false;
		Col = null;
	}

	
	
	public Composition(Attribute id, String op, String value, String type) {
		this.id = id;
		this.op = op;
		this.value = value;
		this.type = type;
		this.withCol = false;
		this.Col = null;
	}

	

	public Composition(Attribute id, String op, Attribute col) {
		this.id = id;
		this.op = op;
		this.Col = col;
		this.withCol = true;
		this.value = null;
		this.type = null;
	}



	public Attribute getId() {
		return id;
	}
	
	public void setId(Attribute id) {
		this.id = id;
	}
	
	public String getOp() {
		return op;
	}
	
	public void setOp(String op) {
		this.op = op;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String gettype() {
		return type;
	}

	public void settype(String type) {
		this.type = type;
	}

	public boolean isWithCol() {
		return withCol;
	}
	
	public void setWithCol(boolean withCol) {
		this.withCol = withCol;
	}
	
	public Attribute getCol() {
		return Col;
	}
	
	public void setCol(Attribute col) {
		Col = col;
	}
}

