package io.github.s3s3l.yggdrasil.utils.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import io.github.s3s3l.yggdrasil.bean.exception.NetException;

public abstract class NetUtils {
    public static final String IPV4 = getFirstIpv4Address();
    public static final String HOST_NAME = getHostName();

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost()
                    .getHostName();
        } catch (UnknownHostException e) {
            throw new NetException(e);
        }
    }

    public static String getFirstIpv4Address() {
        try {

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                if (iface.isLoopback() || iface.isVirtual() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            throw new NetException(e);
        }

        return null;
    }
}
