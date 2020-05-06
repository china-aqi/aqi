package com.gaojing.aqi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Qualityfactor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rowid;

    private String code;
    private float pquality;
    private float pprofitability;
    private float pgrowth;
    private float psafe;

    public float getPprofitability() {
        return pprofitability;
    }

    public float getPgrowth() {
        return pgrowth;
    }

    public float getPsafe() {
        return psafe;
    }

    public float getPpayout() {
        return ppayout;
    }

    private float ppayout;

    public String getCode() {
        return code;
    }

    public float getPquality() {
        return pquality;
    }
}