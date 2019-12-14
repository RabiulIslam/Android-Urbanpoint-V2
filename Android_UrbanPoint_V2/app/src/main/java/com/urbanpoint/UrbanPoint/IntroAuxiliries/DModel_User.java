package com.urbanpoint.UrbanPoint.IntroAuxiliries;

/**
 * Created by Lenovo on 8/9/2018.
 */

public class DModel_User {
    public String mUserId;
    public String mName;
    public String mEmail;
    public String mAge;
    public String mDob;
    public String mGender;
    public String mNationality;
    public boolean isNationalityLstDisplyd;
    public String mPinCode;
    public String mNetworkType;
    public String mPhoneNumber = "0";
    public String mAuthorizationToken;
    public String mFCMToken;
    public float mAppVersion;
    public boolean isEligible;
    public boolean isNeverAskActive;
    public boolean isForcefullyUpdateActive;
    public boolean isUberRequired;
    public boolean isSubscribed;
    public boolean mCanUnSubscribe;
    public String masterMerchant;
    public boolean isLoggedIn;
    public boolean isPremierUser;
    public String EmailVerified;
    public String PhoneVerified;
    public String mReferralCode;
    public float wallet;
    public String zone;

    //Changes by Rashmi
    public String expiryDate;
    public String phnoVerified;

    public DModel_User() {
        this.mUserId = "";
        this.mName = "";
        this.mEmail = "";
        this.mAge = "";
        this.mDob = "";
        this.mGender = "";
        this.mNationality = "";
        this.isNationalityLstDisplyd = false;
        this.mPinCode = "";
        this.mNetworkType = "";
        this.mPhoneNumber = "";
        this.mAuthorizationToken = "";
        this.mFCMToken = "";
        this.mAppVersion = 0;
        this.isEligible = false;
        this.isNeverAskActive = false;
        this.isForcefullyUpdateActive = false;
        this.isUberRequired = false;
        this.isSubscribed = false;
        this.mCanUnSubscribe = false;
        this.masterMerchant = "";
        this.isLoggedIn = false;
        this.isPremierUser = false;
        this.zone = "";

        this.expiryDate = "";
        this.phnoVerified = "";
    }

    public void clearUserModel() {
        this.mUserId = "";
        this.mName = "";
        this.mEmail = "";
        this.mAge = "";
        this.mDob = "";
        this.mGender = "";
        this.mNationality = "";
        this.isNationalityLstDisplyd = false;
        this.mPinCode = "";
        this.mNetworkType = "";
        this.mPhoneNumber = "";
        this.mAuthorizationToken = "";
        this.mFCMToken = "";
        this.mAppVersion = 0;
        this.isEligible = false;
        this.isNeverAskActive = false;
        this.isForcefullyUpdateActive = false;
        this.isUberRequired = false;
        this.isSubscribed = false;
        this.mCanUnSubscribe = false;
        this.masterMerchant = "";
        this.isLoggedIn = false;
        this.isPremierUser = false;
        this.zone = "";

        this.expiryDate = "";
        this.phnoVerified = "";
    }

    public String getPhnoVerified() {
        return phnoVerified;
    }

    public void setPhnoVerified(String phnoVerified) {
        this.phnoVerified = phnoVerified;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getEmailVerified() {
        return EmailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        EmailVerified = emailVerified;
    }

    public String getPhoneVerified() {
        return PhoneVerified;
    }

    public void setPhoneVerified(String phoneVerified) {
        PhoneVerified = phoneVerified;
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmDob() {
        return mDob;
    }

    public void setmDob(String mDob) {
        this.mDob = mDob;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmNationality() {
        return mNationality;
    }

    public void setmNationality(String mNationality) {
        this.mNationality = mNationality;
    }

    public String getmPinCode() {
        return mPinCode;
    }

    public void setmPinCode(String mPinCode) {
        this.mPinCode = mPinCode;
    }

    public String getmNetworkType() {
        return mNetworkType;
    }

    public void setmNetworkType(String mNetworkType) {
        this.mNetworkType = mNetworkType;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.isSubscribed = subscribed;
    }

    public boolean ismCanUnSubscribe() {
        return mCanUnSubscribe;
    }

    public void setmCanUnSubscribe(boolean mCanUnSubscribe) {
        this.mCanUnSubscribe = mCanUnSubscribe;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public float getmAppVersion() {
        return mAppVersion;
    }

    public void setmAppVersion(float mAppVersion) {
        this.mAppVersion = mAppVersion;
    }

    public String getmFCMToken() {
        return mFCMToken;
    }

    public void setmFCMToken(String mFCMToken) {
        this.mFCMToken = mFCMToken;
    }

    public String getmAuthorizationToken() {
        return mAuthorizationToken;
    }

    public void setmAuthorizationToken(String mAuthorizationToken) {
        this.mAuthorizationToken = mAuthorizationToken;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    public boolean isPremierUser() {
        return isPremierUser;
    }

    public void setPremierUser(boolean premierUser) {
        isPremierUser = premierUser;
    }

    public boolean isUberRequired() {
        return isUberRequired;
    }

    public void setUberRequired(boolean uberRequired) {
        isUberRequired = uberRequired;
    }

    public boolean isForcefullyUpdateActive() {
        return isForcefullyUpdateActive;
    }

    public void setForcefullyUpdateActive(boolean forcefullyUpdateActive) {
        isForcefullyUpdateActive = forcefullyUpdateActive;
    }

    public String getMasterMerchant() {
        return masterMerchant;
    }

    public void setMasterMerchant(String masterMerchant) {
        this.masterMerchant = masterMerchant;
    }

    public boolean isNeverAskActive() {
        return isNeverAskActive;
    }

    public void setNeverAskActive(boolean neverAskActive) {
        isNeverAskActive = neverAskActive;
    }

    public boolean isNationalityLstDisplyd() {
        return isNationalityLstDisplyd;
    }

    public void setNationalityLstDisplyd(boolean nationalityLstDisplyd) {
        isNationalityLstDisplyd = nationalityLstDisplyd;

    }

    public String getmReferralCode() {
        return mReferralCode;
    }

    public void setmReferralCode(String mReferralCode) {
        this.mReferralCode = mReferralCode;
    }
}
