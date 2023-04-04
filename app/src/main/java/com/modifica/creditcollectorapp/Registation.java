package com.modifica.creditcollectorapp;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import 	java.util.Calendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Registation extends AppCompatActivity implements View.OnClickListener {
    Button button1;
    EditText editText1;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    TextView editText8;
    EditText editText2;
    EditText nik_name;
    EditText location_txt;

    String lattitude,longitude,ful_date;
    String add3 = null;
    int year,month,date;
    private static final int REQUEST_LOCATION = 1;
    ImageButton imageButton;
    private LocationManager locationManager;

    private DatabaseReference databaseReg;


    private String[] array_8 = new String [4];
    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;


    public static final String EXTRA_TEXT_NIK_NAME_REG = "com.modifica.creditcollector.EXTRA_TEXT_NIK_NAME_REG";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();


        //+++++++++++++++++ date +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        final Calendar cal = Calendar.getInstance();
         year = cal.get(Calendar.YEAR);
         month = cal.get(Calendar.MONTH);
         date = cal.get(Calendar.DAY_OF_MONTH);

         ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);

        editText8 = (TextView) findViewById(R.id.editText8);

        editText8.setText(ful_date);


        //+++++++++++++++++ location +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        imageButton = (ImageButton) findViewById(R.id.imageButton_LC);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

            }
        });



        //+++++++++++++++++++++++ update data to fire base +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        databaseReg = FirebaseDatabase.getInstance().getReference("Customers").child(array_8[2]).child(array_8[3]);

        button1 = (Button) findViewById(R.id.button1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText2 = (EditText) findViewById(R.id.editText2);
        nik_name = (EditText) findViewById(R.id.nik_name);
        location_txt =(EditText) findViewById(R.id.location);




        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue();
            }
        });

        }

    //+++++++++++++++++ methord for get location +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++get lat and log for location+++++++++++++++++++++++++

        private void getLocation() {
            if (ActivityCompat.checkSelfPermission(Registation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (Registation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Registation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

                if (location != null) {
                    double latti = location.getLatitude();
                    double longi = location.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    location_txt.setText( lattitude + "/" + longitude);

                } else  if (location1 != null) {
                    double latti = location1.getLatitude();
                    double longi = location1.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    location_txt.setText(lattitude + "/" + longitude);


                } else  if (location2 != null) {
                    double latti = location2.getLatitude();
                    double longi = location2.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    location_txt.setText(lattitude + "/" + longitude);

                }else{

                    Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

                }
            }
        }

        protected void buildAlertMessageNoGps() {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Turn ON your GPS Connection")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }





    //++++++++++++ methord_______open popup+++++++++++++++++


    public void openactivity_regpopup(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to create a Loan")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        openactiviti_finder();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        openacticiti_selector();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //++++++++++++ methord_______add valu to fire base+++++++++++++++++


    public void addValue(){
        String nic = editText2.getText().toString().trim();
        String fname = editText1.getText().toString().trim();
        String lname = editText3.getText().toString().trim();
        String add1 = editText4.getText().toString().trim();
        String add2 = editText5.getText().toString().trim();
        add3 = editText6.getText().toString().trim();
        String phone = editText7.getText().toString().trim();
        String date = editText8.getText().toString().trim();
        String location_reg = location_txt.getText().toString().trim();
        String nikname = nik_name.getText().toString().trim();
        final String ID = nic+"_"+nikname;

        if (!TextUtils.isEmpty(nic)&&!TextUtils.isEmpty(fname)&&!TextUtils.isEmpty(lname)&&!TextUtils.isEmpty(nikname)){

            if (!TextUtils.isEmpty(add1)&&!TextUtils.isEmpty(add2)){

                if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(date)&&!TextUtils.isEmpty(location_reg)){

                    boolean net = isOnline();

                    if (!net){Internet();}else {

                        Customer customer = new Customer(nic, fname, lname, nikname, add1, add2, add3, phone, location_reg, date);

                        databaseReg.child(ID).setValue(customer);
                        databaseReg.child(ID).child("cus_date").setValue(date);
                        databaseReg.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(ID).exists()) {
                                    Toast.makeText(Registation.this, "Registation Complete", Toast.LENGTH_LONG).show();
                                    openactivity_regpopup();
                                }else {
                                    Interneterro();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else{
                    Toast.makeText(this,"Please Complete The Phone Number or Date or Location", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this,"Please Complete The Address", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this,"Please Complete The Name or NIC or Nick Name", Toast.LENGTH_LONG).show();
        }
    }

    public void openacticiti_selector(){
        Intent intent =  new Intent(this, Selector.class);
        startActivity(intent);
        finish();
    }

    public void openactiviti_finder(){
        Intent intent = new Intent(this, Create_loan.class);
        String nikname_reg = nik_name.getText().toString().trim();
        intent.putExtra(EXTRA_TEXT_NIK_NAME_REG,nikname_reg);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

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
                    array_8[i] = (String.valueOf(cursor01.getString(1)));
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

    public void Interneterro (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Creation Incomplete Please Check your Internet Connection.It is slow and try again")
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
