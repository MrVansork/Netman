package com.adrian.netman.client;

import com.adrian.netman.server.Server;
import com.adrian.netman.shared.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class AppClient extends Client {

    public AppClient(String address, int port, Server.Type type){
        super(type);
        try {
            this.ip = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        pw = null;
        br = null;
        running = false;
    }

    @Override
    public synchronized void init() {
        try {
            if(type == Server.Type.TCP){
                socket = new Socket(ip, port);
                pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }else if(type == Server.Type.UDP){
                ds = new DatagramSocket();
            }
            running = true;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetAddress getAddress() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }
}
