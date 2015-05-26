package com.whiletime.linyeah;

import java.text.DecimalFormat;

/**
 * Created by k on 5/27/15.
 */
public class FileSizeHelper {

    public static String parse(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log(size) / Math.log(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }

}
