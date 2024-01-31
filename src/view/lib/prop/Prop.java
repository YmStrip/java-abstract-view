package st.provider.yview.lib.prop;

import st.provider.yview.lib.ref.Ref;
import st.provider.yview.lib.vnode.VNode;

public class Prop<T> extends Ref<T> {
	public String name;
	
	public Prop(String name) {
		this.name = name;
	}
	
	public static Prop prop(String name, VNode<?> parent) {
		return new Prop<>(name).inject(parent);
	}
	
	public Prop(String name, T def) {
		this(name);
		set(def);
	}
	
	public static <T> Prop<T> prop(String name, T def, VNode<?> parent) {
		return new Prop<>(name, def).inject(parent);
	}
	
	public Prop<T> inject(VNode<?> parent) {
		var def = (Prop<T>) parent.$props.get(name);
		if (def!=null) return def;
		on(t -> parent.$updater.update());
		parent.$props.put(name,this);
		return this;
	}
}
