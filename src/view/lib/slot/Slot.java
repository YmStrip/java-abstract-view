package st.provider.yview.lib.slot;

import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import st.provider.yview.lib.template.Template;
import st.provider.yview.lib.ref.Ref;
import st.provider.yview.lib.template.TemplateRenderOption;
import st.provider.yview.lib.vnode.VNode;

import java.util.ArrayList;
import java.util.HashMap;

//slot相当于调用渲染函数,template相当于定义函数
public class Slot extends Ref<Template> {
	
	//该slot名称
	public String name;
	public HashMap<String, Object> emptyData = new HashMap<>();
	
	//注入到组件的周期内
	public Slot put(VNode<?> root) {
		root.$slots.put(name, this);
		return this;
	}
	
	public Slot inject(VNode<?> root) {
		//default
		var def_template = root.$templates.get(name);
		if (def_template == null) return this;
		set(def_template);
		
		return this;
	}
	
	public Slot(String name) {
		this.name = name;
	}
	
	public static Slot slot(String name, VNode<?> parent) {
		var def = parent.$slots.get(name);
		if (def != null) return def.inject(parent);
		return new Slot(name).put(parent).inject(parent);
	}
	
	public Slot() {
		this("default");
	}
	
	public static Slot slot(VNode<?> parent) {
		return Slot.slot("default", parent);
	}
	
	public Slot data(HashMap<String, Object> data) {
		var a = new ArrayList<>();
		a.add(data);
		array(a);
		return this;
	}
	
	public Slot render(SlotRenderOption<?, ?> part) {
		if (value == null || part.data == null || part.el == null || part.call == null) return this;
		var res = value.render(new TemplateRenderOption() {{
			data = part.data;
			parent = part.parent;
			el = part.el;
		}});
		if (res.el == null) return this;
		res.parent.parent = part.parent;
		part.call.get(res.el);
		return this;
	}
	
	public Slot render(VNode<?> parentX, SlotRenderCallback callX) {
		if (value == null) return this;
		return render(new SlotRenderOption<>() {{
			data = emptyData;
			parent = parentX;
			el = parentX.el;
			call = callX;
		}});
	}
	
	public Slot render(VNode<?> parentX) {
		return render(parentX, parentX.el::addChild);
	}
	
	public Slot render(WidgetGroup elX, SlotRenderCallback callX) {
		if (value == null) return this;
		return render(new SlotRenderOption<>() {{
			data = emptyData;
			el = elX;
			call = callX;
		}});
	}
	
	public Slot render(WidgetGroup el) {
		return render(el, el::addChild);
	}
	
	public Slot renderTable(Table el) {
		return render(el, el::add);
	}
	public Slot renderTableX(Table el) {
		return render(el, t -> el.add(t).expandX().fillX());
	}
	
	public Slot renderTable(VNode<Table> parent) {
		return render(parent, parent.el::add);
	}
	
	public Slot renderTableX(VNode<Table> parent) {
		return render(parent, t -> parent.el.add(t).expandX().fillX());
	}
	
	public Slot renderFor(WidgetGroup elX, SlotRenderCallback callX) {
		if (value == null) return this;
		var i = -1;
		for (var d : array) {
			++i;
			int finalI = i;
			render(new SlotRenderOption<>() {{
				index = finalI;
				el = elX;
				data = (HashMap<String, Object>) d;
				call = callX;
			}});
		}
		return this;
	}
	
	public Slot renderFor(WidgetGroup elX) {
		return renderFor(elX, elX::addChild);
	}
	
	public Slot renderForTable(Table elX) {
		return renderFor(elX, elX::add);
	}
	
	public Slot renderForTableX(Table elX) {
		return renderFor(elX, t -> elX.add(t).expandX().fillX());
	}
	
	public Slot renderFor(VNode<?> parentX, SlotRenderCallback callX) {
		if (value == null) return this;
		var i = -1;
		for (var d : array) {
			++i;
			int finalI = i;
			render(new SlotRenderOption<>() {{
				index = finalI;
				parent = parentX;
				el = parentX.el;
				data = (HashMap<String, Object>) d;
				call = callX;
			}});
		}
		return this;
	}
	
	public Slot renderFor(VNode<?> parentX) {
		return renderFor(parentX, parentX.el::addChild);
	}
	
	public Slot renderForTable(VNode<Table> parentX) {
		return renderFor(parentX, parentX.el::add);
	}
	
	public Slot renderForTableX(VNode<Table> parentX) {
		return renderFor(parentX, t -> parentX.el.add(t).expandX().fillX());
	}
	
	@Override
	public String toString() {
		return "<slot name=\"" + name + "\">" + super.toString() + "</slot>";
	}
}
