package st.provider.yview.lib.event;
public class YEvents {
	public static class ClickEvent extends YEvent<ClickEvent> {
		public float x = 0;
		public float y = 0;
		public ClickEvent() {
			super("click");
		}
	}
}
