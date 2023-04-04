package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Create_loan extends AppCompatActivity {

    EditText editText9;
    TextView LN_nikname;
    TextView textView12;
    TextView textView15;
    EditText editText17;
    EditText editText18;
    EditText editText19;
    Spinner spinner2;
    Button button4;
    Button button5;

    ImageView imageView1;
    ImageView imageView2;

    private String[] array_2 = new String [4];

    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;


    private static DecimalFormat df2 = new DecimalFormat(".##");
    public static final String EXTRA_TEXT_NIK_NAME = "com.modifica.creditcollector.EXTRA_TEXT_NIK_NAME";

    private DatabaseReference databaseLoan;
    String n_name,ful_date;
    String key_1,full_Amount;
    int year,month,date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_loan);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        imageView1 = (ImageView) findViewById(R.id.imageView47);
        imageView2 = (ImageView) findViewById(R.id.imageView48);



        Intent intent = getIntent();
        final String key = intent.getStringExtra(Lone_number_list.EXTRA_TEXT_KEY);
        key_1=key;
        final String text_reg = intent.getStringExtra(Registation.EXTRA_TEXT_NIK_NAME_REG);
        final String text = intent.getStringExtra(Lone_number_list.EXTRA_TEXT_NIK_NAME);

        databaseLoan = FirebaseDatabase.getInstance().getReference("Loan").child(array_2[2]);

        editText9 = (EditText) findViewById(R.id.editText9);
        editText17 = (EditText) findViewById(R.id.editText17);
        editText18 = (EditText) findViewById(R.id.editText18);
        editText19 = (EditText) findViewById(R.id.editText19);
        LN_nikname = (TextView) findViewById(R.id.LN_nikname);
        textView12 = (TextView) findViewById(R.id.textView12);
        textView15 = (TextView) findViewById(R.id.textView15);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        if (key != null) {
            editText9.setText(key);
        }

        if (text != null){
            LN_nikname.setText(text);
            n_name=text;
        }else if (text_reg != null) {
            LN_nikname.setText(text_reg);
            n_name=text_reg;
        }else{
            LN_nikname.setText("NOT");
        }



        editText9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loan_list();
            }
        });


        button4 = (Button) findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView15.setText(null);
                textView12.setText(null);
                calculate();
                if (TextUtils.isEmpty(textView15.getText().toString().trim())||TextUtils.isEmpty(textView12.getText().toString().trim())){
                imageView1.setImageResource(R.drawable.ic_action_name);
                imageView2.setImageResource(R.drawable.ic_action_name);
                }
            }
        });


        button5 = (Button) findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_Val();
            }
        });



    }





    public void Loan_list(){
        Intent intent = new Intent(this,Lone_number_list.class);
        intent.putExtra(EXTRA_TEXT_NIK_NAME, n_name);
        startActivity(intent);
        finish();
    }

    public void selector(){
        Create_loan.this.finish();
    }






    public void calculate(){



        if (!TextUtils.isEmpty(editText9.getText().toString().trim())&&!TextUtils.isEmpty(editText17.getText().toString().trim())&&!TextUtils.isEmpty(editText18.getText().toString().trim())&&
                !TextUtils.isEmpty(editText19.getText().toString().trim())){


        double loan_amount = Double.parseDouble(editText17.getText().toString().trim());
        double rate = Double.parseDouble(editText18.getText().toString().trim());
        double loan_period = Double.parseDouble(editText19.getText().toString().trim());
        String collection_type = spinner2.getSelectedItem().toString();

        double Amount_per_term;
        double Num_of_terms;
        double full_amount;
        int coll_type = 0;



        full_amount = (((loan_amount*rate)/100)*loan_period)+loan_amount;
        full_Amount = String.valueOf(full_amount);

        if (collection_type.equals("Daily")){

            coll_type = 29;

        }else if (collection_type.equals("Weekly")){

            coll_type = 4;

        }


        Num_of_terms = (loan_period * coll_type);

        Double N_O_T = new Double(Num_of_terms);
        int N_O_T_int = N_O_T.intValue(); // convert double to int

        Amount_per_term = full_amount / N_O_T_int;

        textView12.setText(String.valueOf(df2.format(Amount_per_term)));
        textView15.setText(String.valueOf(N_O_T_int));
            imageView1.setImageResource(R.drawable.ic_action_4444);
            imageView2.setImageResource(R.drawable.ic_action_4444);

            Toast.makeText(this,"Calculation Complete", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this,"Please Complete all", Toast.LENGTH_LONG).show();
        }


    }


    public void add_Val(){
        final String loan_num = editText9.getText().toString().trim();
        String nik_name = LN_nikname.getText().toString().trim();
        String f_amount = editText17.getText().toString().trim();
        String amount_per_term = textView12.getText().toString().trim();
        String full_terms = textView15.getText().toString().trim();
        String loan_rate = editText18.getText().toString().trim();
        String loan_period = editText19.getText().toString().trim();
        String collection_type = spinner2.getSelectedItem().toString().trim();
        String activation = "NO";

        if (!TextUtils.isEmpty(loan_num)&&!TextUtils.isEmpty(nik_name)&&!TextUtils.isEmpty(loan_num)&&
                !TextUtils.isEmpty(f_amount)&&!TextUtils.isEmpty(loan_rate)&&!TextUtils.isEmpty(loan_period)){
            if (!TextUtils.isEmpty(full_terms)&&!TextUtils.isEmpty(amount_per_term)){

                boolean net = isOnline();

                if (!net){Internet();}else {

                    Loan loan = new Loan(loan_num, nik_name, full_Amount, amount_per_term, full_terms, full_Amount,
                            loan_rate, loan_period, collection_type, activation, array_2[3], ful_date, ful_date);

                    databaseLoan.child("TO_activation").child(array_2[3] + "/" + loan_num).setValue(loan);

                    delete_LN();

                    databaseLoan.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("TO_activation").child(array_2[3] + "/" + loan_num).exists()) {
                                Toast.makeText(Create_loan.this, "Loan Creation Complete", Toast.LENGTH_LONG).show();
                                selector();
                            }else {
                                Interneterro();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }else {
                Toast.makeText(this,"Please click the Calculation button", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Please Complete all", Toast.LENGTH_LONG).show();
        }



    }

    public void delete_LN(){
        databaseLoan.child(array_2[3

                ]).child("free").child(key_1).removeValue();
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
                    array_2[i] = (String.valueOf(cursor01.getString(1)));
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
