package st.provider.yview.compiler;

import st.provider.yview.lib.vnode.VNode;

//编译器[懒得写暂时咕咕咕]
public class Compiler {
	//将目标文档解析成XML,中间件
	public Object xmlParser (String code) {
		return null;
	}
	//code -> 词法解析 [库]
	//词法 -> ast(xml) [语法生成器]/[直接写]
	//ast -> component
	public YAst treeParser(String code) {
		return null;
	}
	public VNode<?> parse(String code) {
		return null;
	}
}
