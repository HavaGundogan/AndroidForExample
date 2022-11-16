package com.gundogan.dosyalariayirma;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ArtActivity extends AppCompatActivity {


    private ActivityArtBinding binding;
    ArrayList<NotConcructer> notConcructerArrayList;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String>permissionLauncher;
    Bitmap secilenGorsel;
    SQLiteDatabase database;
    EditText date,time;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher();
        database = this.openOrCreateDatabase("NotDefteri",MODE_PRIVATE,null);


        Intent intent=getIntent();
        String info= intent.getStringExtra("info");
        if (info.matches("new"))
        {
            binding.notText.setText("");
            binding.buttonKaydet.setVisibility(View.VISIBLE);

            Bitmap selectimage= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.handmaking);
            binding.imageView2.setImageBitmap(selectimage);
        }else
        {
            int artid=intent.getIntExtra("artid",1);
            binding.buttonKaydet.setVisibility(View.INVISIBLE);
        }
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
            cursor.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        //tarih ve zaman icin
        date = findViewById(R.id.dateInput);
        time=findViewById(R.id.timeInput);

        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(date);

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(time);

            }
        });



    }
    private void showTimeDialog(EditText time) {
        calendar=Calendar.getInstance();

        timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(calendar.getTime()));


            }
        };

        new TimePickerDialog(ArtActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();



    }
    private void showDateDialog(EditText date) {
        calendar=Calendar.getInstance();

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                date.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(ArtActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();




    }
    public void fotografSec(View view)
    {
        //izin verilmemiş mi kontrol et,
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"İzne ihtiyacım var",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                //reguest permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }



        }
        else
        {
            //galeriye git
            Intent intentgalerry = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentgalerry);

        }
    }
    //resaultlauncer in ne yapacagını burda belirleyecegiz
    private void registerLauncher()
    {
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== RESULT_OK)
                {
                    Intent intentFromResult =result.getData();
                    if (intentFromResult!= null)
                    {
                        Uri imageData= intentFromResult.getData();
                        binding.imageView2.setImageURI(imageData);
                        try {

                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageData);
                            secilenGorsel = ImageDecoder.decodeBitmap(source);
                            binding.imageView2.setImageBitmap(secilenGorsel);

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } );


        permissionLauncher= registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if(result)
                {
                    //izin verildi(permission granted)
                    Intent intentgalerry = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentgalerry);
                }
                else
                {
                    //izin verilmedi(permission denied)
                    Toast.makeText(ArtActivity.this, "İzin Lazım", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void kaydet(View view){

        //kaydedilecek text
        String not = binding.notText.getText().toString();

        System.out.println("sana da hello");
        //kaydedilecek gorselin byte seklinde hazırlanmasi
        // Bitmap kucukgorsel=gorseliKucult(secilenGorsel,300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //kucukgorsel.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray=  outputStream.toByteArray();
        try {
            database = this.openOrCreateDatabase("NotDefteri",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS notlar(id INTEGER PRIMARY KEY,notum NVARCHAR,gorsel BLOB)");
            String sqlString = "INSERT INTO notlar(notum,gorsel) VALUES (?,?)";

            SQLiteStatement sqLiteStatement =database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,not);
            sqLiteStatement.bindBlob(2,byteArray);

            sqLiteStatement.execute();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //bildirim islemleri
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

        //notification code goes here
        NotificationCompat.Builder builder= new NotificationCompat.Builder(ArtActivity.this,"My Notification");
        builder.setContentTitle("Bildirim");
        builder.setContentText("Yeni bir not kaydedildi.");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ArtActivity.this);
        managerCompat.notify(1,builder.build());





        Intent intent=new Intent(ArtActivity.this,MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//bundan onceki butun aktivityleri kapat sadece gidecegimi ac
        startActivity(intent);

    }


/*    public Bitmap gorseliKucult(Bitmap image,int maximumsize)
    {
        int width=image.getWidth();
        int height=image.getHeight();
        float bitmapRatio= (float) width/(float)height;
        if (bitmapRatio>1)
        {

            //landspace image
            width=maximumsize;
            height=(int) (width/bitmapRatio);
        }else
        {
            //portrait image
            height=maximumsize;
            width=(int) (height*bitmapRatio);
        }
        return image.createScaledBitmap(image,width,height,true);
    }

 */
}