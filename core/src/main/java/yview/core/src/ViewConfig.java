package yview.core.src;

import yview.core.interfaces.Prov;

public class ViewConfig {
	public String name = "app";
	public Prov<String> handelInfo = (String code) -> System.out.println("[INFO]" + code);
	public Prov<String> handelError = (String code) -> System.out.println("[Error]" + code);
	public Prov<String> handelWarning = (String code) -> System.out.println("[WARNING]" + code);
}
