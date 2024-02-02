package yview.core.lib;

import yview.core.interfaces.Prov;

import java.util.ArrayList;

public class CallList<T> {
	public ArrayList<Prov<T>> callList = new ArrayList<>();
	public ArrayList<Prov<T>> callListOnce = new ArrayList<>();
	
	public CallList<T> clear() {
		callList.clear();
		return this;
	}
	
	public CallList<T> on(Prov<T> on) {
		if (!callList.contains(on)) callList.add(on);
		return this;
	}
	
	public CallList<T> once(Prov<T> once) {
		if (!callListOnce.contains(once)) callListOnce.add(once);
		return this;
	}
	
	public CallList<T> off(Prov<T> on) {
		callList.remove(on);
		callListOnce.remove(on);
		return this;
	}
	
	public CallList<T> emit(T T) {
		for (var i : callListOnce) i.get(T);
		callListOnce.clear();
		for (var i : callList) i.get(T);
		return this;
	}
}
