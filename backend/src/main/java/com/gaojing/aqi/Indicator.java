package com.gaojing.aqi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rowid;

    private String code;
    private float eps;
    private float bps;


    public String getCode() { return code; }

    public float getEps() { return eps; }

    public float getBps() { return bps; }
}