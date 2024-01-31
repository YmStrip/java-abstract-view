package st.provider.yview.lib.template;

import arc.scene.ui.layout.WidgetGroup;
import st.provider.yview.lib.vnode.VNode;

import java.util.HashMap;

//渲染template的数据
public class TemplateRenderOption {
	public HashMap<String, Object> data;
	public VNode<?> parent;
	public int index = 0;
	public WidgetGroup el;
}
