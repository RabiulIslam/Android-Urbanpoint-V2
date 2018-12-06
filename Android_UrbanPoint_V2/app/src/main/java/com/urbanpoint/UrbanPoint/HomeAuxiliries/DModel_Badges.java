package com.urbanpoint.UrbanPoint.HomeAuxiliries;

/**
 * Created by Lenovo on 8/13/2018.
 */

public class DModel_Badges {
    public int notificationCount ;
    public int reviewCount ;
    public int newOfferCount ;
    public int favoriteCount ;

    public DModel_Badges() {
        this.notificationCount = 0;
        this.reviewCount = 0;
        this.newOfferCount = 0;
        this.favoriteCount = 0;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getNewOfferCount() {
        return newOfferCount;
    }

    public void setNewOfferCount(int newOfferCount) {
        this.newOfferCount = newOfferCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
