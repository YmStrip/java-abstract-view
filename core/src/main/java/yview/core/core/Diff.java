package yview.core.core;

import java.util.ArrayList;

public class Diff {
	public static boolean isSameProps(VNode oldNode, VNode newNode) {
		return false;
	}
	
	public static boolean isSameNode(VNode oldNode, VNode newNode) {
		return
			oldNode.tagName.equals(newNode.tagName)
				&&
				oldNode.isText == newNode.isText
				&&
				isSameNode(oldNode, newNode);
	}
	
	public void patch(VNode oldNode, VNode newNode) {
		if (isSameNode(oldNode, newNode)) {
		
		}
		//
		else {
			var old = oldNode.el;
			var oldParentEl = oldNode.el.parent();
			newNode.create();
			if (oldParentEl != null) {
				//添加新元素
				oldParentEl.insertChild(old.index(), newNode.el);
				oldParentEl.removeChild(old);
			}
		}
	}
	
	public void patchNode(VNode oldNode, VNode newNode) {
		var el = oldNode.el = newNode.el;
		//获取新旧虚拟节点的子节点数组
		var oldCh = oldNode.children;
		var newCh = newNode.children;
		//如果新旧虚拟节点是同一个对象，则终止
		if (oldNode == newNode) return;
		//如果新旧虚拟节点是文本节点
		if (oldNode.isText && newNode.isText) {
			//更新文本节点
			el.setText(newNode.text);
			return;
		}
		//新旧虚拟节点都有子节点，且子节点不一样
		if (!oldCh.isEmpty() && !newCh.isEmpty()) {
			//对比子节点，并更新
			updateChildren(el, oldCh, newCh);
			return;
		}
		//新 has 老 not
		else if (!newCh.isEmpty()) {
			//清理老节点的dom内容
			oldNode.el.empty();
			//添加新节点
			newNode.create();
			oldNode.el.addChild(newNode.el);
		}
		//新 not 老 has
		else if (!oldCh.isEmpty()) {
			//清理老节点的dom内容
			oldNode.el.empty();
		}
		//not do
	}
	
	public void updateChildren(Node parentElm, ArrayList<VNode> oldCh, ArrayList<VNode> newCh) {
		var oldStartIdx = 0;
		var newStartIdx = 0;
		var oldEndIdx = oldCh.size() - 1;
		var oldStartVnode = oldCh.get(0);
		var oldEndVnode = oldCh.get(oldEndIdx);
		var newEndIdx = newCh.size() - 1;
		var newStartVnode = newCh.get(0);
		var newEndVnode = newCh.get(newEndIdx);
		int oldKeyToIdx = -1;
		int idxInOld = -1;
		int elmToMove = -1;
		int before = -1;
		while (oldStartIdx <= oldEndIdx && newStartIdx <= newEndIdx) {
			if (oldStartVnode == null) {
				oldStartVnode = oldCh.get(++oldStartIdx);
			} else if (oldEndVnode == null) {
				oldEndVnode = oldCh.get(--oldEndIdx);
			} else if (newStartVnode == null) {
				newStartVnode = newCh.get(++newStartIdx);
			} else if (newEndVnode == null) {
				newEndVnode = newCh.get(--newEndIdx);
			} else if (isSameNode(oldStartVnode, newStartVnode)) {
				patchNode(oldStartVnode, newStartVnode);
				oldStartVnode = oldCh.get(++oldStartIdx);
				newStartVnode = newCh.get(++newStartIdx);
			} else if (isSameNode(oldEndVnode, newEndVnode)) {
				patchNode(oldEndVnode, newEndVnode);
				oldEndVnode = oldCh.get(--oldEndIdx);
				newEndVnode = newCh.get(--newEndIdx);
			} else if (isSameNode(oldStartVnode, newEndVnode)) {
				patchNode(oldStartVnode, newEndVnode);
				parentElm.insertChild(oldEndVnode.el.index(), oldStartVnode.el);
				//api.insertBefore(parentElm, oldStartVnode.el, api.nextSibling(oldEndVnode.el))
				oldStartVnode = oldCh.get(++oldStartIdx);
				newEndVnode = newCh.get(--newEndIdx);
			} else if (isSameNode(oldEndVnode, newStartVnode)) {
				patchNode(oldEndVnode, newStartVnode);
				parentElm.insertChild(oldStartVnode.el.index() - 1, oldEndVnode.el);
				//api.insertBefore(parentElm, oldEndVnode.el, oldStartVnode.el)
				oldEndVnode = oldCh.get(--oldEndIdx);
				newStartVnode = newCh.get(++newStartIdx);
			} else {
				// 使用key时的比较
				if (oldKeyToIdx == -1) {
					oldKeyToIdx = createKeyToOldIdx(oldCh, oldStartIdx, oldEndIdx) // 有key生成index表
				}
				idxInOld = oldKeyToIdx[newStartVnode.key]
				if (!idxInOld) {
					api.insertBefore(parentElm, createEle(newStartVnode).el, oldStartVnode.el)
					newStartVnode = newCh[++newStartIdx]
				} else {
					elmToMove = oldCh[idxInOld]
					if (elmToMove.sel != = newStartVnode.sel) {
						api.insertBefore(parentElm, createEle(newStartVnode).el, oldStartVnode.el)
					} else {
						patchVnode(elmToMove, newStartVnode)
						oldCh[idxInOld] = null
						api.insertBefore(parentElm, elmToMove.el, oldStartVnode.el)
					}
					newStartVnode = newCh[++newStartIdx]
				}
			}
		}
		if (oldStartIdx > oldEndIdx) {
			before = newCh[newEndIdx + 1] == null ? null : newCh[newEndIdx + 1].el
			addVnodes(parentElm, before, newCh, newStartIdx, newEndIdx)
		} else if (newStartIdx > newEndIdx) {
			removeVnodes(parentElm, oldCh, oldStartIdx, oldEndIdx)
		}
	}
}
