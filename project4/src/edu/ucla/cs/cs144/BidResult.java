package edu.ucla.cs.cs144;

/**
 * Created by Roger on 3/1/2015.
 */
public class BidResult implements Comparable<BidResult> {
    private String bidderID;
    private String bidderRating;
    private String bidderLocation;
    private String bidderCountry;
    private String bidTime;
    private String bidAmount;

    public BidResult() {}

    public BidResult(String id, String rating, String time, String amount) {
        this.bidderID = id;
        this.bidderRating = rating;
        this.bidTime = time;
        this.bidAmount = amount;
    }

    public int compareTo(BidResult other) {
        // Descending order
        return other.getBidTime().compareTo(this.getBidTime());
    }

    public void setLocationInfo(String location, String country) {
        this.bidderLocation = location;
        this.bidderCountry = country;
    }

    public String getBidderID() {
        return bidderID;
    }

    public String getBidderRating() {
        return bidderRating;
    }

    public String getBidderLocation() {
        return bidderLocation;
    }

    public String getBidderCountry() {
        return bidderCountry;
    }

    public String getBidTime() {
        return bidTime;
    }

    public String getBidAmount() {
        return bidAmount;
    }
}
