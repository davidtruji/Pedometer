package com.dtsoftware.pedometer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {


    public static int getTodayKey() {
        return Integer.parseInt(new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date()));
    }


}
