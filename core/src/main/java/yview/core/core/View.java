package yview.core.core;

import yview.core.util.ColorType;
import yview.core.interfaces.Prov;

import java.util.HashMap;

//一个View实例
public abstract class View {
	public String href = "view";
	
	public View() {
	}
	
	public View(String name) {
		this.name = name;
	}
	
	public String name = "";
	public String description = "";
	//组件
	public HashMap<String, Prov<VNode>> components = new HashMap<>();
	
	//基础信息获取
	public abstract float windowWidth();
	
	public abstract float windowHeight();
	
	public abstract float windowX();
	
	public abstract float windowY();
	
	//基本元素
	public abstract Object el();
	
	//检验是否为元素
	public abstract boolean isEl(Object parent);
	
	public abstract Object stringEl(Object parent);
	
	public abstract void setElColor(Object parent, ColorType color);
	
	public abstract void setElWidth(Object parent, float w);
	
	public abstract void setElHeight(Object parent, float h);
	
	public abstract void setElX(Object parent, float x);
	
	public abstract void setElY(Object parent, float y);
	
	public abstract void delEl(Object parent);
	
	public abstract void setElBackcolor(Object parent, ColorType color);
	
	public abstract void setElBackground(Object parent, String name);
	
	public abstract void insertChild(Object parent, int index, Object child);
	
	public abstract Object parent(Object object);
	
	public abstract Object getChild(Object parent, int index);
	
	public abstract void delChild(Object parent, int index);
	
	public abstract int childLength(Object parent);
	
	public void prompt() {
		prompt(href, "", "", s -> {
		});
	}
	
	public void prompt(Prov<String> p) {
		prompt(href, "", "", p);
	}
	
	public void prompt(String data, Prov<String> p) {
		prompt(href, data, "", p);
	}
	
	public void prompt(String data) {
		prompt(href, data, "", s -> {
		});
	}
	
	public void prompt(String data, String def, Prov<String> p) {
		prompt(href, data, def, p);
	}
	
	public void prompt(String data, String def) {
		prompt(href, data, def, s -> {
		});
	}
	
	public abstract void alert(String title, String content);
	
	public void alert() {
		alert(href);
	}
	
	public void confirm() {
		confirm(href, "", e -> {
		});
	}
	
	public void confirm(String context) {
		confirm(href, context, e -> {
		});
	}
	
	public void confirm(Prov<Boolean> p) {
		confirm(href, "", p);
	}
	
	public void confirm(String context, Prov<Boolean> p) {
		confirm(href, context, p);
	}
	
	public void alert(String code) {
		alert(href, code);
	}
	
	public abstract void prompt(String title, String content, String def, Prov<String> result);
	
	public abstract void confirm(String title, String content, Prov<Boolean> result);
	
	public abstract Object textEl();
	
	public abstract void setElText(Object el, String text);
	public abstract String getElText(Object el);
	@Override
	public String toString() {
		return String.format("%s\n<App name=\"%s\"></App>", description, name);
	}
	
	public Node createEl(String tag) {
		return new Node(el());
	}
	
	public Node createEl(String tag, Node parent) {
		return new Node(el(), parent);
	}
	
	public VNode createApp(VNode node) {
		return null;
	}
}
