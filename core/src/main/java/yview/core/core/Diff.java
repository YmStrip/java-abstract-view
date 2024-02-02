package yview.core.core;

import java.util.ArrayList;
import java.util.HashMap;

public class Diff {
	
	public static boolean isSameNode(VNode oldNode, VNode newNode) {
		return
			oldNode == newNode
				||
				oldNode.diffKey.equals(newNode.diffKey)
					&&
					oldNode.tagName.equals(newNode.tagName)
					&&
					oldNode.isText == newNode.isText;
		
	}
	
	public static void patch(VNode oldNode, VNode newNode) {
		var el = oldNode.el;
		if (oldNode.isText && newNode.isText) {
			//更新文本节点
			el.empty();
			el.setText(newNode.text);
			return;
		}
		if (isSameNode(oldNode, newNode)) return;
		//新老子节点
		var oldCh = oldNode.children;
		var newCh = newNode.children;
		//如果都有child,那么diff
		if (!oldCh.isEmpty() && !newCh.isEmpty()) {
			diff(oldNode, oldCh, newCh);
			return;
		}
		//旧节点没有,新的有
		if (!newCh.isEmpty()) {
			oldNode.el.empty();
			//添加新节点
			newNode.create();
			oldNode.el.addChild(newNode.el);
			return;
		}
		if (!oldCh.isEmpty()) {
			//清理老节点的dom内容
			oldNode.el.empty();
		}
	}
	
	public static int hashTime = 0;
	
	public static String hash() {
		++hashTime;
		return Integer.toHexString(hashTime);
	}
	
	//暴力diff
	public static void diff(VNode parent, ArrayList<VNode> oldCh, ArrayList<VNode> newCh) {
		var time = System.currentTimeMillis();
		var diffKeys = new HashMap<String, VNode>();
		for (VNode n : oldCh) {
			diffKeys.put(n.diffKey, n);
		}
		for (var i = 0; i < newCh.size(); ++i) {
			var n = newCh.get(i);
			var dn = diffKeys.get(n.diffKey);
			if (dn == null) {
				n.create();
				n.diffIndex = i;
				n.diffTime = time;
				diffKeys.put(n.diffKey, n);
				parent.el.setChild(i, n.el);
				continue;
			}
			if (i != dn.diffIndex) {
				dn.diffIndex = i;
				parent.el.setChild(i, dn.el);
			}
			n.diffTime = time;
			dn.diffTime = time;
			patch(dn, n);
		}
		for (var i : diffKeys.entrySet()) {
			var n = i.getValue();
			if (n.diffTime == time) continue;
			diffKeys.remove(n.diffKey);
			parent.el.removeChild(n.el);
		}
	}
}
