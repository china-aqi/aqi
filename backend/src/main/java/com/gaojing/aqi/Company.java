package com.gaojing.aqi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rowid;

    private String code;
    private float marketcap;
    private String industry;
    private String mainbusiness;
    private String province;
    private String city;


    public String getCode() { return code; }

    public float getMarketcap() { return marketcap; }

    public String getIndustry() { return industry; }

    public String getMainbusiness() { return mainbusiness; }

    public String getProvince() { return province; }

    public String getCity() { return city; }
}