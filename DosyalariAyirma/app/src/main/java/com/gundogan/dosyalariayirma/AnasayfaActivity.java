package com.gundogan.dosyalariayirma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class AnasayfaActivity extends AppCompatActivity {
    private AnasayfaActivityBinding binding;
    ArrayList<NotConcructer> notConcructerArrayList;
    NotAdapter notAdapter;
    EditText date,time;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =AnasayfaActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        notConcructerArrayList = new ArrayList<NotConcructer>();

        binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        notAdapter= new NotAdapter(notConcructerArrayList);
        binding.recylerView.setAdapter(notAdapter);
        getData(); 
    }

    //verileri getir
    public void getData()
    {
        try {
            SQLiteDatabase sqLiteDatabase =this.openOrCreateDatabase("NotDefteri",MODE_PRIVATE,null);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT *FROM notlar",null);
            int nameIx=cursor.getColumnIndex("notum");
            int idIx=cursor.getColumnIndex("id");

            while (cursor.moveToNext())
            {
                String notum= cursor.getString(nameIx);
                int id=cursor.getInt(idIx);
                NotConcructer nottext= new NotConcructer(notum,id);
                notConcructerArrayList.add(nottext);
            }
            notAdapter.notifyDataSetChanged();
            cursor.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.artmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuid) {
            Intent intent = new Intent(AnasayfaActivity.this, ArtActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}