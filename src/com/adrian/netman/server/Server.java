package com.adrian.netman.server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    public enum Type{
        TCP, UDP
    }

    //SINGLETON
    private static Server singleton = null;
    public static Server get(){
        if(singleton == null)
            System.err.println("Server isn't created!");

        return singleton;
    }
    public static void create(int port, Type type){
        singleton = new Server(port, type);
    }
    //---------

    private Type type;
    private int port;
    private boolean running;
    private List<ServerClient> clients;

    private ServerSocket ss;

    private DatagramSocket ds;
    private byte[] buffer;


    private Server(int port, Type type){
        this.port = port;
        this.type = type;
        running = false;
        clients = new ArrayList<>();
        ss = null;
    }

    public void init(){
        try {
            if(type == Type.TCP){
                ss = new ServerSocket(port);
            }else if(type == Type.UDP){
                buffer = new byte[256];
                ds = new DatagramSocket(port);
            }
            running = true;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server started!");
        if(type == Type.TCP)
            TCPProcess();
        else if(type == Type.UDP)
            UDPProcess();
    }

    private void UDPProcess(){
        while(running){
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("["+packet.getAddress().toString()+"]: "+received);

                /*InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                ds.send(packet);*/
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    private void TCPProcess(){
        while(running){
            try {
                Socket socket = ss.accept();
                ServerClient sc = new ServerClient(socket, type);
                clients.add(sc);
                sc.init();
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    public void sendStringToAll(String msg){
        for(ServerClient sc:clients){
            sc.sendString(msg);
        }
    }
}
