package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import java.util.List;

/**
 * Created by Lenovo on 8/28/2018.
 */

public class DModelPurchaseHistoryList {

    String strDate;
    String totalSavings;
    List<Child> child;


    public DModelPurchaseHistoryList(String strDate, List<Child> child, String totalSavings) {
        this.strDate = strDate;
        this.child = child;
        this.totalSavings = totalSavings;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public static class Child {
        String ImgUrl;
        String offerName;
        String merchantName;
        String merchantAddr;
        String approxSavings;

        public Child(String imgUrl, String offerName, String merchantName, String merchantAddr, String approxSavings) {
            this.ImgUrl = imgUrl;
            this.offerName = offerName;
            this.merchantName = merchantName;
            this.merchantAddr = merchantAddr;
            this.approxSavings = approxSavings;
        }

        public String getOfferName() {
            return offerName;
        }

        public void setOfferName(String offerName) {
            this.offerName = offerName;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String name) {
            this.merchantName = name;
        }

        public String getMerchantAddr() {
            return merchantAddr;
        }

        public void setMerchantAddr(String merchantAddr) {
            this.merchantAddr = merchantAddr;
        }

        public String getApproxSavings() {
            return approxSavings;
        }

        public void setApproxSavings(String approxSavings) {
            this.approxSavings = approxSavings;
        }

     }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public String getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(String totalSavings) {
        this.totalSavings = totalSavings;
    }

}
