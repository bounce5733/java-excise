package com.jyh.excise.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/24 09:16
 * @Version 1.0
 **/
public class Sync {

    public volatile static int count = 0;

    private final static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread incCountThread = new IncCount(lock);
        Thread decCountThread = new DecCount(lock);
        incCountThread.start();
        decCountThread.start();
        incCountThread.join();
        decCountThread.join();
        System.out.println("count=" + count);
    }
}

class IncCount extends Thread {

    private Lock lock;

    public IncCount(Lock lock) {
        this.lock = lock;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            try {
                lock.lock();
                Sync.count++;
            } finally {
                lock.unlock();
            }
        }
    }
}

class DecCount extends Thread {

    private Lock lock;

    public DecCount(Lock lock) {
        this.lock = lock;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            try {
                lock.lock();
                Sync.count--;
            } finally {
                lock.unlock();
            }
        }
    }
}
