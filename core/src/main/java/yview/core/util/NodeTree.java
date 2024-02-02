package yview.core.util;

import yview.core.src.Node;
import yview.core.src.VNode;

public class NodeTree {
	public static String formatValue(Object v) {
		if (v instanceof VNode n) {
			return String.format("VNode(\"%s\")", n.diffKey);
		}
		if (v instanceof Node n) {
			return String.format("Node(\"%s\")", n.tag);
		}
		return v + "";
	}
	public static void recNode(StringBuilder builder, Node node, int deep, int maxDeep) {
		if (deep > maxDeep) return;
		builder.append("  ".repeat(Math.max(0, deep)));
		builder.append("<").append(node.tag);
		for (var i : node.attributes.entrySet()) {
			builder.append(" ");
			var key = i.getKey();
			var value = i.getValue();
			builder.append(key).append("=").append("\"").append(formatValue(value)).append("\"");
		}
		builder.append(">");
		for (var i : node.child) {
			builder.append("\n");
			recNode(builder, i, deep + 1, maxDeep);
		}
		if (!node.child.isEmpty()) {
			builder.append("\n");
			builder.append("  ".repeat(Math.max(0, deep)));
		}
		builder.append("</").append(node.tag).append(">");
	}
	public static String nodeTree(Node node) {
		return nodeTree(node, 9999);
	}
	public static String nodeTree(Node node, int maxDeep) {
		var builder = new StringBuilder();
		recNode(builder, node, 0, maxDeep);
		return builder.toString();
	}
	public static void recVNode(StringBuilder builder, VNode node, int deep, int maxDeep) {
		if (deep > maxDeep) return;
		builder.append("  ".repeat(Math.max(0, deep)));
		builder.append("<").append(node.tag);
		for (var i : node.data.props.object.entrySet()) {
			builder.append(" ");
			var key = i.getKey();
			var value = i.getValue().value;
			builder.append(key).append("=").append("\"").append(formatValue(value)).append("\"");
		}
		builder.append(">");
		for (var i : node.children) {
			builder.append("\n");
			recVNode(builder, i, deep + 1, maxDeep);
		}
		if (!node.children.isEmpty()) {
			builder.append("\n");
			builder.append("  ".repeat(Math.max(0, deep)));
		}
		builder.append("</").append(node.tag).append(">");
	}
	public static String VNodeTree(VNode node) {
		return VNodeTree(node, 9999);
	}
	public static String VNodeTree(VNode node, int maxDeep) {
		var builder = new StringBuilder();
		recVNode(builder, node, 0, maxDeep);
		return builder.toString();
	}
}
