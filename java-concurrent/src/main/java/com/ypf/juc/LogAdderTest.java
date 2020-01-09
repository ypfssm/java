package com.ypf.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author shuaifei
 */
public class LogAdderTest {

    @Test
    public void test() throws InterruptedException {
        LongAdder counter = new LongAdder();
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        int numberOfThreads = 4;
        int numberOfIncrements = 100;

        Runnable incrementAction = () -> IntStream
                .range(0, numberOfIncrements)
                .forEach(i -> counter.increment());

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(incrementAction);
        }

        executorService.shutdown();
//        try {
//            //等待所有任务结束，最多等待30分钟
//            executorService.awaitTermination(30, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (executorService.isTerminated()) {
            assertEquals(counter.sum(), numberOfIncrements * numberOfThreads);
        }
    }

    /**
     * 测试线程中断异常
     */
    public static void main(String[] args) throws InterruptedException {
        test2();
    }


    public static void test2() throws InterruptedException {
        BlockingQueue blockingQueue = new ArrayBlockingQueue(1024);
        Thread t1 = new Thread(() -> {
            try {
                // 会响应中断，且会把中断标识（interrupt flag）恢复为 false。
                blockingQueue.take();
            } catch (InterruptedException e) {
                System.out.println(" t1 first is interrupted ......");
            }
        });
        t1.start();

        TimeUnit.MILLISECONDS.sleep(100L);

        t1.interrupt();
    }

}
