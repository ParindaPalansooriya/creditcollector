package com.modifica.creditcollectorapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Selector extends Activity {

    Button reg_b;
    Button crt_loan_b;
    Button Collect_b;

    String active_key;
    ImageButton logout;

    private String[] array = new String [4];
    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;
    boolean root_result;

    DatabaseReference databaseRef_sel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        reg_b = (Button) findViewById(R.id.button_reg);
        crt_loan_b = (Button) findViewById(R.id.button_crt_ln);
        Collect_b = (Button) findViewById(R.id.button_coll);
        logout = (ImageButton) findViewById(R.id.imageButton_logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_button();
            }
        });

        databaseRef_sel = FirebaseDatabase.getInstance().getReference();

        boolean net = isOnline();

        if (net != true){Internet();}

        databaseRef_sel.child("Registation").child(array[2]).child("Offices").child(array[3]).setValue("yes");

            if (!array[2].equals(null)) {

                databaseRef_sel.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        active_key = dataSnapshot.child("Activation").child(array[2]).getValue(String.class);

                        if (active_key.equals("Active")) {


                            reg_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_reg();
                                }
                            });

                            crt_loan_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_loan();
                                }
                            });

                            Collect_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_coll();
                                }
                            });

                        } else {
                            Active_dialog();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {
                openLoging();
            }


    }


    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Selector.this);
        builder.setMessage("Are you finish Your Works?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseRef_sel.child("Registation").child(array[2]).child("Offices").child(array[3]).setValue("0");
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void open_reg() {

        Intent intent = new Intent(this,Registation.class);
        startActivity(intent);

    }

    private void open_loan() {

        Intent intent = new Intent(this,Finder.class);
        startActivity(intent);

    }


    private void open_coll() {

        Intent intent = new Intent(this,Collection_list.class);
        startActivity(intent);

    }

    public void saveFile(String A,String B,String C,String D)  {

        root_sql_db = openOrCreateDatabase(ROOT_DATABASE_NAME,MODE_PRIVATE,null);
        boolean root = checkForTableExists(root_sql_db,TABLE_NAME_ROOT);

        if (!root){
            root_result = MYDB_ROOT.insertFullData("Level",A);
            root_result = MYDB_ROOT.insertFullData("Bundle",B);
            root_result = MYDB_ROOT.insertFullData("Number",C);
            root_result = MYDB_ROOT.insertFullData("Score",D);
            Toast.makeText(this,"set root", Toast.LENGTH_LONG).show();
            root_sql_db.close();
        }else {
            root_result = MYDB_ROOT.insertOneData("Level",A);
            root_result = MYDB_ROOT.insertOneData("Bundle",B);
            root_result = MYDB_ROOT.insertOneData("Number",C);
            root_result = MYDB_ROOT.insertOneData("Score",D);
            Toast.makeText(this,"root set ok", Toast.LENGTH_LONG).show();
        }
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
                    array[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }

    public void Active_dialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account was Blocked").setMessage("Contact your Owner to Active Your Account")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void logout_button(){

        saveFile(null,null,null,null);
        databaseRef_sel.child("Registation").child(array[2]).child("Offices").child(array[3]).setValue("0");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();


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
        builder.setMessage("Please Check your Internet Connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void openLoging(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
