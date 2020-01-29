package com.jyh.excise.thread;

import java.util.concurrent.*;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 20:33
 * @Version 1.0
 **/
public class FutureSimple {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(5);

        Callable<String> task = new FutureTask();

        Future<String> future = es.submit(task);

        String result = future.get(); // 可能阻塞，主线程也会被迫等待

        System.out.println(result);
    }

}

class FutureTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName() + "[" + System.currentTimeMillis() + "]";
    }
}
