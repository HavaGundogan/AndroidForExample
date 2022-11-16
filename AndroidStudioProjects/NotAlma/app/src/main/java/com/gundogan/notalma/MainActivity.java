package com.gundogan.notalma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ikincisayfayagec=(Button)findViewById(R.id.ikincisayfagecis);

        ikincisayfayagec.setOnClickListener(new View.OnClickListener() //mainden notgoster sayfasına gecis
        {
            @Override
            public void onClick(View view) {
                Intent ikincisayfagecis=new Intent(MainActivity.this, NotgosterActivity.class);
                startActivity(ikincisayfagecis);
            }
        });
    }

}