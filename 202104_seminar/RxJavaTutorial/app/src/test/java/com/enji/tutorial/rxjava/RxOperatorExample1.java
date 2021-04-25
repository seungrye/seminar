package com.enji.tutorial.rxjava;

import org.junit.Test;

import io.reactivex.Observable;

public class RxOperatorExample1 {
    int repeat = 3;
    int start = 0;

    @SuppressWarnings({"ResultOfMethodCallIgnored", "Convert2MethodRef"})
    @Test
    public void operatorRange() { // loop using rx
        Observable<Integer> observable = Observable.range(0, 3);
        observable.subscribe((x) -> System.out.println(x));
    }

    @Test
    public void operatorDefer() {
        Observable<Integer> observable = Observable.defer(() -> {
            System.out.println("start : " + start + ", repeat: " + repeat);
            return Observable.range(start, repeat);
        });
        observable.subscribe((x) -> System.out.println(x));

        repeat = 2; // change repeat count
        observable.subscribe((x) -> System.out.println(x));
    }

    @Test
    public void operatorMap() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        // Note. map 함수의 파라메터를 보면 Function 이라는 클래스 이며, Function::apply 함수의
        // parameter 와 return 의 타입이 다른것을 알수 있음. 따라서, int 를 파라메터로 받아서 string 을 반환할수도 있다.
        observable.map(v -> "Hello Number " + v)
                .subscribe(v -> System.out.println("subscribed : " + v));
    }

    @Test
    public void operatorFilter() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        System.out.println("Try print even numbers only");
        observable.filter(v -> v % 2 == 0)
                .subscribe(v -> System.out.println("subscribed : " + v));
    }

    @Test
    public void combinationOperators() {
        Observable<Integer> observable = Observable.just(-7, -6, -3, -2, 1, 4, 5, 8);
        System.out.println("Try make positive and print even numbers only");
        observable.map(v -> (v < 0) ? -1 * v : v)
                .filter(v -> v % 2 == 0)
                .subscribe(v -> System.out.println("subscribed : " + v));
    }

    @Test
    public void operatorDistinct() {
        Observable<Integer> observable = Observable.just(-4, -3, -2, -1, 1, 2, 3, 4);
        System.out.println("Try make positive and print numbers only except duplicates");
        observable.map(v -> (v < 0) ? -1 * v : v)
                .sorted()
                .distinct()
                .subscribe(v -> System.out.println("subscribed : " + v));
    }

    @Test
    public void operatorFlatMap() {
        Observable<String> observable = Observable.just("foo", "bar", "who");
        observable.map(v -> {
            switch (v) {
                case "foo":
                    return Observable.empty();
                case "bar":
                    return Observable.fromArray(v.split(""));
                default:
                    return Observable.just(v);
            }
        }).subscribe(v -> System.out.println(v));
    }

    @Test
    public void operatorFlatMapWithObservableSource() {
        Observable<String> observable = Observable.just("foo", "bar", "who");
        observable.flatMap(v -> {
                    switch (v) {
                        case "foo":
                            return Observable.empty();
                        case "bar":
                            return Observable.fromArray(v.split(""));
                        default:
                            return Observable.just(v);
                    }
                },
                (actual, second) -> actual + " " + second) // 마블 다이어그램이랑 보면 이해됨.
                .subscribe(v -> System.out.println(v));
    }
}
