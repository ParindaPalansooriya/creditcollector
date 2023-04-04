package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SP_Note extends AppCompatActivity {

    String spiner;
    String text_note;
    DatabaseReference DB_REF;
    int year,month,date;

    private String[] array_1_SP = new String [4];

    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp__note);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        Intent intent = getIntent();
        final String loanNumber = intent.getStringExtra(getCollection.EXTRA_TEXT_LOAN_NUMBER);

        readFile();

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        String ful_date = String.valueOf(year) + "|" + String.valueOf(month) + "|" + String.valueOf(date);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_SP);
        final EditText editText = (EditText) findViewById(R.id.editText12);
        spiner = spinner.getSelectedItem().toString().trim();
        text_note = editText.getText().toString().trim();


        DB_REF = FirebaseDatabase.getInstance().getReference().child("Collaction").child(array_1_SP[2]).child(ful_date).child("SP_note").child(array_1_SP[3]);

        Button button = (Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean net = isOnline();

                if (!net){Internet();}else {
                    spiner = spinner.getSelectedItem().toString().trim();
                    text_note = editText.getText().toString().trim();
                    if (!text_note.isEmpty()) {
                        DB_REF.push().child(loanNumber).child(spiner).setValue(text_note);
                        finish();
                    }
                }


            }
        });

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
                    array_1_SP[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void Internet (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Check your Internet Connection in your phone setting")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
