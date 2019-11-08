package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Finder extends AppCompatActivity {

    private Button button3;
    private DatabaseReference BD_ref;
    private EditText editText8;
    private TextView textView_phone;
    private TextView textView_Fname;
    private TextView textView_Lname;
    private TextView textView_Nname;

    private String[] array_3 = new String [4];
    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;

    private String nic,f_name,l_name,n_name="null",phone_num;

    public static final String EXTRA_TEXT_NIK_NAME = "com.modifica.creditcollector.EXTRA_TEXT_NIK_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        button3 = (Button) findViewById(R.id.button3);
        editText8 = (EditText) findViewById(R.id.editText8);
        textView_Fname = (TextView) findViewById(R.id.textView_Fname);
        textView_Lname = (TextView) findViewById(R.id.textView_Lname);
        textView_phone = (TextView) findViewById(R.id.textView_phone);
        textView_Nname = (TextView) findViewById(R.id.textView_Nname);

        Intent intent = getIntent();
        final String text = intent.getStringExtra(InfoList.EXTRA_TEXT_NIC);



        if(text != null){

            BD_ref = FirebaseDatabase.getInstance().getReference().child("Customers").child(array_3[2]).child(array_3[3]).child(text);

            BD_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nic = dataSnapshot.child("cus_nic").getValue().toString();
                    editText8.setText(nic);
                    f_name = dataSnapshot.child("cus_fname").getValue().toString();
                    textView_Fname.setText(f_name);
                    l_name = dataSnapshot.child("cus_lname").getValue().toString();
                    textView_Lname.setText(l_name);
                    phone_num = dataSnapshot.child("cus_phone").getValue().toString();
                    textView_phone.setText(phone_num);
                    n_name = dataSnapshot.child("cus_nikname").getValue().toString();
                    textView_Nname.setText(n_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        editText8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openactivity_list();
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                select();
            }
        });



    }

    public void openactivity_create_loan(){

            Intent intent = new Intent(this, Create_loan.class);
            intent.putExtra(EXTRA_TEXT_NIK_NAME, n_name);
            startActivity(intent);
            finish();

    }

    public void openactivity_list(){
        Intent intent = new Intent(this,InfoList .class);
        startActivity(intent);
        finish();
    }

    public void select(){
            openactivity_create_loan();
    }

    private boolean checkForTableExists(SQLiteDatabase db, String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }


    public void readFile() {

        root_sql_db = openOrCreateDatabase(ROOT_DATABASE_NAME,MODE_PRIVATE,null);
        boolean root = checkForTableExists(root_sql_db,TABLE_NAME_ROOT);
        if (root){
            int i = 0;
            Cursor cursor01 = MYDB_ROOT.getAllData();
            if (cursor01!=null && cursor01.getCount()>0){
                while (cursor01.moveToNext()){
                    array_3[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }
}
