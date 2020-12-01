package com.example.mybucketlist.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;

import java.net.MalformedURLException;

import static com.example.mybucketlist.login.LoginActivity.url;

public class SignUpActivity extends AppCompatActivity {
    private boolean id_confirm = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        NetworkUtil.setNetworkPolicy();

        TextView id_text = findViewById(R.id.join_email);
        TextView pw_text = findViewById(R.id.join_password);
        TextView pwck_text = findViewById(R.id.join_pwck);
        TextView name_text = findViewById(R.id.join_name);

        Button id_check = findViewById(R.id.check_button);
        id_check.setOnClickListener(v -> {
            String input_id = id_text.getText().toString();
            try{
                PHPRequest request = new PHPRequest(url+"/id_check.php");
                request.PhPid_check(input_id);
                if(request.result_string.equals("1")){
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "사용 가능한 ID 입니다.",Toast.LENGTH_SHORT);
                    myToast.show();
                    id_confirm = true;
                }else{
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "이미 사용중인 ID 입니다.",Toast.LENGTH_SHORT);
                    myToast.show();
                    id_confirm = false;
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        });

        Button sign_up = findViewById(R.id.join_button);
        sign_up.setOnClickListener(v->{
            String input_id = id_text.getText().toString();
            String input_pw = pw_text.getText().toString();
            String input_pwck = pwck_text.getText().toString();
            String input_name = name_text.getText().toString();

            if(input_pw.equals(input_pwck) && id_confirm){
                try{
                    PHPRequest request = new PHPRequest(url+"/sign_up.php");
                    request.PhPsign_up(input_id, input_pw, input_name);
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "환영합니다.",Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }else{
                if(id_confirm){
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "비밀번호 불일치",Toast.LENGTH_SHORT);
                    myToast.show();
                }else{
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "ID 확인 필요",Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });

        Button cancel = findViewById(R.id.delete);
        cancel.setOnClickListener(v->{
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}