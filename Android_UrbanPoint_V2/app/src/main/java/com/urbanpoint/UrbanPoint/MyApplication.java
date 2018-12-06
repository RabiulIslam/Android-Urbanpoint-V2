package com.urbanpoint.UrbanPoint;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Danish on 1/25/2018.
 */

public class MyApplication extends Application {

    private static MyApplication applicationContext;
    private DisplayMetrics displaymetrics;
    Context mContext;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppConfig.initInstance(mContext);
        setLanguageSpecificFonts(true);


   /*     CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ubuntu_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/


        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, AppConstt.FLURRY_TOKEN);
        mContext = this;

        String projectToken = AppConstt.MIXPANEL_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

//        Mint.enableDebug();
//        Mint.initAndStartSession(getApplicationContext(), AppConstt.BugSenseConstants.SPLUNK_API_KEY);

//        AnalyticsTrackers.initialize(this);
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        sAnalytics = GoogleAnalytics.getInstance(this);
        getDefaultTracker();

        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //-----------


    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }

        return sTracker;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private static final String THEME_DEFAULT_FONT = "serif";//Must be same as used in styles.xml

    // Defining sans as the normal (default) typeface.
    private String DEFAULT_NORMAL_BOLD_FONT_FILENAME = "";
    private String DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "";
    private String DEFAULT_LIGHT_FONT_FILENAME = "";
    private String DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_MEDIUM_FONT_FILENAME = "";
    private String DEFAULT_MEDIUM_ITALIC_FONT_FILENAME = "";


    private String DEFAULT_SANS_BOLD_FONT_FILENAME = "";
    private String DEFAULT_SANS_BOLD_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_SANS_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_SANS_NORMAL_FONT_FILENAME = "";


    private String DEFAULT_SERIF_BOLD_FONT_FILENAME = "";
    private String DEFAULT_SERIF_BOLD_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_SERIF_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_SERIF_NORMAL_FONT_FILENAME = "";


    private String DEFAULT_MONOSPACE_BOLD_FONT_FILENAME = "";
    private String DEFAULT_MONOSPACE_BOLD_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_MONOSPACE_ITALIC_FONT_FILENAME = "";
    private String DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME = "";
    private static final int normal_idx = 0;
    private static final int sans_idx = 1;
    private static final int serif_idx = 2;
    private static final int monospace_idx = 3;

    public void setLanguageSpecificFonts(boolean isEnglish) {
        if (!isEnglish) {
            //Arabic fonts

            DEFAULT_NORMAL_BOLD_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_SANS_NORMAL_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_LIGHT_FONT_FILENAME = "fonts/proxima_nova_alt_thin.ttf";
            DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_thin.ttf";


        } else {
            //English fonts
            DEFAULT_NORMAL_BOLD_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_SANS_NORMAL_FONT_FILENAME = "fonts/proxima_nova_alt_regular.ttf";
            DEFAULT_LIGHT_FONT_FILENAME = "fonts/proxima_nova_alt_thin.ttf";
            DEFAULT_LIGHT_ITALIC_FONT_FILENAME = "fonts/proxima_nova_alt_thin.ttf";


        }

        try {
            setThemeDefaultFonts(THEME_DEFAULT_FONT);

            // The following code is only necessary if you are using the android:typeface attribute
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setThemeDefaultFonts(THEME_DEFAULT_FONT);

            }
        } catch (NoSuchFieldException e) {
            // Field does not exist in this (version of the) class
            //logFontError(e);
        } catch (IllegalAccessException e) {
            // Access rights not set correctly on field, i.e. we made a programming error
            // logFontError(e);
        } catch (Throwable e) {
            // Must not crash app if there is a failure with overriding fonts!
            // logFontError(e);
        }
    }

    private void setThemeDefaultFonts(String defaultFontNameToOverride) throws NoSuchFieldException, IllegalAccessException {

        final Typeface normal = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
        final Typeface bold = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME);
        //setTypeFaceDefaults(normal, bold, italic, boldItalic, sans_idx);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Map<String, Typeface> normalFontMap = new HashMap<String, Typeface>();
            normalFontMap.put(defaultFontNameToOverride, normal);
            final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
            staticField.setAccessible(true);
            staticField.set(null, normalFontMap);

        } else {
            Field defaultField = Typeface.class.getDeclaredField("DEFAULT");
            defaultField.setAccessible(true);
            defaultField.set(null, normal);

            Field defaultBoldField = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            defaultBoldField.setAccessible(true);
            defaultBoldField.set(null, bold);

            Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
            sDefaults.setAccessible(true);
            sDefaults.set(null, new Typeface[]{normal, bold, italic, boldItalic});

            final Typeface normal_sans = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
            Field sansSerifDefaultField = Typeface.class.getDeclaredField("SANS_SERIF");
            sansSerifDefaultField.setAccessible(true);
            sansSerifDefaultField.set(null, normal_sans);

            final Typeface normal_serif = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);
            Field serifDefaultField = Typeface.class.getDeclaredField("SERIF");
            serifDefaultField.setAccessible(true);
            serifDefaultField.set(null, normal_serif);

            final Typeface normal_monospace = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME);
            Field monospaceDefaultField = Typeface.class.getDeclaredField("MONOSPACE");
            monospaceDefaultField.setAccessible(true);
            monospaceDefaultField.set(null, normal_monospace);
        }

    }

    private void setDefaultFontForTypeFaceSans() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SANS_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SANS_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SANS_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SANS_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, sans_idx);
    }

    private void setDefaultFontForTypeFaceSansSerif() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SERIF_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SERIF_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SERIF_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_SERIF_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, serif_idx);
    }

    private void setDefaultFontForTypeFaceMonospace() throws NoSuchFieldException, IllegalAccessException {

        final Typeface bold = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_MONOSPACE_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_MONOSPACE_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_MONOSPACE_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(mContext.getAssets(), DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, monospace_idx);
    }

    @TargetApi(16)
    private void setTypeFaceDefaults(Typeface normal, Typeface bold, Typeface italic, Typeface boldItalic, int typefaceIndex) throws NoSuchFieldException, IllegalAccessException {
        Field typeFacesField = Typeface.class.getDeclaredField("sTypefaceCache");
        typeFacesField.setAccessible(true);

        LongSparseArray<LongSparseArray<Typeface>> sTypefaceCacheLocal = new LongSparseArray<LongSparseArray<Typeface>>(3);
        typeFacesField.get(sTypefaceCacheLocal);

        LongSparseArray<Typeface> newValues = new LongSparseArray<Typeface>(4);
        newValues.put(Typeface.NORMAL, normal);
        newValues.put(Typeface.BOLD, bold);
        newValues.put(Typeface.ITALIC, italic);
        newValues.put(Typeface.BOLD_ITALIC, boldItalic);
        sTypefaceCacheLocal.put(typefaceIndex, newValues);

        typeFacesField.set(null, sTypefaceCacheLocal);
    }

    public void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static MyApplication getInstance() {
        return applicationContext;
    }


    //This will print logs only in staging mode in with Log.e
    public void printLogs(String key, String value) {
        Log.e(key, "" + value);
    }

    public Typeface getFont() {
        Typeface typeface = Typeface.createFromAsset(applicationContext.getAssets(), "fonts/openSans_regular.ttf");
        return typeface;
    }


    public String getDensityDpi(Activity activityContext) {
        displaymetrics = new DisplayMetrics();
        activityContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int densityDpi = displaymetrics.densityDpi;
        String status = "";

        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                // LDPI
                status = "LDPI";
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                // MDPI
                status = "MDPI";
                break;

            case DisplayMetrics.DENSITY_TV:
            case DisplayMetrics.DENSITY_HIGH:
                // HDPI
                status = "HDPI";
                break;

            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_280:
                // XHDPI
                status = "XHDPI";
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_360:
            case DisplayMetrics.DENSITY_400:
            case DisplayMetrics.DENSITY_420:
                // XXHDPI
                status = "XXHDPI";
                break;

            case DisplayMetrics.DENSITY_XXXHIGH:
            case DisplayMetrics.DENSITY_560:
                // XXXHDPI
                status = "XXXHDPI";
                break;
        }

        printLogs("getDensityDpi", "getDensityDpi " + status);
        return status;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

      /*  AppInstance.rModel_merchintList = null;
        AppInstance.offers = null;*/
    }

//    public synchronized Tracker getGoogleAnalyticsTracker() {
//        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
//        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
//    }
//
//
//    public void trackScreenView(String screenName) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Set screen name.
//        t.setScreenName(screenName);
//
//        // Send a screen view.
//        t.send(new HitBuilders.ScreenViewBuilder().build());
//
//        GoogleAnalytics.getInstance(this).dispatchLocalHits();
//    }
//
//
//    public void trackException(Exception e) {
//        if (e != null) {
//            Tracker t = getGoogleAnalyticsTracker();
//
//            t.send(new HitBuilders.ExceptionBuilder()
//                    .setDescription(
//                            new StandardExceptionParser(this, null)
//                                    .getDescription(Thread.currentThread().getName(), e))
//                    .setFatal(false)
//                    .build()
//            );
//        }
//    }
//
//
//    public void trackEvent(String category, String action, String label) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Build and send an Event.
//        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action)
//                .setLabel(label)
//                .build());
//    }

//    @Override
//    public void onTrimMemory(int level) {
//        //This method is called when app is in background
//        super.onTrimMemory(level);
//        AppConfig.getInstance().isCommingFromSplash = false;
//        Log.d("PUSHNOTIFICATN", "onTrimMemory: "+ AppConfig.getInstance().isCommingFromSplash);
//    }
}

