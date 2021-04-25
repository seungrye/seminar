package com.enji.tutorial.rxjava;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulerExample1 {
    @Test
    public void observableObserveOn_n_SubscribeOn() {
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                System.out.println(String.format("observable. subscribe on Thread : %s ", Thread.currentThread().getName()));
                emitter.onNext(Long.valueOf(1));
                emitter.onNext(Long.valueOf(2));
                emitter.onComplete(); // let know complete
            }
        });

        Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println(String.format("observer. onSubscribe on Thread : %s ", Thread.currentThread().getName()));
            }

            @Override
            public void onNext(@NonNull Long aLong) {
                System.out.println(String.format("observer. onNext on Thread : %s ", Thread.currentThread().getName()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(String.format("observer. onError on Thread : %s ", Thread.currentThread().getName()));
            }

            @Override
            public void onComplete() {
                System.out.println(String.format("observer. onComplete on Thread : %s ", Thread.currentThread().getName()));
            }
        };
        System.out.println("Current Thread : " + Thread.currentThread().getName());

        observable
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

        pause(1000);
    }

    @Test
    public void observableOnIo() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS); // interval observeOn computation scheduler
        System.out.println("Current Thread : " + Thread.currentThread().getName());

        for (int i = 0; i < 30; i++) {  // multiple (30) subscribers
            observable.observeOn(Schedulers.io())  // concurrent
                    .doOnNext(v -> System.out.println("observe on Thread : " + Thread.currentThread().getName()))
                    .subscribe(v -> System.out.println(String.format("3. value %s. on subscribe Thread: %s", v, Thread.currentThread().getName())));
        }

        pause(3000);
    }

    @Test
    public void observableOnComputation() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        System.out.println("Current Thread : " + Thread.currentThread().getName());
        for (int i = 0; i < 30; i++) { // multiple (30) subscribers
            observable.observeOn(Schedulers.computation())  // parallel
                    .doOnNext(v -> System.out.println("observe on Thread : " + Thread.currentThread().getName()))
                    .subscribe(v -> System.out.println(String.format("value %s. on subscribe Thread: %s", v, Thread.currentThread().getName())));
        }

        pause(3000);
    }

    @Test
    public void observableOnCustomScheduler() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        System.out.println("Current Thread : " + Thread.currentThread().getName());

        Executor executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 30; i++) { // multiple (30) subscribers
            observable.observeOn(Schedulers.from(executor))  // custom executor
                    .doOnNext(v -> System.out.println("observe on Thread : " + Thread.currentThread().getName()))
                    .subscribe(v -> System.out.println(String.format("value %s. on subscribe Thread: %s", v, Thread.currentThread().getName())));
        }

        pause(3000);
    }

    @Test
    public void observableOnMixedSchedulers() {
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                System.out.println(String.format("observable. subscribe on Thread : %s ", Thread.currentThread().getName()));
                for (Long i = Long.valueOf(0); i < 10; ++i) {
                    emitter.onNext(i);
                    Thread.sleep(10);
                }
                emitter.onComplete(); // let know complete
            }
        });
        System.out.println("Current Thread : " + Thread.currentThread().getName());

        Executor custom_executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 1; i++) { // multiple (30) subscribers
            observable.observeOn(Schedulers.from(custom_executor))  // custom executor
                    .doOnNext(v -> System.out.println(String.format("observer.1. observe value : %d. on Thread : %s ", v, Thread.currentThread().getName())))
                    .observeOn(Schedulers.computation())
                    .doOnNext(v -> System.out.println(String.format("observer.2. observe value : %d. on Thread : %s ", v, Thread.currentThread().getName())))
                    .filter(v -> v % 2 == 0)
                    .observeOn(Schedulers.io())
                    .doOnNext(v -> System.out.println(String.format("observer.3. observe value : %d. on Thread : %s ", v, Thread.currentThread().getName())))
                    .subscribeOn(Schedulers.newThread())  // ObservableOnSubscribe.subscribe 가 실행될 쓰레드 지정
                    .subscribe(v -> System.out.println(String.format("observer.4. value %s. on subscribe Thread: %s", v, Thread.currentThread().getName())));
        }

        pause(800);
    }


    private void pause(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
