package com.jyh.excise.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock和ReadWriteLock相比，改进之处在于：读的过程中也允许获取写锁后写入！这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。
 * <p>
 * 乐观锁的意思就是乐观地估计读的过程中大概率不会有写入，因此被称为乐观锁。反过来，悲观锁则是读的过程中拒绝有写入，也就是写入必须等待。显然乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行。
 * <p>
 * StampedLock还提供了更复杂的将悲观读锁升级为写锁的功能，它主要使用在if-then-update的场景：即先读，如果读的数据满足条件，就返回，如果读的数据不满足条件，再尝试写。
 *
 * @Author jiangyonghua
 * @Date 2020/1/27 18:10
 * @Version 1.0
 **/
public class StampedLockSimple {

    public static void main(String[] args) throws InterruptedException {

        Point point = new Point();
        Thread writeThread = new Thread(() -> {
            point.move(100, 200);
        });
        writeThread.start();
        writeThread.join();

        Thread readThread = new Thread(() -> {
            System.out.println(point.distanceFromOrigin());
        });
        readThread.start();
        readThread.join();
    }
}

class Point {

    private final StampedLock stampedLock = new StampedLock();
    int x;
    int y;

    public void move(double x, double y) {
        long stamp = stampedLock.writeLock();
        try {
            this.x += x;
            this.y += y;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获取乐观读锁，stamp是一个版本号
        // 注意洗面两步并非原子操作
        //假设现在是(100,200)
        double currentX = this.x; // currentX = 100
        // 这时候其他线程写入了(300,400)
        double currentY = this.y; // currentY = 400
        // 现在结果就是(100,400)
        if (!stampedLock.validate(stamp)) { // 校验版本号，检查乐观读锁后是否有其他写锁发生
            stamp = stampedLock.readLock(); //获取悲观读锁
            try {
                currentX = this.x;
                currentY = this.y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}

