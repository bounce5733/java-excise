package com.jyh.excise.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/15 22:12
 * @Version 1.0
 **/
public class Simple {

    public static void main(String[] args) throws IOException {
        Stream<Integer> numStream = Stream.of(1, 2, 3);
        Stream<Integer> bitNumStream = numStream.map(Integer::bitCount);
        bitNumStream.forEach(System.out::println);

        // 创建流-1
        Stream<String> strStream = Stream.of("jack", "bob", "sam");

        //创建流-2
        List<String> strList = new ArrayList<>();
        Stream<String> strStream2 = strList.stream();
        String[] strArr = {"jack", "bob", "sam"};
        Stream<String> strStream3 = Arrays.stream(strArr);

        //创建流-3
        Stream<Integer> intStream = Stream.generate(new NatualSupplier());
        intStream.limit(20).forEach(System.out::println);

        // 创建流-4
        Pattern p = Pattern.compile("\\s+");
        Stream<String> s = p.splitAsStream("I'm very hungry!");
        s.forEach(System.out::println);

        // 基本类型流
        IntStream is = Arrays.stream(new int[]{1, 2, 3});
        LongStream ls = is.asLongStream();
        ls.forEach(System.out::println);

        // 从文件创建流
        try (Stream<String> fs = Files.lines(Paths.get(Simple.class.getResource("demo.txt").getPath()))) {
            fs.forEach(System.out::println);
        }

        // filter
        Stream.generate(new LocalDataSupplier()).limit(20).filter(ldt -> ldt.getDayOfWeek() == DayOfWeek.SATURDAY).forEach(System.out::println);

        // reduce
        int sum = Stream.of(1, 2, 3, 4, 5).reduce(0, (acc, n) -> acc + n);
        System.out.println(sum);

        // reduce to map
        List<String> props = new ArrayList<>();
        props.add("profile=native");
        props.add("debug=true");
        props.add("name=app");
        Map<String, String> propMap = props.stream().map(kv -> {
            String[] keyval = kv.split("\\=", 2);
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put(keyval[0], keyval[1]);
            return tempMap;
        }).reduce(new HashMap<String, String>(), (m, kv) -> {
            m.putAll(kv);
            return m;
        });
        propMap.forEach((k, v) -> {
            System.out.println("[key]=" + k + "[val]=" + v);
        });
    }
}

class NatualSupplier implements Supplier<Integer> {

    int n = 0;

    @Override
    public Integer get() {
        n++;
        return n;
    }
}

class LocalDataSupplier implements Supplier<LocalDate> {

    LocalDate start = LocalDate.of(2020, 1, 1);

    int n = -1;

    @Override
    public LocalDate get() {
        n++;
        return start.plusDays(n);
    }
}
