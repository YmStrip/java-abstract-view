package yview.ast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DocumentParser {
	//解析节点
	public static Document parse(String code) {
		var p = Parser.htmlParser().setTrackPosition(true);
		return p.parseInput(code, "/");
	}
}
