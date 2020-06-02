package com.android.sgzcommon.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.content.FileProvider;

import static android.content.Context.ACTIVITY_SERVICE;

public class SystemUtil {

    private static final String TAG = "SystemUtil";

    /**
     * 获取屏幕尺寸，屏幕的宽：point.x；屏幕的高：point.y
     *
     * @return point
     */
    @SuppressLint("NewApi")
    public static Point getWindowSize(Context context) {
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(size);
        return size;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        int height = 96;
        if (resourceId > 0) {
            height = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 判断自己的一个Service是否已经运行
     *
     * @param serviceName service全名
     */
    public static boolean serviceWorking(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(50);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        try {
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * sdk < 21 时，返回栈顶app的包名。sdk >= 21时，如果当前appprocess在栈顶时，返回包名，否则返回"";
     *
     * @param context
     * @return
     */
    public static String getTopPackage(Context context) {
        Log.d(TAG, "getTopPackage: >1");
        String packageName = "";
        ActivityManager mAm = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //获得当前运行的task
        if (Build.VERSION.SDK_INT < 21) {
            Log.d(TAG, "getTopPackage: >2");
            List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
            if (taskList != null && taskList.size() > 0) {
                packageName = taskList.get(0).topActivity.getPackageName();
                Log.d("SystemUtil", "getTopPackage: >3 packageName = " + packageName);
            }
        } else {
            Log.d("SystemUtil", "getTopPackage: >4");
            List<ActivityManager.RunningAppProcessInfo> tasks = mAm.getRunningAppProcesses();
            if (tasks != null && tasks.size() > 0) {
                Log.d("SystemUtil", "getTopPackage: >5");
                ActivityManager.RunningAppProcessInfo task = tasks.get(0);
                if (task.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.d("SystemUtil", "getTopPackage: >6");
                    packageName = task.processName;
                }
            }
        }
        return packageName;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    public static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * @param context
     * @param c
     */
    public static void startApp(Context context, Class<?> c) {
        Log.d("SystemUtil", "startApp: ");
        Intent intent = new Intent(context, c);//设置action
        intent.setAction(Intent.ACTION_MAIN);//设置category
        intent.addCategory(Intent.CATEGORY_LAUNCHER);//设置category
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * App置于前台显示，若程序已启动在后台运行，直接切换前台，否则重新启动app。
     * 如果api < 21需要添加权限 android.permission.GET_TASKS。
     * 当前进程处于前台，但是activity不可见，则启动 c
     *
     * @param context
     * @param launcher Launcher Activity
     */
    public static void frontOrStartApp(Context context, Class<?> launcher) {
        Log.d(TAG, "frontOrStartApp: >>>>>>>>>>>>>>>>>>1");
        //首先判断调用的apk是否安装
        String pakName = context.getPackageName();
        if (isInstall(context, pakName)) {
            Log.d(TAG, "frontOrStartApp: >>>>>>>>>>>>>>>>>>2");
            //获取ActivityManager
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            boolean startActivity = true;
            Log.d(TAG, "frontOrStartApp: >>>>>>>>>>>>>>>>>>7");
            //获得当前运行的task
            List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
            if (tasks.size() > 0) {
                Log.d(TAG, "frontOrStartApp: >>>>>>>>>>>>>>>>>>8");
                ActivityManager.RecentTaskInfo taskInfo = tasks.get(0).getTaskInfo();
                if (taskInfo != null) {
                    Log.d(TAG, "frontOrStartApp: >>>>>>>>>>>>>>>>>>9 id = " + taskInfo.id);
                    activityManager.moveTaskToFront(taskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                    if (Build.VERSION.SDK_INT >= 23 && taskInfo.numActivities > 0) {
                        Log.d("SystemUtil", "frontOrStartApp: >>>>>>>>>>>>>>>>>>>>10");
                        startActivity = false;
                    }
                }
            }
            if (startActivity) {
                startApp(context, launcher);
            }
        } else {
            throw new IllegalArgumentException("没有找到对应的应用程序");
        }
    }

    /**
     * 判断当前应用是否置于前台
     *
     * @param context
     * @return
     */
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        try {
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重启应用
     */
    public static void reStartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 根据包名判断某个应用程序是否安装方法
     */
    public static boolean isInstall(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 判断app是否安装
     */

    public static boolean isInstall(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            // 去进行判断网络是否连接
            return manager.getActiveNetworkInfo().isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
            String result = null;
            try {
                result = getAdressMacByInterface();
                if (result != null) {
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
                e.printStackTrace();
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    /**
     * @param wifiMan
     * @return
     * @throws Exception
     */
    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();
            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


    /**
     * 获取当前应用版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        String packageName = context.getPackageName();
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String packageName = context.getPackageName();
        String version = "";
        try {
            version = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 判断SD卡是否被挂载
     */
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static String getSDCardRootDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }


    /**
     * 获取SD卡的完整空间大小，返回KB
     *
     * @return
     */
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            return getSize(getSDCardRootDir());
        }
        return 0;
    }

    /**
     * 获取SD卡的剩余空间大小(KB)
     *
     * @return
     */
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            return getFreeSize(getSDCardRootDir());
        }
        return 0;
    }

    /**
     * 获取SD卡的可用空间大小(KB)
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            return getAvailableSize(getSDCardRootDir());
        }
        return 0;
    }

    /**
     * 获取手机内部存储空间
     *
     * @return 以KB为单位的容量
     */
    public static long getInternalMemorySize() {
        File file = Environment.getDataDirectory();
        return getSize(file.getPath());
    }

    /**
     * 获取手机内部可用存储空间
     *
     * @return 以KB为单位的容量
     */
    public static long getAvailableInternalMemorySize() {
        File file = Environment.getDataDirectory();
        return getAvailableSize(file.getPath());
    }

    private static long getSize(String path) {
        StatFs fs = new StatFs(path);
        long count = 0;
        long size = 0;
        if (Build.VERSION.SDK_INT >= 18) {
            count = fs.getBlockCountLong();
            size = fs.getBlockSizeLong();
        } else {
            count = fs.getBlockCount();
            size = fs.getBlockSize();
        }
        return count * size / 1024;
    }

    private static long getFreeSize(String path) {
        StatFs fs = new StatFs(path);
        long count = 0;
        long size = 0;
        if (Build.VERSION.SDK_INT >= 18) {
            count = fs.getFreeBlocksLong();
            size = fs.getBlockSizeLong();
        } else {
            count = fs.getFreeBlocks();
            size = fs.getBlockSize();
        }
        return count * size / 1024;
    }

    private static long getAvailableSize(String path) {
        StatFs fs = new StatFs(path);
        long count = 0;
        long size = 0;
        if (Build.VERSION.SDK_INT >= 18) {
            count = fs.getAvailableBlocksLong();
            size = fs.getBlockSizeLong();
        } else {
            count = fs.getAvailableBlocks();
            size = fs.getBlockSize();
        }
        return count * size / 1024;
    }

    /**
     * 截图（系统必须root）
     *
     * @param filePath
     */
    public static void screenshot(String filePath) {
        String cmd = "screencap -p " + filePath;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            outputStream = p.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.writeBytes("\n");
            dataOutputStream.flush();
        } catch (Throwable t) {
            t.printStackTrace();
            Log.d("Screenshot", "err: " + Log.getStackTraceString(t));
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置系统时间、时间格式为：yyyyMMdd.HHmmss，注意，系统必须root
     *
     * @param dateTime
     */
    public static void setTime(String dateTime) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            //            os.writeBytes("setprop persist.sys.timezone Asia/Shanghai\n");
            os.writeBytes("/system/bin/date -s " + dateTime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装apk
     * <p>
     * 1.
     * manifest.xml文件中注册provider
     * <p>
     * <provider
     * android:authorities="com.qh.display.little.fileprovider" //自定义
     * android:name="android.support.v4.content.FileProvider"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/fabcd" />
     * </provider>
     * <p>
     * 2.
     * 在res下新建 fabcd.xml，内容：
     * <paths>
     * <external-path
     * name="apk" //随便写，没影响
     * path="DispalySystem/apk" /> //根目录后的目录，有多少级都要写全
     * </paths>
     * <p>
     * 3.调用以下方法
     *
     * @param context
     * @param fileprovider manifests.xml 文件中 <provider> 标签的 android:authorities 属性
     * @param filePath
     */
    public static void installAPK(Context context, String fileprovider, String filePath) {
        File apkFile = new File(filePath);
        Intent installIntent = new Intent();
        installIntent.setAction(Intent.ACTION_VIEW);
        installIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            installIntent.setDataAndType(FileProvider.getUriForFile(context.getApplicationContext(), fileprovider, apkFile), "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        if (context.getPackageManager().queryIntentActivities(installIntent, 0).size() > 0) {
            context.startActivity(installIntent);
        }
        //关闭旧版本的应用程序的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static final int REQUEST_CODE_TAKE_PHOTO = 1000;
    public static final int REQUEST_CODE_SELECTE_PICTURE = 1001;
    public static final int REQUEST_CODE_CROP = 1002;

    public static void takePhoto(Activity activity, File outputFile) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = FileProviderUtils.uriFromFile(activity, outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    public static void selectePicture(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivityForResult(intent, REQUEST_CODE_SELECTE_PICTURE);
    }

    public static void cropPicture(Activity activity, Uri uri, File outputFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        FileProviderUtils.setIntentDataAndType(activity, intent, "image/*", uri, true);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //return-data为true时，直接返回bitmap，可能会很占内存，不建议，小米等个别机型会出异常！！！
        //所以适配小米等个别机型，裁切后的图片，不能直接使用data返回，应使用uri指向
        //裁切后保存的URI，不属于我们向外共享的，所以可以使用fill://类型的URI
        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

}
