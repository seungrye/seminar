package com.enji.tutorial.rxjava;

import org.junit.Test;

import java.util.function.UnaryOperator;

public class LamdaExample1 {
    private void runIt() {
        System.out.println("Hello World");
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    public void main_ThreadWithLambda() {
        Thread thread = new Thread(() -> {
            runIt();
        });
    }

    @Test
    public void main_LambdaInterfaces() {
        StringFunction exclaim = (s) -> s + "!";
        System.out.println(exclaim.apply("Hello"));

        UnaryOperator<String> ask = (String s) -> s + "?";
        System.out.println(ask.apply("Hello"));
    }

    interface StringFunction {
        String apply(String str);
    }
}
