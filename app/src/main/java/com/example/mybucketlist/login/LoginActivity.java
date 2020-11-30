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

    @Deprecated
    @SuppressLint("StaticFieldLeak")
    public String getResponse(String url) {
        PHPRequest result_response = null;
        try {
            AsyncTask<String, Void, PHPRequest> asyncTask = new AsyncTask<String, Void, PHPRequest>() {
                @Override
                protected PHPRequest doInBackground(String... url) {
                    PHPRequest response = null;
                    try {
                        response = new PHPRequest(url[0]+url[1]);
                        response.PhPgetData("minedge");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return response;
                }
            };

            result_response = asyncTask.execute(url, "/apmtest.php").get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result_response.result_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkUtil.setNetworkPolicy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수
            }
        }

        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(v->{
            try{
                PHPRequest request = new PHPRequest(url+"/apmtest.php");
                request.PhPgetData("minedge");
                Toast myToast = Toast.makeText(this.getApplicationContext(), request.result_string,Toast.LENGTH_SHORT);
                myToast.show();
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
            /*
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            */
        });

        Button sign_up = findViewById(R.id.join_button);
        sign_up.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
