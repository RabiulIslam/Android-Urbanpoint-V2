package com.urbanpoint.UrbanPoint.Utils;


public interface ApiMethod {

    interface GET {
        String getOutlets = "getOutlets";
        String getOffers = "getOffers";
        String getMyNotifications = "getMyNotifications";
        String getMyFavouriteOffers  = "getMyFavouriteOffers ";
        String getOfferDetail = "getOfferDetail";
        String logout = "logout";
        String uber_time_estimate = "https://api.uber.com/v1.2/estimates/time";
        String uber_price_estimate = "https://api.uber.com/v1.2/estimates/price";
        String sendMT = "sendMT";
        String addPremierUser = "addPremierUser";
        String getMyPurchaseHistory = "getMyPurchaseHistory";
        String getMyOrdersWithoutReview = "getMyOrdersWithoutReview";

      }

    interface POST {
        String signIn = "signIn";
        String signUp = "registration-step-one";
        String forgotPassword = "forgotPassword";
        String getAuthorization = "getAuthorization";
        String checkPhone = "registration-step-two";
        String verifyEmail="verifyEmail";
        String checkEmail = "checkEmail";
        String homeApi = "homeApi";
        String eligibilitychecker = "eligibilitychecker";
        String addFavouriteOffer = "addFavouriteOffer";
        String deleteMyFavouriteOffer = "deleteMyFavouriteOffer";
        String redeemOffer = "redeemOffer";
        String usePromoCode = "usePromoCode";
         String subscribe = "subsribeuser";
        String validatemsisdn = "validatemsisdn";
        String unsubscribe = "unsubscribe";
        String contactUs = "contactUs";
        String updateProfile = "updateProfile";
        String addReview = "addReview";
        String readNotification = "readNotification";
        String updatePermission = "updatePermission";

        String SEND_OTP = "sendotp.php";
        String VERIFY_OTP = "verifyotp.php";
     }

    interface PATCH {
        String re_assign = "order/reassign/";

    }

    interface HEADER {
        String Access_Token = "Access-Token";
        String Authorization = "Authorization";
        String app_id = "app_id ";
        String CONTENT_TYPE = "Content-Type";
    }
    interface HeadersValue {
        String Authorization = "UP!and$";
        String app_id = "1";
        String CONTENT_TYPE = "application/json";
    }
}

