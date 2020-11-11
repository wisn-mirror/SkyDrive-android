package com.library.base.net.broadcast;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class BroadcastGroup {
    private static final String TAG = "BroadcastGroup";
    private static BroadcastGroup broadcastGroup;

    private BroadcastGroup() { }

    public static BroadcastGroup getInstance() {
        if (broadcastGroup == null) {
            synchronized (BroadcastGroup.class) {
                if (broadcastGroup == null) {
                    broadcastGroup = new BroadcastGroup();
                }
            }
        }
        return broadcastGroup;
    }


    public void listenerMessage(String IP, int listenerPort, final MessageCall messageCall) {
        try {
            final byte[] data = new byte[256];
            final MulticastSocket ms = new MulticastSocket(listenerPort);
            ms.joinGroup(InetAddress.getByName(IP));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            //receive()是阻塞方法，会等待客户端发送过来的信息
                            DatagramPacket packet = new DatagramPacket(data, data.length);
                            ms.receive(packet);
                            String message = new String(packet.getData(), 0, packet.getLength());
                            Log.d(TAG, message);
                            if (messageCall != null) {
                                messageCall.callBackMessage(message, packet.getAddress().getHostAddress());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ms.close();
                    }
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBackMessage(String IP, int sendPort, String message) {
        try {
            MulticastSocket ms = new MulticastSocket();
            ms.setTimeToLive(32);
            //将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的
            byte[] data = message.getBytes();
            InetAddress address = InetAddress.getByName(IP);
            ms.setNetworkInterface(NetworkInterface.getByName("wlan0"));
            DatagramPacket dataPacket = new DatagramPacket(data, data.length, address, sendPort);
            ms.send(dataPacket);
            ms.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
