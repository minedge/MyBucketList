package com.example.mybucketlist.ui.home;

import android.content.Intent;
import android.media.midi.MidiDeviceService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mybucketlist.CustomAdapter;
import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.PersonalItem;
import com.example.mybucketlist.R;
import com.example.mybucketlist.login.NetworkUtil;
import com.example.mybucketlist.login.PHPRequest;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.mybucketlist.login.LoginActivity.url;

public class HomeFragment extends Fragment {
    private TextView search_text;
    private Spinner date_spn, local_spn, complete_spn;

    private String target_search, sort_date, sort_local, sort_complete;

    private CustomAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NetworkUtil.setNetworkPolicy();

        adapter = new CustomAdapter();
        ListView list_view = root.findViewById(R.id.Item_list_view);
        list_view.setAdapter(adapter);

        try {
            PHPRequest request = new PHPRequest(url + "/get_all.php");
            request.PhPgetAll(MainActivity._id);
            String[] array = request.result_string.split("@");
            for(int loop = 0; loop < (array.length / 5); loop++){
                PersonalItem item = new PersonalItem(array[(loop * 5)], array[(loop * 5) + 1], array[(loop * 5) + 2], array[(loop * 5) + 3], array[(loop * 5) + 4]);
                adapter.add(item);
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        search_text = root.findViewById(R.id.search_text);
        date_spn = root.findViewById(R.id.date_sort_spinner);
        local_spn = root.findViewById(R.id.locate_sort_spinner);
        complete_spn = root.findViewById(R.id.complete_sort_spinner);

        ImageButton searchButton = root.findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            target_search = search_text.getText().toString();
            adapter.notifyDataSetChanged();
        });

        ImageButton addNewItem = root.findViewById(R.id.new_item);
        addNewItem.setOnClickListener(v -> {
            EditActivity.input_detail = "";
            EditActivity.input_local = "0";
            EditActivity.input_complete = "0";
            EditActivity.input_title = "";
            EditActivity.input_date = "";
            EditActivity.ident_num = "0";

            Intent intent = new Intent(MainActivity.mainActivity, EditActivity.class);
            startActivity(intent);
            MainActivity.mainActivity.finish();
        });

        return root;
    }
}