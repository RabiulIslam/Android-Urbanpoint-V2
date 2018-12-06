package com.urbanpoint.UrbanPoint.HomeAuxiliries;

/**
 * Created by Lenovo on 6/21/2017.
 */

public class DModel_NotificationList {
    private String notificationId;
    private String merchantName;
    private String merchantMessage;
    private String date;
    private boolean isRead;

    public DModel_NotificationList(String notificationId, String merchantName, String merchantMessage,String date, boolean isRead) {
        this.notificationId = notificationId;
        this.merchantName = merchantName;
        this.merchantMessage = merchantMessage;
        this.date = date;
        this.isRead = isRead;
    }

    public DModel_NotificationList() {
        this.notificationId = "";
        this.merchantName = "";
        this.merchantMessage = "";
        this.date = "";
        this.isRead = false;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantMessage() {
        return merchantMessage;
    }

    public void setMerchantMessage(String merchantMessage) {
        this.merchantMessage = merchantMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
