package com.example.mybucketlist.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;

import java.io.IOException;
import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity {
    public static String url = "http://minpark307.hopto.org:88";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkUtil.setNetworkPolicy();

        TextView id_text = findViewById(R.id.login_email);
        TextView pw_text = findViewById(R.id.login_password);

        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(v->{
            String input_id = id_text.getText().toString();
            String input_pw = pw_text.getText().toString();
            try{
                PHPRequest request = new PHPRequest(url+"/login.php");
                request.PhPlogin(input_id, input_pw);
                if(request.result_string.equals("1")){
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "환영합니다.",Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "ID / PW Error",Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        });

        Button sign_up = findViewById(R.id.join_button);
        sign_up.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
