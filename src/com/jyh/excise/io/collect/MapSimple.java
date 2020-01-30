package com.jyh.excise.io.collect;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 正确使用Map必须保证：
 * <p>
 * 作为key的对象必须正确覆写equals()方法，相等的两个key实例调用equals()必须返回true；
 * <p>
 * 作为key的对象还必须正确覆写hashCode()方法，且hashCode()方法要严格遵循以下规范：
 * <p>
 * 如果两个对象相等，则两个对象的hashCode()必须相等；
 * 如果两个对象不相等，则两个对象的hashCode()尽量不要相等。
 * 即对应两个实例a和b：
 * <p>
 * 如果a和b相等，那么a.equals(b)一定为true，则a.hashCode()必须等于b.hashCode()；
 * 如果a和b不相等，那么a.equals(b)一定为false，则a.hashCode()和b.hashCode()尽量不要相等。
 * 上述第一条规范是正确性，必须保证实现，否则HashMap不能正常工作。
 * <p>
 * 而第二条如果尽量满足，则可以保证查询效率，因为不同的对象，如果返回相同的hashCode()，会造成Map内部存储冲突，使存取的效率下降。
 *
 * @Author jiangyonghua
 * @Date 2020/1/30 10:24
 * @Version 1.0
 **/
public class MapSimple {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 10);
        map.put("banana", 20);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer val = entry.getValue();
            System.out.println(key + "=" + val);
        }

        // Student demo
        Map<Student, Integer> studentMap = new HashMap<>();
        studentMap.put(new Student("zhang", "san", 20), 100);
        studentMap.put(new Student("xiao", "ming", 18), 200);
        System.out.println(studentMap.get(new Student("xiao", "ming", 18)));

        // EnumMap demo
        Map<DayOfWeek, String> enumMap = new EnumMap<>(DayOfWeek.class);
        enumMap.put(DayOfWeek.MONDAY, "星期一");
        enumMap.put(DayOfWeek.TUESDAY, "星期二");
        System.out.println(enumMap.get(DayOfWeek.TUESDAY));
    }
}

class Student {

    int age;
    String firstName;
    String lastName;

    public Student(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    /**
     * 注意到String类已经正确实现了hashCode()方法，我们在计算Person的hashCode()时，反复使用31*h，这样做的目的是为了尽量把不同的Person实例的hashCode()均匀分布到整个int范围。
     * <p>
     * 和实现equals()方法遇到的问题类似，如果firstName或lastName为null，上述代码工作起来就会抛NullPointerException。为了解决这个问题，我们在计算hashCode()的时候，经常借助Objects.hash()来计算：
     * <p>
     * 所以，编写equals()和hashCode()遵循的原则是：
     * <p>
     * equals()用到的用于比较的每一个字段，都必须在hashCode()中用于计算；equals()中没有使用到的字段，绝不可放在hashCode()中计算。
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(age, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student) {
            Student p = (Student) o;
            boolean firstNameEqual = false;
            if (this.firstName == null && p.firstName == null) {
                firstNameEqual = true;
            }
            if (this.firstName != null) {
                firstNameEqual = this.firstName.equals(p.firstName);
            }
            boolean lastNameEqual = false;
            if (this.lastName == null && p.lastName == null) {
                lastNameEqual = true;
            }
            if (this.lastName != null) {
                lastNameEqual = this.lastName.equals(p.lastName);
            }
            return firstNameEqual && lastNameEqual && this.age == p.age;
        }
        return false;
    }
}
