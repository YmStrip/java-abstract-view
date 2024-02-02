package yview.src;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;

public class CompilerConfig {
	public String src = "/document.vue";
	
	private String src(String src) {
		return src.replaceAll("\\\\", " / ");
	}
	
	public String src() {
		return src(src);
	}
	
	public String filename() {
		var a = src().split("/");
		if (a.length == 0) return "";
		return a[a.length - 1];
	}
	
	public String dirname() {
		var a = src().split("/");
		if (a.length == 0) return "";
		return String.join("/", Arrays.copyOfRange(a, 0, a.length - 1));
	}
	
	public int sourceFixTop = 1;
	
	public String sourceFix(String code) {
		return "<html><body>\n" + code + "\n</body></html>";
	}
	
	public Compiler compiler;
	
	public CompilerConfig(Compiler compiler) {
		this.compiler = compiler;
	}
	
	public ProvCall handelWarning = (data) -> {
		log("warning", data);
	};
	public ProvCall handelError = (data) -> {
		log("error", data);
	};
	public ProvCall handelInfo = (data) -> {
		log("info", data);
	};
	
	public CompilerConfig error(String error, Element element) {
		var str = new StringBuilder();
		var pos = element.sourceRange();
		str.append(src);
		str.append(":").append(pos.start().lineNumber() - sourceFixTop);
		str.append(":").append(pos.start().columnNumber());
		str.append("\n");
		str.append(error);
		str.append("\n");
		var html = element.clone();
		//允许3个子节点,其中子节点的子节点只能一个
		var q = 0;
		for (var i : html.childNodes()) {
			++q;
			if (q > 3) {
				i.remove();
			} else {
				i.empty();
			}
		}
		str.append(html.outerHtml());
		error(str.toString());
		return this;
	}
	
	public CompilerConfig keywordError(String error) {
		return error("Keywords("+error+") cannot be used as identifiers");
	}
	
	public CompilerConfig keywordError(String error, Element element) {
		return error("Keywords("+error+") cannot be used as identifiers", element);
	}
	
	public CompilerConfig error(String code) {
		throw new Error(code);
	}
	
	public CompilerConfig log(String name, String code) {
		code = String.format("[%s] %s", name, code);
		System.out.println(code);
		logs.add(code);
		return this;
	}
	
	public ArrayList<String> logs = new ArrayList<>();
	
	public interface ProvCall {
		void get(String name);
	}
}
