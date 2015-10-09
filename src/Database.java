import java.util.*;
public class Database {
	private HashMap<String,String> table;
	private HashMap<String,Integer> count;
	public Database(){
		table = new HashMap<String,String>();
		count = new HashMap<String,Integer>();
	}
	public Database(Database oldDB){
		 this.table = new HashMap<String,String>(oldDB.returnTable());
		 this.count = new HashMap<String,Integer>(oldDB.returnCount());
		 return;
	}
	public HashMap<String,String> returnTable(){
		return this.table;
	}
	public HashMap<String,Integer> returnCount(){
		return this.count;
	}
	public String get(String key){
		if(table.containsKey(key)){
			return table.get(key);
		}else{
			return "NULL";
		}
	}
	public void set(String key, String value){
		if(table.containsKey(key)){
			//the old key got replaced, so is its count
			String previousVal = table.get(key);
			count.put(previousVal,count.get(previousVal)-1);
			table.put(key, value);
			if(count.containsKey(value)){
				count.put(value, count.get(value) + 1);
			}else{
				count.put(value, 1);
			}
		}else{
			table.put(key, value);
			if(count.containsKey(value)){
				count.put(value, count.get(value) + 1);
			}else{
				count.put(value, 1);
			}
		}
		
	}
	public void unset(String key){
		if(table.containsKey(key)){
			String previousVal = table.get(key);
			count.put(previousVal,count.get(previousVal)-1);
			table.remove(key);
		}
	}
	public int numEqualTo(String val){
		if(!count.containsKey(val)){
			return 0;
		}else{
			return count.get(val);
		}
	}
	
	
	
	

}
