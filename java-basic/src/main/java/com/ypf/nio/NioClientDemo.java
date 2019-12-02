package com.ypf.nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NioClientDemo {

    public static void main(String[] args) throws IOException {
        SocketAddress address = new InetSocketAddress("localhost", 12345);
        Socket s = new Socket();
        s.connect(address);
        PrintWriter writer = new PrintWriter( new OutputStreamWriter(s.getOutputStream()));
        writer.println("abc");
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String str;
        do{
            str = reader.readLine();
            System.out.println(str);
        }while(str != null);


        writer.close();
        reader.close();
        s.close();
    }

}
