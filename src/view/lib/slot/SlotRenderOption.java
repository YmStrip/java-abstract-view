package st.provider.yview.lib.slot;

import arc.scene.ui.layout.WidgetGroup;
import st.provider.yview.lib.vnode.VNode;

import java.util.HashMap;

public class SlotRenderOption<A extends VNode<?>, B extends WidgetGroup> {
	public int index = 0;
	public A parent;
	public B el;
	public HashMap<String, Object> data;
	public SlotRenderCallback call;
}
