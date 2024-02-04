import org.junit.jupiter.api.Test;
import yview.core.src.VNode;
import yview.core.src.View;
import yview.core.interfaces.Prov;
import yview.core.util.ColorType;

import java.util.ArrayList;

public class Main {
	//0.5 * 3^(n+1)-3
	public VNode timeTest(View<?> v, int n, int count) {
		var root = new VNode(v);
		return timeTest(v, root, 1, n, count);
	}
	public VNode timeTest(View<?> v, VNode parent, int n, int maxN, int count) {
		if (n > maxN) return parent;
		for (var i = 0; i < count; ++i) {
			var p = new VNode(v);
			parent.child(parent.children.size(), p);
			timeTest(v, p, n + 1, maxN, count);
		}
		return parent;
	}
	public void timeTest(float count, VNode old, VNode newNode) {
		var time = System.currentTimeMillis();
		newNode.patch(old);
		System.out.println("patch node " + count + " use " + (System.currentTimeMillis() - time));
	}
	public void timeTest(View<?> v, int n) {
		var LIST = new ArrayList<VNode>();
		for (var i = 0; i < n; ++i) {
			LIST.add(timeTest(v, i + 1, 2));
		}
		for (var i = 0; i < LIST.size(); ++i) {
			timeTest((float) (0.5 * (Math.pow(2, i + 1) - 2)), v.node, LIST.get(i));
		}
	}
	@Test
	public void test() {
		var view = new TextView();
		view.node(new VNode(view)
			.child("div")
			.child("div", a -> a
				.child("button")
				.child("a")
			)
		);
		var b = view.mount();
		//挂载到真实节点
		System.out.println(b.el);
		//尝试diff-patch
		var ch0 = view.node.child(0);
		ch0.child("newH");
		var ch1 = view.node.child(1);
		ch1.insert(0, "newCh").remove(2);
		ch1.insert(1, "qu", n -> n
			.child("mu")
		)
			.child(0,timeTest(view,2,3));
		view.node.patch(view.node);
		System.out.println(b.el);
		//m =
		//3
		//timeTest(view,16);
	}
	public static class El {
		public String name = "";
		public Boolean isText = false;
		public String text;
		public El(String name) {
			this.name = name;
		}
		public El() {
			this("div");
		}
		public El parent;
		public ArrayList<El> child = new ArrayList<>();
	}
	
	public static class TextView extends View<El> {
		
		@Override
		public void insertChild(El parent, int index, El child) {
			parent.child.add(index, child);
		}
		@Override
		public El createElementByName(String name) {
			return new El();
		}
		@Override
		public El element() {
			return new El();
		}
		@Override
		public void removeElement(El parent) {
		
		}
		@Override
		public boolean isElement(El parent) {
			return true;
		}
		@Override
		public void setElementText(El el, String text) {
			el.text = text;
		}
		@Override
		public String getElementText(El el) {
			return el.text;
		}
		@Override
		public El parent(El el) {
			return el.parent;
		}
		@Override
		public El getChild(El parent, int index) {
			return parent.child.get(index);
		}
		@Override
		public void removeChild(El parent, int index) {
			parent.child.remove(index);
		}
		@Override
		public int childLength(El parent) {
			return parent.child.size();
		}
		@Override
		public float windowWidth() {
			return 0;
		}
		@Override
		public float windowHeight() {
			return 0;
		}
		@Override
		public float windowX() {
			return 0;
		}
		@Override
		public float windowY() {
			return 0;
		}
		@Override
		public void setElColor(El parent, ColorType color) {
		
		}
		@Override
		public void setElWidth(El parent, float w) {
		
		}
		@Override
		public void setElHeight(El parent, float h) {
		
		}
		@Override
		public void setElX(El parent, float x) {
		
		}
		@Override
		public void setElY(El parent, float y) {
		
		}
		@Override
		public void setElBackcolor(El parent, ColorType color) {
		
		}
		@Override
		public void setElBackground(El parent, String name) {
		
		}
		@Override
		public void alert(String title, String content) {
		
		}
		@Override
		public void prompt(String title, String content, String def, Prov<String> result) {
		
		}
		@Override
		public void confirm(String title, String content, Prov<Boolean> result) {
		
		}
	}
}
