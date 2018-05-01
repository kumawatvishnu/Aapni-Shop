package com.example.kumawat.aapnishop;

import java.io.Serializable;

/**
 * Created by Kumawat on 4/24/2018.
 */

class Transaction implements Serializable{
    private int id;
    private double trAmount;
    private String trDetails;
    private String trType;

    public Transaction(int id, double trAmount, String trDetails, String trType) {
        this.id = id;
        this.trAmount = trAmount;
        this.trDetails = trDetails;
        this.trType = trType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTrAmount() {
        return trAmount;
    }

    public void setTrAmount(double trAmount) {
        this.trAmount = trAmount;
    }

    public String getTrDetails() {
        return trDetails;
    }

    public void setTrDetails(String trDetails) {
        this.trDetails = trDetails;
    }

    public String getTrType() {
        return trType;
    }

    public void setTrType(String trType) {
        this.trType = trType;
    }
}
