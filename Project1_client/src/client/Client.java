/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import game.Login;
import game.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author iremayvaz
 */
public class Client {
    
    public static Socket csocket; // Server ile bağlantı soketi
    public static ObjectOutputStream coutput; // giden veri (client'tan)
    public static ObjectInputStream cinput; // gelen veri (client'a)
    public static ServerListener server_listener; // server dinleyici
    
    // Server'a IP ve Port ile bağlanır.
    public static void Connect(String ip, int port) throws IOException {
        // Server'a bağlan
        Client.csocket = new Socket(ip, port); 
        System.out.println("Server'a bağlandı.");
        
        // Veri alış-verişi
        Client.cinput = new ObjectInputStream(csocket.getInputStream()); // Veri girişi
        Client.coutput = new ObjectOutputStream(csocket.getOutputStream()); // Veri çıkışı
        
        // Server'ı dinle
        Client.server_listener = new ServerListener(); 
        Client.server_listener.start(); // Server'dan gelen mesajları almak için yeni bir thread başlatır.
        
        // İlk bağlantı mesajı
        Message first_msg = new Message(Message.Type.JOIN_SERVER, 
                                        Login.login.txt_gamer_name.getText());
        Client.SendMessageToServer(first_msg); // İsim ve bağlantı bilgisi gönderildi
    }
    
    public static void SendMessageToServer(Message message) throws IOException {
        Client.coutput.writeObject(message);
    }
}
