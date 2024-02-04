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
		public boolean staticKey = true;
		public boolean staticValue = true;
		//属性名称[代码]
		public String key;
		public String strKey(String fi, String rep) {
			return fi + key.replace(fi, rep) + fi;
		}
		public String strKey() {
			return strKey("\"", "\\\"");
		}
		//属性值[静态|代码]
		public String value;
		public String strValue(String fi, String rep) {
			return fi + value.replace(fi, rep) + fi;
		}
		public String strValue() {
			return strValue("\"", "\\\"");
		}
	}
	
	public PropData(Compiler compiler, Element el) {
		this.compiler = compiler;
		this.el = el;
		this.parse(el);
	}
	
	public Compiler compiler;
	public Element el;
	public ArrayList<Prop> props = new ArrayList<>();
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
					staticKey = false;
					staticValue = false;
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
					staticKey = true;
					staticValue = false;
					key = bindKey;
					value = v;
				}});
			}
			return this;
		}
		//xxx = xxx
		{
			props.add(new Prop() {{
				staticKey = true;
				staticValue = true;
				key = k;
				value = v;
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
		for (var prop : props) {
			if (!prop.staticKey && Keywords.is(prop.key)) {
				compiler.config.keywordError(":" + prop.key + "=", el);
			}
		}
		return this;
	}
	@Override
	public String toString() {
		var data = new StringBuilder().append("{");
		for (var i : props) {
			data.append(i.key).append(":").append(i.value).append(",");
		}
		return data.append("}").toString();
	}
}
