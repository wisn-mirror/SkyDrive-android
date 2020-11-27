package com.library.base.net.broadcast;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastAll {
    private static final String TAG = "BroadcastAll";

    private static BroadcastAll broadcastAll;
    private boolean isRungFlag = true;
    DatagramSocket ds = null;
    private BroadcastAll() { }

    public static BroadcastAll getInstance() {
        if (broadcastAll == null) {
            synchronized (BroadcastAll.class) {
                if (broadcastAll == null) {
                    broadcastAll = new BroadcastAll();
                }
            }
        }
        return broadcastAll;
    }

    public void listenerMessage(final int listenerPort, final MessageCall messageCall) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[65507];
                    ds = new DatagramSocket(listenerPort);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (isRungFlag) {
                        ds.receive(packet);
                        if(isRungFlag){
                            String message = new String(packet.getData(), 0, packet.getLength());
                            Log.d(TAG, packet.getAddress() + " hostname" + packet.getAddress().getHostName() + ":" + packet.getPort() + "    →    ");
                            if (messageCall != null) {
                                messageCall.callBackMessage(message, packet.getAddress().getHostAddress());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null && !ds.isClosed()) {
                        ds.close();
                    }
                }
            }
        }).start();

    }

    public void sendBackMessage(String IP, int sendPort, String message) {
        try {
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.getBytes().length,
                    InetAddress.getByName(IP), sendPort);
            ds.send(dp);
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cancel() {
        isRungFlag = false;
        try {
            if (ds != null && ds.isConnected()) {
                ds.disconnect();
            }
            if (ds != null && !ds.isClosed()) {
                ds.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
