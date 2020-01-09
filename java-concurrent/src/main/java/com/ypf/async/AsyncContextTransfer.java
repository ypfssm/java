package com.ypf.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 场景：在微服务中的分布式链路跟踪时，traceId 如何在异步线程中传递 traceId 呢？
 *
 *
 * https://juejin.im/post/5d42ef2ef265da03cd0a5f23
 *
 *
 * 异步调用中上下文信息传递
 * @author shuaifei
 */
public class AsyncContextTransfer {


    /**
     * 通过inheritableThreadLocals我们可以在父线程创建子线程的时候将Local中的值传递给子线程，
     * 这个特性已经能够满足大部分的需求了，但是还有一个很严重的问题是如果是在线程复用的情况下就会出问题，
     * 比如线程池中去使用inheritableThreadLocals 进行传值，因为inheritableThreadLocals 只是会再新创建线程的时候进行传值，
     * 线程复用并不会做这个操作，那么要解决这个问题就得自己去扩展线程类，实现这个功能。
     *
     * 阿里开源的 transmittable-thread-local 解决了上面这个问题。那它是怎么解决的？？？
     *
     * https://www.jianshu.com/p/e0774f965aa3
     * https://github.com/alibaba/transmittable-thread-local#1-%E7%AE%80%E5%8D%95%E4%BD%BF%E7%94%A8
     */
    @Test
    public void test(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService = TtlExecutors.getTtlExecutorService(executorService);

        TransmittableThreadLocal<String> parent = new TransmittableThreadLocal<String>();
        parent.set("value-set-in-parent");
        executorService.submit(() -> System.out.println(parent.get()));
    }


}
