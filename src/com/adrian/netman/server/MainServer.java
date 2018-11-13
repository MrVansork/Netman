package com.adrian.netman.server;


public class MainServer {

    public static void main(String[] args) {
        Server.create(8192, Server.Type.UDP);
        Server.get().init();
    }

}
