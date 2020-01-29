package com.jyh.excise.thread;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join线程池在Java标准库中就有应用。Java标准库提供的java.util.Arrays.parallelSort(array)可以进行并行排序，它的原理就是内部通过Fork/Join对大数组分拆进行并行排序，在多核CPU上就可以大大提高排序的速度。
 * <p>
 * Fork/Join是一种基于“分治”的算法：通过分解任务，并行执行，最后合并结果得到最终结果。
 * <p>
 * ForkJoinPool线程池可以把一个大任务分拆成小任务并行执行，任务类必须继承自RecursiveTask或RecursiveAction。
 * <p>
 * 使用Fork/Join模式可以进行并行计算以提高效率。
 *
 * @Author jiangyonghua
 * @Date 2020/1/28 17:49
 * @Version 1.0
 **/
public class ForkJoinSimple {

    private static final int TOTAL = 10000;

    public static void main(String[] args) {

        long[] array = new long[TOTAL];

        for (int i = 0; i < TOTAL; i++) {
            array[i] = random();
        }

        long exceptSum = 0;
        // 单线程计算
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TOTAL; i++) {
            exceptSum += array[i];
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Single thread sum:" + exceptSum + " in " + (endTime - startTime) + "ms");

        //fork/join
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        endTime = System.currentTimeMillis();
        System.out.println("Form/Join sum:" + result + " in " + (endTime - startTime) + "ms");
    }

    static Random r = new Random(0);

    static long random() {
        return r.nextInt(TOTAL);
    }
}

class SumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 500;
    private long[] array;
    private int start;
    private int end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        if (end - start <= THRESHOLD) { // 小于阀值一次性直接计算
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        // 任务太大，一分为二
        int middle = (end + start) / 2;
        System.out.println(String.format("split %d ~ %d ==> %d ~ %d, %d ~ %d", start, end, start, middle, middle, end));
        SumTask task1 = new SumTask(array, start, middle);
        SumTask task2 = new SumTask(array, middle, end);
        invokeAll(task1, task2);
        Long result1 = task1.join();
        Long result2 = task2.join();
        long result = result1 + result2;
        System.out.println("result = " + result1 + "+" + result2 + "=" + result);
        return result;
    }
}
