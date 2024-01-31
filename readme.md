# component
```java
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

class a {
	{
		new example() {{
			width.set(800f);
			height.set(500f);
			template("default");
		}}.mount();
	}
}
```