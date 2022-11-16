package com.gundogan.cryptosun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gundogan.cryptosun.R;
import com.gundogan.cryptosun.adapter.RecyclerViewAdaptor;
import com.gundogan.cryptosun.model.CryptoModel;
import com.gundogan.cryptosun.service.CryptoApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
public class MainActivity extends AppCompatActivity {

    ArrayList<CryptoModel> cryptoModels;
    private String BASE_URL = "https://raw.githubusercontent.com/";

    Retrofit retrofit;

    RecyclerView recyclerView ;
    RecyclerViewAdaptor recyclerViewAdaptor;
    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.rcyclerViewId);
        //Retrofit&json
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        loadData();

    }

    private void loadData() {
       final CryptoApi cryptoApi = retrofit.create(CryptoApi.class);

       compositeDisposable = new CompositeDisposable();
       compositeDisposable.add(cryptoApi.getData()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(this::handleResponse));

       /*
        Call<List<CryptoModel>> call = cryptoApi.getData();
        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if (response.isSuccessful()) {
                    List<CryptoModel> responseList = response.body();
                    cryptoModels = new ArrayList<>(responseList);

                    //RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerViewAdaptor=new RecyclerViewAdaptor(cryptoModels);
                    recyclerView.setAdapter(recyclerViewAdaptor);

                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {

                t.printStackTrace();
            }
        });
        */

    }
    private void  handleResponse(List<CryptoModel>cryptoModelList){
        cryptoModels = new ArrayList<>(cryptoModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewAdaptor=new RecyclerViewAdaptor(cryptoModels);
        recyclerView.setAdapter(recyclerViewAdaptor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}