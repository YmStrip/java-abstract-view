package st.provider.yview;

import arc.Core;
import st.provider.yview.lib.YComponents;

public class YView {
	public interface func {
		void call ();
	};
	//属性值
	public static float sceneWidth() {
		return Core.scene.getWidth();
	}
	public static float perW (float width) {
		if (width < 0) width = 0;
		if (width > 100) width = 100;
		return sceneWidth() / width * 100;
	}
	public static float perH (float width) {
		if (width < 0) width = 0;
		if (width > 100) width = 100;
		return sceneHeight() / width * 100;
	}
	public static float centerX (float width) {
		return (sceneWidth() - width)/2;
	}
	public static float centerY (float height) {
		return (sceneHeight() - height)/2;
	}
	public static float sceneHeight() {
		return Core.scene.getHeight();
	}
	
	public static class Components extends YComponents {
	}
	public static class Slot extends st.provider.yview.lib.slot.Slot {}
	public static class components extends YComponents {
	}
}
