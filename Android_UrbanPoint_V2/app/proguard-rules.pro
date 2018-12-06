# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#------------------------------------------------------ NEW DATA -------------------------------------------

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Anurag Sethi\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


###############---------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontpreverify

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


########################################################
-keep public class android.util.FloatMath
-dontwarn     android.util.FloatMath

-keep class org.apache.http.**
-keep interface org.apache.http.**
-dontwarn org.apache.http.**

-keep class android.net.http.**

-dontwarn com.google.android.gms.**
 -dontwarn com.google.android.gms.internal.zzhu


 ########################################################

 ##---------------Begin: proguard configuration for Gson  ----------
 # Gson uses generic type information stored in a class file when working with fields. Proguard
 # removes such information by default, so configure it to keep all of it.
 -keepattributes Signature

 # For using GSON @Expose annotation
 -keepattributes *Annotation*

 # Gson specific classes
 -keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

 # Application classes that will be serialized/deserialized over Gson
 #-keep class com.google.gson.examples.android.model.** { *; }

# Application classes that will be serialized/deserialized over Gson
 -keep class  com.urbanpoint.UrbanPoint.dataobject.** { *; }
-ignorewarnings
-keep class * {
    public private *;
}
 ##---------------End: proguard configuration for Gson  ----------

 #####-------- ActionBarSherlock -----------############
-keep class android.support.** { *; }

-keep interface android.support.** { *; }

-keep class com.actionbarsherlock.** { *; }

-keep interface com.actionbarsherlock.** { *; }
#####-------- ActionBarSherlock -----------############

#########################//------------------------------------------

#####-keep public class com.google.android.gms.* { public *; }
#####-dontwarn com.google.android.gms.**

#-keepnames class org.apache.** {*;}
#-keep public class org.apache.** {*;}


#####-dontwarn android.support.**
#//----
#####-keep class com.viewpagerindicator.**
#####-dontwarn com.viewpagerindicator.**
#//----

#####-keep class org.apache.commons.**
#####-dontwarn  org.apache.commons.**
#####-keep class org.apache.http.** { *; }
#####-dontwarn org.apache.http.**

#####-dontwarn org.apache.commons.logging.LogFactory
#####-dontwarn org.apache.http.annotation.ThreadSafe
#####-dontwarn org.apache.http.annotation.Immutable
#####-dontwarn org.apache.http.annotation.NotThreadSafe