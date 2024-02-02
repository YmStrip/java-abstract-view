package yview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import yview.core.util.ColorType;
import yview.core.core.View;

import java.awt.*;


public class LibgdxView extends View {
	{
		description = "LibgdxApp";
	}
	
	public static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	@Override
	public float windowWidth() {
		return toolkit.getScreenSize().width;
	}
	
	@Override
	public float windowHeight() {
		return toolkit.getScreenSize().height;
	}
	
	@Override
	public float windowX() {
		return 0;
	}
	
	@Override
	public float windowY() {
		return 0;
	}
	
	@Override
	public Object el() {
		return new Table();
	}
	
	@Override
	public boolean isEl(Object d) {
		return d instanceof Actor;
	}
	
	@Override
	public Object stringEl(Object d) {
		return new Table() {{
			add(new Label("", new Label.LabelStyle()));
		}};
	}
	
	@Override
	public void setElColor(Object d, ColorType color) {
		if (d instanceof Actor a) {
			a.setColor(color.r, color.g, color.b, color.a);
		}
	}
	
	@Override
	public void setElWidth(Object d, float w) {
		if (d instanceof Actor a) {
			a.setWidth(w);
		}
	}
	
	@Override
	public void setElHeight(Object d, float h) {
		if (d instanceof Actor a) {
			a.setHeight(h);
		}
	}
	
	@Override
	public void setElX(Object d, float x) {
		if (d instanceof Actor a) {
			a.setX(x);
		}
	}
	
	@Override
	public void setElY(Object d, float y) {
		if (d instanceof Actor a) {
			a.setY(y);
		}
	}
	
	@Override
	public void delEl(Object d) {
		if (d instanceof Actor a) {
			a.remove();
		}
	}
	
	@Override
	public void setElBackcolor(Object d, ColorType color) {
		if (d instanceof Table a) {
			//a.setBackground();
		}
	}
	
	@Override
	public void setElBackground(Object d, String name) {
		if (d instanceof Table a) {
			//a.setBackground();
		}
	}
	
	@Override
	public void addChild(Object d, Object child) {
		if ((d instanceof Group a) && (child instanceof Group b)) {
			a.addActor(b);
		}
	}
	
	@Override
	public Object[] getChild(Object d) {
		if ((d instanceof Group a)) {
			return a.getChildren().items;
		}
		return new Object[0];
	}
	
	@Override
	public void setChildren(Object parent, int index, Object child) {
		if ((parent instanceof Group a) && (child instanceof Group b)) {
			a.addActorAt(index, b);
			a.removeActorAt(index + 1, false);
		}
	}
	
	@Override
	public void delChildren(Object d, int index) {
		if ((d instanceof Group a)) {
			a.removeActorAt(index, false);
		}
	}
	
	@Override
	public void setElOpacity(Object d, float opacity) {
		//after
		if ((d instanceof Group a)) {
			var c = a.getColor();
			c.a = opacity;
			c.clamp();
			a.setColor(c);
		}
	}
}
