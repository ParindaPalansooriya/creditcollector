package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Splash extends AppCompatActivity {

    ImageView imageView02;
    ImageView imageView03;

    Intent intent;

    private String[] info_e = {null,null,null,null};

    ArrayList<String> finish_arraylist = new ArrayList<>();
    ArrayList <String> finish_key = new ArrayList<>();
    DatabaseReference databaseReference;


    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();


        if (info_e[1].equals("KEEP")){
            intent = new Intent(Splash.this,Selector.class);
        }else {
            intent = new Intent(Splash.this,MainActivity.class);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference();

        boolean net = isOnline();

        if (net != true){Internet();}

        if (info_e[2] !=null) {

            databaseReference.child("Registation").child(info_e[2]).child("Offices").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.exists()) {

                        String key = dataSnapshot.getKey();
                        finish_key.add(key);
                    }

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

            databaseReference.child("Loan").child(info_e[2]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (int k = 0; k < finish_key.size(); k++) {


                            if (dataSnapshot.child(finish_key.get(k)).exists()) {

                                for (DataSnapshot finishSnapshot : dataSnapshot.child(finish_key.get(k)).getChildren()) {

                                    Loan finishLoan = finishSnapshot.getValue(Loan.class);

                                    if (finishLoan.getLoan_num() != null) {

                                        finish_arraylist.add(finishLoan.getAvailable_amount());

                                        if (Double.parseDouble(finishLoan.getAvailable_amount().trim()) <= 0) {

                                            databaseReference.child("Loan").child(info_e[2]).child("TO_finish").child(finish_key.get(k)).child(finishLoan.getLoan_num()).setValue(finishLoan);
                                            databaseReference.child("Loan").child(info_e[2]).child(finish_key.get(k)).child(finishLoan.getLoan_num()).removeValue();


                                        }

                                    }

                                }

                            }

                        }

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        imageView02 = (ImageView) findViewById(R.id.logo);
        imageView03 = (ImageView) findViewById(R.id.pic_01);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splam);

        imageView03.startAnimation(animation);
        imageView02.startAnimation(animation);



        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();

    }

    public void readFile() {

        root_sql_db = openOrCreateDatabase(ROOT_DATABASE_NAME,MODE_PRIVATE,null);
        boolean root = checkForTableExists(root_sql_db,TABLE_NAME_ROOT);
        if (root){
            int i = 0;
            Cursor cursor01 = MYDB_ROOT.getAllData();
            if (cursor01!=null && cursor01.getCount()>0){
                while (cursor01.moveToNext()){
                    info_e[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            info_e[1]= "no";
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


    private boolean checkForTableExists(SQLiteDatabase db, String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }
}
