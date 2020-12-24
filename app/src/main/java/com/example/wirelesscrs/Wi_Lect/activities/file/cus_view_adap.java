package com.example.wirelesscrs.Wi_Lect.activities.file;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wirelesscrs.R;

import java.util.List;

public class cus_view_adap extends ArrayAdapter<filesUpload> {
    private Activity context;
    private List<filesUpload> fu;

    public cus_view_adap(Activity context,List<filesUpload> fu){
        super(context,R.layout.custom_view_file_cloud,fu);
        this.context=context;
        this.fu=fu;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View listViewItem=inflater.inflate(R.layout.custom_view_file_cloud,null,true);
        TextView txt1=listViewItem.findViewById(R.id.txt_list_sh_fs);
        filesUpload filesUpload=fu.get(position);
        txt1.setText(filesUpload.getName());
        return listViewItem;
    }
}
