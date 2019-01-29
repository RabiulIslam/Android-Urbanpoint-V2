package com.urbanpoint.UrbanPoint.Utils;


import com.google.gson.annotations.SerializedName;

public class SignUpUser {

    // Response object from server.

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String responseMessage;

    @SerializedName("data")
    private String userData;


    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    //-----------------------------------------------------------------------------

    // Input object data for server.
    private String customerPin;
    private String categoryPreference;
    private String eMailId;
    private String firstName;


    private String age;
    private int gender;
    private int vodafonecustomer = -1;
    private int ooredoocustomer = -1;

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

    public String getCategoryPreference() {
        return categoryPreference;
    }

    public void setCategoryPreference(String categoryPreference) {
        this.categoryPreference = categoryPreference;
    }

    public String geteMailId() {
        return eMailId;
    }

    public void seteMailId(String eMailId) {
        this.eMailId = eMailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getVodafonecustomer() {
        return vodafonecustomer;
    }

    public void setVodafonecustomer(int vodafonecustomer) {
        this.vodafonecustomer = vodafonecustomer;
    }

    public int getOoredoocustomer() {
        return ooredoocustomer;
    }

    public void setOoredoocustomer(int ooredoocustomer) {
        this.ooredoocustomer = ooredoocustomer;
    }

    @Override
    public String toString() {
        return "SignUpUser{" +
                "status='" + status + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", customerPin='" + customerPin + '\'' +
                ", categoryPreference='" + categoryPreference + '\'' +
                ", eMailId='" + eMailId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gender=" + gender +
                '}';
    }
}
