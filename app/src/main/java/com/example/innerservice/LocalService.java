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
