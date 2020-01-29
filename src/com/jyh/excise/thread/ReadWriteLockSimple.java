package com.jyh.excise.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock可以解决多线程同时读，但只有一个线程能写的问题。
 * <p>
 * 如果我们深入分析ReadWriteLock，会发现它有个潜在的问题：如果有线程正在读，写线程需要等待读线程释放锁后才能获取写锁，即读的过程中不允许写，这是一种悲观的读锁。
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 17:40
 * @Version 1.0
 **/
public class ReadWriteLockSimple {
    public static void main(String[] args) throws InterruptedException {

        Count count = new Count();

        Thread writeThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                count.add(i);
            }
        });

        writeThread.start();
        writeThread.join();

        Thread readThread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(count.get(i));
            }
        });


        Thread readThread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(count.get(i));
            }
        });

        readThread1.start();
        readThread2.start();
        readThread1.join();
        readThread2.join();
    }
}

class Count {

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rlock = rwl.readLock();
    private final Lock wlock = rwl.writeLock();
    private final int[] nums = new int[5];

    public void add(int index) {
        try {
            wlock.lock();
            nums[index] += 1;
        } finally {
            wlock.unlock();
        }
    }

    public int get(int index) {
        try {
            rlock.lock();
            return nums[index];
        } finally {
            rlock.unlock();
        }
    }
}
