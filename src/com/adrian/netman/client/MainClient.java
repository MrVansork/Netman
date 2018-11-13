package com.adrian.netman.client;

import com.adrian.netman.server.Server;

import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        AppClient client = new AppClient("192.168.1.30", 8192, Server.Type.UDP);
        client.init();
        Scanner keyboard = new Scanner(System.in);
        String input;
        while((input = keyboard.nextLine()) != null){
            client.sendString(input);
        }
    }

}
