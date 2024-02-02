package yview.core.core;

import java.util.ArrayList;

//真实节点
public class Node {
	public Node(Object el) {
		this(el, null);
	}
	
	public Node(Object el, Node parent) {
		this.el = el;
		parent.addChild(this);
	}
	
	public int index() {
		if (parent == null) return -1;
		return parent.child.indexOf(this);
	}
	
	public Node parent;
	public ArrayList<Node> child;
	public String name;
	public View view;
	public Object el;
	
	//[抽象层]
	public int getChildLength() {
		return child.size();
	}
	
	public Node insertChild(int index, Node e) {
		if (index >= child.size()) index = child.size() - 1;
		if (index < 0) index = 0;
		view.insertChild(el, index, e);
		child.add(index, e);
		return this;
	}
	
	public Node removeChild(int index) {
		if (child.isEmpty()) return this;
		if (index >= child.size()) index = child.size() - 1;
		if (index < 0) index = 0;
		child.remove(index);
		view.delChild(el, index);
		return this;
	}
	
	public Node removeChild(Node e) {
		var index = child.indexOf(e);
		if (index < 0) return this;
		child.remove(e);
		view.delChild(el, index);
		return this;
	}
	
	public Node addChild(Node e) {
		view.insertChild(el, child.size() - 1, e);
		child.add(e);
		return this;
	}
	
	public Node setChild(int index, Node e) {
		child.set(index, e);
		view.insertChild(el, index, e.el);
		view.delChild(el, index + 1);
		return this;
	}
	
	public Node getChild(int index) {
		if (child.isEmpty()) return null;
		if (index >= child.size()) return child.get(child.size() - 1);
		if (index < 0) return child.get(0);
		return child.get(index);
	}
	
	public Node parent() {
		return parent;
	}
	
	public Node delChild(int index) {
		if (child.isEmpty()) return this;
		if (index>=child.size()) index = child.size()-1;
		if (index<0) index = 0;
		child.remove(index);
		view.delChild(el, index);
		return this;
	}
	
	public Node empty() {
		for (var i = getChildLength() - 1; i >= 0; --i) view.delChild(el, i);
		child.clear();
		return this;
	}
	
	public boolean isText = false;
	
	//设置文本,仅对文本节点
	public Object setText(String text) {
		if (isText) view.setElText(el, text);
		return this;
	}
	
	//[创建节点] node包含子节点数据,但是只应该创建该节点
	public Object create(VNode node) {
		return view.el();
	}
}
