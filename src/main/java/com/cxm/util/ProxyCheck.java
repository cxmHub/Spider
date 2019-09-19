package com.cxm.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author cxm
 * @description ip校验
 * @date 2019-09-17 14:14
 **/
public class ProxyCheck {




    public static boolean check(String host, int port) {
        System.out.println("正在校验:" + host + "::" + port);
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (Exception e) {
            System.err.println("校验失败:" + host + "::" + port);
//            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static void main(String[] args) {
        String ip = "27.46.20.75";
        int port = 8888;
        boolean check = check(ip, port);
        System.out.println(check);
    }


}
