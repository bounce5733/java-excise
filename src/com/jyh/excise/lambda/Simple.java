package com.jyh.excise.lambda;

import com.jyh.excise.lambda.interfun.GreetService;
import com.jyh.excise.lambda.interfun.MyCompare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/15 19:58
 * @Version 1.0
 **/
public class Simple {


    public static void main(String[] args) {
        // 方法引用
        String[] strArr = {"3", "1", "2"};
        Arrays.sort(strArr, String::compareTo);
        System.out.println(String.join(",", strArr));

        // 构造方法引用
        List<String> names = new ArrayList<>();
        names.add("bob");
        names.add("jack");
        List<Person> persons = names.stream().map(Person::new).collect(Collectors.toList());
        System.out.println(persons);

        // my interface function
        GreetService greet = msg -> System.out.println(msg);
        greet.say("hello jack!");

        // my compare function
        MyCompare compare = (a, b) -> a > b;
        System.out.println(compare.descn());
        System.out.println("1 > 2:" + compare.biggernum(1, 2));
    }

}

class Person {
    String name;

    public Person(String name) {
        this.name = name;
    }

    public String toString() {
        return "Person:" + this.name;
    }
}