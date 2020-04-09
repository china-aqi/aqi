package com.gaojing.aqi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rowid;

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    private String displayname;
    private String name;
    private String code;
    private String match;


    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public String getCode() { return code; }

    public String getMatch() { return match; }

}