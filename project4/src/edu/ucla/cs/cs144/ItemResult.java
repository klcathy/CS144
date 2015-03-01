package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;

public class ItemResult {

    private String itemID;
    private String name;
    private String currently;
    private String first_bid;
    private String buy_price;
    private String num_bids;
    private String location;
    private String country;
    private String started;
    private String ends;
    private String longitude;
    private String latitude;
    private String sellerID;
    private String rating;
    private String description;

    public ItemResult() {}

    public ItemResult(String itemID, String name, String currently, String first_bid, String num_bids,
                      String started, String ends, String sellerID, String rating, String description) {
        this.itemID = itemID;
        this.name = name;
        this.currently = currently;
        this.first_bid = first_bid;
        this.num_bids = num_bids;
        this.started = started;
        this.ends = ends;
        this.sellerID = sellerID;
        this.rating = rating;
        this.description = description;
    }

    public void setBuyPrice (String buy_price) {
        if (buy_price != null)
            this.buy_price = buy_price;
        else
            this.buy_price = "N/A";
    }

    public void setLocationInfo (String location, String country, String longitude, String latitude) {
        this.location = location;
        this.country = country;
        if (longitude != null) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
        else {
            this.longitude = "N/A";
            this.latitude = "N/A";
        }
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getCurrently() {
        return currently;
    }

    public String getFirstBid() {
        return first_bid;
    }

    public String getBuyPrice() {
        return buy_price;
    }

    public String getNumBids() {
        return num_bids;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public String getStarted() {
        return started;
    }

    public String getEnds() {
        return ends;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

}
