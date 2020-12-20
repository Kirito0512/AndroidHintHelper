package com.example.mylibrary.viewhint;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class AndroidUtils {
    public static int dpToPixel(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int getDeviceNavigationBarHeight(Context context) {
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            return rs.getDimensionPixelSize(id);
        }
        return 0;
    }

    public static boolean hasNavigationBar(Context context) {
        if (getDeviceNavigationBarHeight(context) == 0)
            return false;
        if (Build.BRAND.equalsIgnoreCase("HUAWEI") && isHuaWeiHideNav(context))
            return false;
        if (Build.BRAND.equalsIgnoreCase("XIAOMI") && isXiaoMiFullScreen(context))
            return false;
        if (Build.BRAND.equalsIgnoreCase("VIVO") && isVivoFullScreen(context))
            return false;
        return isHasNavigationBar(context);
    }

    /**
     * 华为手机是否隐藏了虚拟导航栏
     * @return true 表示隐藏了，false 表示未隐藏
     */
    private static boolean isHuaWeiHideNav(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "navigationbar_is_min", 0) != 0;
    }

    /**
     * 小米手机是否开启手势操作
     * @return true 表示使用的是手势，false 表示使用的是虚拟导航栏(NavigationBar)，默认是false
     */
    private static boolean isXiaoMiFullScreen(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
    }

    /**
     * Vivo手机是否开启手势操作
     * @return true 表示使用的是手势，false 表示使用的是虚拟导航栏(NavigationBar)，默认是false
     */
    private static boolean isVivoFullScreen(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0) != 0;
    }

    public static boolean isHasNavigationBar(Context context) {
        Display d = ((WindowManager) context.getSystemService(Service.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        if (displayHeight > displayWidth) {
            if (displayHeight + getDeviceNavigationBarHeight(context) > realHeight) return false;
        } else {
            if (displayWidth + getDeviceNavigationBarHeight(context) > realWidth) return false;
        }
        return realHeight - displayHeight > 0 || realWidth - displayWidth > 0;
    }


    public static float sp2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
