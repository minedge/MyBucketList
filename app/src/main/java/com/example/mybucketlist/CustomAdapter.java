package com.example.mybucketlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybucketlist.login.LoginActivity;
import com.example.mybucketlist.login.PHPRequest;
import com.example.mybucketlist.ui.home.EditActivity;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.mybucketlist.login.LoginActivity.url;

public class CustomAdapter extends BaseAdapter {
    public ArrayList<PersonalItem> m_List;

    public CustomAdapter() {
        m_List = new ArrayList<PersonalItem>();
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView title = null, date = null;
        CheckBox check_box = null;
        CustomHolder holder = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_custom, parent, false);

            title = convertView.findViewById(R.id.item_title);
            date = convertView.findViewById(R.id.target_date);
            check_box = convertView.findViewById(R.id.checkBox);

            holder = new CustomHolder();
            holder.m_title = title;
            holder.m_target_date = date;
            holder.m_checkbox = check_box;
            convertView.setTag(holder);
        }
        else {
            holder = (CustomHolder) convertView.getTag();
            title = holder.m_title;
            date = holder.m_target_date;
            check_box = holder.m_checkbox;
        }

        check_box.setEnabled(false);

        title.setText(m_List.get(position).title);
        date.setText(m_List.get(position).date);
        check_box.setChecked(m_List.get(position).complete.equals("1"));

        convertView.setOnClickListener(v -> {
            EditActivity.ident_num = m_List.get(pos).ident;
            EditActivity.input_title = m_List.get(pos).title;
            EditActivity.input_date = m_List.get(pos).date;
            EditActivity.input_local = m_List.get(pos).locate;
            EditActivity.input_complete = m_List.get(pos).complete;

            Intent intent = new Intent(context, EditActivity.class);
            context.startActivity(intent);
            MainActivity.mainActivity.finish();
        });


        convertView.setOnLongClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("경고!").setMessage("삭제하시겠습니까?");

            builder.setPositiveButton("OK", (dialog, id) -> {
                try {
                    PHPRequest request = new PHPRequest(url + "/delete_item.php");
                    request.PhPdelete_item(MainActivity._id, m_List.get(pos).ident);
                    notifyDataSetChanged();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
                remove(pos);
            } );

            builder.setNegativeButton("Cancel", (dialog, id) -> {});

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });

        return convertView;
    }

    public void add(PersonalItem _item) {
        m_List.add(_item);
    }

    public void remove(int _position) {
        m_List.remove(_position);
    }

    private class CustomHolder {
        TextView    m_title, m_target_date;
        CheckBox      m_checkbox;
    }
}