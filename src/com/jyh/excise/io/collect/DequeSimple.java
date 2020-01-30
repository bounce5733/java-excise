package com.jyh.excise.io.collect;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Java集合提供了接口Deque来实现一个双端队列，它的功能是：
 * <p>
 * 既可以添加到队尾，也可以添加到队首；
 * 既可以从队首获取，又可以从队尾获取。
 * 我们来比较一下Queue和Deque出队和入队的方法：
 * <p>
 * Queue	Deque
 * 添加元素到队尾	add(E e) / offer(E e)	addLast(E e) / offerLast(E e)
 * 取队首元素并删除	E remove() / E poll()	E removeFirst() / E pollFirst()
 * 取队首元素但不删除	E element() / E peek()	E getFirst() / E peekFirst()
 * 添加元素到队首	无	addFirst(E e) / offerFirst(E e)
 * 取队尾元素并删除	无	E removeLast() / E pollLast()
 * 取队尾元素但不删除	无	E getLast() / E peekLast()
 * 对于添加元素到队尾的操作，Queue提供了add()/offer()方法，而Deque提供了addLast()/offerLast()方法。添加元素到对首、取队尾元素的操作在Queue中不存在，在Deque中由addFirst()/removeLast()等方法提供。
 *
 * @Author jiangyonghua
 * @Date 2020/1/30 18:17
 * @Version 1.0
 **/
public class DequeSimple {

    public static void main(String[] args) {
        Deque<String> deque = new LinkedList<>();
        deque.offerFirst("a");
        deque.offerFirst("b");
        deque.offerFirst("c");
        System.out.println(deque.pollFirst());
        System.out.println(deque.pollLast());
        System.out.println(deque.pollLast());
        System.out.println(deque.pollLast());
    }
}
