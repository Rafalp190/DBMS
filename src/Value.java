
public class Value {
	private String value;
	private String type;
	private int size;
	
	//Constructor without params
	public Value(){
		super();
		value = "";
		type = "";
		this.size = 0;	
	}
	//Constructor without size constraint
	//To be used with non constrainted value types
	public Value(String value, String type){
		super();
		this.value = value;
		this.type = type;
		this.size = 0;
	}
	//Constructor with params
	//To be used with size constrainted values
	public Value(String value, String type, int size){
		super();
		this.value = value;
		this.type = type;
		this.size = size;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String text = this.value + ':' + this.type;
		
		return text;
	}
}
