package com.enji.tutorial.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxDisposableExample1 {
    @Test
    public void handleDisposable() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disposable = observable.subscribe(v -> System.out.println("Observer1 : " + v));
        pause(3000);
        disposable.dispose();
        pause(3000);
    }

    @Test
    public void composableDisposable() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disposable1 = observable.subscribe(v -> System.out.println("Observer1 : " + v));
        Disposable disposable2 = observable.subscribe(v -> System.out.println("Observer2 : " + v));
        compositeDisposable.addAll(disposable1, disposable2);
        pause(3000);
        compositeDisposable.dispose();
        pause(3000);
    }

    private void pause(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
