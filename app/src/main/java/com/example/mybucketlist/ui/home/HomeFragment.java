package com.example.mybucketlist.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton addNewItem = root.findViewById(R.id.new_item);
        addNewItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.mainActivity, EditActivity.class);
            startActivity(intent);
        });

        return root;
    }
}