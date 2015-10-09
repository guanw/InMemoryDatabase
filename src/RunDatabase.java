import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class RunDatabase {
	private static Database db = new Database();
	private static int state = Util.IMMEDIATE_COMMIT; 
	private static Stack<Log> rollbackAction = new Stack<Log>();
	private static void readFromCommandLine() throws IOException{
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()){
			String command = in.nextLine();
			String[] commands = command.split("\\s+");
			if(commands.length == 1){
				if(commands[0].equals("END")){
					return;
				}else{
					parseTransaction(commands);
				}
			}else{
				parseData(commands);
			}
		}
	}
	private static void readFromFile(String filename) throws IOException {
		File file = new File(filename);
		Scanner sc = new Scanner(file);
	    while (sc.hasNext()) {
	    	String command = sc.nextLine();
	    	String[] commands = command.split("\\s+");
			if(commands.length == 1){
				if(commands[0].equals("END")){
					return;
				}else{
					parseTransaction(commands);
				}
			}else{
				parseData(commands);
			}
	    }
	    sc.close();
	}
	private static void parseTransaction(String[] commands) throws IOException{
		String command = commands[0];
		if(command.equals("BEGIN")){
			state = Util.NO_COMMIT_YET;
			processBegin();
		}else if(command.equals("COMMIT")){
			state = Util.IMMEDIATE_COMMIT;
			processCommit();
		}else if(command.equals("ROLLBACK")){
			state = Util.IMMEDIATE_COMMIT;
			processRollback();
		}else{
			throw new IOException("not valid input");
		}
	}
	private static void processRollback() {
		if(rollbackAction.size() == 0){
			System.out.println("NO TRANSACTION");
			return;
		}
		while(rollbackAction.peek().getAction() != Util.SAVEPOINT){
			Log l = rollbackAction.pop();
			if(l.getAction() == Util.SET){
				db.set(l.getKey(), l.getValue());
			}else if(l.getAction() == Util.UNSET){
				db.unset(l.getKey());
			}
		}
//		System.out.println();
		rollbackAction.pop();
		return;
	}
	private static void processCommit() {
		rollbackAction = new Stack<Log>();
//		System.out.println();
		return;
	}
	private static void processBegin() {
		if(state == Util.NO_COMMIT_YET){
			rollbackAction.push(new Log(Util.SAVEPOINT));
		}
//		System.out.println();
		return;
	}
	private static void parseData(String[] commands) throws IOException{
		String command = commands[0];
		if(command.equals("SET")){
			processSet(commands[1],commands[2]);
		}else if(command.equals("GET")){
			processGet(commands[1]);
		}else if(command.equals("UNSET")){
			processUnset(commands[1]);
		}else if(command.equals("NUMEQUALTO")){
			processNumEqualTo(commands[1]);
		}else{
			throw new IOException("Not Valid Input");
		}
	}
	private static void processNumEqualTo(String val) {
		System.out.println(db.numEqualTo(val));
	}
	private static void processUnset(String command) {
		HashMap<String,String> table = db.returnTable();
		if(table.containsKey(command)){
			String previousVal = table.get(command);
			if(state == Util.NO_COMMIT_YET){
				rollbackAction.push(new Log(Util.SET,command,previousVal));
			}
		}
		db.unset(command);
	}
	private static void processGet(String command) {
		System.out.println(db.get(command));
	}
	private static void processSet(String key, String val) {
		HashMap<String,String> table = db.returnTable();
		if(!table.containsKey(key)){
			if(state == Util.NO_COMMIT_YET){
				rollbackAction.push(new Log(Util.UNSET,key));
			}
		}else{
			String previousValue = table.get(key);
			if(state == Util.NO_COMMIT_YET){
				rollbackAction.push(new Log(Util.SET,key,previousValue));
			}
		}
//		System.out.println();
		db.set(key, val);
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length == 1){
			String fileName = args[0];
			readFromFile(fileName);
		}else if(args.length == 0){
			readFromCommandLine();
		}else{
			throw new IOException("You should include zero or one parameter");
		}
	}

}
