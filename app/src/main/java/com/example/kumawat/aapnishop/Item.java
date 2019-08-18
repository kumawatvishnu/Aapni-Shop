package com.example.kumawat.aapnishop;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

import java.io.Serializable;

//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by Kumawat on 4/22/2018.
 */

public class Item implements Serializable{
    private int id;
    private String pName;
    private String pDetails;
    private double purPrice;
    private double custPrice;
    private int image;

    public Item(int id, String pName, String pDetails, double purPrice, double custPrice){
        this.id = id;
        this.pName = pName;
        this.pDetails = pDetails;
        this.purPrice = purPrice;
        this.custPrice = custPrice;
        //this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDetails() {
        return pDetails;
    }

    public void setpDetails(String pDetails) {
        this.pDetails = pDetails;
    }

    public double getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(double purPrice) {
        this.purPrice = purPrice;
    }

    public double getCustPrice() {
        return custPrice;
    }

    public void setCustPrice(double custPrice) {
        this.custPrice = custPrice;
    }

    /*@Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof Item) {
            final Item wordModel = (Item) item;
            return wordModel.id == id;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof Item) {
            final Item other = (Item) item;
            if (id != other.id) {
                return false;
            }
            return pName != null ? pName.equals(other.pName) : other.pName == null;
        }
        return false;
    }

    /*public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }*/
}
