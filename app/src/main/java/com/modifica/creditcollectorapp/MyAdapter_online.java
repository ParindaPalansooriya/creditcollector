package com.modifica.creditcollectorapp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter_online extends ArrayAdapter<String> {

    ArrayList<String> T1Array;
    ArrayList<String> T2Array;


    public MyAdapter_online(Context context, ArrayList<String> T11, ArrayList<String> T21){
        super((Context) context,R.layout.cus_list,R.id.loanNumber,T11);
        this.T1Array=T11;
        this.T2Array=T21;
    }

    @NonNull
    @Override
    public View getView(int position, View convertViwe, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.online_list,parent,false);

        TextView myT1 = (TextView) row.findViewById(R.id.online_root);
        ImageView myT2 = (ImageView) row.findViewById(R.id.online);

        myT1.setText(T1Array.get(position));

        if (!T2Array.get(position).equals("0")){
            myT2.setImageResource(R.drawable.ic_action_name_online02);
        }

        return row;
    }

}
