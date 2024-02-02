package yview.ast;

import org.jsoup.nodes.Element;
import yview.script.Keywords;
import yview.src.Compiler;

import java.util.Arrays;

public class ForData {
	//v-for="(data,index) in xxx"
	public Compiler compiler;
	
	public ForData(Compiler compiler, Element el) {
		this.el = el;
		this.compiler = compiler;
		this.parse(el);
	}
	
	public Element el;
	public String forData = null;
	public String forIndex = null;
	public String forFrom = null;
	public int forFromRange = -1;
	
	public ForData error(String code) {
		compiler.config.error(code, el);
		return this;
	}
	
	public ForData after_init() {
		if (forData != null && forData.isEmpty()) forData = null;
		if (forIndex != null && forIndex.isEmpty()) forIndex = null;
		if (forFrom != null && forFrom.isEmpty()) forFrom = null;
		//number
		if (forFrom != null) {
			if (forFrom.matches("-?[0-9]+(\\\\.[0-9]+)?")) {
				//数字类型
				forFromRange = Integer.valueOf(forFrom);
				if (forFromRange <= 0) error("v-for error");
			}
		}
		
		return this;
	}
	
	public ForData init() {
		var vFor = el.attr("v-for");
		if (vFor == null) return this;
		var res = (vFor.trim()).replaceAll("\\s{2,}", " ").split("\\)");
		//| ( x , x ) in xxx |
		if (res.length > 1) {
			//| ( x , x |
			var left = res[0];
			//| x |,| x |
			var arg = left.replaceAll("\\(", "").split(",");
			if (arg.length == 1) {
				//data
				forData = arg[0].trim();
			}
			//
			else if (arg.length == 2) {
				forData = arg[0].trim();
				forIndex = arg[1].trim();
			} else error("v-for error");
			//| in xxx |
			var right = String.join(" ", Arrays.copyOfRange(res, 1, res.length));
			//
			var inA = right.indexOf("in");
			if (inA < 0) error("v-for error");
			forFrom = right.substring(inA + 2).trim();
		}
		//| x in xxx |
		else if (res.length == 1) {
			var code = res[0];
			var index = code.indexOf("in");
			if (index < 0) error("v-for error");
			forData = code.substring(0, index).trim();
			forFrom = code.substring(index + 2).trim();
		} else error("v-for error");
		return this;
	}
	
	public boolean isFor() {
		return !(forFrom == null || forData == null);
	}
	
	public ForData check() {
		if (!isFor()) error("v-for error");
		if (forData != null && Keywords.is(forData)) compiler.config.keywordError(forData, el);
		if (forIndex != null && Keywords.is(forIndex)) compiler.config.keywordError(forIndex, el);
		return this;
	}
	
	public ForData parse(Element el) {
		this.el = el;
		forData = null;
		forFromRange = -1;
		forFrom = null;
		forIndex = null;
		init();
		after_init();
		check();
		return this;
	}
	
	public static boolean isFor(Element el) {
		return !el.attr("v-for").isEmpty();
	}
	
	@Override
	public String toString() {
		if (isFor())
			return String.format("v-for=\"(%s,%s) in %s\"", forData, forIndex == null ? "index" : forIndex, forFromRange > 0 ? forFromRange : forFrom);
		return "v-for=\"\"";
	}
}
