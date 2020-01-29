package com.jyh.excise.thread;

import java.util.concurrent.CompletableFuture;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 22:09
 * @Version 1.0
 **/
public class CompletableFutureSimple2 {

    public static void main(String[] args) throws InterruptedException {
        // --------------串行执行--------------
        System.out.println("串行执行demo...");
        // first task
        CompletableFuture<String> getName = CompletableFuture.supplyAsync(() -> {
            String name = getName("apple");
            System.out.println("getName:" + name);
            return name;
        });

        // second task
        CompletableFuture<Double> getPrice = getName.thenApplyAsync(CompletableFutureSimple2::getPrice);

        getPrice.thenAccept((val) -> {
            System.out.println(val);
        });

        getPrice.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        Thread.sleep(3000);
        // ----------------------------------- //

        // --------------并行执行--------------
        System.out.println("并行执行demo...");
        // first query name task
        CompletableFuture<String> getName1 = CompletableFuture.supplyAsync(() -> {
            String name = getName("orange");
            System.out.println(Thread.currentThread().getName() + "获取到水果:" + name);
            return name;
        });

        // second query name task
        CompletableFuture<String> getName2 = CompletableFuture.supplyAsync(() -> {
            String name = getName("apple");
            System.out.println(Thread.currentThread().getName() + "获取到水果:" + name);
            return name;
        });

        // 用anyOf合并为一个新的CompleteFuture，任何一个返回即可
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(getName1, getName2);

        // 用两个CompleteFuture执行获取价格操作，任何一个返回即可
        CompletableFuture<Double> fetchPrice1 = cfQuery.thenApplyAsync((name) -> {
            double price = getPrice(String.valueOf(name));
            System.out.println(Thread.currentThread().getName() + name + "价格：" + price);
            return price;
        });

        CompletableFuture<Double> fetchPrice2 = cfQuery.thenApplyAsync((name) -> {
            double price = getPrice(String.valueOf(name));
            System.out.println(Thread.currentThread().getName() + name + "价格：" + price);
            return price;
        });

        // 用anyOf合并，任何一个返回即可
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(fetchPrice1, fetchPrice2);

        cfFetch.thenAccept((price) -> {
            System.out.println("最终获取到价格为：" + price);
        });

        Thread.sleep(5000);
    }

    public static String getName(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static Double getPrice(String code) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ("apple".equals(code)) {
            return 10d;
        } else {
            return 5d;
        }
    }

}
