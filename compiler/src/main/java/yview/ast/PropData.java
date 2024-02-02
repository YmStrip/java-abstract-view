package yview.ast;

import org.jsoup.nodes.Element;
import yview.script.Keywords;
import yview.src.Compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class PropData {
	public static class Prop {
		//是否是静态的 string | code
		public boolean isStatic = true;
		//属性名称[代码]
		public String key;
		//属性值[静态|代码]
		public String value;
	}
	
	public PropData(Compiler compiler, Element el) {
		this.compiler = compiler;
		this.el = el;
		this.parse(el);
	}
	
	public Compiler compiler;
	public Element el;
	public ArrayList<Object> props = new ArrayList<>();
	public HashMap<String, Boolean> keys = new HashMap<>();
	
	public PropData parse(Element el) {
		this.el = el;
		this.props.clear();
		this.init();
		this.check();
		return this;
	}
	
	public PropData init(String k, String v) {
		var trimKey = k.trim();
		if (trimKey.equals("v-if") || trimKey.equals("v-for") || trimKey.equals("v-else") || trimKey.equals("v-else-if"))
			return this;
		if (trimKey.charAt(0) == ':') trimKey = "v-bind" + trimKey;
		//v-bind:xxx = xxx
		//v-bind:[xxx] = xxx
		if (trimKey.startsWith("v-bind:")) {
			var bindKey = trimKey.substring(7);
			if (bindKey.charAt(0) == '[') {
				//动态属性名称
				var nameA = bindKey.substring(1).split("]");
				props.add(new Prop() {{
					isStatic = false;
					if (nameA.length == 1) {
						key = nameA[0];
					}
					//
					else {
						key = String.join("", Arrays.copyOfRange(nameA, 0, nameA.length - 1));
					}
					
					value = v;
				}});
			}
			//
			else {
				//静态属性名称
				props.add(new Prop() {{
					isStatic = false;
					key = "\"" + bindKey.replaceAll("\"", "\\\"") + "\"";
					value = v;
				}});
			}
			return this;
		}
		//xxx = xxx
		{
			props.add(new Prop() {{
				isStatic = true;
				key = "\"" + k.replaceAll("\"", "\\\"") + "\"";
				value = "\"" + v.replaceAll("\"", "\\\"") + "\"";
			}});
		}
		return this;
	}
	
	public PropData init() {
		//循环元素的所有属性
		for (var i : el.attributes()) {
			var key = i.getKey();
			var value = i.getValue();
			init(key, value);
		}
		return this;
	}
	
	public PropData check() {
		for (var i : props) {
			if (i instanceof Prop prop) {
				if (!prop.isStatic && Keywords.is(prop.key)) {
					compiler.config.keywordError(":" + prop.key + "=", el);
				}
			}
		}
		return this;
	}
	
	@Override
	public String toString() {
		var s = new ArrayList<String>();
		for (var i : props) {
			if (i instanceof Prop p) {
				s.add(p.key + "=" + p.value);
			}
			s.add(" ");
		}
		return String.join(" ", s.toArray(new String[0]));
	}
}
