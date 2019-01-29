package com.urbanpoint.UrbanPoint.Utils;

/**
 * Created by Ibrar on 3/20/2017.
 */

public interface INavBarUpdateUpdateListener {

    public void setNavBarTitle(String strTitle);

    public void setMenuBadgeVisibility(boolean _shouldBageVisible);

    public void setBackBtnVisibility(int _visibility);

    public void setCancelBtnVisibility(int _visibility);

    public void setToolBarbackgroudVisibility(int _visibility);

    public void setReviewCount(int _count);

    public void setProfileCountVisibility(int _visibility);

    public void setUnSubscribeVisibility(int _visibility);

    public void navToLogin();

    public void setProfileCount(String Count);
}

