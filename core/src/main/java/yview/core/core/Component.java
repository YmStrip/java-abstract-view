package yview.core.core;

public class Component {
	//时间列表
	//在实例初始化之后，数据观测 (data observer) 和 event/watcher 事件配置之前被调用。
	public Watch<VNode> beforeCreate = new Watch<>();
	//在实例创建完成后被立即调用。在这一步，实例已完成以下的配置：数据观测 (data observer)，属性和方法的运算，watch/event 事件回调。然而，挂载阶段还没开始，$el 属性目前尚不可用。
	public Watch<VNode> create = new Watch<>();
	//在挂载开始之前被调用。相关的 render 函数首次被调用。
	public Watch<VNode> beforeMount = new Watch<>();
	//实例被挂载后调用，此时 el 被新创建的 vm.$el 替换了。如果根实例挂载到了一个文档内的元素上，当 mounted 被调用时 vm.$el 也在文档内。
	public Watch<VNode> mounted = new Watch<>();
	//数据更新时调用，发生在虚拟 DOM 重新渲染和打补丁之前。可以在该钩子中对更新之前的状态做一些操作。
	public Watch<VNode> beforeUpdate = new Watch<>();
	//由于数据更改导致的虚拟 DOM 重新渲染和打补丁完成后调用。调用时，组件 DOM 已经更新，所以可以执行依赖于 DOM 的操作。
	public Watch<VNode> updated = new Watch<>();
	//实例销毁之前调用。在这一步，实例仍然完全可用。
	public Watch<VNode> beforeDestroy = new Watch<>();
	//实例销毁后调用。此时，所有的事件监听器被移除，所有的子实例也被销毁。
	public Watch<VNode> destroyed = new Watch<>();
	//当捕获一个来自子孙组件的错误时被调用。
	public Watch<VNode> errorCaptured = new Watch<>();
}
