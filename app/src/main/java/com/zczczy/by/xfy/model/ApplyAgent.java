package com.zczczy.by.xfy.model;

/**
 * Created by zczczy on 2015/11/15.
 * 代理申请
 */
public class ApplyAgent {

    public String id;
    public String company;
    public  String company_city;
    public  String regional_agent;
    public String person;
    public String tel;
    public String email;
    public String truetime;
    public String state;
    public  ApplyAgent(){

    }
    public ApplyAgent( String company,String company_city,String regional_agent, String person, String
            tel, String email) {
        this.company = company;
        this.company_city = company_city;
        this.person = person;
        this.regional_agent = regional_agent;
        this.tel = tel;
        this.email = email;
    }

}
