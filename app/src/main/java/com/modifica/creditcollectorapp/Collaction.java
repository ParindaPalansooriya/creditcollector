package com.modifica.creditcollectorapp;

/**
 * Created by Parinda on 4/22/2018.
 */

public class Collaction {

    String loan_num;
    String nik_name;
    String payment;
    String paid_terms;
    String date;
    String location;
    String due;
    String logT;
    String latT;

    public Collaction() {

    }

    public Collaction(String loan_num, String nik_name, String payment, String paid_terms, String date, String location, String due,String logT ,String latT) {
        this.loan_num = loan_num;
        this.nik_name = nik_name;
        this.payment = payment;
        this.paid_terms = paid_terms;
        this.date = date;
        this.location = location;
        this.due = due;
        this.latT = latT;
        this.logT = logT;
    }

    public String getLoan_num() {
        return loan_num;
    }

    public String getNik_name() {
        return nik_name;
    }

    public String getPayment() {
        return payment;
    }

    public String getPaid_terms() {
        return paid_terms;
    }

    public String getDate() {
        return date;
    }

    public String getLogT() {
        return logT;
    }

    public String getLatT() {
        return latT;
    }

    public String getDue() {
        return due;
    }

    public String getLocation() {
        return location;
    }
}
