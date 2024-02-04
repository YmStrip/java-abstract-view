package yview.script;

import yview.ast.VAst;

import java.util.ArrayList;

public class VNodeBuilder extends CodeBuilder {
	public static int varTime = 0;
	public static String varName() {
		++varTime;
		return "$" + Integer.toHexString(varTime);
	}
	public CodeBuilder h = new CodeBuilder();
	public CodeBuilder build(VAst ast) {
		return buildFor(ast);
	}
	public CodeBuilder buildFor(VAst ast) {
		if (ast.vFor == null) return buildChunk(ast);
		var ch = new CodeBuilder();
		ch.add("this." + ast.vFor.forFrom + ".map((");
		if (ast.vFor.forIndex != null) {
			ch.add(ast.vFor.forData + "," + ast.vFor.forIndex);
		}//
		else {
			ch.add(ast.vFor.forData);
		}
		ch.add(")=>");
		ch.add(buildChunk(ast));
		ch.add(")");
		return ch;
	}
	public CodeBuilder buildProp(VAst ast) {
		var data = new CodeBuilder();
		data.add("{");
		for (var i : ast.props.props) {
			if (i.staticKey) {
				data.add(i.strKey());
			}//
			else {
				data.add("[" + i.key + "]");
			}
			data.add(":");
			if (i.staticValue) {
				data.add(i.strValue());
			}//
			else {
				var c = ast.varContext.get(i.value);
				if (c != null) {
					var str = c.range.start();
					data.add("/*for:" + str.lineNumber() + ":" + str.columnNumber() + "*/");
					data.add(i.value);
				} //
				else {
					data.add("this." + i.value);
				}
			}
			data.add(",");
		}
		return data.add("}");
	}
	public CodeBuilder buildArray(ArrayList<VAst> asts) {
		var ch = new CodeBuilder();
		if (asts.size() > 1) {
			var ch1 = new CodeBuilder();
			ch1.add("[");
			//逗号只能在:普通元素前,v-if前
			//连续for
			var forCont = -1;
			for (var i = 0; i < asts.size(); ++i) {
				var v = asts.get(i);
				if (i > 0) {
					if (v.vIf != null) {
						if (v.vIf.ifData != null) ch1.add(",");
					}//
					else {
						ch1.add(",");
					}
				}
				if (v.vIf != null) {
					//初始化
					if (v.vIf.ifData != null) forCont = 0;
					//如果目标是v-else-if v-else
					if (v.vIf.elseIfData != null || v.vIf.ifData != null) {
						if (v.left != null && v.left.vIf != null && v.vIf.elseIfData!=null) {
							if (v.left.vIf.elseIfData != null || v.left.vIf.ifData!=null) {
								++forCont;
								ch1.add("(");
							}
						}
						ch1.add(v.vIf.ifData == null ? v.vIf.elseIfData : v.vIf.ifData);
						ch1.add("?");
						ch1.add(buildFor(v));
						ch1.add(":");
						//如果目标right不是v-else-if 或 v-else, 那么添加空目标
						if (v.right != null && v.right.vIf != null) {
							if (v.right.vIf.ifData != null) {
								ch1.add("null");
								ch1.add(")".repeat(forCont));
							}
						}//
						else {
							ch1.add("null");
							ch1.add(")".repeat(forCont));
						}
					}
					//如果目标是v-else
					if (v.vIf.elseData != null) {
						ch1.add(buildFor(v));
						ch1.add(")".repeat(forCont));
					}
				}
				//
				else {
					ch1.add(buildFor(v));
				}
			}
			ch1.add("]");
			ch.add(ch1.add(0, ","));
		} //
		else if (asts.size() == 1) {
			ch.add(build(asts.get(0)).add(0, ","));
		}
		return ch;
	}
	public CodeBuilder buildChunk(VAst ast) {
		var ch = new CodeBuilder();
		ch.add("h(");
		ch.add("\"" + ast.name + "\"");
		if (!ast.props.props.isEmpty()) ch.add(buildProp(ast).add(0, ","));
		ch.add(buildArray(ast.child));
		ch.add(")");
		return ch;
	}
	public VNodeBuilder(VAst ast) {
		if (ast.isRoot) {
			h.add("{").add("render(){").add("return ");
			h.add(build(ast));
			h.add("}");
			return;
		}//
		else {
			h.add(build(ast));
		}
	}
	@Override
	public String dump() {
		return h.dump();
	}
}
