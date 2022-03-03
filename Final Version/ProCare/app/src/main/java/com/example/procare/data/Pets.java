package com.example.procare.data;

import java.io.Serializable;

public class Pets implements Serializable {
    private String mName;
    private Integer mId;
    private String mType;
    private Integer mIdOwner;

    public Integer getId() {return mId;}
    public String getName() {return mName;}
    public String getType() {return mType;}
    public Integer getIdOwner() {return mIdOwner;}

    public void setId(Integer id) {mId = id;}
    public void setName(String name) {mName = name;}
    public void setType(String type) {mType = type;}
    public void setIdOwner(int idOwner) { mIdOwner = idOwner;}
}

