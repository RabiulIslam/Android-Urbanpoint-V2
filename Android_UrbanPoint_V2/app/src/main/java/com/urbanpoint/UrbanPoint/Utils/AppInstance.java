package com.urbanpoint.UrbanPoint.Utils;

//import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.AddToWishlist;
//import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.FoodOfferDeatils;
//import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.MerchantDetailsList;
//import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.Offer;
//import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.RModel_MerchintList;
//import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_CheckSubscription;
//import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_FavoritesList;
//import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
//import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_Notifications;
//import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.LookingForFilterList;
//import com.urbanpoint.UrbanPoint.dataobject.drawerItem.AccessFree;
//import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsList;
//import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;
//import com.urbanpoint.UrbanPoint.dataobject.main.HomeViewDetails;
//import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItems;
//import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.PromoCodeStatus;
//import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.VodafoneBillInfoDetails;
//import com.urbanpoint.UrbanPoint.dataobject.profile.ProfileInfo;
//import com.urbanpoint.UrbanPoint.dataobject.wishList.WishListList;
//import com.urbanpoint.UrbanPoint.utils.Constants;

import java.util.List;

//import sdei.support.lib.debug.LogUtility;



public class AppInstance {

    private static AppInstance appInstance = null;
//    public static LogUtility logObj;
//    public static InputUser userObj;
//    public static LoggedInUser loggedInUser;
    public static SignUpUser signUpUser;
//    public static SignUpUser tempLocalSignUpUser;
//
//    public static UserChangePin userChangePin;
//    public static MyEarningsInfo myEarningsInfo;
//    public static PurchaseHistory purchaseHistory;
//    public static Offer offers;
//    public static RModel_MerchintList rModel_merchintList;
//    public static FoodOfferDeatils foodOfferDeatils;
//    public static AddToWishlist addToWishlist, removeFromWishlist;
//    public static MerchantDetailsList merchantDetailsList;
//    public static LookingForFilterList lookingForFilterList;
//    public static Redeem redeem;
//    public static WishListList wishListList;
//    public static ForgotPassword forgotPassword;
//
//    public static OfferPackagesItems offerPackagesItems;
//
//    public static MyReviewsList myReviewsList;
//    public static OoredooValidation ooredooValidation;
//    public static VodafoneValidation vodafoneValidation;
//    public static RModel_Notifications rModel_notifications;
//    public static RModel_FavoritesList rModel_favOffers;
//    public static RModel_HomeOffers rModel_newOffers, rModel_homeOffers;
//    public static List<DModelHomeGrdVw> mlstNewOffers;
//    public static RModel_CheckSubscription rModel_checkSubscription;

    public static boolean isBroadCastHomeCompleted = false;
    public static boolean isBroadCastSubscriptionCompleted = false;
    public static int myReviewsCount = 0;
    public static boolean isProfileCompleted = false;
    private static boolean isSubscriptionCheckCalled = false;
    private static boolean isSubscriptionUpdateCheckCalled = false;
    public static int offersMyWishListCountToDisplay = 0;
    public static boolean isNetworkMsgShowd = false;

//    public static PromoCodeStatus promoCodeStatus;
//    public VodafoneBillInfoDetails vodafoneBillInfoDetails;


    public static boolean isAppLogedIn = false;
    public static boolean isUserSubscribed = false;
    public static boolean canUserUnSubscribe = false;
    public static String strUserStatus = "";
    public static boolean isUberRequired = true;
    public static int PurchaseCount = 0;


    public static int getPurchaseCount() {
        return PurchaseCount;
    }

    public static void setPurchaseCount(int purchaseCount) {
        PurchaseCount = purchaseCount;
    }

    public static String getStrUserStatus() {
        return strUserStatus;
    }


    public static boolean isIsUberRequired() {
        return isUberRequired;
    }

    public static void setIsUberRequired(boolean isUberRequired) {
        AppInstance.isUberRequired = isUberRequired;
    }

    public static void setStrUserStatus(String strUserStatus) {
        AppInstance.strUserStatus = strUserStatus;
    }

//    private static HomeViewDetails homeScreenDetails;

    // TO check that ,VODA ACTIVATION IN PROGRESS/COMPLETED
    public static boolean vodaSubscriptionActivationStatus = false;

    public static boolean isMyFavesDeleteMessageShown = false;
    public static boolean isUserSubscribeStatusServiceCalled = false;
//    public static ProfileInfo profileData;

    public static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        AppInstance.name = name;
    }

    //---
    public static boolean goHome=false;
//    public static AccessFree accessFree;
    //---

    /**
     * To initialize the appInstance Object
     *
     * @return singleton instance
     */

    public static AppInstance getAppInstance() {
        if (appInstance == null) {
            appInstance = new AppInstance();
//            profileData = new ProfileInfo();
//            userObj = new InputUser();
//            /**
//             * the object will manage the logs in the logcat
//             */
//            logObj = new LogUtility(Constants.DebugLog.APP_MODE, Constants.DebugLog.APP_TAG);
        }

        return appInstance;
    }

    public static void setIsSubscriptionCheckCalled(boolean isSubscriptionCheckCalled) {
        AppInstance.isSubscriptionCheckCalled = isSubscriptionCheckCalled;
    }

    public static boolean isSubscriptionCheckCalled() {
        return isSubscriptionCheckCalled;
    }

    public static void setIsSubscriptionUpdateCheckCalled(boolean isSubscriptionCheckCalled) {
        AppInstance.isSubscriptionUpdateCheckCalled = isSubscriptionCheckCalled;
    }

    public static boolean isSubscriptionUpdateCheckCalled() {
        return isSubscriptionUpdateCheckCalled;
    }

    public static int getOffersMyWishListCountToDisplay() {
        return offersMyWishListCountToDisplay;
    }

    public static void setOffersMyWishListCountToDisplay(int offersMyWishListCountToDisplay) {
        AppInstance.offersMyWishListCountToDisplay = offersMyWishListCountToDisplay;
    }


    public boolean isUserSubscribed() {
        return isUserSubscribed;
    }

    public void setIsUserSubscribed(boolean isUserSubscribed) {
        this.isUserSubscribed = isUserSubscribed;
    }

//    public static HomeViewDetails getHomeScreenDetails() {
//        return homeScreenDetails;
//    }
//
//    public static void setHomeScreenDetails(HomeViewDetails homeScreenDetails) {
//        AppInstance.homeScreenDetails = homeScreenDetails;
//    }
//
//    public VodafoneBillInfoDetails getVodafoneBillInfoDetails() {
//        return vodafoneBillInfoDetails;
//    }
//
//    public void setVodafoneBillInfoDetails(VodafoneBillInfoDetails vodafoneBillInfoDetails) {
//        this.vodafoneBillInfoDetails = vodafoneBillInfoDetails;
//    }
//
//    public static AccessFree getAccessFree() {
//        return accessFree;
//    }
//
//    public static void setAccessFree(AccessFree accessFree) {
//        AppInstance.accessFree = accessFree;
//    }
}
