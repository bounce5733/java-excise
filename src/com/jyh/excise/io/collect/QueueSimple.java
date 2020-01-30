package com.jyh.excise.io.collect;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 在Java的标准库中，队列接口Queue定义了以下几个方法：
 * <p>
 * int size()：获取队列长度；
 * boolean add(E)/boolean offer(E)：添加元素到队尾；
 * E remove()/E poll()：获取队首元素并从队列中删除；
 * E element()/E peek()：获取队首元素但并不从队列中删除。
 * 对于具体的实现类，有的Queue有最大队列长度限制，有的Queue没有。注意到添加、删除和获取队列元素总是有两个方法，这是因为在添加或获取元素失败时，这两个方法的行为是不同的。我们用一个表格总结如下：
 * <p>
 * throw Exception	返回false或null
 * 添加元素到队尾	add(E e)	boolean offer(E e)
 * 取队首元素并删除	E remove()	E poll()
 * 取队首元素但不删除	E element()	E peek()
 *
 * @Author jiangyonghua
 * @Date 2020/1/30 18:01
 * @Version 1.0
 **/
public class QueueSimple {

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("a");
        queue.offer("b");
        System.out.println(queue.peek()); // 不删元素
        System.out.println(queue.poll()); // 删除元素

        Queue<User> priorityQueue = new PriorityQueue<>(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                if (user1.getCode().charAt(0) == user2.getCode().charAt(0)) {
                    return user1.getCode().compareTo(user2.getCode());
                }
                if (user1.getCode().charAt(0) == 'V') { // 号码V开头的优先级高
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        /*
        PriorityQueue实现了一个优先队列：从队首获取元素时，总是获取优先级最高的元素。
        PriorityQueue默认按元素比较的顺序排序（必须实现Comparable接口），也可以通过Comparator自定义排序算法（元素就不必实现Comparable接口）。
         */
        priorityQueue.offer(new User("jack", "N1"));
        priorityQueue.offer(new User("sam", "V10"));
        priorityQueue.offer(new User("katy", "N2"));

        System.out.println(priorityQueue.poll().getName());
        System.out.println(priorityQueue.poll().getName());
        System.out.println(priorityQueue.poll().getName());
    }
}

class User {

    private String name;

    private String code;

    public User(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
