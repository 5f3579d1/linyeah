package com.whiletime.linyeah;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Created by k on 5/25/15.
 */
public class FTPHelper {

    private static FTPHelper instance = new FTPHelper();

    final static String host = "101.231.199.66";
    final static int port = 21;
    final static String user = "djd";
    final static String pass = "2345621q";
    final boolean debug = false;

    private FTPHelper() {
    }

    static FTPClient ftpClient = new FTPClient();

    public static FTPHelper getInstance() {
        return instance;
    }

    public void connect() {
        try {
            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
            ftpClient.setControlEncoding("UTF-8");

            if (debug)
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload(String path) {

        try {

            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            ftpClient.storeFile(new String(file.getName().getBytes("UTF-8"), "iso-8859-1"), inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
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
