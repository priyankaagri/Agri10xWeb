package com.mobile.agri10x.Model;

import android.graphics.Bitmap;

import java.util.Arrays;

public class SecurityData {
    private static Bitmap userImage;
    private static  String[] Commodity;
    private static String PendingBalance;
    private static long dateLimit = 90; //days
    private static Long WithdthrawBalance;
    private static Long ClosingBalance;


    public static long getDateLimit() {
        return dateLimit;
    }

    public static void setDateLimit(long dateLimit) {
        SecurityData.dateLimit = dateLimit;
    }

    public static String getPendingBalance() {
        return PendingBalance;
    }

    public static void setPendingBalance(String pendingBalance) {
        PendingBalance = pendingBalance;
    }

    public static Long getWithdthrawBalance() {
        return WithdthrawBalance;
    }

    public static void setWithdthrawBalance(Long withdthrawBalance) {
        WithdthrawBalance = withdthrawBalance;
    }

    public static Long getClosingBalance() {
        return ClosingBalance;
    }

    public static void setClosingBalance(Long closingBalance) {
        ClosingBalance = closingBalance;
    }

    public static String[] getCommodity() {
        return Commodity;
    }

    public static void setCommodity(String[] commodity) {

        Arrays.sort(commodity);
        Commodity = commodity;
    }

    public static Bitmap getUserImage() {
        return userImage;
    }

    public static void setUserImage(Bitmap userImage) {
        SecurityData.userImage = userImage;
    }
}
