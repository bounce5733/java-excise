package com.jyh.excise.thread;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/18 21:13
 * @Version 1.0
 **/
public class Simple {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        Thread.sleep(1);
        t.interrupt();
        t.join();
        System.out.println("thread end...");
    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            System.out.println(n++);
        }
    }
}
