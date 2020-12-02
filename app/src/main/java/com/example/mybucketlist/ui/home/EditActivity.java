package com.example.mybucketlist.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;
import com.example.mybucketlist.login.NetworkUtil;
import com.example.mybucketlist.login.PHPRequest;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.mybucketlist.login.LoginActivity.url;

public class EditActivity  extends AppCompatActivity {
    private TextView date_text, title_text, detail_text;
    private Spinner local_spinner;

    public String input_title, input_local, input_date, input_detail, input_complete = "0", ident_num = "0";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        NetworkUtil.setNetworkPolicy();

        title_text = findViewById(R.id.edit_title);
        local_spinner = findViewById(R.id.select_local_spinner);
        date_text = findViewById(R.id.date_view_text);
        detail_text = findViewById(R.id.detail_text);

        if(!ident_num.equals("0")){
            title_text.setText(input_title);
            local_spinner.setSelection(Integer.parseInt(input_local));
            date_text.setText(input_date);
            detail_text.setText(input_detail);
        }

        Button select_date = findViewById(R.id.set_time);
        select_date.setOnClickListener(this::showDatePicker);

        Button saveBT = findViewById(R.id.save_button);
        saveBT.setOnClickListener(v -> {
            input_title = title_text.getText().toString();
            input_local = Integer.toString(local_spinner.getSelectedItemPosition());
            input_date = date_text.getText().toString();
            input_detail = detail_text.getText().toString();
            try {
                PHPRequest request = new PHPRequest(url + "/insert.php");
                request.PhPsave(MainActivity._id, input_title, input_local, input_date, input_detail, input_complete, ident_num);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        });

        Button complete = findViewById(R.id.complete_button);
        complete.setOnClickListener(v -> {
            if(input_complete.equals("0")){
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                String year = yearFormat.format(currentTime);
                String month = monthFormat.format(currentTime);
                String day = dayFormat.format(currentTime);

                title_text.setEnabled(false);
                local_spinner.setEnabled(false);
                date_text.setText(year + "년 " + month + "월 " + day + "일 ");
                date_text.setEnabled(false);
                select_date.setText("완료날짜");
                select_date.setEnabled(false);
                detail_text.setEnabled(false);
                complete.setText("완료 취소");
                input_complete = "1";
            }else{
                title_text.setEnabled(true);
                local_spinner.setEnabled(true);
                date_text.setEnabled(true);
                select_date.setText("날짜선택");
                select_date.setEnabled(true);
                detail_text.setEnabled(true);
                complete.setText("완료");
                input_complete = "0";
            }
        });

    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    @SuppressLint("SetTextI18n")
    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        date_text.setText(year_string + "년 " + month_string + "월 " + day_string + "일");
    }
}
