package com.jyh.excise.lambda.interfun;

/**
 * TODO
 *
 * @Author jiangyonghua
 * @Date 2020/1/18 16:40
 * @Version 1.0
 **/
@FunctionalInterface
public interface MyCompare {

    boolean biggernum(int a, int b);

    default String descn() {
        return "This is my compare num FunctionInterface!";
    }
}
