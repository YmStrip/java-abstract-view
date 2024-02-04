package yview.ast;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Range;
import yview.script.CodeBuilder;
import yview.script.VNodeBuilder;
import yview.src.Compiler;

import java.util.ArrayList;
import java.util.HashMap;


public class VAst {
	public boolean isRoot = false;
	public Compiler compiler;
	//节点名称
	public String name;
	//节点
	public Element el;
	//坐标
	public Range range;
	//VFor
	public ForData vFor;
	public IfData vIf;
	public PropData props;
	public VAst parent;
	public VAst left;
	public VAst right;
	public HashMap<String, VAst> varContext = new HashMap<>();
	public VAst check() {
		if (name.equals("script")) compiler.config.error("Unable to add script inside template", el);
		if (isRoot && vFor != null) {
			compiler.config.error("v-for cannot be used in root template", el);
		}
		return this;
	}
	
	public ArrayList<VAst> child = new ArrayList<>();
	public VAst child() {
		VAst last = null;
		for (var i : el.children()) {
			var ch = new VAst(compiler, i);
			ch.left = last;
			if (last != null) last.right = ch;
			last = ch;
			ch.parent = this;
			child.add(ch);
			if (vFor != null) {
				if (vFor.forIndex != null) {
					ch.varContext.put(vFor.forIndex, this);
				}
				if (vFor.forData != null) {
					ch.varContext.put(vFor.forData, this);
				}
			}
			ch.init();
		}
		return this;
	}
	public VAst init() {
		this.name = el.tagName();
		this.range = el.sourceRange();
		if (ForData.isFor(el)) this.vFor = new ForData(compiler, el);
		if (IfData.isIf(el)) vIf = new IfData(compiler, el);
		if (left != null && vIf != null) vIf.checkLeft(left.vIf);
		props = new PropData(compiler, el);
		check();
		child();
		return this;
	}
	public VAst(Compiler compiler, Element el) {
		this.compiler = compiler;
		this.el = el;
	}
	@Override
	public String toString() {
		return new VNodeBuilder(this).toString();
	}
}
