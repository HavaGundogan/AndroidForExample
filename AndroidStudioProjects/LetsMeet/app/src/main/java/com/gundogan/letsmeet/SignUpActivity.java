package com.gundogan.letsmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usernameText=findViewById(R.id.user_name_signup_activity);
        passwordText=findViewById(R.id.password_text_signup_activity);
    }

    public void signUp(View view){

        ParseUser user=new ParseUser();
        user.setUsername(usernameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"User Signed Up!!!",Toast.LENGTH_LONG).show();

                    Intent intent =new Intent(getApplicationContext(),LocationsActivity.class);
                    startActivity(intent);
                }
            }
        });



    }
    public void signIn(View view)
    {

        ParseUser.logInInBackground(usernameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
             if(e!=null){
                 Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

             }else{
                 Toast.makeText(getApplicationContext(),"Welcome:"+user.getUsername(),Toast.LENGTH_LONG).show();
                 Intent intent =new Intent(getApplicationContext(),LocationsActivity.class);
                 startActivity(intent);
                 //intent
             }

            }
        });
    }
}