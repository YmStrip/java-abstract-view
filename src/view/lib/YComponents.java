package st.provider.yview.lib;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.event.*;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import st.provider.yview.YView;
import st.provider.yview.lib.event.YEvents;
import st.provider.yview.lib.prop.Prop;
import st.provider.yview.lib.ref.Ref;
import st.provider.yview.lib.slot.Slot;
import st.provider.yview.lib.vnode.VNode;


public class YComponents {
	public static class div extends VNode<Table> {
		public Slot defaultSlot = slot();
		
		{
			render(() -> {
				watch(() -> {
					el.clearChildren();
					defaultSlot.renderTableX(el);
				}, defaultSlot);
			});
		}
	}
	
	public static class closeBtn extends VNode<Table> {
		public static Drawable back = new TextureRegionDrawable(Core.atlas.find("st-close-btn"));
		public static Drawable back_active = new TextureRegionDrawable(Core.atlas.find("st-close-btn-active"));
		public Prop<Float> width = prop("width", 50f);
		public Prop<Float> height = prop("height", 50f);
		
		{
			render(() -> {
				var label = new Label("x");
				YView.func $notActive = () -> {
					el.setBackground(back);
					label.setColor(Color.white);
				};
				YView.func $active = () -> {
					el.setBackground(back_active);
					label.setColor(Color.white);
				};
				el.addListener(new ClickListener() {
					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Element fromActor) {
						super.enter(event, x, y, pointer, fromActor);
						$active.call();
					}
					
					@Override
					public void clicked(InputEvent event, float xX, float yX) {
						emit(new YEvents.ClickEvent() {{
							x = xX;
							y = yX;
							bubble();
						}});
					}
					
					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Element toActor) {
						super.exit(event, x, y, pointer, toActor);
						$notActive.call();
					}
				});
				$notActive.call();
				watch(() -> {
					el.clearChildren();
					el.setWidth(width.value);
					el.setHeight(height.value);
					el.add(label).pad(0);
				}, width, height);
				
			});
		}
	}
	
	public static class alert extends VNode<Table> {
		public Prop<TextureRegion> back = prop("back", Core.atlas.find("st-alert-back"));
		public Prop<Float> width = prop("width", 400f);
		public Prop<Float> widthP = prop("widthP");
		public Prop<Float> height = prop("height", 600f);
		public Prop<Float> heightP = prop("heightP");
		public Prop<Float> x = prop("x", 0f);
		public Prop<Float> y = prop("y", 0f);
		public Prop<Boolean> center = prop("center", true);
		public Prop<Boolean> closeBtn = prop("closeBtn", true);
		public Prop<String> title = prop("title", "");
		public Prop<Boolean> isModal = prop("isModal", true);
		public Slot defaultSlot = slot();
		
		{
			//参数限制
			{
				heightP.on(t -> {
					if (t != null) {
						height.set(YView.perH(heightP.value));
					}
				});
				widthP.on(t -> {
					if (t != null) {
						height.set(YView.perW(widthP.value));
					}
				});
				height.on(t -> {
					if (center.value) {
						x.set(YView.centerX(t));
					}
				});
				width.on(t -> {
					if (center.value) {
						y.set(YView.centerY(t));
					}
				});
				x.on(t -> {
					if (center.value) x.value = YView.centerX(width.value);
				});
				y.on(t -> {
					if (center.value) y.value = YView.centerY(height.value);
				});
			}
			var alert_that = this;
			//渲染
			render(() -> {
				el.touchable = Touchable.enabled;
				var title_container = new Table();
				var body_container = new Table();
				final Table[] closeBtnT = {null};
				watch(() -> {
					el.setBackground(new TextureRegionDrawable(back.value));
				}, back);
				watch(() -> {
					el.setWidth(width.get());
					el.setHeight(height.get());
					el.x = x.value;
					el.y = y.value;
				}, width, height, x, y);
				watch(() -> {
					title_container.clearChildren();
					if (title.get() != null && !title.get().isEmpty()) {
						title_container.add(new Label(title.get())).padLeft(50).expandX();
					}
					if (closeBtnT[0] != null) {
						closeBtnT[0].remove();
						closeBtnT[0] = null;
					}
					if (closeBtn.value) {
						closeBtnT[0] = node(new closeBtn() {{
							x.set(alert.this.x.value + 1);
							y.set(alert.this.y.value);
							on(new YEvents.ClickEvent().stop().call(t -> {
								alert_that.remove();
							}));
						}});
						title_container.add(closeBtnT[0]);
					}
				}, title, closeBtn);
				watch(() -> {
					body_container.clearChildren();
					defaultSlot.render(body_container, t -> body_container.add(t).expandX().fillX().align(Align.left));
				}, defaultSlot);
				el.add(title_container).expandX().fillX();
				el.row();
				el.add(new ScrollPane(body_container)).fill().expand();
			});
			afterMounted(() -> {
				System.out.println(el);
				var stage = Core.scene;
				stage.add(el);
				stage.setKeyboardFocus(el);
				stage.setScrollFocus(el);
			});
		}
	}
	
	public static class tabBtn extends VNode<Table> {
		public TextureRegion btn = Core.atlas.find("st-tab-btn");
		public TextureRegion btn_active = Core.atlas.find("tab-btn-active");
		public Prop<Float> width = prop("width", 100f);
		public Prop<Float> height = prop("height", 50f);
		public Prop<String> text = prop("text", "button");
		
		{
			watch(() -> {
				el.clearChildren();
				el.add(new Label(text.get())).align(1);
				el.setWidth(width.get());
				el.setHeight(height.get());
			}, text, width, height);
		}
	}
	
	public static class tabs extends VNode<Table> {
		public Prop<Boolean> closeBtn = prop("closeBtn", true);
		public Prop<Float> width = prop("width", 400f);
		public Prop<Float> height = prop("height", 600f);
		public Prop<String> selectTab = prop("selectTab");
		public Prop<String> tabs = prop("tabs");
		public Prop<String> defaultTab = prop("defaultTab");
		
		//其中的所有-slot-会被处理成tab,并且需要手动添加相应的tab
		{
			render(() -> {
				//创建结构
				el.clearChildren();
				var tabContainer = new Table();
				var viewContainer = new Table();
				var viewScroll = new ScrollPane(viewContainer);
				//创建所有的tabs
				watch(() -> {
					el.setWidth(width.get());
					el.setHeight(height.get());
				}, height, width);
				watch(() -> {
					tabContainer.clearChildren();
					var closeBtnWidth = closeBtn.get() ? 120f : 0f;
					var tabWidth = (width.get() - closeBtnWidth) / tabs.array.size();
					if (closeBtn.get()) {
						tabContainer.add(new tabBtn() {{
							width.set(closeBtnWidth);
							text.set("@close");
						}}.mount()).pad(0);
					}
					for (var i : tabs.array) {
						tabContainer.add(new tabBtn() {{
							width.set(tabWidth);
							text.set((String) i);
						}}.mount()).pad(0);
					}
				}, tabs, closeBtn);
				watch(() -> {
				});
			});
		}
	}
	
	public static class btn extends VNode<Table> {
		public static Drawable back = new TextureRegionDrawable(Core.atlas.find("st-btn"));
		public static Drawable back_active = new TextureRegionDrawable(Core.atlas.find("st-btn-active"));
		public Prop<Float> width = prop("width");
		public Prop<Float> height = prop("height");
		public Slot defaultSlot = slot();
		
		{
			render(() -> {
				el.touchable = Touchable.enabled;
				YView.func $notActive = () -> {
					el.setBackground(back);
					el.setColor(Color.white);
				};
				YView.func $active = () -> {
					el.setBackground(back_active);
					el.setColor(Color.white);
				};
				el.addListener(new ClickListener() {
					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Element fromActor) {
						super.enter(event, x, y, pointer, fromActor);
						$active.call();
					}
					
					@Override
					public void clicked(InputEvent event, float xX, float yX) {
						emit(new YEvents.ClickEvent() {{
							x = xX;
							y = yX;
							bubble();
						}});
					}
					
					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Element toActor) {
						super.exit(event, x, y, pointer, toActor);
						$notActive.call();
					}
				});
				$notActive.call();
				watch($notActive::call, back, back_active);
				watch(() -> {
					el.clearChildren();
					if (width.value != null) el.setWidth(width.value);
					if (height.value != null) el.setHeight(height.value);
					defaultSlot.render(el, t -> el.add(t));
				}, width, height, defaultSlot);
				
			});
		}
	}
	
	//Example
	class example extends VNode<Table> {
		public Ref<Integer> num = ref(0);
		public Prop<Float> width = prop("width", 300f);
		public Prop<Float> height = prop("height", 500f);
		public Slot defaultSlot = slot();
		
		{
			render(() -> {
				var bindC = new Table();
				var bindView = new Table();
				var slotC = new Table();
				el.touchable = Touchable.enabled;
				watch(() -> {
					if (width.value != null) el.setWidth(width.value);
					if (height.value != null) el.setHeight(height.value);
				}, width);
				{
					var c = new Table();
					bindC.button("+", () -> {
						num.set(num.value + 1);
					});
					bindC.add(c);
					bindC.row();
					bindC.add(bindView);
				}
				watch(() -> {
					bindView.clearChildren();
					bindView.labelWrap(num.value + "");
					if (num.value % 5 == 0) {
						template(b -> new Table() {{
							labelWrap("5x");
						}});
					}
					//
					else {
						template(new Table());
					}
					if (num.value % 100 == 0) {
						$forceUpdate();
					}
				}, bindC);
				watch(() -> {
					defaultSlot.renderTableX(slotC);
				}, defaultSlot);
				afterMounted(() -> {
					System.out.println(el);
					var stage = Core.scene;
					stage.add(el);
					stage.setKeyboardFocus(el);
					stage.setScrollFocus(el);
				});
				el.add(bindC).expandX().fillX();
				el.row();
				el.add(slotC).expandX().fillX();
			});
		}
	}
	/*
	{
		new example() {{
			width.set(800f);
			height.set(500f);
			template("default");
		}}.mount();
	}*/
}
