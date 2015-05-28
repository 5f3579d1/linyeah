package com.whiletime.linyeah;

import javax.swing.*;

/**
 * Created by k on 5/27/15.
 */
public class FileOperateFactory {

    public static FileOperate getInstance(String className, JProgressBar bar) {
        if ("FTPHelper".equals(className))
            return FTPHelper.getInstance(bar);
        return CopyToLocal.getInstance(bar);
    }

}
