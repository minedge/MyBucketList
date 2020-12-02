package com.example.mybucketlist.ui.profile;

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

import java.net.MalformedURLException;

public class ProfileFragment extends Fragment {
    private TextView user_text, count_total, count_complete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        user_text = root.findViewById(R.id.user_id);
        count_total = root.findViewById(R.id.total_text);
        count_complete = root.findViewById(R.id.complete_text);
        Button pushBT = root.findViewById(R.id.push_button);
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


        return root;
    }
}