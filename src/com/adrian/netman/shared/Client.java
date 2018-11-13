package com.adrian.netman.shared;

import com.adrian.netman.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{

    //TCP
    protected Socket socket;
    protected PrintWriter pw;
    protected BufferedReader br;
    //UDP
    protected DatagramSocket ds;
    //---
    protected InetAddress ip;
    protected int port;
    protected boolean running;
    protected Server.Type type;

    public Client(Server.Type type){
        this.type = type;
    }

    public synchronized void init(){
        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close(){
        running = false;
    }

    @Override
    public void run() {
        if(type == Server.Type.TCP){
            TCPProcess();
        }else if(type == Server.Type.UDP){
            UDPProcess();
        }
    }

    public void TCPProcess(){
        while(running){
            String msg = null;
            try {
                msg = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(msg);
        }
    }

    public void UDPProcess(){
        while(running){
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                ds.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendString(String msg){
        if(type == Server.Type.TCP){
            pw.println(msg);
        }else if(type == Server.Type.UDP){
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ip, port);
            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public InetAddress getAddress(){
        return ip;
    }

    public int getPort(){
        return socket.getPort();
    }

    public Server.Type getType() {
        return type;
    }
}
