package com.ypf.interview;

import java.util.concurrent.TimeUnit;

/**
 * 多线程并发交替打印。
 * @author shuaifei
 */
public class ConcurrentlyPrintAlternate {

    public static void main(String[] args) throws InterruptedException {
        question1();
    }

    /**
     * 两个线程交替打印0~100的奇偶数：
     *      偶线程：0
     *      奇线程：1
     *      偶线程：2
     *      奇线程：3
     */
    public static void question1() throws InterruptedException {
        Thread evenThread = new Thread(new SolutionTask(), "thread-print-even");
        Thread oddThread = new Thread(new SolutionTask(), "thread-print-odd");

        evenThread.start();
        TimeUnit.MILLISECONDS.sleep(10);
        oddThread.start();
    }

    static class SolutionTask implements Runnable {
        /**
         * 因为两个线程会争抢 SolutionTask 这个类的 monitor 对象，所以类变量线程安全。
         */
        static int value = 0;

        public void run() {

            while (value <= 100) {
                System.out.println(Thread.currentThread().getName() + ":" + value++);
                synchronized (SolutionTask.class) {
                    SolutionTask.class.notify();
                    try {
                        SolutionTask.class.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
