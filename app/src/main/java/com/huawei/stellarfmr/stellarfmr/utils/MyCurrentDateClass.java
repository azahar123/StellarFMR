package com.huawei.stellarfmr.stellarfmr.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ${Azahar} on 12/13/2015.
 */
public class MyCurrentDateClass {

    public String myDevicedate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String MyCurrentDate = df.format(c.getTime());
        return  MyCurrentDate;
    }
}
