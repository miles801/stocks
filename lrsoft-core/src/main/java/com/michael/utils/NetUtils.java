package com.michael.utils;

import com.michael.utils.string.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author miles
 * @datetime 2014/4/24 15:52
 */
public class NetUtils {
    /**
     * 获得所有网卡的Mac地址
     *
     * @return
     * @throws SocketException
     */
    public static Set<String> getLocalMacAddress() throws SocketException {
        Set<String> macs = new HashSet<String>();
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        while (networks.hasMoreElements()) {
            NetworkInterface network = networks.nextElement();
            byte[] mac = network.getHardwareAddress();
            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                if (!StringUtils.isEmpty(sb.toString())) {
                    macs.add(sb.toString());
                }
            }
        }
        return macs;
    }


    /**
     * 获得本地IP
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 根据请求信息获得客户端的ip地址
     *
     * @param request request请求
     * @return ip地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        String ip = request.getRemoteAddr();
        //如果访问的是localhost，那么则转换成本地IP
        if (ip != null && "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                return Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

}
