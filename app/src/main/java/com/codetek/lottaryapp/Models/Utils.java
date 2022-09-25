package com.codetek.lottaryapp.Models;

import com.codetek.lottaryapp.Models.DB.Lottery;
import com.codetek.lottaryapp.Models.DB.User;

public class Utils {
    private static String baseUrl="http://192.168.1.170:8002/";
    private static String apiUrl=baseUrl+"api/";
    private static User user;

    public static Lottery scanned;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Utils.user = user;
    }
}
