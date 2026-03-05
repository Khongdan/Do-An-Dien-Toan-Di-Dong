package common;

import java.net.*;
import java.util.*;

public class NetUtils {

    public static List<InetAddress> getAllIPv4() {
        List<InetAddress> result = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> ifaces =
                    NetworkInterface.getNetworkInterfaces();

            while (ifaces.hasMoreElements()) {
                NetworkInterface ni = ifaces.nextElement();

                if (!ni.isUp() || ni.isLoopback()) continue;

                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();

                    if (addr instanceof Inet4Address) {
                        result.add(addr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
