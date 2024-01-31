package st.provider.yview.lib.template;

import st.provider.yview.lib.vnode.VNode;

public interface TemplateProvNode {
	VNode<?> get(TemplateRenderOption d);
}
