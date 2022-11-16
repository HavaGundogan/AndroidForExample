package com.gundogan.denememethod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("on create called");
        makeMusicians();
        test();
    }

    public void makeMusicians () {

        Musicians james = new Musicians("james","guitar",50) ;
        System.out.println(james.instrumet);


    }

    public void test()
    {
        int x = 4+4;
        x=9;
        System.out.println("value of x:"+x);

    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on resume called");
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("on pause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on stop called");
    }
}