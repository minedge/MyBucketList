package com.example.mybucketlist.ui.profile;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;
import com.example.mybucketlist.login.LoginActivity;
import com.example.mybucketlist.login.PHPRequest;
import com.example.mybucketlist.login.SignUpActivity;

import java.net.MalformedURLException;

import static com.example.mybucketlist.login.LoginActivity.url;

public class ProfileFragment extends Fragment {
    private TextView user_text, count_total, count_complete;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        user_text = root.findViewById(R.id.user_id);
        count_total = root.findViewById(R.id.total_text);
        count_complete = root.findViewById(R.id.complete_text);
        Button pullBT = root.findViewById(R.id.pull_button);

        try {
            PHPRequest request = new PHPRequest(LoginActivity.url + "/profile.php");
            request.PhPprofile_id(MainActivity._id);
            user_text.setText(request.result_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            PHPRequest request = new PHPRequest(LoginActivity.url + "/total.php");
            request.PhPprofile_id(MainActivity._id);
            count_total.setText(request.result_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            PHPRequest request = new PHPRequest(LoginActivity.url + "/complete.php");
            request.PhPprofile_id(MainActivity._id);
            count_complete.setText(request.result_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Button pull_button = root.findViewById(R.id.pull_button);
        pull_button.setOnClickListener(v->{

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainActivity);
            builder.setTitle("회원탈퇴").setMessage("탈퇴하시겠습니까?");

            builder.setPositiveButton("확인", (dialog, id) -> {
                try {
                    PHPRequest request = new PHPRequest(LoginActivity.url + "/delete_user.php");
                    request.PhPprofile_id(MainActivity._id);
                    count_complete.setText(request.result_string);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.mainActivity, LoginActivity.class);
                startActivity(intent);
                MainActivity.mainActivity.finish();
            } );

            builder.setNegativeButton("취소", (dialog, id) -> {});

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });


        return root;
    }
}