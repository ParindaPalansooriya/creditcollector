package com.modifica.creditcollectorapp;



import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Collection_list extends AppCompatActivity {

    ListView LW;
    MyAdapter adapter;
    String key;
    String loanNumber,nikName,amountPerTerms,fullTerms,availableTerms,fullAmount;
    private DatabaseReference BD_ref;
    private ArrayList <String> f_amount = new ArrayList<>();
    private ArrayList <String> nik_name = new ArrayList<>();
    private ArrayList <String> full_terms = new ArrayList<>();
    private ArrayList <String> available_terms = new ArrayList<>();
    private ArrayList <String> amount_per_term = new ArrayList<>();
    private ArrayList <String> loan_num = new ArrayList<>();

    ImageButton imageButton;
    EditText editText_call;


    private String[] array_1 = new String [4];

    SQLiteDatabase root_sql_db;
    public static final String ROOT_DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME_ROOT = "collaction_info";
    DatabaseHelperForRoot MYDB_ROOT;



    public static final String EXTRA_TEXT_LOAN_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_LOAN_NUMBER";
    public static final String EXTRA_TEXT_NIk = "com.modifica.creditcollector.EXTRA_TEXT_NIk";
    public static final String EXTRA_TEXT_AMOUT_PER_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_AMOUT_PER_TERMS";
    public static final String EXTRA_TEXT_F_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_F_TERMS";
    public static final String EXTRA_TEXT_A_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_A_TERMS";
    public static final String EXTRA_TEXT_F_AMOUNT = "com.modifica.creditcollector.EXTRA_TEXT_F_AMOUNT";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MYDB_ROOT = new DatabaseHelperForRoot(this);

        readFile();

        imageButton = (ImageButton) findViewById(R.id.imageButton_CLL);
        editText_call = (EditText) findViewById(R.id.editText10);

        LW = (ListView) findViewById(R.id.collect_list);
        BD_ref = FirebaseDatabase.getInstance().getReference().child("Loan").child(array_1[2]).child(array_1[3]);
        adapter = new MyAdapter(this,loan_num,nik_name,amount_per_term,available_terms,f_amount,full_terms);
        LW.setAdapter(adapter);


        BD_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                full_terms.clear();
                f_amount.clear();
                nik_name.clear();
                available_terms.clear();
                amount_per_term.clear();
                loan_num.clear();
                for (DataSnapshot collSnapshot : dataSnapshot.getChildren()){


                    Loan loan = collSnapshot.getValue(Loan.class);

                    if (loan.getAmount_per_term() != null ) {

                        f_amount.add(loan.getF_amount());
                        nik_name.add(loan.getNik_name());
                        full_terms.add(loan.getF_amount());
                        available_terms.add(loan.getAvailable_amount());
                        amount_per_term.add(loan.getAmount_per_term());
                        loan_num.add(loan.getLoan_num());

                        adapter.notifyDataSetChanged();

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        LW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loanNumber = loan_num.get(i);
                nikName = nik_name.get(i);
                amountPerTerms = amount_per_term.get(i);
                fullAmount = f_amount.get(i);
                fullTerms = full_terms.get(i);
                availableTerms = available_terms.get(i);

                openCollaction();

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String serch = editText_call.getText().toString().trim();

                if (!serch.isEmpty()) {

                    BD_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(serch).exists()) {

                                Loan loan = dataSnapshot.child(serch).getValue(Loan.class);

                                assert loan != null;
                                loanNumber = loan.getLoan_num();
                                nikName = loan.getNik_name();
                                amountPerTerms = loan.getAmount_per_term();
                                fullAmount = loan.getF_amount();
                                fullTerms = loan.getF_amount();
                                availableTerms = loan.getAvailable_amount();

                                openCollaction();

                            }else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Collection_list.this);
                                builder.setMessage("Please check Loan number \n\t ** that loan number have not loan or \n that loan not Active \n Please Contact your owner")
                                        .setTitle("This Loan number is not Exists")
                                        .setCancelable(false)
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                final AlertDialog alert = builder.create();
                                alert.show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(Collection_list.this,"Please enter your Loan number",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void openCollaction(){
        Intent intent = new Intent(this, getCollection.class);
        intent.putExtra(EXTRA_TEXT_NIk,nikName);
        intent.putExtra(EXTRA_TEXT_LOAN_NUMBER,loanNumber);
        intent.putExtra(EXTRA_TEXT_AMOUT_PER_TERMS,amountPerTerms);
        intent.putExtra(EXTRA_TEXT_F_AMOUNT,fullAmount);
        intent.putExtra(EXTRA_TEXT_F_TERMS,fullTerms);
        intent.putExtra(EXTRA_TEXT_A_TERMS,availableTerms);
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
                    array_1[i] = (String.valueOf(cursor01.getString(1)));
                    i++;
                }
            }
        }else {
            Toast.makeText(this,"SQL Reading Error!___ NO DATA BASE ERROR", Toast.LENGTH_LONG).show();
        }
    }

}

