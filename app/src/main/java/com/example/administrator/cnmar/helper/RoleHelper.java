package com.example.administrator.cnmar.helper;

import android.content.Context;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RoleHelper {

    //    判断是否是超级用户
    public static boolean isSuper(Context context) {
        boolean isSuper = SPHelper.getBoolean(context, "isSuper");
        if (isSuper)
            return true;
        else
            return false;
    }

    //    判断是否是系统管理员
    public static boolean isAdministrator(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "1" + ","))
            return true;
        else
            return false;
    }

    //    判断是否是检验员
    public static boolean isTestman(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "5" + ","))
            return true;
        else
            return false;
    }

    //    判断是否是原料库管员
    public static boolean isMaterialStockman(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "2" + ","))
            return true;
        else
            return false;
    }

    //    判断是否是半成品库管员
    public static boolean isHalfStockman(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "3" + ","))
            return true;
        else
            return false;
    }

    //    判断是否是成品库管员
    public static boolean isProductStockman(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "4" + ","))
            return true;
        else
            return false;
    }

    //    判断是否是车间班组长
    public static boolean isWorkshopLeader(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "6" + ","))
            return true;
        else
            return false;
    }

    //        判断是否是统计员
    public static boolean isStatistician(Context context) {
        String role = SPHelper.getString(context, "RoleId");
        if (role.contains("," + "7" + ","))
            return true;
        else
            return false;
    }

}

