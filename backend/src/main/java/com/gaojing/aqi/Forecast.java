package com.gaojing.aqi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rowid;

    private String code;
    private float eps2018;
    private float eps2019;
    private float eps2020;
    private float eps2021;


    public String getCode() { return code; }

    public float getEps2018() { return eps2018; }

    public float getEps2019() { return eps2019; }

    public float getEps2020() { return eps2020; }

    public float getEps2021() { return eps2021; }
}