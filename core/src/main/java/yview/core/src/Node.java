package yview.core.src;

import yview.core.util.NodeTree;

import java.util.ArrayList;
import java.util.HashMap;

//真实节点
public class Node {
	public Node(View<?> view) {
		this(view, view.element());
	}
	public Node(View<?> view, Object el) {
		this.view = view;
		this.el = el;
	}
	public Node(View<?> view, Object el, Node parent) {
		this(view, el);
		parent.addChild(this);
	}
	
	public int index() {
		if (parent == null) return -1;
		return parent.child.indexOf(this);
	}
	public String tag = "div";
	public Node parent;
	public ArrayList<Node> child = new ArrayList<>();
	public View view;
	public Object el;
	public HashMap<String, Object> attributes = new HashMap<>();
	//[抽象层]
	public int getChildLength() {
		return child.size();
	}
	
	public Node insertChild(int index, Node e) {
		e.parent = this;
		if (index >= child.size()) index = child.size() - 1;
		if (index < 0) index = 0;
		view.insertChild(el, index, e.el);
		child.add(index, e);
		return this;
	}
	
	public Node removeChild(int index) {
		if (child.isEmpty()) return this;
		if (index >= child.size()) index = child.size() - 1;
		if (index < 0) index = 0;
		child.remove(index);
		view.removeChild(el, index);
		return this;
	}
	
	public Node removeChild(Node e) {
		var index = child.indexOf(e);
		if (index < 0) return this;
		child.remove(e);
		view.removeChild(el, index);
		return this;
	}
	
	public Node addChild(Node e) {
		e.parent = this;
		child.add(e);
		view.insertChild(el, Math.max(child.size() - 1, 0), e.el);
		return this;
	}
	
	public Node setChild(int index, Node e) {
		if (index<0) index = 0;
		if (index >= child.size()) {
			return addChild(e);
		}
		e.parent = this;
		child.set(index,e);
		view.insertChild(el, index, e.el);
		view.removeChild(el, index + 1);
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
	public Node parent(Node parent) {
		this.parent = parent;
		return this;
	}
	public Node delChild(int index) {
		if (child.isEmpty()) return this;
		if (index >= child.size()) index = child.size() - 1;
		if (index < 0) index = 0;
		child.remove(index);
		view.removeChild(el, index);
		return this;
	}
	
	public Node empty() {
		for (var i = getChildLength() - 1; i >= 0; --i) view.removeChild(el, i);
		child.clear();
		return this;
	}
	
	public boolean isText = false;
	
	//设置文本,仅对文本节点
	public Object setText(String text) {
		if (isText) view.setElementText(el, text);
		return this;
	}
	
	//[创建节点] node包含子节点数据,但是只应该创建该节点
	public Object create(VNode node) {
		return view.element();
	}
	@Override
	public String toString() {
		return NodeTree.nodeTree(this);
	}
}
