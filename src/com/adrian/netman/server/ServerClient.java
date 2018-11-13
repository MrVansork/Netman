package com.adrian.netman.server;

import com.adrian.netman.shared.Client;

import java.net.Socket;

public class ServerClient extends Client {

    public ServerClient(Socket socket, Server.Type type){
        super(type);
        this.socket = socket;
        pw = null;
        br = null;
        running = false;
        System.out.println("Client conected! ["+getAddress()+"@"+getPort()+"]");
    }
}
