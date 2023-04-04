package com.modifica.creditcollectorapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;

public class getCollection extends AppCompatActivity {

    private static DecimalFormat df2 = new DecimalFormat(".####");


    int year,month,date;
    String lattitude,longitude,ful_date;
    private LocationManager locationManager;
    private DatabaseReference BD_ref_coll;
    private DatabaseReference BD_ref_loan;
    private static final int REQUEST_LOCATION = 1;

    private String[] array_4 = new String [4];

    TextView location_txt;
    EditText loan_number;
    TextView nik_name;
    TextView A_per_terms;
    TextView full_terms;
    TextView paid_terms;
    TextView Available;
    EditText payment;
    TextView date_txt;
    Button button6;
    ImageButton refresh;

    ImageView location_pik;
    ImageView date_pik;
    ImageView pay_pik;
    ImageView APT_pik;

    String loanNumber,nikName,amountPerTerms,fullTerms,availableTerms,fullAmount;

    public static final String EXTRA_TEXT_LOAN_NUMBER= "com.modifica.creditcollector.EXTRA_TEXT_LOAN_NUMBER";

    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_collection);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        location_pik = (ImageView) findViewById(R.id.imageView6_4);
        date_pik = (ImageView) findViewById(R.id.imageView6_3);
        pay_pik = (ImageView) findViewById(R.id.imageView6_1);
        APT_pik = (ImageView) findViewById(R.id.imageView6_2);

        refresh = (ImageButton) findViewById(R.id.imageButton_refresh);



        Intent intent = getIntent();
        loanNumber = intent.getStringExtra(Collection_list.EXTRA_TEXT_LOAN_NUMBER);
        nikName = intent.getStringExtra(Collection_list.EXTRA_TEXT_NIk);
        amountPerTerms = intent.getStringExtra(Collection_list.EXTRA_TEXT_AMOUT_PER_TERMS);
        fullTerms = intent.getStringExtra(Collection_list.EXTRA_TEXT_F_TERMS);
        availableTerms = intent.getStringExtra(Collection_list.EXTRA_TEXT_A_TERMS);
        fullAmount = intent.getStringExtra(Collection_list.EXTRA_TEXT_F_AMOUNT);




        BD_ref_coll = FirebaseDatabase.getInstance().getReference().child("Collaction").child(array_4[2]);
        BD_ref_loan = FirebaseDatabase.getInstance().getReference();


        //+++++++++++++++++ get data +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        nik_name = (TextView) findViewById(R.id.editTextNIK_NAME);
        A_per_terms = (TextView) findViewById(R.id.textView_term);
        full_terms = (TextView) findViewById(R.id.textView_full);
        paid_terms = (TextView) findViewById(R.id.textView_paied);
        Available = (TextView) findViewById(R.id.textView_available);
        payment = (EditText) findViewById(R.id.editText16);
        button6 = (Button) findViewById(R.id.button6);
        loan_number = (EditText) findViewById(R.id.editText11);

        nik_name.setText(nikName);
        full_terms.setText(fullTerms);
        loan_number.setText(loanNumber);
        A_per_terms.setText(amountPerTerms);
        loan_number.setText(loanNumber);
        Available.setText(availableTerms);
        payment.setText(amountPerTerms);

        //+++++++++++++++++get paid tems++++++++++++++++++++++++++++++++++++++
         get_paied_terms();

        //+++++++++++++++++ date +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);

        date_txt = (TextView) findViewById(R.id.textView24);

        date_txt.setText(ful_date);


        //+++++++++++++++++ location +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        location_txt = (TextView) findViewById(R.id.textView22);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        //+++++++++++ set click button ++++++++++++++++++++++++++++

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean net = isOnline();

                if (!net){Internet();}else {

                    addValue();
                }

               if (TextUtils.isEmpty(payment.getText().toString().trim())){
                    pay_pik.setImageResource(R.drawable.ic_action_name);
                }else{
                    pay_pik.setImageResource(R.drawable.ic_action_4444);
                }

            }
        });


        if (TextUtils.isEmpty(location_txt.getText().toString().trim())){
            location_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            location_pik.setImageResource(R.drawable.ic_action_4444);
        } if (TextUtils.isEmpty(date_txt.getText().toString().trim())){
            date_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            date_pik.setImageResource(R.drawable.ic_action_4444);
        }if (TextUtils.isEmpty(A_per_terms.getText().toString().trim())){
            APT_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            APT_pik.setImageResource(R.drawable.ic_action_4444);
        } if (TextUtils.isEmpty(payment.getText().toString().trim())){
            pay_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            pay_pik.setImageResource(R.drawable.ic_action_4444);
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh_act();
            }
        });



        ImageButton massage = (ImageButton) findViewById(R.id.imageButton_massage);
        massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getCollection.this,SP_Note.class);
                intent1.putExtra(EXTRA_TEXT_LOAN_NUMBER,loanNumber);
                startActivity(intent1);
                finish();
            }
        });


    }



    //+++++++++++++++++ methord for get location +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++get lat and log for location+++++++++++++++++++++++++

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getCollection.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getCollection.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getCollection.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

                Toast.makeText(this,"Unble to Trace your location. Please check your phone System",Toast.LENGTH_SHORT).show();

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


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    public void get_paied_terms(){
        double FT = Double.parseDouble(full_terms.getText().toString().trim());
        double AT = Double.parseDouble(Available.getText().toString().trim());
        double pid = (FT - AT);

        paid_terms.setText(String.valueOf(pid));
    }

    public String get_availableTerms_now_terms(){

        double PT = Double.parseDouble(payment.getText().toString().trim());
        double AT = Double.parseDouble(Available.getText().toString().trim());
        double NOW = (AT - PT);

        return String.valueOf(NOW);
    }

    public String cal_paid_terms(){
       double pay = Double.parseDouble(payment.getText().toString().trim());
       double APTr = Double.parseDouble(A_per_terms.getText().toString().trim());

       double RT_Trms = (pay/APTr);
       String RT_terms = String.valueOf(df2.format(RT_Trms));

       return RT_terms;

    }

    protected void getTermsDialog() {

        final String Terms = cal_paid_terms();
        final String payment_user = payment.getText().toString().trim();
        final String getDure_Aount = getDue();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Payment :-   "+payment_user+"\nDue :-  "+getDure_Aount+"\nTerms :-   "+Terms)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        String DT = date_txt.getText().toString().trim();
                        String ID = DT+"/"+array_4[3];

                        String AVAIABLE_TERMS_NOW = get_availableTerms_now_terms();

                        Collaction collaction = new Collaction(loanNumber,nikName,payment_user,Terms,DT,location_txt.getText().toString().trim(),AVAIABLE_TERMS_NOW,longitude,lattitude);

                        BD_ref_coll.child(ID).push().setValue(collaction);
                        BD_ref_loan.child("Loan").child(array_4[2]).child(array_4[3]).child(loanNumber).child("available_amount").setValue(AVAIABLE_TERMS_NOW);

                        Toast.makeText(getCollection.this,"Collaction Complete", Toast.LENGTH_LONG).show();

                        openSelector();

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

    public void openSelector(){
        finish();
    }

    public void addValue(){

        String pyment  = payment.getText().toString().trim();
        String loanNumber = loan_number.getText().toString().trim();

        if (!TextUtils.isEmpty(location_txt.getText().toString().trim())){

        if (!TextUtils.isEmpty(pyment)&&!TextUtils.isEmpty(loanNumber)){
            getTermsDialog();
        }else {
            Toast.makeText(getCollection.this,"Please Complete the Loan Number or Payment", Toast.LENGTH_LONG).show();
        }
        }else {
            Toast.makeText(getCollection.this,"Your location is Empty. Please Complete it with click Refresh button ", Toast.LENGTH_LONG).show();
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
                    array_4[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }

    public String getDue(){

        double pay = Double.parseDouble(payment.getText().toString().trim());
        double APTr = Double.parseDouble(A_per_terms.getText().toString().trim());

        double due = APTr - pay;
        String due_return = String.valueOf(df2.format(due));

        return due_return;

    }

    public void refresh_act(){

        location_txt.setText("");

        getLocation();

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);

        date_txt = (TextView) findViewById(R.id.textView24);

        date_txt.setText(ful_date);


        if (TextUtils.isEmpty(date_txt.getText().toString().trim())){
            date_pik.setImageResource(R.drawable.ic_action_name);
        }else {
            date_pik.setImageResource(R.drawable.ic_action_4444);
        }if (TextUtils.isEmpty(location_txt.getText().toString().trim())){
            location_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            location_pik.setImageResource(R.drawable.ic_action_4444);
        }if (TextUtils.isEmpty(payment.getText().toString().trim())){
            pay_pik.setImageResource(R.drawable.ic_action_name);
        }else{
            pay_pik.setImageResource(R.drawable.ic_action_4444);
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
