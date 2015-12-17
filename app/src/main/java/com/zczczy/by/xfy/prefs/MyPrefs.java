package com.zczczy.by.xfy.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Leo on 2015/3/9.
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface MyPrefs {

    @DefaultString("")
    String token();
    @DefaultString("")
    String tid();

    @DefaultString("")
    String username();

    @DefaultString("")
    String password();

    @DefaultString("")
    String truetime();

    @DefaultString("")
    String city();

    @DefaultString("")
    String endtime();

    @DefaultString("")
    String industry();

    @DefaultString("")
    String state();

    @DefaultString("")
    String N_client_id();

    @DefaultString("")
    String N_unit_name();

    @DefaultString("")
    String N_phone();

    @DefaultString("")
    String N_address();

    @DefaultString("")
    String N_onduty_place();

    @DefaultString("")
    String N_area();

    @DefaultString("")
    String n_building();

    @DefaultString("")
    String n_opened();

    @DefaultString("")
    String N_remark();

    @DefaultString("")
    String C_name();
    @DefaultString("")
    String C_phone();

    @DefaultString("")
    String C_address();

    @DefaultString("")
    String C_linkman();

    @DefaultString("")
    String C_remark();

    @DefaultString("")
    String server_name();

    @DefaultString("")
    String ip();

    @DefaultString("")
    String dbuser();

    @DefaultString("")
    String dbpass();

    @DefaultString("")
    String dbname();

    @DefaultString("")
    String province();

    @DefaultString("")
    String port();



}
