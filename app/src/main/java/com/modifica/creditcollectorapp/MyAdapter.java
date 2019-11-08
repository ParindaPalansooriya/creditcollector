package com.modifica.creditcollectorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class MyAdapter extends ArrayAdapter<String> {

    ArrayList<String> T1Array;
    ArrayList<String> T2Array;
    ArrayList<String> T3Array;
    ArrayList<String> T4Array;
    ArrayList<String> T5Array;
    ArrayList<String> T6Array;

    public MyAdapter(Collection_list context, ArrayList<String> T11, ArrayList<String> T21, ArrayList<String> T31, ArrayList<String> T41, ArrayList<String> T51, ArrayList<String> T61){
        super((Context) context,R.layout.cus_list,R.id.loanNumber,T11);
        this.T1Array=T11;
        this.T2Array=T21;
        this.T3Array=T31;
        this.T4Array=T41;
        this.T5Array=T51;
        this.T6Array=T61;
    }

    @NonNull
    @Override
    public View getView(int position, View convertViwe, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.cus_list,parent,false);

        TextView myT1 = (TextView) row.findViewById(R.id.loanNumber);
        TextView myT2 = (TextView) row.findViewById(R.id.nikName);
        TextView myT3 = (TextView) row.findViewById(R.id.fullAmount);
        TextView myT4 = (TextView) row.findViewById(R.id.Terms);
        TextView myT6 = (TextView) row.findViewById(R.id.Available_l);

        myT1.setText(T1Array.get(position));
        myT2.setText(T2Array.get(position));
        myT3.setText("Rs."+ T3Array.get(position));
        myT4.setText(T4Array.get(position));
        myT6.setText(T6Array.get(position));

        return row;
    }

}
