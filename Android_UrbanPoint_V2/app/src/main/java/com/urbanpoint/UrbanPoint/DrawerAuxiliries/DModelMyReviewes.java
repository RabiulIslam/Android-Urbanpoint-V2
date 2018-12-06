package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

/**
 * Created by Danish on 3/14/2018.
 */

public class DModelMyReviewes {

    String id;
    String merchantName;
    String offerName;
    String image;
    String reviewe;

    public DModelMyReviewes(String id, String merchantName, String offerName, String image, String reviewe) {
        this.id = id;
        this.merchantName = merchantName;
        this.offerName = offerName;
        this.image = image;
        this.reviewe = reviewe;
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

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setReviewe(String reviewe) {
        this.reviewe = reviewe;
    }

    public String getReviewe() {
        return reviewe;
    }
}

