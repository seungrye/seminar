package com.enji.tutorial.rxjava;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

public class RxCreateObservableExample1 {
    @SuppressWarnings({"Convert2MethodRef", "ResultOfMethodCallIgnored"})
    @Test
    public void createObservableWithJust() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable.subscribe(item -> System.out.println(item));
    }

    @SuppressWarnings({"Convert2MethodRef"})
    @Test
    public void createObservableFromIterable() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Observable<Integer> observable = Observable.fromIterable(list);
        observable.subscribe(item -> System.out.println(item));
    }

    @SuppressWarnings({"Convert2MethodRef", "ResultOfMethodCallIgnored"})
    @Test
    public void createObservableUsingCreate() {
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
//            emitter.onNext(null); // not a valid value
            emitter.onComplete(); // let know complete
        });
        observable.subscribe(item -> System.out.println(item),
                error -> System.out.println(error.getLocalizedMessage()),
                () -> System.out.println("Completed"));
    }
}
