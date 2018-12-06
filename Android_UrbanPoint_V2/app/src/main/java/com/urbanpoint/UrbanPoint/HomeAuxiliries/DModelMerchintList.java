package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 8/28/2018.
 */

public class DModelMerchintList {

    String id;
    String merchantName;
    String merchantAddress;
    String merchantTimmings;
    String merchantDescription;
    int merchantDistance;
    boolean isDistanceRequired;
    String festival;
    String merchantsImageUrl;
    String merchantsLogoUrl;
    String merchantsPhone;
    String strSpecial;
    List<Child> child;


    public DModelMerchintList(String id,  String merchantName, String merchantAddress, String merchantTimmings,
                              String merchantDescription, int merchantDistance,boolean isDistanceRequired ,String special, String festival,
                              String merchantsImageUrl,String merchantsLogoUrl, String merchantsPhone, List<Child> child) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantTimmings = merchantTimmings;
        this.merchantDescription = merchantDescription;
        this.isDistanceRequired = isDistanceRequired;
        this.merchantDistance = merchantDistance;
        this.festival = festival;
        this.merchantsImageUrl = merchantsImageUrl;
        this.merchantsLogoUrl = merchantsLogoUrl;
        this.merchantsPhone = merchantsPhone;
        this.child = child;
        this.strSpecial = special;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public void setStrSpecial(String strSpecial) {
        this.strSpecial = strSpecial;
    }

    public String getStrSpecial() {
        return strSpecial;
    }

    public String getMerchantTimmings() {
        return merchantTimmings;
    }

    public void setMerchantTimmings(String merchantTimmings) {
        this.merchantTimmings = merchantTimmings;
    }

    public String getMerchantDescription() {
        return merchantDescription;
    }

    public void setMerchantDescription(String merchantDescription) {
        this.merchantDescription = merchantDescription;
    }

    public boolean isDistanceRequired() {
        return isDistanceRequired;
    }

    public void setDistanceRequired(boolean distanceRequired) {
        isDistanceRequired = distanceRequired;
    }

    public int getMerchantDistance() {
        return merchantDistance;
    }

    public void setMerchantDistance(int merchantDistance) {
        this.merchantDistance = merchantDistance;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String getMerchantsImageUrl() {
        return merchantsImageUrl;
    }

    public void setMerchantsImageUrl(String merchantsImageUrl) {
        this.merchantsImageUrl = merchantsImageUrl;
    }

    public String getMerchantsLogoUrl() {
        return merchantsLogoUrl;
    }

    public void setMerchantsLogoUrl(String merchantsLogoUrl) {
        this.merchantsLogoUrl = merchantsLogoUrl;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

    public static class Child {
        String ProductId;
        String ImgUrl;
        String Name;
        String Festival;
        String Status;
        String Gender;
        String Thumbnail;
        String special;

        public Child(String productId, String imgUrl, String name, String special1, String festival, String status, String gender, String thumbnail) {
            ProductId = productId;
            ImgUrl = imgUrl;
            Name = name;
            Festival = festival;
            Status = status;
            Gender = gender;
            Thumbnail = thumbnail;
            special = special1;
        }

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String productId) {
            ProductId = productId;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public String getSpecial() {
            return special;
        }

        public String getFestival() {
            return Festival;
        }

        public void setFestival(String festival) {
            Festival = festival;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getThumbnail() {
            return Thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            Thumbnail = thumbnail;
        }
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

}
