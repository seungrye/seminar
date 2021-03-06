package com.enji.tutorial.rxjava;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;


// https://www.raywenderlich.com/2071847-reactive-programming-with-rxandroid-in-kotlin-an-introduction
// ref : https://medium.com/@31536k_/rxandroid-tutorial-raywenderlich-d949ac277f21
public class MainActivity extends AppCompatActivity {

    final static String TAG = "RXJ";
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Observable<?> searchTextObservable = createButtonClickObservable();
        Disposable searchTextDisposable = searchTextObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> Toast.makeText(getApplicationContext(), (CharSequence) v, Toast.LENGTH_SHORT).show());

        Observable<CharSequence> textChangeObservable = createTextChangeObservable();
        Disposable textChangeDisposable = textChangeObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    TextView textView = findViewById(R.id.textView);
                    textView.setText(v);
                });

        compositeDisposable.addAll(searchTextDisposable, textChangeDisposable);
    }

    // 1. strings??? ???????????? observable??? ???????????? ???????????? ????????????
    private Observable<String> createButtonClickObservable() {
        // 2. new ObservableOnSubscribe ??? ??????????????? Observable.create()??? ???????????? observable??? ????????????
        return Observable.create(new ObservableOnSubscribe<String>() {
            //3. subscribe()??? ????????????????????? ObservableOnSubscribe??? ????????????.
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                Button button = findViewById(R.id.button);
                //4. ????????? OnClickListener ??? ??????
                button.setOnClickListener(new View.OnClickListener() {
                    // 5. ?????? ???????????? ????????????, emitter.onNext??? ???????????? editText ??? ???????????? ????????????.
                    @Override
                    public void onClick(View v) {
                        EditText editText = findViewById(R.id.editText);
                        emitter.onNext(editText.getText().toString());
                    }
                });

                // 6. ObservableEmitter ??? setCancellable ??? cancel()??? ???????????? Observable ??? ????????? ??? ????????? clickListener ??? ???????????? ??????
                // ????????? ??? ????????? ?????? ?????? ????????????. Observable ??? completed ????????? Observer ??? unsubscribe ??? ??? ????????????.
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        // 7. setOnClickListener(null)??? clickListener ??? ?????????
                        button.setOnClickListener(null);
                    }
                });
            }
        });
    }

    private Observable<CharSequence> createTextChangeObservable() {
        return Observable.create(new ObservableOnSubscribe<CharSequence>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<CharSequence> emitter) throws Exception {
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        emitter.onNext(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };

                EditText editText = findViewById(R.id.editText);
                editText.addTextChangedListener(textWatcher);


                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        editText.removeTextChangedListener(textWatcher);
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}