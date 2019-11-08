package com.modifica.creditcollectorapp;

/**
 * Created by Parinda on 3/22/2018.
 */

public class Customer {

    String cus_date1;
    String cus_nic;
    String cus_fname;
    String cus_lname;
    String cus_add1;
    String cus_add2;
    String cus_add3 ;
    String cus_phone ;
    String cus_date ;
    String cus_nikname ;
    String cus_location;


    public Customer(){

    }

    public Customer(String cus_nic, String cus_fname, String cus_lname, String cus_nikname, String cus_add1,
                    String cus_add2, String cus_add3, String cus_phone, String cus_location,String cus_date) {
        this.cus_nic = cus_nic;
        this.cus_fname = cus_fname;
        this.cus_lname = cus_lname;
        this.cus_add1 = cus_add1;
        this.cus_add2 = cus_add2;
        this.cus_add3 = cus_add3;
        this.cus_phone = cus_phone;
        this.cus_date = cus_date;
        this.cus_nikname = cus_nikname;
        this.cus_location = cus_location;

    }


    public String getCus_nic() {
        return cus_nic;
    }

    public String getCus_fname() {
        return cus_fname;
    }

    public String getCus_lname() {
        return cus_lname;
    }

    public String getCus_add1() {
        return cus_add1;
    }

    public String getCus_add2() {
        return cus_add2;
    }

    public String getCus_add3() {
        return cus_add3;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public String getCus_nikname() {
        return cus_nikname;
    }

    public String getCus_location() {
        return cus_location;
    }


}
