package yview.core.lib;

import yview.core.src.Node;
import yview.core.src.VNode;

import java.util.ArrayList;
import java.util.HashMap;

public class Diff {
	
	public static boolean isSameNode(VNode oldNode, VNode newNode) {
		return
			oldNode == newNode
				||
				oldNode.diffKey.equals(newNode.diffKey)
					&&
					oldNode.tag.equals(newNode.tag)
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
		//新老子节点
		var oldCh = oldNode.children;
		var newCh = newNode.children;
		//如果都有child,那么diff
		if (!oldCh.isEmpty() && !newCh.isEmpty()) {
			diff(oldNode, newCh);
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
	public static void diff(VNode parent, ArrayList<VNode> newCh) {
		var time = System.currentTimeMillis();
		var diffKeys = new HashMap<String, VNode>();
		//keys
		for (var i = parent.el.child.size() - 1; i >= 0; --i) {
			var n = parent.el.child.get(i);
			VNode node = (VNode) n.attributes.get("v-node");
			if (node == null) {
				parent.el.removeChild(n);
				continue;
			}
			diffKeys.put(node.diffKey, node);
		}
		for (var i = 0; i < newCh.size(); ++i) {
			var n = newCh.get(i);
			var dn = diffKeys.get(n.diffKey);
			//create
			if (dn == null) {
				//System.out.println("add" + n.tag+" key:"+n.diffKey+" index:"+i);
				n.create();
				n.diffIndex = i;
				n.diffTime = time;
				diffKeys.put(n.diffKey, n);
				parent.el.setChild(i, n.el);
				continue;
			}
			//move
			if (i != dn.diffIndex) {
				//System.out.println(dn.tag + " key:" + dn.diffKey + " oldIndex:" + dn.diffIndex + " newIndex:" + i);
				dn.diffIndex = i;
				parent.el.setChild(i, dn.el);
			}
			n.diffTime = time;
			dn.diffTime = time;
			patch(dn, n);
		}
		//delete
		for (var i : diffKeys.entrySet()) {
			var n = i.getValue();
			if (n.diffTime == time) continue;
			//System.out.println("del:"+n.tag);
			parent.el.removeChild(n.el);
		}
	}
}
