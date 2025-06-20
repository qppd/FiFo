package com.qppd.fifov2.Libs.Internetz;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class InternetClass {

    private Context context;

    private Enumeration<NetworkInterface> enumNetworkInterfaces;

    private Enumeration<InetAddress> enumInetAddress;

    private NetworkInterface networkInterface;

    private InetAddress inetAddress;

    private String ip;

    public InternetClass(Context context){
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private String getIp() {

        try {

            enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (enumNetworkInterfaces.hasMoreElements()) {

                networkInterface = enumNetworkInterfaces.nextElement();
                enumInetAddress = networkInterface.getInetAddresses();

                while (enumInetAddress.hasMoreElements()) {

                    inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ip;
    }

}
