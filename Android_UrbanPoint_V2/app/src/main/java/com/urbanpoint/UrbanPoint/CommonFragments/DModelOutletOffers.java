package com.urbanpoint.UrbanPoint.CommonFragments;

/**
 * Created by Danish on 3/5/2018.
 */

public class DModelOutletOffers {

    String id;
    String image;
    String offerName;
    String merchantName;
    double merchantDistance;
    String special;

    public DModelOutletOffers(String id, String image, String offerName, String merchantName, double merchantDistance, String special) {
        this.id = id;
        this.image = image;
        this.offerName = offerName;
        this.merchantName = merchantName;
        this.merchantDistance = merchantDistance;
        this.special = special;
    }

    public DModelOutletOffers() {
        this.id = "";
        this.image = "";
        this.offerName = "";
        this.merchantName = "";
        this.merchantDistance = 0;
        this.special = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantDistance(double merchantDistance) {
        this.merchantDistance = merchantDistance;
    }

    public double getMerchantDistance() {
        return merchantDistance;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSpecial() {
        return special;
    }
}
