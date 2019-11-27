package com.ypf.interview;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 多线程并发交替打印。
 *
 * 参考链接：https://juejin.im/post/5c89b9515188257e5b2befdd
 * @author shuaifei
 */
public class PrintAlternateConcurrently {

    private static int result = 0;
    private static final int N = 3;

    /**
     * 通过N个线程顺序循环打印从0至100，如给定N=3则输出:
     *      thread0: 0
     *      thread1: 1
     *      thread2: 2
     *      thread0: 3
     *      thread1: 4
     *      .....
     *
     * 解决思路： 每个线程一个信号量，当前线程在执行打印前需先获取上一个线程的信号量，
     * 当前线程打印完毕后释放自己的信号量，从而下一个线程可以获取当前线程的信号量执行打印。
     *
     * 举个例子：假设 N = 3，有三个线程 a,b,c
     * 刚开始初始化信号量状态时， a 、b 的信号量 acquire，c 的信号量处于空闲。从 a 开始执行，由于 a 的上一个线程 c 的信号量是空闲的，
     * 所以 a 可以执行打印，a 做完了打印会释放自己的信号量，也就是 a 做完了通知 b， b 做完了通知 c，.....
     *
     * @throws InterruptedException
     */
    @Test
    public void question1() throws InterruptedException {
        Thread[] threads = new Thread[N];
        Semaphore[] semaphores = new Semaphore[N];
        // 初始化信号量状态
        for (int i = 0; i < N; i++) {
            semaphores[i] = new Semaphore(1);
            if (i != N - 1) {
                semaphores[i].acquire();
            }
        }

        for (int i = 0; i < N; i++) {
            // 上一个线程的信号量
            final Semaphore lastSemaphore = i == 0 ? semaphores[N - 1] : semaphores[i-1];
            // 当前线程的信号量
            final Semaphore curSemaphore = semaphores[i];
            threads[i] = new Thread(() -> {
                try {
                    while (true) {
                        lastSemaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + ":" + result++);
                        if (result > 100) {
                            System.exit(1);
                        }
                        curSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"thead-"+i);
            threads[i].start();
        }
    }

    /**
     * 两个线程交替打印0~100的奇偶数：
     *      偶线程：0
     *      奇线程：1
     *      偶线程：2
     *      奇线程：3
     */
    @Test
    public void question2() throws InterruptedException {
        Thread evenThread = new Thread(new PrintTask(), "thread-print-even");
        Thread oddThread = new Thread(new PrintTask(), "thread-print-odd");

        evenThread.start();
        TimeUnit.MILLISECONDS.sleep(10);
        oddThread.start();
    }


    static class PrintTask implements Runnable {
        /**
         * 因为两个线程会争抢 SolutionTask 这个类的 monitor 对象，所以类变量线程安全。
         */
        static int value = 0;

        public void run() {

            while (value <= 100) {
                System.out.println(Thread.currentThread().getName() + ":" + value++);
                synchronized (PrintTask.class) {
                    PrintTask.class.notify();
                    try {
                        PrintTask.class.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
