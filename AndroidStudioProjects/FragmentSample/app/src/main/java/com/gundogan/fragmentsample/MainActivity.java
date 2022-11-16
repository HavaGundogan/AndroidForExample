package com.gundogan.fragmentsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToFirst(View view)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        FirstFragment firstFragment=new FirstFragment();
        fragmentTransaction.replace(R.id.FrameLayoutId,firstFragment).commit();

    }
    public void goToSecond(View view)
    {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        SecondFragment secondFragment= new SecondFragment();
        //fragmentTransaction.add(R.id.FrameLayoutId,secondFragment).commit();
        fragmentTransaction.replace(R.id.FrameLayoutId,secondFragment).commit();

    }
}