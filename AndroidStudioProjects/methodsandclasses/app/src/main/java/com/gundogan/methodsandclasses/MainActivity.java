package com.gundogan.methodsandclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("on create called");


    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on resume called");
    }
    @Override
        protected void onStop () {
            super.onStop();
            System.out.println("on stop called");


        }

    }




