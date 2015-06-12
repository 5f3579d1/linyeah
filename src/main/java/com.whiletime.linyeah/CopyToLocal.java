package com.whiletime.linyeah;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by k on 5/25/15.
 */
public class CopyToLocal implements FileOperate {

    private static CopyToLocal instance = null;

    public static CopyToLocal getInstance() {
        if (instance == null) {
            synchronized (CopyToLocal.class) {
                if (instance == null)
                    instance = new CopyToLocal();
            }
        }
        return instance;
    }

    public boolean deal(String path) {

        File localFile = new File(path);

        try {

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(localFile));

            String dest = Main.PREFIX + localFile.getName();
            File dir = new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dir.mkdirs();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dir + File.separator + dest));
            System.err.println("复制：" + dest);

            int read;
            byte[] bytesIn = new byte[8196];
            while ((read = inputStream.read(bytesIn)) != -1)
                outputStream.write(bytesIn, 0, read);

            outputStream.close();
            inputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
