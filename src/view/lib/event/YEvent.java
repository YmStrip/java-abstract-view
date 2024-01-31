package st.provider.yview.lib.event;

import java.util.HashMap;

public class YEvent<T extends YEvent> {
	
	public YEvent(String name) {
		this.name = name;
	}
	
	public String name;
	public HashMap<String, Boolean> tags = new HashMap<>();
	
	//本event,检查目标没有该tag
	public boolean tag(YEvent e) {
		for (var i : tags.entrySet()) {
			var h = e.tags.get(i.getKey());
			if (!(h instanceof Boolean b) || !b) return false;
		}
		return true;
	}
	
	public T tag(String name) {
		tags.put(name, true);
		return (T) this;
	}
	
	public EventCallback<T> call = (e) -> {
	};
	
	public T call(EventCallback<T> c) {
		call = c;
		return (T) this;
	}
	
	public boolean bubble = false;
	public boolean stop = false;
	
	public T stop() {
		stop = true;
		return (T) this;
	}
	
	public T bubble() {
		this.bubble = true;
		return (T) this;
	}
	
}
