package st.provider.yview.lib.ref;

import java.util.ArrayList;
import java.util.HashMap;

public class Ref<T> {
	
	public Ref(T T) {
		value = T;
	}
	
	public Ref() {
	}
	
	public T value;
	public HashMap<String, Object> object = new HashMap<>();
	public ArrayList<Object> array = new ArrayList<>();
	
	public interface on<T> {
		void get(T T);
	}
	
	
	public ArrayList<on<T>> listenerList = new ArrayList<>();
	public ArrayList<on<T>> onceListenerList = new ArrayList<>();
	
	public Ref<T> on(on<T> on) {
		if (!listenerList.contains(on)) listenerList.add(on);
		return this;
	}
	
	public Ref<T> once(on<T> on) {
		if (!onceListenerList.contains(on)) onceListenerList.add(on);
		return this;
	}
	
	public Ref<T> emitUpdate() {
		for (var i : onceListenerList) {
			i.get(value);
		}
		onceListenerList.clear();
		for (var i : listenerList) {
			i.get(value);
		}
		return this;
	}
	
	public Ref<T> off(on<T> on) {
		listenerList.remove(on);
		onceListenerList.remove(on);
		return this;
	}
	
	public Ref<T> clearListener() {
		listenerList.clear();
		return this;
	}
	
	public Ref<T> set(T v) {
		value = v;
		emitUpdate();
		return this;
	}
	
	public Ref<T> set(String key, T v) {
		object.put(key, v);
		emitUpdate();
		return this;
	}
	
	public Ref<T> set(int key, T v) {
		array.set(key, v);
		emitUpdate();
		return this;
	}
	
	public Ref<T> del(String key) {
		object.remove(key);
		return this;
	}
	
	public Ref<T> del(int key) {
		array.remove(key);
		return this;
	}
	
	public Ref<T> clearArray() {
		array.clear();
		emitUpdate();
		return this;
	}
	
	public Ref<T> clearValue() {
		value = null;
		emitUpdate();
		return this;
	}
	
	public Ref<T> clearObject() {
		object.clear();
		emitUpdate();
		return this;
	}
	
	public Ref<T> object(HashMap<String, Object> obj) {
		if (obj == null) obj = new HashMap<>();
		object = obj;
		emitUpdate();
		return this;
	}
	
	public Ref<T> array(ArrayList<Object> obj) {
		if (obj == null) obj = new ArrayList<>();
		array = obj;
		emitUpdate();
		return this;
	}
	
	public Ref<T> add(Object v) {
		array.add(v);
		emitUpdate();
		return this;
	}
	
	public T get() {
		return value;
	}
	
	public ArrayList<Object> array() {
		return array;
	}
	
	public HashMap<String, Object> object() {
		return object;
	}
	
	@Override
	public String toString() {
		return "ref(" + value + ")";
	}
}
