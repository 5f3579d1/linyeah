package com.whiletime.linyeah;

import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by k on 5/25/15.
 */
public class CopyToLocal implements FileOperate {

    private static CopyToLocal instance = null;

    private static JProgressBar pBar;
    private String filename;
    private String fileSize;
    private int val;

    Runnable updateProgress = () -> {
        pBar.setValue(val);
        pBar.setString(filename + ": " + fileSize + " " + val + "%");
    };

    Timer timer = new Timer(50, e -> SwingUtilities.invokeLater(updateProgress));

    private CopyToLocal() {
    }

    public static CopyToLocal getInstance(JProgressBar pBar) {
        if (instance == null) {
            synchronized (CopyToLocal.class) {
                if (instance == null)
                    instance = new CopyToLocal();
            }
            CopyToLocal.pBar = pBar;
        }
        return instance;
    }

    public boolean deal(String path) {

        timer.start();

        File localFile = new File(path);
        this.filename = localFile.getName();
        long totalLength = localFile.length();
        this.fileSize = FileSizeHelper.parse(totalLength);

        try {

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(localFile));

            String dest = localFile.getName();
            File dir = new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dir.mkdirs();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dir + File.separator + dest));
            System.err.println("复制：" + dest);

            int read;
            byte[] bytesIn = new byte[8196];
            int current = 0;
            double lengthPerPercent = 100.0 / totalLength;
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
                current += read;
                val = (int) Math.round(lengthPerPercent * current);
            }
            outputStream.close();
            inputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pBar.setValue(100);
            timer.stop();
        }

        return false;
    }

}
