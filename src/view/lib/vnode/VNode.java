package st.provider.yview.lib.vnode;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import st.provider.yview.lib.template.*;
import st.provider.yview.lib.event.YEvent;
import st.provider.yview.lib.prop.Prop;
import st.provider.yview.lib.ref.Ref;
import st.provider.yview.lib.slot.Slot;
import st.provider.yview.lib.watch.Watch;
import st.provider.yview.lib.watch.WatchCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VNode<T extends WidgetGroup> {
	public String name = getClass().getSimpleName();
	public T el;
	public VNode<?> parent;
	public Watch $updater = new Watch(this::update);
	public HashMap<String, Prop<?>> $props = new HashMap<>();
	public HashMap<String, Slot> $slots = new HashMap<>();
	public HashMap<String, Template> $templates = new HashMap<>();
	public ArrayList<Watch> $watches = new ArrayList<>();
	public ArrayList<YEvent> $event = new ArrayList<>();
	
	public VNode<T> $forceUpdate() {
		for (var i : $watches) i.update();
		$updater.update();
		return this;
	}
	
	public VNode<T> forceUpdate() {
		return $forceUpdate();
	}
	
	public VNode<T> on(YEvent event) {
		if (!$event.contains(event)) $event.add(event);
		return this;
	}
	
	public VNode<T> off(YEvent event) {
		$event.remove(event);
		return this;
	}
	
	public VNode<T> emit(YEvent event) {
		//向父亲节点传递
		var isStop = false;
		//现在获取父亲节点包含的值
		for (var i : $event) {
			if (!Objects.equals(i.name, event.name)) continue;
			if (!i.tag(event)) continue;
			i.call.get(event);
			if (i.stop) {
				isStop = true;
				break;
			}
		}
		if (isStop) return this;
		if (!event.bubble) return this;
		if (parent != null) parent.emit(event);
		return this;
	}
	
	public <C> Ref<C> ref(C v) {
		return new Ref<>(v);
	}
	
	public <C> Ref<C> ref() {
		return ref(null);
	}
	
	//创建prop
	public <C> Prop<C> prop(String name, C v) {
		return Prop.prop(name, v, this);
	}
	
	public <C> Prop<C> prop(String name, Ref<C> ref) {
		var def = (Prop<C>) prop(name);
		ref.on(t -> {
			def.value = ref.value;
			def.object = ref.object;
			def.array = ref.array;
			def.emitUpdate();
		});
		return def;
	}
	
	public <C> Prop<C> prop(String name) {
		return Prop.prop(name, this);
	}
	
	//template分为静态和动态,无论是静态还是动态,一旦调用函数就会更新slot
	//template名称对应的slot名称,slot应该在render函数内,配合watch渲染
	//动态template也就是watch加上template
	public VNode<T> template(Template t) {
		t.inject(this);
		return this;
	}
	
	public VNode<T> template(String name, String data) {
		return template(name, new Table() {{
			labelWrap(() -> data).align(Align.center).expandX();
		}});
	}
	
	public VNode<T> template(String data) {
		return template("default", data);
	}
	
	//静态template
	public VNode<T> template(String name, VNode<?> temp) {
		var def = temp.mount();
		var that = this;
		return template(new Template(name) {
			@Override
			public TemplateRenderResult render(TemplateRenderOption data) {
				return new TemplateRenderResult() {{
					el = def;
					parent = that;
				}};
			}
		});
	}
	
	public VNode<T> template(String name, WidgetGroup temp) {
		var that = this;
		return template(new Template(name) {
			@Override
			public TemplateRenderResult render(TemplateRenderOption data) {
				return new TemplateRenderResult() {{
					el = temp;
					parent = that;
				}};
			}
		});
	}
	
	public VNode<T> template(VNode<?> temp) {
		return template("default", temp);
	}
	
	public VNode<T> template(WidgetGroup temp) {
		return template("default", temp);
	}
	
	public VNode<T> template(String name, TemplateProvObj obj) {
		var that = this;
		return template(new Template(name) {
			@Override
			public TemplateRenderResult render(TemplateRenderOption data) {
				return new TemplateRenderResult() {{
					var o = obj.get(data);
					if (o instanceof WidgetGroup e) {
						el = e;
					}
					//
					else if (o instanceof VNode<?> v) {
						el = node(v);
					}
					parent = that;
				}};
			}
		});
	}
	
	public VNode<T> template(TemplateProvObj obj) {
		return template("default", obj);
	}
	
	//定义slot
	public Slot slot(String name) {
		return Slot.slot(name, this);
	}
	
	public Slot slot() {
		return Slot.slot(this);
	}
	
	//mount的时候执行,可能会多次
	public T create() {
		return (T) new Table();
	}
	
	public ArrayList<VNodeCallback> render = new ArrayList<>();
	
	public VNode<T> render(VNodeCallback call) {
		render.add(call);
		return this;
	}
	
	//default
	public Prop<Color> backcolor = prop("backcolor");
	public Prop<TextureRegion> background = prop("background");
	public Prop<Color> color = prop("color", Color.white);
	
	//在create之后执行
	public void render() {
		watch(() -> {
			el.setColor(color.value);
			if (el instanceof Table table) {
				el.setColor(color.value);
				if (background.value != null) {
					table.background(new TextureRegionDrawable(background.value));
				}
				//
				else if (backcolor.value != null) {
					table.setBackground(genBackground(backcolor.value));
				}
			}
		}, backcolor, background, color);
		for (var i : render) {
			i.get();
		}
	}
	
	public ArrayList<VNodeCallback> update = new ArrayList<>();
	
	public VNode<T> update(VNodeCallback call) {
		update.add(call);
		return this;
	}
	
	//在render之后,update时执行
	public void update() {
		for (var i : update) {
			i.get();
		}
	}
	
	public VNode<T> watch(WatchCallback call, Ref<?>... p) {
		var w = new Watch(call, p);
		$watches.add(w);
		for (var i : p) {
			i.on(t -> {
				w.update(i);
			});
		}
		return this;
	}
	
	public VNode<T> watch(WatchCallback call, Object... p) {
		var list = new ArrayList<Prop<?>>();
		for (var i : p) {
			if (i instanceof Prop<?> type_p) {
				list.add(type_p);
			}
			//
			else if (i instanceof String type_str) {
				var def = $props.get(type_str);
				if (def != null) list.add(def);
			}
		}
		return watch(call, list.toArray(new Prop[0]));
	}
	
	//style
	public static HashMap<String, Drawable> backgroundList = new HashMap<>();
	public static TextureRegionDrawable backUI = new TextureRegionDrawable(new TextureRegion(new Texture(100, 100), 100, 100));
	
	public static Drawable genBackground(Color color) {
		var key = color.toString();
		var def = backgroundList.get(key);
		if (def != null) return def;
		var res = backUI.tint(color);
		backgroundList.put(key, res);
		return res;
	}
	
	public ArrayList<VNodeCallback> afterMounted = new ArrayList<>();
	
	public <Q extends WidgetGroup> Q node(VNode<Q> node) {
		node.parent = this;
		return node.mount();
	}
	
	public VNode<T> remove() {
		if (el != null) el.remove();
		return this;
	}
	
	public VNode<T> afterMounted(VNodeCallback call) {
		afterMounted.add(call);
		return this;
	}
	
	//mount只应该调用一次
	public T mount() {
		//创建根节点
		el = create();
		//文档结构 周期
		render();
		forceUpdate();
		//mounted之后hook
		for (var i : afterMounted) {
			i.get();
		}
		return el;
	}
}
