package Strategy;

import java.util.ArrayList;

import allocation.Allocation;
import users.User;

public class Strategy<T> {
	
	public ArrayList<T> add(ArrayList<T> a, T t){
		if(a == null) 
			a = new ArrayList<T>();
		a.add(t);
		return a;
	}
	
	public Object search (ArrayList<T> a, String key) {
		T found = null;
		if(a!=null) {
			for (T t : a) {
				try {	
					
					Allocation x = (Allocation) t;
					if(x.getName().equals(key) || x.getCode().equals(key) || x.getName().substring(0, 5).equals(key)) {
						found = (T) x;
						break;
					}
											
				}catch(ClassCastException exception) {
					
					try {
						
						User x = (User) t;
						if(x.getName().equals(key) || x.getID().equals(key)) {
							found = (T) x;
							break;
						}
						
					}catch(ClassCastException excepition) {
						
						String s = (String) t;
						if(s.equals(key)) {
							found = (T) s;
							break;
						}
						
					}
				}			
			}
		}
		return found;
	}	
}
