##  需要掌握的第三方库
EventBus
1 干嘛用的？
针对Android优化的发布/订阅事件总线。主要功能是替代Intent,Handler,BroadCast在Fragment，Activity，Service，线程之间传递消息.
优点是开销小，代码更优雅。以及将发送者和接收者解耦。
2 怎么使用
	1 自定义一个类，可以是空类，比如：
...
public class AnyEventType {
	 public AnyEventType(){}
 }
 ...
	2 在要接收消息的页面注册：
	eventBus.register(this);  
	3 发送消息
	eventBus.post(new AnyEventType event);
	4 接受消息的页面实现(共有四个函数，各功能不同，这是其中之一，可以选择性的实现，这里先实现一个)：
	public void onEvent(AnyEventType event) {}  
	5 解除注册
	eventBus.unregister(this);  
3 具体例子
当击btn_try按钮的时候，跳到第二个Activity
当点击第二个activity上面的First Event按钮的时候向第一个Activity发送消息
当第一个Activity收到消息后，一方面将消息Toast显示，一方面放入textView中显示。

...
EventBus.getDefault().register(this);

Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
startActivity(intent);

EventBus.getDefault().post( new FirstEvent("FirstEvent btn clicked"));

public class FirstEvent {

	private String mMsg;
	public FirstEvent(String msg) {
		mMsg = msg;
	}
	public String getMsg(){
		return mMsg;
	}
}

	public void onEventMainThread(FirstEvent event) {

		String msg = "onEventMainThread " + event.getMsg();
		Log.d("harvic", msg);
		tv.setText(msg);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

EventBus.getDefault().unregister(this);

高级例子
四种订阅函数
告知观察者事件发生时通过EventBus.post函数实现，这个过程叫做事件的发布，观察者被告知事件发生叫做事件的接收，是通过下面的订阅函数实现的。
onEvent:如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，也就是说发布事件和接收事件线程在同一个线程。
使用这个方法时，在onEvent方法中不能执行耗时操作，如果执行耗时操作容易导致事件分发延迟。
onEventMainThread:如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，
onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，这个在Android中是非常有用的，因为在Android中只能在UI线程中更新UI，
所以在onEvnetMainThread方法中是不能执行耗时操作的。
onEventBackground:如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，那么onEventBackground就会在子线程中运行，
如果事件本来就是子线程中发布出来的，那么onEventBackground函数直接在该子线程中执行。
onEventAsync：使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync.

消息的接收是根据参数中的类名来决定执行哪一个的
EventBus.getDefault().register(this);

Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
startActivity(intent);

	btn_FirstEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new FirstEvent("FirstEvent btn clicked"));
			}
		});
		
		btn_SecondEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new SecondEvent("SecondEvent btn clicked"));
			}
		});

		btn_ThirdEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new ThirdEvent("ThirdEvent btn clicked"));

			}
		});
public class FirstEvent {

	private String mMsg;
	public FirstEvent(String msg) {
		mMsg = msg;
	}
	public String getMsg(){
		return mMsg;
	}
}
public class SecondEvent{

	private String mMsg;
	public SecondEvent(String msg) {
		mMsg = "MainEvent:"+msg;
	}
	public String getMsg(){
		return mMsg;
	}
}
public class ThirdEvent {

	private String mMsg;
	public ThirdEvent(String msg) {
		mMsg = msg;
	}
	public String getMsg(){
		return mMsg;
	}
}


	public void onEventMainThread(FirstEvent event) {
		Log.d("harvic", "onEventMainThread FirstEvent" + event.getMsg());
	}

	//SecondEvent
	public void onEventMainThread(SecondEvent event) {

		Log.d("harvic", "onEventMainThread SecondEvent" + event.getMsg());
	}
	//SecondEvent
	public void onEventBackgroundThread(SecondEvent event){
		Log.d("harvic", "onEventBackground SecondEvent" + event.getMsg());
	}
	//SecondEvent
	public void onEventAsync(SecondEvent event){
		Log.d("harvic", "onEventAsync SecondEvent" + event.getMsg());
	}

	public void onEvent(ThirdEvent event) {
		Log.d("harvic", "OnEvent ThirdEvent" + event.getMsg());
	}

EventBus.getDefault().unregister(this);

4 源码解析
https://github.com/android-cn/android-open-project-analysis/tree/master/event-bus



