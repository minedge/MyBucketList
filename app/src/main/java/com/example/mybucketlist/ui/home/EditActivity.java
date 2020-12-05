package com.example.mybucketlist.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.mybucketlist.MainActivity;
import com.example.mybucketlist.R;
import com.example.mybucketlist.login.NetworkUtil;
import com.example.mybucketlist.login.PHPRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.mybucketlist.login.LoginActivity.url;

public class EditActivity  extends AppCompatActivity {
    private TextView date_text, title_text, detail_text;
    ImageView selected_image;
    String imgPath = "null";
    private Spinner local_spinner;

    public static String input_title, input_local, input_date, input_detail, input_complete = "0", ident_num = "0";

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
        selected_image = findViewById(R.id.selected_image);
        Button select_date = findViewById(R.id.set_time);
        Button saveBT = findViewById(R.id.save_button);
        Button complete = findViewById(R.id.complete_button);

        select_date.setOnClickListener(this::showDatePicker);

        if(!ident_num.equals("0")){
            try {
                PHPRequest request = new PHPRequest(url + "/get_detail.php");
                request.PhPgetDetail(MainActivity._id, ident_num);
                try{
                    JSONArray jArray=new JSONArray(request.result_string);
                    JSONObject jsonObject=jArray.getJSONObject(0);

                    String detail = jsonObject.getString("detail");
                    detail_text.setText(detail);

                    Bitmap bit_img = StringToBitMap(jsonObject.getString("bitmap"));
                    BitMapToString(bit_img);
                    selected_image.setImageBitmap(bit_img);
                    selected_image.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        }else{
            input_title = "";
            input_local = "0";
            input_date = "";
            detail_text.setText("");
            selected_image.setVisibility(View.INVISIBLE);
        }
        title_text.setText(input_title);
        local_spinner.setSelection(Integer.parseInt(input_local));
        date_text.setText(input_date);

        if(input_complete.equals("1")){
            title_text.setEnabled(false);
            local_spinner.setEnabled(false);
            date_text.setEnabled(false);
            select_date.setEnabled(false);
            select_date.setText("완료날짜");
        }

        saveBT.setOnClickListener(v -> {

            input_title = title_text.getText().toString();
            input_local = Integer.toString(local_spinner.getSelectedItemPosition());
            input_date = date_text.getText().toString();
            input_detail = detail_text.getText().toString();

            if(input_title.equals("") || input_local.equals("") || input_date.equals("") || input_detail.equals("")) {
                Toast myToast = Toast.makeText(this.getApplicationContext(), "모든 정보를 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast.show();
            }else{
                try {
                    PHPRequest request = new PHPRequest(url + "/insert.php");
                    request.PhPsave(MainActivity._id, input_title, input_local, input_date, input_detail, input_complete, ident_num, imgPath);

                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }

        });

        complete.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 0);

            if(input_complete.equals("0")){
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                String year = yearFormat.format(currentTime);
                String month = monthFormat.format(currentTime);
                String day = dayFormat.format(currentTime);

                date_text.setText(year + "-" + month + "-" + day);
                select_date.setText("완료날짜");
                select_date.setEnabled(false);
                input_complete = "1";
            }
        });
    }

    public static Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public void BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);	//bitmap compress
        byte [] arr=baos.toByteArray();
        String image= Base64.encodeToString(arr, Base64.DEFAULT);
        try{
            imgPath = URLEncoder.encode(image,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                try{
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in, new Rect(), options);
                    selected_image.setImageBitmap(img);
                    selected_image.setVisibility(View.VISIBLE);
                    BitMapToString(img);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
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
        date_text.setText(year_string + "-" + month_string + "-" + day_string);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
