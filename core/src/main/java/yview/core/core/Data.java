package yview.core.core;

public class Data {
	
	public Reactive props = new Reactive();
	//数据 Record<Event>
	public Reactive events = new Reactive() {{
		on(t -> {
		
		});
	}};
	//数据 Record<Watcher>
	public Reactive watches = new Reactive() {{
		on(t -> {
		
		});
	}};
	public Ref<?> prop(String name) {
		var def = props.get(name);
		if (def != null) return (Ref<?>) def;
		def = new Ref<>();
		props.set(name, def);
		return (Ref<?>) def;
	}
	
	public Ref<?> prop(String name, Object value) {
		var a = (Ref<Object>) prop(name);
		a.set(value);
		return a;
	}
}
