package com.jyh.excise.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock比直接使用synchronized更安全，可以替代synchronized进行线程同步。
 * <p>
 * 但是，synchronized可以配合wait和notify实现线程在条件不满足时等待，条件满足时唤醒，用ReentrantLock我们怎么编写wait和notify的功能呢？
 * <p>
 * 答案是使用Condition对象来实现wait和notify的功能。
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 00:11
 * @Version 1.0
 **/
public class WaitNotify {

    public static void main(String[] args) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        MyQueue myQueue = new MyQueue();

        for (int i = 0; i < 5; i++) {
            Thread getThread = new Thread(() -> {
                while (true) {
                    try {
                        String msg = myQueue.get();
                        System.out.println("Get Message:" + msg);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            });
            getThread.start();
            ts.add(getThread);
        }

        Thread addThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                String msg = "message[" + i + "]";
                myQueue.add(msg);
            }
        });
        addThread.start();
        addThread.join();
        Thread.sleep(1000);
        for (Thread t : ts) {
            t.interrupt();
        }
    }
}

class MyQueue {

    private final Lock lock = new ReentrantLock();
    // Condition提供的await()、signal()、signalAll()原理和synchronized锁对象的wait()、notify()、notifyAll()是一致的，并且其行为也是一样的
    private final Condition condition = lock.newCondition();
    private Queue<String> myQueue = new LinkedList<>();

    public void add(String msg) {
        try {
            lock.lock();
            myQueue.add(msg);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String get() throws InterruptedException {
        try {
            lock.lock();
            while (myQueue.isEmpty()) {
                condition.await();
            }
            return myQueue.poll();
        } finally {
            lock.unlock();
        }
    }
}
