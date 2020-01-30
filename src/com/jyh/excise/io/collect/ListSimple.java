package com.jyh.excise.io.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author jiangyonghua
 * @Date 2020/1/30 09:27
 * @Version 1.0
 **/
public class ListSimple {

    public static void main(String[] args) {

        // list通过equals来判断，string和基本类型已经实现了equals
        List<String> strList = new ArrayList<>();
        strList.add("a");
        strList.add("C");
        System.out.println(strList.contains(new String("C"))); // true
        System.out.println(strList.indexOf(new String("a"))); // 0 not -1

        List<Person> personList = new ArrayList<>();
        personList.add(new Person("jack", 17));
        personList.add(new Person("sam", 40));
        System.out.println(personList.contains(new Person("jack", 17))); //true
        System.out.println(personList.indexOf(new Person("sam", 40))); // 1

        // 随机交换list中的元素
        System.out.println("Before shuffle:" + personList);
        Collections.shuffle(personList);
        System.out.println("After shuffle:" + personList);
    }
}

class Person {

    int age;
    String name;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 因此，我们总结一下equals()方法的正确编写方法：
     * * <p>
     * * 先确定实例“相等”的逻辑，即哪些字段相等，就认为实例相等；
     * * 用instanceof判断传入的待比较的Object是不是当前类型，如果是，继续比较，否则，返回false；
     * * 对引用类型用Objects.equals()比较，对基本类型直接用==比较。
     * * 使用Objects.equals()比较两个引用类型是否相等的目的是省去了判断null的麻烦。两个引用类型都是null时它们也是相等的。
     * * <p>
     * * 如果不调用List的contains()、indexOf()这些方法，那么放入的元素就不需要实现equals()方法。
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            Person p = (Person) o;
            boolean equals = false;
            if (this.name == null && p.name == null) {
                equals = true;
            }
            if (this.name != null) {
                equals = this.name.equals(p.name);
            }
            return equals && this.age == p.age;
        }
        return false;
    }
}
