package st.provider.yview.lib.watch;

import st.provider.yview.lib.ref.Ref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Watch {
	public int wait = 50;
	
	public Watch(WatchCallback call, Ref<?>... p) {
		this.call = call;
		$refs.addAll(Arrays.asList(p));
	}
	
	public Watch(WatchCallback call) {
		this.call = call;
	}
	
	public void update(Ref<?> p) {
		if (!$refs.contains(p)) return;
		update();
	}
	
	public void update() {
		//每次调用有100ms冷却时间
		var now = System.currentTimeMillis();
		//正常调用
		if (now - lastTime > wait && !isWait) {
			lastTime = now;
			call.get();
		}
		//如果等待中，表示计划执行一次更新，但是还没完成
		if (isWait) return;
		//此时已经完成，但是间隔时间小于100ms，创建计划更新
		var timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				lastTime = System.currentTimeMillis();
				isWait = false;
				timer.cancel();
				call.get();
			}
		}, wait);
		isWait = true;
	}
	
	//组件更新器,50ms内只能更新一次
	public WatchCallback call = () -> {
	};
	public boolean isWait = false;
	public long lastTime = 0;
	public ArrayList<Ref<?>> $refs = new ArrayList<>();
	
	@Override
	public String toString() {
		return "watch(" + $refs + ")";
	}
}
