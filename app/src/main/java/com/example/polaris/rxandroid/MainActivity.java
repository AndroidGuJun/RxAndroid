package com.example.polaris.rxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="11" ;
    private Button btnClick;
    private TextView tvContent;
    private String url = "http://piao.163.com/m/movie/list.html?app_id=2&mobileType=iPhone&ver=2.5&channel=lede&deviceId=C1985DD9-0125-4AB5-B66B-B91A85824BBA&apiVer=11&city=110000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClick = (Button) findViewById(R.id.btn_click);
        tvContent = (TextView) findViewById(R.id.textview_content);

    }

    public void click(View view) {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        String content = response.body().string();
                        subscriber.onNext(content);
                    }else {
                        Log.i(TAG, "call: "+00);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Movies>() {
                    @Override
                    public Movies call(String s) {
                        Gson gson = new Gson();
                        return gson.fromJson(s,Movies.class);
                    }
                })
                .subscribe(new Subscriber<Movies>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Movies movies) {
                        List<MovieBean> moviesList = movies.getList();
                        for (MovieBean bean : moviesList) {
                            Log.d("-----", "" + bean.getName());
                        }
                    }
                })
        ;
    }
}
