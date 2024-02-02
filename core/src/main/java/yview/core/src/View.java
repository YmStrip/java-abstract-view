package yview.core.src;

import yview.core.util.ColorType;
import yview.core.interfaces.Prov;

import java.util.HashMap;

//一个View实例
public abstract class View<T extends Object> {
	public View() {
	}
	public View(String name) {
		this.name = name;
	}
	public String name = "view";
	public String description = "";
	public ViewConfig config = new ViewConfig();
	public HashMap<String, Prov<VNode>> components = new HashMap<>();
	public abstract void insertChild(T parent, int index, T child);
	public abstract T createElementByName(String name);
	public T element() {
		return createElementByName("div");
	}
	public T createTextElement() {
		return createElementByName("text");
	}
	public abstract void setElementText(T el, String text);
	public abstract String getElementText(T el);
	
	public abstract void removeElement(T parent);
	public abstract boolean isElement(T parent);
	public abstract T parent(T el);
	public abstract T getChild(T parent, int index);
	public abstract void removeChild(T parent, int index);
	public abstract int childLength(T parent);
	//基础信息获取
	public abstract float windowWidth();
	public abstract float windowHeight();
	public abstract float windowX();
	public abstract float windowY();
	public abstract void setElColor(T parent, ColorType color);
	public abstract void setElWidth(T parent, float w);
	public abstract void setElHeight(T parent, float h);
	public abstract void setElX(T parent, float x);
	public abstract void setElY(T parent, float y);
	public abstract void setElBackcolor(T parent, ColorType color);
	public abstract void setElBackground(T parent, String name);
	public void prompt() {
		prompt(name, "", "", s -> {
		});
	}
	public void prompt(Prov<String> p) {
		prompt(name, "", "", p);
	}
	public void prompt(String data, Prov<String> p) {
		prompt(name, data, "", p);
	}
	public void prompt(String data) {
		prompt(name, data, "", s -> {
		});
	}
	public void prompt(String data, String def, Prov<String> p) {
		prompt(name, data, def, p);
	}
	public void prompt(String data, String def) {
		prompt(name, data, def, s -> {
		});
	}
	public abstract void alert(String title, String content);
	public void alert() {
		alert(name);
	}
	public void confirm() {
		confirm(name, "", e -> {
		});
	}
	public void confirm(String context) {
		confirm(name, context, e -> {
		});
	}
	public void confirm(Prov<Boolean> p) {
		confirm(name, "", p);
	}
	public void confirm(String context, Prov<Boolean> p) {
		confirm(name, context, p);
	}
	public void alert(String code) {
		alert(name, code);
	}
	public abstract void prompt(String title, String content, String def, Prov<String> result);
	public abstract void confirm(String title, String content, Prov<Boolean> result);
	@Override
	public String toString() {
		return String.format("App(" + name + ")");
	}
	public Node createNodeByName(String tag) {
		var node = new Node(this, element());
		node.tag = tag;
		return node;
	}
	public Node createNodeByName(String tag, Node parent) {
		var node = createNodeByName(tag);
		parent.addChild(node);
		return node;
	}
	public VNode node;
	public View<T> node(VNode node) {
		this.node = node;
		return this;
	}
	//挂载到新节点
	public VNode mount() {
		return mount(new Node(View.this, element()));
	}
	//挂载到节点
	public VNode mount(Node parent) {
		if (node == null) return null;
		node.patch(new VNode(this, new Node(this, element(), parent)));
		return node;
	}
}
