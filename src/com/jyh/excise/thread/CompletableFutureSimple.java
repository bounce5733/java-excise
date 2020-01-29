package com.jyh.excise.thread;

import java.util.concurrent.CompletableFuture;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 21:49
 * @Version 1.0
 **/
public class CompletableFutureSimple {

    public static void main(String[] args) throws InterruptedException {

        // 创建异步执行任务
        /*
        public interface Supplier<T> {
            T get();
        }
         */
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(CompletableFutureSimple::fetchPrice);
        /*
        public interface Consumer<T> {
            void accept(T t);
         }
         */
        cf.thenAccept((result) -> {
            System.out.println("receive val:" + result);

        });
        /*
        public interface Function<T, R> {
            R apply(T t);
        }
         */
        cf.exceptionally((error) -> {
            error.printStackTrace();
            return null;
        });

        Thread.sleep(3000);
    }

    public static double fetchPrice() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double result = Math.random();
        if (result < 0.3) {
            throw new RuntimeException("invalid value!!!");
        }
        return result;
    }
}

