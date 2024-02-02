package yview.src;

import org.jsoup.nodes.Document;
import yview.ast.VAst;
import yview.ast.DocumentParser;


public class Compiler {
	public CompilerConfig config = new CompilerConfig(this);
	public String code = "";
	public Document document;
	public CompilerData compilerData;
	public VAst ast;
	
	public Compiler() {
	
	}
	
	public Compiler(String code) {
		this.code = code;
	}
	
	public Compiler compile() {
		try {
			document = null;
			compilerData = null;
			ast = null;
			//解析文档
			document = DocumentParser.parse(config.sourceFix(code));
			//提取template,script,并检查
			compilerData = new CompilerData(document);
			compilerData.check(this);
			//创建Ast
			ast = new VAst(this, compilerData.template) {{
				isRoot = true;
				init();
			}};
			System.out.println(compilerData.template);
		} catch (Exception e) {
			config.handelError.get(e + "");
			throw new Error(e);
		}
		return this;
	}
	
	public Compiler compile(String code) {
		this.code = code;
		compile();
		return this;
	}
}
