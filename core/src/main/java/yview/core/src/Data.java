package yview.core.src;

import yview.core.lib.Watch;
import yview.core.ref.Reactive;
import yview.core.ref.Ref;


public class Data {
	public Reactive<Ref<?>> props = new Reactive<>();
	public Watch<?> watches = new Watch<>();
	public Ref<?> slot = new Ref<>();
	public Reactive<Ref<?>> scopedSlots = new Reactive<>();
	public Ref<?> ref = new Ref<>();
	public Ref<Boolean> refInFor = new Ref<>(false);
	
	public Ref<?> prop(String name) {
		var def = props.get(name);
		if (def != null) return (Ref<?>) def;
		def = new Ref<>();
		props.set(name, (Ref<?>) def);
		return (Ref<?>) def;
	}
	
	public Ref<?> prop(String name, Object value) {
		var a = (Ref<Object>) prop(name);
		a.set(value);
		return a;
	}
}
