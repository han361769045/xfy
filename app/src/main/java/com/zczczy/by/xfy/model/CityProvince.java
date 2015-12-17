package com.zczczy.by.xfy.model;


import java.util.List;

/**
 * Created by zczczy on 2015/11/13.
 */
public class CityProvince {

        public String id;
        public String province_name;
        public List<Cities> cities;

        public  static  class Cities{
                public  String id;
                public  String province_id;
                public  String city_name;
        }



}
