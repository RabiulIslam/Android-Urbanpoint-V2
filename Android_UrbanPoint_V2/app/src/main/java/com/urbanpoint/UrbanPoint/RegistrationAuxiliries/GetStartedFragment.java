package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.appIntroFragments.IntroFragment_1;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.appIntroFragments.IntroFragment_2;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.appIntroFragments.IntroFragment_3;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.appIntroFragments.IntroFragment_Start;
import com.urbanpoint.UrbanPoint.Utils.AppInstance;

import me.relex.circleindicator.CircleIndicator;

public class GetStartedFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mGetStartedView;
    private Button mLoginView;

    // ViewPager Indicator
    private ViewPager mViewPager;
    private CircleIndicator mIndicator;
    int numberOfViewPagerChildren = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.introduction, null);
        View view = inflater.inflate(R.layout.introduction_main, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
       // MyApplication.getInstance().trackScreenView(getString(R.string.app_first_screen));
        bindViews();
        return view;
    }

    private void bindViews() {
        mGetStartedView = (Button) mRootView.findViewById(R.id.getStartedButton);
        mGetStartedView.setOnClickListener(this);
        mLoginView = (Button) mRootView.findViewById(R.id.loginButton);
//        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "helveticaneue.ttf");
        // mLoginView.setTypeface(typeface);
        // mGetStartedView.setTypeface(typeface);

        mLoginView.setOnClickListener(this);
        bindViewsOfViewPager();
    }
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        switch (v.getId()) {
            case R.id.getStartedButton:
                fragmentTransaction.hide(this);
                AppInstance.signUpUser = null;
                fragmentTransaction.detach(this);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepOne());
//                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepThree());
                fragmentTransaction.commitAllowingStateLoss();
             //   MyApplication.getInstance().trackEvent(getString(R.string.ga_get_started_btn_click_category),getString(R.string.ga_get_started_btn_click_action),getString(R.string.ga_get_started_btn_click_action));
                break;
            case R.id.loginButton:
                fragmentTransaction.hide(this);
                fragmentTransaction.detach(this);
                fragmentTransaction.replace(R.id.containerIntroFragments, new LoginFragment());
                fragmentTransaction.commit();
             //   MyApplication.getInstance().trackEvent(getString(R.string.ga_get_started_btn_click_category),getString(R.string.ga_get_login_btn_click_action),getString(R.string.ga_get_started_btn_click_action));
                break;
        }

    }

    private void bindViewsOfViewPager() {
        mViewPager = (ViewPager) mRootView.findViewById(R.id.pager);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

       /* final LayerDrawable background = (LayerDrawable) mViewPager.getBackground();

        background.getDrawable(0).setAlpha(0); // this is the lowest drawable
        background.getDrawable(1).setAlpha(0);
        background.getDrawable(2).setAlpha(255); // this is the upper one


        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {

                int index = (Integer) view.getTag();
                Drawable currentDrawableInLayerDrawable;
                currentDrawableInLayerDrawable = background.getDrawable(index);

                if (position <= -1 || position >= 1) {
                    currentDrawableInLayerDrawable.setAlpha(0);
                } else if (position == 0) {
                    currentDrawableInLayerDrawable.setAlpha(255);
                } else {
                    currentDrawableInLayerDrawable.setAlpha((int) (255 - Math.abs(position * 255)));
                }

            }
        });*/

        mIndicator = (CircleIndicator) mRootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);

    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new IntroFragment_Start();
            }
            if (i == 1) {
                fragment = new IntroFragment_1();
            }
            if (i == 2) {
                fragment = new IntroFragment_2();
            }
            if (i == 3) {
                fragment = new IntroFragment_3();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return numberOfViewPagerChildren;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (object instanceof IntroFragment_Start) {
                view.setTag(3);
            }
            if (object instanceof IntroFragment_1) {
                view.setTag(2);
            }
            if (object instanceof IntroFragment_2) {
                view.setTag(1);
            }
            if (object instanceof IntroFragment_3) {
                view.setTag(0);
            }
            return super.isViewFromObject(view, object);
        }


    }
    //-- NEW IMPLEMENTATION : VIEWPAGER : 1 APR 2016 : END


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("GETFRAGMENT:onDestroy", "onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("GETFRAGMENT:onDetach", "onDetach");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView", "onDestroyView");
    }



}
