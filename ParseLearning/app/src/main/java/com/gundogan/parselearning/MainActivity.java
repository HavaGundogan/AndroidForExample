package com.gundogan.parselearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject object =new ParseObject("Cars");
        object.put("name","bmw");
        object.put("price","100");

    }
}