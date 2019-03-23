package com.urbanpoint.UrbanPoint.Utils;


public interface AppConstt {

    //   Live Server
    //  String BASE_URL = "http://18.185.217.28/up_qatar/api/v1/";

    //    DEV Server
//     String BASE_URL="http://34.219.2.69/up_qatar/api/v1/";
//     String BASE_URL_OTP="http://34.219.2.69:9010/api/";
    //   Production Server
//    String BASE_URL_OTP = "http://52.37.72.27:9010/api/";
//    String BASE_URL = "https://cms.biyog.com/up_qatar/api/v1/";

//    String BASE_URL_OTP = "http://34.218.241.37:9010/api/";
//    String BASE_URL = "http://34.218.241.37/up_qatar/api/v1/";

    //VPN
    String BASE_URL_OTP = "http://192.168.29.214:3000/api/";
    String BASE_URL = "http://192.168.29.195/biyog/up_qatar/api/v1/";

    //String BASE_URL = "http://34.219.2.69/up_qatar/api/v1/";

    String BASE_URL_MOBILE = BASE_URL + "mobile/";
    String BASE_URL_SUBSCRIPTION = BASE_URL_MOBILE + "subsribeuser";
    String BASE_URL_IMAGES = "http://cms.biyog.com/up_qatar/uploads/";
    String mSignupUsername = "";
    String mSignupAge = "";
    String mSignupGender = "";
    String mSignupEmail = "";

//    Staging Server

    public static final String FLURRY_TOKEN = "MH74JX4SNGK89G427BGP";
    public static final String MIXPANEL_TOKEN = "a37d3739dc37b7bfec03e2d4a7c3ff8b";

    /**
     * Handles the TaskIDs so as to differentiate the web service return values
     */
    int LIMIT_API_RETRY = 0;
    int LIMIT_TIMOUT_MILLIS = 30000;
    public static final String LOGIN_RULES = "privacyStatementAndTermsofUseBiyog.html";
    public static final String REDEEM_RULES = "Rules_of_Purchase.html";
    public static final String TERMS_OF_SALES = "Terms_of_Sales.html";
    public static final String REFER_AND_EARN = "Refer &amp; Earn";
    String ARABIC = "ar";
    String ENGLISH = "en";
    static String DeviceType = "android";
    static String NetworkType = "ooredoo";


    interface UberRideEstimate {
        String CLIENT_ID = "4t0OnuFW8ukSYSn3HbiX_2C7dadRbTb-";
        String CLIENT_SECRET = "q2IZcOzGWg2UJADFV9uTSRgwELerJsAS6L8sfZCl";
        String SERVER_TOKEN = "ItwOBcYSD5Ro85dWfwwl9O2DxVB-By-eN2C8AvjW";
    }

    interface FRGTAG {
        String SplashFragment = "SplashFragment";
        String IntroMainFragment = "GetStartedFragment";
        String IntroGetStartedFragment = "SignupNameFragment";
        String FN_SignUpFragment = "SignUpFragment";
        String FN_SignUpVerificationFragment = "SignUpVerificationFragment";
        String FN_SignInFragment = "SignInFragment";
        String FN_ForgotPasswordFragment = "ForgotPasswordFragment";
        String IntroLoginFragment = "LoginFragment";
        String IntroSignupSelectGenderFragment = "SignupSelectGenderFragment";
        String SignupNameFragment = "SignupNameFragment";
        String SignupAgeFragment = "SignupAgeFragment";
        String SignupNetworkProviderFragment = "SignupNetworkProviderFragment";
        String SignupEmailPinFragment = "SignupEmailPinFragment";
        String HomeFragment = "HomeFragment";
        String NewOffersFragment = "NewOffersFragment";
        String SearchOffersFragment = "SearchOffersFragment";
        String AccessCodeFragment = "AccessCodeFragment";
        String FavouritesFragment = "FavouritesFragment";
        String NotificationsFragment = "NotificationsFragment";
        String MerchantListFragment = "MerchantListFragment";
        String PromocodeFragment = "PromocodeFragment";
        String ChangePinFragment = "ChangePinFragment";
        String UnSubscribeFragment = "UnSubscribeFragment";
        String CategoryFragment = "CategoryFragment";
        String SubscriptionFragment = "SubscriptionFragment";
        String SubscriptionEligibleFragment = "SubscriptionEligibleFragment";
        String SubscriptionConfirmFragment = "SubscriptionConfirmFragment";
        String OrderDetailFragment = "OrderDetailFragment";
        String SubscriptionSuccessFragment = "SubscriptionSuccessFragment";
        String SubscriptionEligibleSuccessFragment = "SubscriptionEligibleSuccessFragment";
        String OfferDetailFragment = "OfferDetailFragment";
        String OutletDetailFramgnet = "OutletDetailFramgnet";
        String MerchantPinFragment = "MerchantPinFragment";
        String HowToUseFragment = "HowToUseFragment";
        String ReferAndEarnFragment = "ReferAndEarnFragment";
        String ProfileFragment = "ProfileFragment";
        String WebViewFragment = "WebViewFragment";
        String FN_PurchaseSuccessFragment = "PurchaseSuccessFragment";
        String MerchantConfirmationPinFragment = "MerchantConfirmationPinFragment";
        String ProfileChangePinFragment = "ProfileChangePinFragment";
        String PurchaseHistoryFragment = "PurchaseHistoryFragment";
        String ContactUsFragment = "ContactUsFragment";
        String MyReviewsFragment = "ReviewOrdersFragment";
        String SearchFragment = "SearchFragment";
        String NotificationDetailFragment = "NotificationDetailFragment";
    }

    interface FireBaseEvents {
        String Successful_Signup = "Successful_Signup";
        String Successful_Login = "Successful_Login";
        String Number_Of_Offers_Viewed = "Number_Of_Offers_Viewed";
        String Food_And_Drink_Views = "Food_And_Drink_Views";
        String Beauty_And_Health_Views = "Beauty_And_Health_Views";
        String Fun_And_Leisure_Views = "Fun_And_Leisure_Views";
        String Retail_And_Services_Views = "Retail_And_Services_Views";
        String Successful_Redemptions = "Successful_Redemptions";
        String Gain_Access = "Gain_Access";
        String OTP_Generation = "OTP_Generation";
        String Successful_Regular_Subscription = "Successful_Regular_Subscription";
        String Successful_Bundled_Subscription = "Successful_Bundled_Subscription";
        String Manual_Unsubscription = "Manual_Unsubscription";
    }

    /* network values*/
    interface Networks {
        String VODA = "voda";
        String OOREDOO = "ooredoo";
    }

    interface Notifications {
        int PUSH_CATG_ANNOUNCEMENTS = 0;

        //Channels for Oreo+
        String CHNL_ID_ANNOUNCEMENT = "announcement";
        String CHNL_NAME_ANNOUNCEMENT = "Announcement";


        // Intent Strings
        String PUSH_NTIFCN_ID = "p_notification_id";
        String PUSH_NTIFCN_TITLE = "p_notification_title";
        String PUSH_NTIFCN_MSG = "p_notification_msg";
        String PUSH_NTIFCN_DATE = "p_notification_date";
        String PUSH_NTIFCN_READ = "p_notification_read";
        String PUSH_NTIFCN_BAGDE = "p_notification_badge";
        String LBCT_INTENT_NOTFICTN_UPDATED = "p_notification_updated";


        String EVENT_RIDE_CANCELLED = "1";
    }

    interface fragmentsValues {
        int home = 0;
        int invite_friends = 1;
        int profile = 2;
        int how_to_use = 3;
        int my_reviews = 4;
        int my_savings = 5;
        int promo_code = 6;
        int contact_us = 7;
    }

    public static class BugSenseConstants {
        //public static String SPLUNK_API_KEY = "963d5a30";
        public static String SPLUNK_API_KEY = "62be38b9";
    }

    public static class mixPanel {
        public static final String MIXPANEL_TOKEN = "82b79bf7bed2c21f5884dee3354f0fb1";
    }


    interface MSG_ERROR {
        //General App messages
        String PREFIX = "Error: ";
        String NETWORK = "Please check your Internet connection";

        String ENABLE_LOCATION = "Please enable location from your device";
        String EMAIL = "Please enter valid email";
        String MOBNUM = "Please enter valid mobile number";
        String PASSWORD_EMPTY = "Password field cannot be left empty";
        String PASSWORD_SHORT = "Please enter atleast 6 characters password";
        String PASSWORD_NOMATCH = "Both the passwords donot match";
        String CODE_EMPTY = "Please fill-in the code above, you recieved via SMS";
        String CODE_WRONG = "Please enter the correct code, you recieved via SMS";
        String CONTACTS_SYNC = "Contacts syncing faile.\nPlease retry";
        String Invalid_code = "This promo code is invalid.";
    }

    interface ServerStatus {
        //Server Response Status
        short OK = 200;
        short CREATED = 201;//
        short ACCEPTED = 202;//
        short NO_CONTENT = 204;//
        short RESET_CONTENT = 205;//
        short BAD_REQUEST = 400;//
        short UNAUTHORIZED = 401;
        short FORBIDDEN = 403;
        short DATABASE_NOT_FOUND = 404;
        short METHOD_NOT_ALLLOWED = 405;
        short NOT_ACCEPTABLE = 406;
        short CONFLICT = 409;//token expired
        short Unprocessable_Entity = 422;
        short INTERNAL_SERVER_ERROR = 500;
        short BAD_GATEWAY = 502;
        short HTTP_VERSION_NOTSUPPORTED = 505;
        short NETWORK_ERROR = 0;
        short ERROR = 104;
        short ERROR_ALREADY_EXIST = 101;

    }


    public static class Gender {
        public static final String MALE = "male";
        public static final String FEMALE = "female";
    }

    interface SubscriptionTypes {
        String Ooredoo = "ooredoo";
        String VodaFone = "vodafone";
        String Card = "card";
        String Code = "code";
    }

    interface BundleStrings {
        String outletId = "outletId";
        String offerId = "offerId";
        String orderId = "orderId";
        String subscribeMsisdn = "subscribeMsisdn";
        String cnfirmationPin = "cnfirmationPin";
        String categoryId = "categoryId";
        String categoryName = "categoryName";
        String offerName = "offerName";
        String merchantImage = "merchantImage";
        String merchantLogo = "merchantLogo";
        String merchantName = "merchantName";
        String merchantAddress = "merchantAddress";
        String merchantTimmings = "merchantTimmings";
        String merchantDescription = "merchantDescription";
        String merchantPhone = "merchantPhone";
        String merchantPIN = "merchantPIN";
        String merchantId = "merchantId";
        String premierUserPhone = "premierUserPhone";
        String premierUserPIN = "premierUserPIN";
        String purchaseSuccessPIN = "purchaseSuccessPIN";
        String notificationTile = "notificationTile";
        String notificationMessage = "notificationMessage";
        String notificationDate = "notificationDate";
        String backBtnVisibility = "backBtnVisibility";
        String userId = "userId";
    }

    //    public static String[] arrFlags = {"Afghanistan", "Aland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
//            "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Ascension Island", "Australia",
//            "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
//            "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia & Herzegovina", "Botswana", "Bouvet Island", "Brazil",
//            "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
//            "Cambodia", "Cameroon", "Canada", "Canary Islands", "Cape Verde", "Caribbean Netherlands", "Cayman Islands",
//            "Central African Republic", "Ceuta", "Chad", "Chile", "China", "Christmas Island", "Clipperton Island",
//            "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo - Brazzaville", "Congo - Kinshasa", "Cook Islands",
//            "Costa Rica", "Cote d'Ivoire", "Croatia", "Curacao", "Cyprus", "Czech Republic", "Denmark", "Diego Garcia Djibouti",
//            "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
//            "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia",
//            "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
//            "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard & McDonald Islands",
//            "Honduras", "Hong Kong (China)", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man",
//            "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan",
//            "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
//            "Martinique", "Mauritania", "Mauritius", "Mayotte", "Melilla", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
//            "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
//            "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territories",
//            "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico",
//            "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "St. Helena", "St. Pierre & Miquelon", "St. Kitts & Nevis", "St. Lucia",
//            "St. Vincent & Grenadines", "Samoa", "San Marino", "Sao Tome & Prfncipe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
//            "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "So. Georgia & So. Sandwich Isl.",
//            "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "St. Barthelemy", "Sudan", "Suriname", "Svalbard & Jan Mayen",
//            "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo",
//            "Tokelau", "Tonga", "Trinidad & Tobago", "Tristan da Cunha", "Tunisia", "Turkey", "Turkmenistan", "Turks & Caicos Islands",
//            "Tuvalu", "U.S. Virgin Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uraguay", "United States",
//            "U.S. Outlying Islands", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis & Futuna", "Western Sahara",
//            "Yemen", "Zambia", "Zimbabwe"};
    public static String[] arrFlags = {"Dhaka North", "Dhaka South", "Others"};

    public static String[] arrNorthZone = {"Adabor", "Agargaon", "Asad Gate", "Badda", "Banani", "Baridhara",
            "Bashundhara", "Dakshinkhan", "Darus Salam", "Dhaka- Cantonment", "Dhamrai", "Farmgate", "Gulshan 1", "Gulshan 2",
            "Kallyanpur", "Karwan Bazar", "Khilgaon", "Khilkhet", "Kuril", "Mirpur 1", "Mirpur 10", "Mirpur 11-13", "Mirpur 60 Feet",
            "Mirpur Cantonment", "Mirpur DOHS", "Mirpur Kazipara", "Mirpur Shewrapara", "Mohakhali", "Mohammadpur", "Monipur para", "Niketon",
            "Sher-e-Bangla Nagar", "Shyamoli", "Tejgaon", "Tejgaon Industrial Area", "Uttara"};
    public static String[] arrSouthZone = {"Azimpur", "Bangabhaban", "Bangshal", "Basabo", "Bijoy Nagar", "Chawk Bazar",
            "Demra", "Dhanmondi", "Dhanmondi", "Elephant Road", "Gendaria", "Gulistan", "Hazaribagh", "Jatrabari", "Kakrail",
            "Kalabagan", "Kamlapur", "Keraniganj Sadar", "Lalbagh", "Matuail", "Moghbazar", "Motijheel", "Nawabganj",
            "New Market", "Palamganj", "Pallabi", "Palton", "Ramna", "Rampura", "Sabujbagh", "Sarulia", "Shahbagh", "Shantinagar",
            "Sutrapur", "Wari"};


    /**
     * Handles the constants for GCM (Google Cloud Messaging)
     */
    public static class GCM {
        // public static String GCM_SENDER_ID = "436986602751 ";
        public static final String RECEIVED_GCM_ID = "RECEIVED_GCM_ID";
        public static String GCM_SENDER_ID = "851294185595";
        public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    }

    // DEFINED/FIXED CATEGORY ID ON SERVER
    public static class IntentPreference {
        public static final int SOURCE_LOCATION_INTENT_CODE = 94;
        public static final int PACKAGE_LOCATION_INTENT_CODE = 93;
        public static final int HOME_SCREEN_INTENT_CODE = 95;
        public static final int FOOD_ID = 17;
        public static final int BEAUTY_ID = 64;
        public static final int FUN_ID = 15;
        public static final int RetailnServices_ID = 65;
    }

    public static class ContactUS {
        public static final String EMAIL = "customersmiles@urbanpoint.com";
    }

    public static class DEFAULT_VALUES {
        public static final String SHARE_URL = "www.biyog.com";
        public static final String SORT_BY_ALPHABETICALLY = "alphabetically";
        public static final String SORT_BY_LOCATION = "location";
        public static final double DEFAULT_LAT = 0;
        public static final double DEFAULT_LNG = 0;
        public static final String COUNTRY_CODE = "880";
        public static final String Male = "0";
        public static final String Female = "1";
        public static final String Both = "2";
    }

    public class LocUpdate {

        public static final long MIN_TIME_BW_UPDATES = 1000 * 5;
        public static final float MIN_DISTANCE_BW_UPDATES = 5;
    }

    class ACTIONS {
        public static final String SMS_RECEIVED = "com.urbanpoint.UrbanPoint.SMS_RECEIVED";
    }

    class EXTRAS {
        public static final String OTP_CODE = "OTP_CODE";
    }
}
