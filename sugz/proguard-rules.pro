# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android_sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !netCode/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Signature
-dontwarn rx.**
-dontwarn org.**
-dontwarn okio.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn freemarker.**
-dontwarn com.smarttop.**
-dontwarn com.sun.**
-dontwarn com.scwang.**
-dontwarn com.alibaba.**
-dontwarn uk.co.senab.**
-dontwarn com.android.volley.**
-dontwarn com.zhpan.bannerview.**
-dontwarn com.github.githubwing.**

-keep class rx.**{  *;}
-keep class okhttp3.**{  *;}
-keep class com.sun.**{  *;}
-keep class com.scwang.**{  *;}
-keep class com.zhpan.bannerview.** {*;}
-keep class com.android.volley.** {*;}
-keep class de.greenrobot.**{  *;}
-keep class com.alibaba.**{  *;}
-keep class uk.co.senab.**{  *;}
-keep class com.android.sgzcommon.**{  *;}
-keep class com.android.volley.**{  *;}
-keep class com.jiongbull.jlog.** { *; }
-keep class com.smarttop.library.** { *; }
-keep class com.github.githubwing.** {*;}
-keep class com.google.android.material.** {*;}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes EnclosingMethod
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;}
-keep class **$Properties

-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
    protected *;
}
-keepclassmembers class ** {
    public static *;
}
-keepclassmembers class ** {
    public void normalThreadRun();
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepattributes SourceFile, LineNumberTable

#greendao
-dontwarn org.greenrobot.**
-dontwarn net.sqlcipher.database.**
-keep class net.sqlcipher.database.** { *; }
-keep class org.greenrobot.greendao.**{  *;}

#butterknife
-keep class butterknife.** { *; }
-keep class com.google.** { *; }
-dontwarn com.google.**
-dontwarn butterknife.internal.**
-dontwarn butterknife.compiler.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}