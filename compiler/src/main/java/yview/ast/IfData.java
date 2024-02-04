package yview.ast;

import org.jsoup.nodes.Element;
import yview.src.Compiler;

public class IfData {
	public Compiler compiler;
	public Element el;
	public String ifData;
	public String elseIfData;
	public String elseData;
	
	public IfData error(String code) {
		compiler.config.error(code, el);
		return this;
	}
	
	public IfData parse(Element el) {
		this.el = el;
		ifData = null;
		elseData = null;
		elseIfData = null;
		init();
		check();
		return this;
	}
	
	public boolean hasIf() {
		return ifData != null || elseIfData != null || elseData != null;
	}
	
	public IfData init() {
		var _if = el.attr("v-if");
		var _elseif = el.attr("v-else-if");
		var _else = el.attribute("v-else");
		if (!_if.isEmpty()) {
			if (hasIf()) error("Only one v-if, v-else, v-else-if can be defined");
			//解析目标的if
			ifData = _if.trim();
		}
		if (_else != null) {
			if (hasIf()) error("Only one v-if, v-else, v-else-if can be defined");
			elseData = "else";
		}
		if (!_elseif.isEmpty()) {
			if (hasIf()) error("Only one v-if, v-else, v-else-if can be defined");
			elseIfData = _elseif.trim();
		}
		return this;
	}
	
	public IfData check() {
		if (!hasIf()) error("v-if error");
		return this;
	}
	
	public static boolean isIf(Element el) {
		return (el.attribute("v-if") != null || el.attribute("v-else-if") != null || el.attribute("v-else") != null);
	}
	
	//检查左边标记
	public IfData checkLeft(IfData left) {
		// [v-if|v-else-if] -> v-else | v-else-if
		if (elseData != null || elseIfData != null) {
			//必须是v-if 和 v-else-if
			if (left == null || left.elseData != null || !(left.ifData != null || left.elseIfData != null))
				error("v-else must be linked to v-if/v-else-if");
		}
		return this;
	}
	
	public IfData(Compiler compiler, Element el) {
		this.el = el;
		this.compiler = compiler;
		parse(el);
	}
}
