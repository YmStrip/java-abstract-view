package yview.src;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Objects;

public class CompilerData {
	public CompilerData(Document doc) {
		this.doc = doc;
	}
	public Document doc;
	public Element body;
	public Element template;
	public Element js;
	//未来支持？
	public Element ts;
	//检查基本数据正确性
	public void check(Compiler compiler) {
		body = doc.body();
		if (doc.getElementsByTag("script").size() > 1) {
			compiler.config.error("There must be only one script node",body);
			return;
		}
		var child = body.children();
		for (var i : child) {
			var tag = i.tag().getName();
			if (Objects.equals(tag, "template")) {
				if (template != null) {
					compiler.config.error("There must be only one template node",i);
					return;
				}
				template = i;
			}
			if (Objects.equals(tag, "script")) {
				if (i.attr("lang").equals("ts") || i.attr("lang").equals("typescript")) {
					compiler.config.error("Not support ts",i);
					return;
				}
				js = i;
			}
		}
		if (template == null) {
			compiler.config.error("There must be only one template node");
			return;
		}
	}
	//创建一个js构建器
}
