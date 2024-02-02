package yview.script;

import java.util.ArrayList;
import java.util.Arrays;

//帮我写一个java预设的arrayList<String>,其中包含如下关键词 break、else、new、var、case、finally、return、void、catch、for、switch、while、continue、function、this、with、default、if、throw、delete、in、try、do、instranceof、typeof，abstract、enum、int、short、boolean、export、interface、static、byte、extends、long、super、char、final、native、synchronized、class、float、package、throws、const、goto、private 、transient、debugger、implements、protected 、volatile、double、import、public
public class Keywords {
	public static ArrayList<String> keywords = new ArrayList<>(Arrays.asList(
		"break", "else", "new", "var", "case", "finally", "return", "void", "catch", "for", "switch",
		"while", "continue", "function", "this", "with", "default", "if", "throw", "delete", "in", "try",
		"do", "instanceof", "typeof", "abstract", "enum", "int", "short", "boolean", "export", "interface",
		"static", "byte", "extends", "long", "super", "char", "final", "native", "synchronized", "class",
		"float", "package", "throws", "const", "goto", "private", "transient", "debugger", "implements",
		"protected", "volatile", "double", "import", "public"
	));
	
	public static boolean is(String code) {
		return keywords.contains(code.trim());
	}
}
