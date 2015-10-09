
public class Log {
	private int action;
	private String key;
	private String value;
	//log type
	//1. Log("SET",key,value);
	//2. Log("UNSET",key);
	//3. Log("SAVEPOINT");
	public Log(int action){
		this.action = action;
	}
	public Log(int action, String key){
		this.action = action;
		this.key = key;
	}
	public int getAction(){
		return this.action;
	}
	public String getKey(){
		return this.key;
	}
	public String getValue(){
		return this.value;
	}
	public Log(int action, String key, String value){
		this.action = action;
		this.key = key;
		this.value = value;
	}
}
