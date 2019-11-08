package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lone_number_list extends AppCompatActivity {

    private DatabaseReference data_ref;
    private ListView loan_number_list;
    private ArrayList<String> key_arrayList = new ArrayList<>();
    public static final String EXTRA_TEXT_KEY = "com.modifica.creditcollector.EXTRA_TEXT_KEY";
    public static final String EXTRA_TEXT_NIK_NAME = "com.modifica.creditcollector.EXTRA_TEXT_NIK_NAME";


    private String chil_1;
    String name;

    private String[] array_7 = new String [4];
    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lone_number_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        loan_number_list = (ListView) findViewById(R.id.loan_number_list);

        data_ref = FirebaseDatabase.getInstance().getReference().child("Loan").child(array_7[2]).child(array_7[3]).child("free");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,key_arrayList);

        loan_number_list.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        final String n_name = intent.getStringExtra(Lone_number_list.EXTRA_TEXT_NIK_NAME);
        name=n_name;



       data_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (key.equals("Do not change any things")){
                    key="";
                }
                key_arrayList.add(key);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


       loan_number_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               if (!key_arrayList.get(i).equals("")) {

                   chil_1 = key_arrayList.get(i);
                   open_create_loan();

               }
           }
       });

    }

    public void open_create_loan(){
        Intent intent = new Intent(this, Create_loan.class);
        intent.putExtra(EXTRA_TEXT_KEY,chil_1);
        intent.putExtra(EXTRA_TEXT_NIK_NAME, name);
        startActivity(intent);
        finish();
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
                    array_7[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }

}
