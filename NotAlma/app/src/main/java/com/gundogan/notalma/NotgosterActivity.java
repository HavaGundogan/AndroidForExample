package com.gundogan.notalma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gundogan.notalma.databinding.ActivityMainBinding;
import com.gundogan.notalma.databinding.ActivityNotgosterBinding;

import java.util.ArrayList;

public class NotgosterActivity extends AppCompatActivity {

    private ActivityNotgosterBinding binding;//menu icin
    ArrayList<Nots> notsArrayList;
    NotAdapter notAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotgosterBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        notsArrayList = new ArrayList<>();
        binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        notAdapter= new NotAdapter(notsArrayList);
        binding.recylerView.setAdapter(notAdapter);

        getData();

    }

    private void getData()
    {
        try {
            SQLiteDatabase sqLiteDatabase =this.openOrCreateDatabase("NotAlma",MODE_PRIVATE,null);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM notlar",null);
            int notumIx=cursor.getColumnIndex("gnot");
            int idIx=cursor.getColumnIndex("id");
            int tarihimIx=cursor.getColumnIndex("gtarih");
            int saatimIx=cursor.getColumnIndex("gsaat");
            while (cursor.moveToNext())
            {
                String notum=cursor.getString(notumIx);
                int id=cursor.getInt(idIx);
                String tarihim=cursor.getString(tarihimIx);
                String saatim=cursor.getString(saatimIx);
                Nots nots=new Nots(notum,tarihim,saatim,id);
                notsArrayList.add(nots);
            }

            notAdapter.notifyDataSetChanged();
            cursor.close();


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //menuyu baglama islemleri
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.not_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)//menuye t??klan??nca ne olacag??
    {
        if (item.getItemId()==R.id.add_not){
            Intent intent=new Intent(this,NotalActivity.class);//nereye gidecegimizi yazdik
            intent.putExtra("info","new");
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}