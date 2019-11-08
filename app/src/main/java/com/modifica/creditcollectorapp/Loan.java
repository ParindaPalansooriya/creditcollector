package com.modifica.creditcollectorapp;

/**
 * Created by Parinda on 4/5/2018.
 */

public class Loan {

    String loan_num;
    String nik_name;
    String f_amount;
    String amount_per_term;
    String full_terms;
    String available_amount;
    String loan_rate;
    String loan_period;
    String collection_type;
    String activation;
    String root_name;
    String start_date;
    String end_date;

    public Loan(){

    }

    public Loan(String loan_num, String nik_name, String f_amount, String amount_per_term, String full_terms, String available_amount,
                String loan_rate, String loan_period, String collection_type, String activation, String root_name, String start_date, String end_date){

        this.loan_num = loan_num;
        this.nik_name = nik_name;
        this.f_amount = f_amount;
        this.amount_per_term = amount_per_term;
        this.full_terms = full_terms;
        this.available_amount = available_amount;
        this.loan_rate = loan_rate;
        this.loan_period = loan_period;
        this.collection_type = collection_type;
        this.activation = activation;
        this.root_name = root_name;
        this.start_date = start_date;
        this.end_date = end_date;

    }


    public String getLoan_num() {
        return loan_num;
    }

    public String getNik_name() {
        return nik_name;
    }

    public String getF_amount() {
        return f_amount;
    }

    public String getAmount_per_term() {
        return amount_per_term;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getFull_terms() {
        return full_terms;
    }

    public String getAvailable_amount() {
        return available_amount;
    }

    public String getLoan_rate() {
        return loan_rate;
    }

    public String getLoan_period() {
        return loan_period;
    }

    public String getCollection_type() {
        return collection_type;
    }

    public String getActivation() {
        return activation;
    }

    public String getRoot_name() {
        return root_name;
    }
}
