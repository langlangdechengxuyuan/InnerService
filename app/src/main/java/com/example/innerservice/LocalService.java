package com.example.innerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

/**
 * Created by 15901 on 2018/3/15.
 * LocalBinder 为客户端提供 getService() 方法，以检索 LocalService 的当前实例。这样，客户端便可调用服务中的公共方法。
 * 例如，客户端可调用服务中的 getRandomNumber()。
 *
 * 总结：应用内部相同进程间服务调用，因为共享同样的内存空间，所以只需要按照统一接口交互就可以，不需要考虑跨进程方法调用问题。
 * Service的onBind方法只传递IBinder的实现类，所以通过内部的Binder实现类中方法返回Service服务对象，通过客户端的ServiceConnection
 * 中onServiceConnection回掉获取到Binder实例，进而获取到Service实例，接下来就可以通过Service实例直接调用Service中的公共方法。
 *
 * 而跨进程通信的原理可以通过下面这句话理解：
 * AIDL（Android 接口定义语言）执行所有将对象分解成原语的工作，操作系统可以识别这些原语并将它们编组到各进程中，以执行 IPC。
 * 之前采用 Messenger 的方法实际上是以 AIDL 作为其底层结构。
 *
 * 以下是一些有关绑定到服务的重要说明：
 * 您应该始终捕获 DeadObjectException 异常，它们是在连接中断时引发的。这是远程方法引发的唯一异常。
 * 对象是跨进程计数的引用。
 * 您通常应该在客户端生命周期的匹配引入 (bring-up) 和退出 (tear-down) 时刻期间配对绑定和取消绑定。 例如：
 * 如果您只需要在 Activity 可见时与服务交互，则应在 onStart() 期间绑定，在 onStop() 期间取消绑定。
 * 如果您希望 Activity 在后台停止运行状态下仍可接收响应，则可在 onCreate() 期间绑定，在 onDestroy() 期间取消绑定。
 * 请注意，这意味着您的 Activity 在其整个运行过程中（甚至包括后台运行期间）都需要使用服务，因此如果服务位于其他进程内，
 * 那么当您提高该进程的权重时，系统终止该进程的可能性会增加。
 *
 * 当服务与所有客户端之间的绑定全部取消时，Android 系统便会销毁服务（除非还使用 onStartCommand() 启动了该服务）。
 * 因此，如果您的服务是纯粹的绑定服务，则无需对其生命周期进行管理 — Android 系统会根据它是否绑定到任何客户端代您管理。
 * 不过，如果您选择实现 onStartCommand() 回调方法，则您必须显式停止服务，因为系统现在已将服务视为已启动。在此情况下，
 * 服务将一直运行到其通过 stopSelf() 自行停止，或其他组件调用 stopService() 为止，无论其是否绑定到任何客户端。
 *
 * 干货：Thread/HandlerThread/IntentService的选择；
 *
 * startService：开启一个服务，让服务处理后台事物
 * bindService：绑定一个服务，调用服务中提供的方法
 *
 * 一个IntentService后台服务会通过广播的形式向应用报告工作状态，广播接收者在后台仍可以接受，如果需要提醒用户，可以通过Notification；
 */

public class LocalService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

}
