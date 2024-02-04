package yview.src;

import org.jsoup.nodes.Document;
import yview.ast.VAst;
import yview.ast.DocumentParser;


public class Compiler {
	public CompilerConfig config = new CompilerConfig(this);
	public String code = "";
	public Document sfcDocument;
	public CompilerData sfcCompilerData;
	public VAst sfcAst;
	
	public Compiler() {
	
	}
	
	public Compiler(String code) {
		this.code = code;
	}
	
	public Compiler compileSfc() {
		try {
			sfcDocument = null;
			sfcCompilerData = null;
			sfcAst = null;
			//解析文档
			sfcDocument = DocumentParser.parse(config.sourceFix(code));
			//提取template,script,并检查
			sfcCompilerData = new CompilerData(sfcDocument);
			sfcCompilerData.check(this);
			//创建Ast
			sfcAst = new VAst(this, sfcCompilerData.template) {{
				isRoot = true;
				init();
			}};
		} catch (Exception e) {
			config.handelError.get(e + "");
			throw new Error(e);
		}
		return this;
	}
	
	public Compiler compileSfc(String code) {
		this.code = code;
		compileSfc();
		return this;
	}
}
