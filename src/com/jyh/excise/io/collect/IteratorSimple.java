package com.jyh.excise.io.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator是一种抽象的数据访问模型。使用Iterator模式进行迭代的好处有：
 * <p>
 * 对任何集合都采用同一种访问模型；
 * 调用者对集合内部结构一无所知；
 * 集合类返回的Iterator对象知道如何迭代。
 * Java提供了标准的迭代器模型，即集合类实现java.util.Iterable接口，返回java.util.Iterator实例。
 *
 * @Author jiangyonghua
 * @Date 2020/1/30 18:26
 * @Version 1.0
 **/
public class IteratorSimple {

    public static void main(String[] args) {
        ReverseList<String> list = new ReverseList<>();
        list.add("apple");
        list.add("orange");
        list.add("watermelon");
        list.add("lemon");

        for (String fruit : list) {
            System.out.println(fruit);
        }
    }
}

class ReverseList<T> implements Iterable<T> {

    private List<T> list = new ArrayList<>();

    public void add(T t) {
        list.add(t);
    }

    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator(list.size());
    }

    class ReverseIterator implements Iterator<T> {

        private int index;

        public ReverseIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            index--;
            return ReverseList.this.list.get(index);
        }
    }
}
