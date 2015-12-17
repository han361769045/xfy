package com.zczczy.by.xfy.model;

/**
 * Created by zczczy on 2015/11/15.
 * 法人申请
 */
public class ApplyAccount {

    
    public String id;
    public String company;
    public String city;
    public String person;
    public String tel;
    public String email;
    public String truetime;
    public String state;
    public  ApplyAccount(){

    }
    public ApplyAccount( String company, String city, String person, String
            tel, String email) {

        this.company = company;
        this.city = city;
        this.person = person;
        this.tel = tel;
        this.email = email;

    }



}
