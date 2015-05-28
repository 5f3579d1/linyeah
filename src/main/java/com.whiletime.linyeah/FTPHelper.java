package com.whiletime.linyeah;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;

import javax.swing.*;
import java.io.*;

/**
 * Created by k on 5/25/15.
 */
public class FTPHelper implements FileOperate {

    private static FTPHelper instance = null;

    private static final FTPClient ftpClient = new FTPClient();
    private static final String host = "101.231.199.66";
    private static final int port = 21;
    private static final String user = "djd";
    private static final String pass = "2345621q";
    private static final boolean debug = true;

    private static JProgressBar pBar;
    private String filename;
    private String fileSize;
    private int val;

    Runnable updateProgress = () -> {
        pBar.setValue(val);
        pBar.setString(filename + ": " + fileSize + " " + val + "%");
    };

    Timer timer = new Timer(50, e -> SwingUtilities.invokeLater(updateProgress));

    private FTPHelper() {
    }

    public static FTPHelper getInstance(JProgressBar pBar) {
        if (instance == null) {
            synchronized (FTPHelper.class) {
                if (instance == null)
                    instance = new FTPHelper();
            }
            FTPHelper.pBar = pBar;
            if (debug)
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

            ftpClient.setControlEncoding("UTF-8");
            ftpClient.enterLocalPassiveMode();
            int timeout = 10 * 60 * 1000;
            ftpClient.setDataTimeout(timeout);
            ftpClient.setConnectTimeout(timeout);
            ftpClient.setDefaultTimeout(timeout);
            ftpClient.setControlKeepAliveTimeout(timeout);
            ftpClient.setControlKeepAliveReplyTimeout(timeout);
        }
        return instance;
    }

    private void connect() {
        try {
            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deal(String path) {

        connect();

        timer.start();

        File localFile = new File(path);
        this.filename = localFile.getName();
        long totalLength = localFile.length();
        this.fileSize = FileSizeHelper.parse(totalLength);

        try {

            InputStream inputStream = new BufferedInputStream(new FileInputStream(localFile));

            String remoteFile = localFile.getName();
            BufferedOutputStream outputStream = new BufferedOutputStream(ftpClient.storeFileStream(remoteFile));
            System.err.println("上传：" + remoteFile);

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
            disconnect();
        }

        return false;
    }

    private void disconnect() {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
