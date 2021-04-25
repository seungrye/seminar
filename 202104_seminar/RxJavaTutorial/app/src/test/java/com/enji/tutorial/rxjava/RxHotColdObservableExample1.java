package com.enji.tutorial.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

public class RxHotColdObservableExample1 {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void createColdObservable() {
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);
        myObservable.subscribe(item -> System.out.println("Observer 1: " + item));
        pause(3000);
        myObservable.subscribe(item -> System.out.println("Observer 2: " + item));
        pause(5000);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void createConnectableObservable() {
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);
        ConnectableObservable<Long> connectableObservable = myObservable.publish();
        connectableObservable.subscribe(item -> System.out.println("Observer 1: " + item));
        connectableObservable.connect();
        pause(3000);
        connectableObservable.subscribe(item -> System.out.println("Observer 2: " + item));
        pause(5000);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @Test
    public void createHotObservable() {
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> hotObservable = myObservable.publish().refCount();  //  stays connected to this ConnectableObservable as long as there is at least one subscription.
        Disposable subscription1 = hotObservable
                .doOnSubscribe(d -> System.out.println("Observer 1 subscribed"))
                .doFinally(() -> System.out.println("Observer 1 unsubscribed"))
                .subscribe(item -> System.out.println("Observer 1: " + item));
        pause(3000);
        Disposable subscription2 = hotObservable
                .doOnSubscribe(d -> System.out.println("Observer 2 subscribed"))
                .doFinally(() -> System.out.println("Observer 2 unsubscribed"))
                .subscribe(item -> System.out.println("Observer 2: " + item));
        pause(3000);
        subscription1.dispose(); // dispose : ~을 없애다[처리하다]
        subscription2.dispose();
        hotObservable
                .doOnSubscribe(d -> System.out.println("Observer 3 subscribed"))
                .doFinally(() -> System.out.println("Observer 3 unsubscribed"))
                .subscribe(item -> System.out.println("Observer 3: " + item));
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
