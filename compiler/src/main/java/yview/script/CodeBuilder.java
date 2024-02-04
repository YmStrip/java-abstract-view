package yview.script;

import java.util.ArrayList;
import java.util.HashMap;

public class CodeBuilder {
	public String value = "";
	
	public CodeBuilder() {
	}
	
	public CodeBuilder(String d) {
		value = d;
	}
	
	public ArrayList<CodeBuilder> data = new ArrayList<>();
	public HashMap<String, CodeBuilder> map = new HashMap<>();
	
	public CodeBuilder add(String code) {
		data.add(new CodeBuilder(code));
		return this;
	}
	public CodeBuilder add(int index, String code) {
		return add(index,new CodeBuilder(code));
	}
	public CodeBuilder add(int index,CodeBuilder code) {
		if (data.isEmpty()) return add(code);
		if (index < 0) index = 0;
		if (index >= data.size()) index = data.size() - 1;
		data.add(index, code);
		return this;
	}
	public CodeBuilder add(CodeBuilder code) {
		data.add(code);
		return this;
	}
	
	public CodeBuilder add(String... code) {
		for (var i : code) add(i);
		return this;
	}
	
	public CodeBuilder set(String key, String code) {
		map.put(key, new CodeBuilder(code));
		return this;
	}
	
	public CodeBuilder set(String key, CodeBuilder code) {
		map.put(key, code);
		return this;
	}
	
	public String dump() {
		var code = new StringBuilder();
		for (var i : map.entrySet()) code.append(i.getValue().dump());
		code.append(value);
		for (var i : data) code.append(i.dump());
		return code.toString();
	}
	@Override
	public String toString() {
		return dump();
	}
}
