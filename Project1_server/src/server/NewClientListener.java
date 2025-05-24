/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import game.Message;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Yeni bağlanan client'ları karşılar
 * Server tarafı
 *
 * @author iremayvaz
 */
public class NewClientListener extends Thread {
    
    @Override
    public void run(){
        System.out.println("Server başlatıldı...");
        while (!Server.ssocket.isClosed()) {
            System.out.println("Oyuncular bekleniyor..");
            try {
                Socket csocket = Server.ssocket.accept(); // blocking
                System.out.println("Oyuncu bağlandı.");
                
                SClient newGamer = new SClient(csocket); // Yeni bağlantı yeni kapı
                Server.players.add(newGamer); // Oyuncu listeye eklendi
                
                newGamer.listenClient.start(); // Thread başlatıldı.

            } catch (IOException ex) {
                Logger.getLogger(NewClientListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
