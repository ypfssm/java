package com.ypf.async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 方法后缀加 Async ： 使用传递式的 Executor 线程池去执行任务。
 * 方法后缀不加 Async ： 使用调用线程运行下一个执行线程阶段。
 *
 * thenRun()：在两个任务 A、B 中，任务 B 不需要任务 A 的计算后的返回值。
 * thenAccept()：在两个任务 A、B 中，任务 B 想要任务 A 计算的结果，且希望 B 执行后返回的 CompletableFuture 中无返回值。
 * thenApply()：在两个任务 A、B 中，任务 B 想要任务 A 计算的结果，且希望 B 执行后返回的 CompletableFuture 中有返回值。
 *
 *
 * https://juejin.im/post/5ca47aa0e51d457131257269#heading-14
 *
 * @author shuaifei
 */
public class CompletableFutureDemoTest {

    /**
     * 异常处理
     */
    @Test
    public void testHandleException() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
        })
                .exceptionally(ex -> "exceptionResultA")
                .thenApply(resultA -> resultA + " resultB");

        assertEquals("exceptionResultA resultB",cf.get());
    }

    /**
     * 多个异步任务协调执行。比如：在两个任务任务 A，任务 B 中，任务 B 想要任务 A 计算的结果，
     * 可以用 thenApply 方法来接受一个函数实例。
     */
    @Test
    public void testManyTaskCoordinationToWork() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "resultA")
                .thenApply(resultA -> resultA + " resultB");

        assertEquals("resultA resultB", cf.get());
    }

    @Test
    public void testMethodSuffixWithAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture
                .thenApplyAsync(s -> s + " World");

        assertEquals("Hello World", future.get());
    }

    @Test
    public void testMethodSuffixWithoutAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture
                .thenApply(s -> s + " World");

        assertEquals("Hello World", future.get());
    }
}
