import java.util.ArrayList;import java.util.Comparator;


public class DatetimeComparable implements Comparator<ArrayList<String>> {

	@Override
	public int compare(ArrayList<String> tupla1, ArrayList<String> tupla2) {
		return 0;
	}
	
	public int compareDate(String date1, String date2){
		//System.out.println(date1+" "+date2);
		date1 = date1.substring(1,date1.length()-1);
		date2 = date2.substring(1, date2.length()-1);
		String[] date1A = date1.split("-");
		String[] date2A = date2.split("-");
		
		int a =0, b=0;
		for (int i = 0; i < date1A.length; i++){
			a = Integer.parseInt(date1A[i]);
			b = Integer.parseInt(date2A[i]);
			//System.out.println("a:"+a+" ,b:"+b);
			if ( a != b) return a-b;
		}
		return a-b;
	}
	
	public int compareNum(String val1, String val2){
		Double a = Double.parseDouble(val1);
		Double b = Double.parseDouble(val2);
		return a.compareTo(b);
	}

}
