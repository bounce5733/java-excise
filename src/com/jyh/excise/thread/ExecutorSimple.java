package com.jyh.excise.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 20:11
 * @Version 1.0
 **/
public class ExecutorSimple {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            es.submit(new Task("task[" + i + "]"));
        }

        es.shutdown();

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 10; i++) {
            ses.scheduleAtFixedRate(new Task("task[" + i + "]"), 2, 1, TimeUnit.SECONDS);
        }


        Thread.sleep(10000);

        ses.shutdown();
    }
}

class Task implements Runnable {

    private final String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "begin execute task:" + taskName);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "finished execute task:" + taskName);
    }
}
