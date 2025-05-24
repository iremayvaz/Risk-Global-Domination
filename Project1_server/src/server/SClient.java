/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import game.Message;
import game.Territory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * Her client'ı temsil eder
 * Server tarafı
 * 
 * Her client için bir nesnesi oluşturulacak 
 * bu sebeple statik değişken kullanmayız.
 *
 * @author iremayvaz
 */
public class SClient {
    public Socket csocket;
    public ObjectOutputStream coutput;
    public ObjectInputStream cinput;
    
    public SClient rival; // Rakip 
    public ClientListener listenClient; // Client'tan gelen mesajları dinler (Thread)
    
    public int id;
    public String name;
    
    public SClient(Socket connectedSocket) throws IOException {
        this.csocket = connectedSocket;
        this.coutput = new ObjectOutputStream(this.csocket.getOutputStream());
        this.cinput = new ObjectInputStream(this.csocket.getInputStream());
        
        this.listenClient = new ClientListener(this);
        
        
        this.id = Server.gamerID; // client'a id
        Server.gamerID++; // yeni client'ın id'si için 
    }
    
    
    public void SendMessage(Message message) throws IOException { // Client'a mesaj gönderir.
        coutput.writeObject(message);
        coutput.flush();
    }
    
}
