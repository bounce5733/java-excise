package com.jyh.excise.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/17 21:10
 * @Version 1.0
 **/
public class Simple2 {

    public static void main(String[] args) {
        // to set
        System.out.println("---------to set---------");
        Stream<String> fruits = Stream.of("apple", "banana", "apple");
        Set<String> fruitsSet = fruits.collect(Collectors.toSet());
        fruitsSet.stream().forEach(System.out::println);

        // to sorted array
        System.out.println("---------to distinct sorted array---------");
        fruits = Stream.of("apple", "banana", "apple");
        String[] fruitsArr = fruits.sorted(String::compareTo).distinct().toArray(String[]::new);
        for (String fruit : fruitsArr) {
            System.out.println(fruit);
        }
        // to map
        System.out.println("---------to map---------");
        Stream<String> fruitsColor = Stream.of("apple:green", "banana:yellow");
        Map<String, String> fruitsColorMap = fruitsColor.collect(Collectors.toMap(s ->
                        s.substring(0, s.indexOf(":"))
                , s -> s.substring(s.indexOf(":") + 1)));
        fruitsColorMap.forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });

        // group by first letter
        System.out.println("---------group student---------");
        Stream<Student> students = Stream.of(new Student("一班", "小明"), new Student("二班", "小王"), new Student("二班", "小李"));
        Map<String, List<Student>> groupFruits = students.collect(Collectors.groupingBy(s -> s.className, Collectors.toList()));

        groupFruits.forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });

        // concat two stream
        System.out.println("---------concat two stream---------");
        Stream<String> stream1 = Stream.of("a", "b", "c");
        Stream<String> stream2 = Stream.of("d", "e", "f");
        Stream<String> twoStream = Stream.concat(stream1, stream2);
        twoStream.forEach(System.out::println);

        // flat map stream
        System.out.println("---------flat map stream---------");
        Stream<List<Integer>> stream3 = Stream.of(Arrays.asList(1, 3, 5), Arrays.asList(2, 6, 4));
        Stream<Integer> flatedStream = stream3.parallel().flatMap(list -> list.stream());
        flatedStream.forEach(System.out::println);

        // find max item
        System.out.println("---------find max item---------");
        Stream<Integer> stream4 = Stream.of(3, 2, 1, 4);
        Integer maxnum = stream4.max(Integer::compareTo).get();
        System.out.println("max item is:" + maxnum);
    }
}

class Student {
    String className;
    String name;

    public Student(String className, String name) {
        this.className = className;
        this.name = name;
    }

    @Override
    public String toString() {
        return className + ":" + name;
    }
}




