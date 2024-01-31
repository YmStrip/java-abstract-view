package st.provider.yview.lib.template;

import st.provider.yview.lib.vnode.VNode;

public class Template {
	
	public VNode<?> node;
	
	public Template(String name) {
		this.name = name;
	}
	
	public Template(String name, VNode<?> parent) {
		this.name = name;
		inject(parent);
	}
	
	public Template() {
		this("default");
	}
	
	public Template(VNode<?> parent) {
		this();
		inject(parent);
	}
	
	public Template inject(VNode<?> parent) {
		parent.$templates.put(name, this);
		var slot = parent.slot(name);
		slot.set(this);
		return this;
	}
	
	//名称
	public String name;
	
	//提供
	public TemplateRenderResult render(TemplateRenderOption data) {
		return null;
	}
	
	@Override
	public String toString() {
		return "<template v-slot:name=\""+name+"\">...</template>";
	}
}
