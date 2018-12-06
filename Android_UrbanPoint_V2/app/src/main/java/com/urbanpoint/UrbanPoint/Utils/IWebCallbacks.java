package com.urbanpoint.UrbanPoint.Utils;

/**
 * Created by Danish on 2/2/2018.
 */

public interface IWebCallbacks {
    void onWebResult(boolean isSuccess, String strMsg);

    void onWebException(Exception ex);

    void onWebLogout();
}
